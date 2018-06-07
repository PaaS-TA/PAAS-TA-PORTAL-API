package org.openpaas.paasta.portal.api.service;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.runtime.directive.Foreach;
import org.apache.velocity.runtime.log.Log;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.archive.ApplicationArchive;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudServiceOffering;
import org.cloudfoundry.client.lib.domain.CloudServicePlan;
import org.cloudfoundry.client.lib.domain.Staging;
import org.cloudfoundry.client.lib.io.DynamicZipInputStream;
import org.cloudfoundry.client.lib.org.codehaus.jackson.map.ObjectMapper;
import org.cloudfoundry.client.lib.org.codehaus.jackson.type.TypeReference;
import org.cloudfoundry.client.v2.applications.*;
import org.cloudfoundry.client.v2.routemappings.CreateRouteMappingRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteRequest;
import org.cloudfoundry.client.v2.servicebindings.*;
import org.cloudfoundry.client.v2.serviceinstances.*;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansRequest;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansResponse;
import org.cloudfoundry.client.v2.services.*;
import org.cloudfoundry.client.v3.droplets.CopyDropletRequest;
import org.cloudfoundry.client.v3.servicebindings.CreateServiceBindingData;
import org.cloudfoundry.operations.applications.StartApplicationRequest;
import org.cloudfoundry.operations.routes.CheckRouteRequest;
import org.cloudfoundry.reactor.TokenProvider;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Catalog;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

//import org.openpaas.paasta.portal.api.mapper.cc.CatalogCcMapper;
//import org.openpaas.paasta.portal.api.mapper.portal.CatalogMapper;

/**
 * 서비스 카탈로그, 개발 환경 카탈로그, 앱 템플릿 카탈로그 정보 조회 및 관리 기능을 구현한 서비스 클래스로 Common(1.3.8) 클래스를 상속하여 구현한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.07.04 최초작성
 */
@Transactional
@Service
public class CatalogService extends Common {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CatalogService.class);

    private final CommonService commonService;
    private final CommonCodeService commonCodeService;
    private final SpaceService spaceService;
    private final DomainService domainService;
    private final AppService appService;

    @Value("${cloudfoundry.authorization}")
    private String cfAuthorizationHeaderKey;

    @Autowired
    public CatalogService(
            CommonCodeService commonCodeService, SpaceService spaceService,
            DomainService domainService, AppService appService, CommonService commonService) throws Exception {
        this.commonCodeService = commonCodeService;
        this.spaceService = spaceService;
        this.domainService = domainService;
        this.appService = appService;
        this.commonService = commonService;
    }

    /**
     * 카탈로그 서비스 이용사양 목록을 조회한다.
     *
     * @param servicename ServiceName(자바클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public ListServicePlansResponse getCatalogServicePlanList(String servicename, HttpServletRequest req) throws Exception {

        ListServicesResponse listServicesResponse = Common.cloudFoundryClient(connectionContext(), tokenProvider(req.getHeader(cfAuthorizationHeaderKey)))
                .services()
                .list(ListServicesRequest
                        .builder()
                        .build())
                .block();
        Optional<ServiceResource> serviceResource = listServicesResponse.getResources().stream()
                .filter(a -> a.getEntity().getLabel().equals(servicename)).findFirst();
        ListServicePlansResponse listServicePlansResponse = Common.cloudFoundryClient(connectionContext(), tokenProvider(req.getHeader(cfAuthorizationHeaderKey)))
                .servicePlans()
                .list(ListServicePlansRequest
                        .builder()
                        .serviceId(serviceResource.get().getMetadata().getId())
                        .build())
                .block();
        return listServicePlansResponse;
    }

    /**
     * 카탈로그 앱 목록을 조회한다.
     *
     * @param orgid String(자바클래스)
     * @param spaceid String(자바클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public ListApplicationsResponse getCatalogAppList(String orgid, String spaceid, HttpServletRequest req) throws Exception {
        ListApplicationsResponse listApplicationsResponse = Common.cloudFoundryClient(connectionContext(), tokenProvider(req.getHeader(cfAuthorizationHeaderKey)))
                .applicationsV2().list(ListApplicationsRequest.builder().organizationId(orgid).spaceId(spaceid).build()).block();
        return listApplicationsResponse;
    }


    /**
     * 카탈로그 앱 이름 생성여부를 조회한다.
     *
     * @param name  name(앱 이름)
     * @param req   HttpServletRequest(자바클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCheckCatalogApplicationNameExists(String name, String orgid, String spaceid, HttpServletRequest req, HttpServletResponse res) throws Exception {

        ListApplicationsResponse listApplicationsResponse = Common.cloudFoundryClient(connectionContext(), tokenProvider(req.getHeader(cfAuthorizationHeaderKey)))
                .applicationsV2().list(ListApplicationsRequest
                        .builder()
                        .organizationId(orgid)
                        .spaceId(spaceid)
                        .build())
                .block();

        for (ApplicationResource applicationResource: listApplicationsResponse.getResources()) {
            if(applicationResource.getEntity().getName().equals(name))
            {commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");}
        }
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 카탈로그 앱 템플릿을 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> executeCatalogStarter(Catalog param, HttpServletRequest req, HttpServletResponse response) throws Exception {
        // CREATE APPLICATION
        this.procCatalogCreateApplication(param, req);

        // IF START APPLICATION
        // UPLOAD APPLICATION
        if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) {
            // UPLOAD APPLICATION
            this.procCatalogUploadApplication(param, req, response);
        }

        // CREATE SERVICE INSTANCE
        // IF BIND SERVICE
        List<Catalog> servicePlanList = param.getServicePlanList();
        List<Catalog> serviceInstanceGuidList = new ArrayList<>();

        for (Catalog servicePlan : servicePlanList) {
            servicePlan.setServiceInstanceName(servicePlan.getServiceInstanceName());
            servicePlan.setAppName(param.getName());
            servicePlan.setOrgName(param.getOrgName());
            servicePlan.setSpaceName(param.getSpaceName());
            // CREATE SERVICE INSTANCE
            //Map<String, Object> tempMap = this.procCatalogCreateServiceInstanceV2(servicePlan, req);

            // FOR TEST CASE
            Catalog bindParam = new Catalog();
            //bindParam.setServiceInstanceGuid(UUID.fromString(tempMap.get("SERVICE_INSTANCE_GUID").toString()));
            serviceInstanceGuidList.add(bindParam);

            // BIND SERVICE
            if (Constants.USE_YN_Y.equals(servicePlan.getAppBindYn())) {
                bindParam.setOrgName(param.getOrgName());
                bindParam.setSpaceName(param.getSpaceName());
                bindParam.setAppName(param.getName());
                bindParam.setApp_bind_parameter(servicePlan.getApp_bind_parameter());

                //this.procCatalogBindService(bindParam, req);
            }
        }

        // IF START APPLICATION
        UUID resultAppGuid = null;
        if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) {
            // START APPLICATION
            // Map<String, Object> resultMap = this.procCatalogStartApplication(param, req);
            //resultAppGuid = (UUID) resultMap.get("APP_GUID");
        }

        UUID finalResultAppGuid = resultAppGuid;
        return new HashMap<String, Object>() {{
            put("APP_GUID", finalResultAppGuid);    // FOR TEST CASE
            put("SERVICE_INSTANCE_GUID_LIST", serviceInstanceGuidList); // FOR TEST CASE
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 앱 템플릿 내역을 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertCatalogHistoryStarter(Catalog param) {
        param.setCatalogType(Constants.CATALOG_TYPE_STARTER);
        //catalogMapper.insertCatalogHistory(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 앱 개발환경을 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> executeCatalogBuildPack(Catalog param, HttpServletRequest req, HttpServletResponse response) throws Exception {
        UUID resultAppGuid = null;

        // CREATE APPLICATION
        this.procCatalogCreateApplication(param, req);

        // UPLOAD APPLICATION
        if (!Constants.USE_YN_N.equals(param.getAppSampleFilePath()))
            this.procCatalogUploadApplication(param, req, response);

        if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) {
            // START APPLICATION
            //Map<String, Object> resultMap = this.procCatalogStartApplication(param, req);
            //resultAppGuid = (UUID) resultMap.get("APP_GUID");
        }

        UUID finalResultAppGuid = resultAppGuid;
        return new HashMap<String, Object>() {{
            put("APP_GUID", finalResultAppGuid);    // FOR TEST CASE
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 앱 개발환경 내역을 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertCatalogHistoryBuildPack(Catalog param) {
        param.setCatalogType(Constants.CATALOG_TYPE_BUILD_PACK);
        //catalogMapper.insertCatalogHistory(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 서비스를 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> executeCatalogServicePack(Catalog param, HttpServletRequest req) throws Exception {
        // CREATE SERVICE INSTANCE
        Map<String, Object> resultMap = this.procCatalogCreateServiceInstance(param, req);

        // BIND SERVICE
        if (Constants.USE_YN_Y.equals(param.getAppBindYn())) {
            Catalog bindParam = new Catalog();
            //bindParam.setServiceInstanceGuid(UUID.fromString(resultMap.get("SERVICE_INSTANCE_GUID").toString()));
            bindParam.setOrgName(param.getOrgName());
            bindParam.setSpaceName(param.getSpaceName());
            bindParam.setAppName(param.getAppName());
            bindParam.setParameter(param.getParameter());

            //this.procCatalogBindService(bindParam, req);
        }

        return new HashMap<String, Object>() {{
            put("SERVICE_INSTANCE_GUID", resultMap.get("SERVICE_INSTANCE_GUID")); // FOR TEST CASE
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 카탈로그 서비스를 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> executeCatalogServicePackV2(Catalog param, HttpServletRequest req) throws Exception {
        try {
            // CREATE SERVICE INSTANCE
            //Map<String, Object> resultMap = this.procCatalogCreateServiceInstanceV2(param, req);

            // BIND SERVICE
            if (Constants.USE_YN_Y.equals(param.getAppBindYn())) {
                Catalog bindParam = new Catalog();
                //bindParam.setServiceInstanceGuid(UUID.fromString(resultMap.get("SERVICE_INSTANCE_GUID").toString()));
                bindParam.setOrgName(param.getOrgName());
                bindParam.setSpaceName(param.getSpaceName());
                bindParam.setAppName(param.getAppName());
                bindParam.setParameter(param.getApp_bind_parameter());

                //this.app_bind_parameter(bindParam, req);
            }
            return new HashMap<String, Object>() {{
                //put("SERVICE_INSTANCE_GUID", resultMap.get("SERVICE_INSTANCE_GUID")); // FOR TEST CASE
                put("RESULT", Constants.RESULT_STATUS_SUCCESS);
            }};

        } catch (Exception e) {
            CloudFoundryException cfe = (CloudFoundryException) e;
            LOGGER.error(cfe.getDescription());
            return new HashMap<String, Object>() {{
                put("SERVICE_INSTANCE_GUID", "");
                put("ERROR_MSG", cfe.getDescription());
                put("RESULT", Constants.RESULT_STATUS_FAIL);
            }};
        }
    }

    /**
     * 카탈로그 서비스 실행 내역을 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertCatalogHistoryServicePack(Catalog param) {
        param.setCatalogType(Constants.CATALOG_TYPE_SERVICE_PACK);
        //catalogMapper.insertCatalogHistory(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 앱을 생성한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void procCatalogCreateApplication(Catalog param, HttpServletRequest req) throws Exception {
        String appName = param.getName();
        String buildPackName = (null != param.getBuildPackName()) ? param.getBuildPackName() : "";
        Staging staging = new Staging(Constants.CREATE_APPLICATION_STAGING_COMMAND, buildPackName);
        Integer disk = param.getDiskSize();
        Integer memory = param.getMemorySize();
        List<String> uris = new ArrayList<String>() {{
            add(param.getHostName());
        }};

        if (disk == 0) disk = Constants.CREATE_APPLICATION_DISK_SIZE;
        if (memory == 0) memory = Constants.CREATE_APPLICATION_MEMORY_SIZE;

        // CREATE APPLICATION
//        if (buildPackName.toLowerCase().contains(Constants.CATALOG_EGOV_BUILD_PACK_CHECK_STRING)) {
//            CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
//            customCloudFoundryClient.createApplicationV2(appName, staging, disk, memory, uris, new HashMap<String, Object>() {{
//                put(Constants.CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_KEY, Constants.CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_VALUE);
//            }});
//
//        } else {
//            CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
//            cloudFoundryClient.createApplication(appName, staging, disk, memory, uris, null);
//
//
//        }

    }


    /**
     * 카탈로그 앱을 업로드한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void procCatalogUploadApplication(Catalog param, HttpServletRequest req, HttpServletResponse response) throws Exception {
        CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
        String appName = param.getName();
        response.setContentType("application/octet-stream");
        //String fileNameForBrowser = getDisposition(param.getAppSampleFileName(), getBrowser(req));
        //response.setHeader("Content-Disposition", "attachment; filename="+fileNameForBrowser);

        OutputStream os = response.getOutputStream();
        InputStream is = new URL(param.getAppSampleFilePath()).openStream();
        cloudFoundryClient.uploadApplication(appName, appName, is);

    }

    private String getBrowser(String header) {

        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        } else if (header.indexOf("Trident/7.0") > -1) {
            //IE 11 이상 //IE 버전 별 체크 >> Trident/6.0(IE 10) , Trident/5.0(IE 9) , Trident/4.0(IE 8)
            return "MSIE";
        }

        return "Firefox";
    }

    private String getDisposition(String filename, String browser) throws Exception {
        String encodedFilename = null;

        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            throw new RuntimeException("Not supported browser");
        }

        return encodedFilename;
    }


    /**
     * 카탈로그 앱을 시작한다.
     *
     * @param applicationid applicationid
     * @param token token
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> procCatalogStartApplication(String applicationid, String token) throws Exception {
  try {
      Thread.sleep(500);
      Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).applicationsV2().update(UpdateApplicationRequest.builder().applicationId(applicationid).state("STARTED").build()).block();
  }
  catch (Exception e){
      LOGGER.info(e.toString());
  }
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 서비스 인스턴스를 생성한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> procCatalogCreateServiceInstance(Catalog param, HttpServletRequest req) throws Exception {
//        CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
//
//        // CREATE SERVICE INSTANCE
//        String resultString = customCloudFoundryClient.createService(param.getServiceInstanceName(), param.getServicePlan(), param.getOrgName(), param.getSpaceName());
//
//        Map<String, Object> tempMap = JsonUtil.convertJsonToMap(resultString);
//        Map tempSubMap = (Map) tempMap.get("metadata"); // FOR TEST CASE
//
//
//
//        return new HashMap<String, Object>() {{
//            put("SERVICE_INSTANCE_GUID", tempSubMap.get("guid"));
//            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
//        }};
        return null;
    }


    public Map<String, Object> createApp(Catalog param, String token, String token2, HttpServletResponse response) throws Exception {
        File file = createTempFile(param, token2, response); // 임시파일을 생성합니다.
        try {
            String applicationid = createApplication(param, token); // App을 만들고 guid를 return 합니다.
            String routeid = createRoute(param, token); //route를 생성후 guid를 return 합니다.

            routeMapping(applicationid, routeid, token); // app와 route를 mapping합니다.
            fileUpload(file, applicationid, token); // app에 파일 업로드 작업을 합니다.

            if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) { //앱 실행버튼이 on일때
                return procCatalogStartApplication(applicationid, token); //앱 시작
            }
            return new HashMap<String, Object>() {{
                put("RESULT", Constants.RESULT_STATUS_SUCCESS);
            }};
        }finally {
            file.delete();
        }
    }

    public Map<String, Object> createAppTemplate(Catalog param, String token, String token2, HttpServletResponse response) throws Exception {
        File file = createTempFile(param, token2, response); // 임시파일을 생성합니다.
        try {
            String applicationid = createApplication(param, token); // App을 만들고 guid를 return 합니다.
            String routeid = createRoute(param, token); //route를 생성후 guid를 return 합니다.

            routeMapping(applicationid, routeid, token); // app와 route를 mapping합니다.
            fileUpload(file, applicationid, token); // app에 파일 업로드 작업을 합니다.

            if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) { //앱 실행버튼이 on일때
                procCatalogStartApplication(applicationid, token); //앱 시작
            }
            LOGGER.info(param.toString());
            if(param.getServicePlanList() != null){
                param.getServicePlanList().forEach(serviceplan -> {
                    try {
                        serviceplan.setSpaceId(param.getSpaceId());
                        if(!serviceplan.getAppGuid().equals("(id_dummy)")){
                            serviceplan.setAppGuid(applicationid);
                        }
                        procCatalogCreateServiceInstanceV2(serviceplan, token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            return new HashMap<String, Object>() {{
                put("RESULT", Constants.RESULT_STATUS_SUCCESS);
            }};
        }finally {
            file.delete();
        }
    }

    private String createApplication(Catalog param, String token) {
        if(param.getBuildPackName().toLowerCase().contains(Constants.CATALOG_EGOV_BUILD_PACK_CHECK_STRING)){
            return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).
                    applicationsV2().create(CreateApplicationRequest.builder().buildpack(param.getBuildPackName()).memory(param.getMemorySize()).name(param.getAppName()).diskQuota(param.getDiskSize()).spaceId(param.getSpaceId()).environmentJsons(new HashMap<String, Object>()
            {{
                put(Constants.CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_KEY, Constants.CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_VALUE);
            }}).build()).block().getMetadata().getId();
        }

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).
                applicationsV2().create(CreateApplicationRequest.builder().buildpack(param.getBuildPackName()).memory(param.getMemorySize()).name(param.getAppName()).diskQuota(param.getDiskSize()).spaceId(param.getSpaceId()).build()).block().getMetadata().getId();

    }

    private String createRoute(Catalog param, String token) {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).
                routes().create(CreateRouteRequest.builder().host(param.getAppName()).domainId(param.getDomainId()).spaceId(param.getSpaceId()).build()).block().getMetadata().getId();
    }

    private void routeMapping(String applicationid, String routeid, String token) {
        Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).
                routeMappings().create(CreateRouteMappingRequest.builder().routeId(routeid).applicationId(applicationid).build()).block();
    }

    private  void fileUpload(File file, String applicationid, String token){
        try {
            Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).
                    applicationsV2().upload(
                    UploadApplicationRequest
                            .builder()
                            .applicationId(applicationid)
                            .application(file.toPath())
                            .build()
            ).block();
        }
        catch(Exception e){
            LOGGER.info(e.toString());
        }
    }
   private File createTempFile(Catalog param, String token2, HttpServletResponse response) throws Exception {

        response.setContentType("application/octet-stream");
        String fileNameForBrowser = getDisposition(param.getAppSampleFileName(), getBrowser(token2));
        response.setHeader("Content-Disposition", "attachment; filename=" + fileNameForBrowser);
        File file = File.createTempFile(param.getAppSampleFileName().substring(0, param.getAppSampleFileName().length() - 4), param.getAppSampleFileName().substring(param.getAppSampleFileName().length() - 4));
        InputStream is = (new URL(param.getAppSampleFilePath()).openConnection()).getInputStream();
        OutputStream out = new FileOutputStream(file);
        IOUtils.copy(is, out);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(out);
        return file;
    }

    /**
     * 카탈로그 서비스 인스턴스를 생성한다.
     *
     * @param param Catalog(모델클래스)
     * @param token   String(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public CreateServiceInstanceResponse procCatalogCreateServiceInstanceV2(Catalog param, String token) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.readValue(param.getParameter(), new TypeReference<Map<String, Object>>() {
        });
        LOGGER.info(param.getName() + " : " + param.getSpaceId() + " : " + parameterMap + " : " + param.getServicePlan());
        CreateServiceInstanceResponse createserviceinstanceresponse =
                Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).
                        serviceInstances().create(
                        CreateServiceInstanceRequest
                                .builder()
                                .name(param.getName())
                                .spaceId(param.getSpaceId())
                                .parameters(parameterMap)
                                .servicePlanId(param.getServicePlan())
                                .build()
                ).block();

        if(!param.getAppGuid().equals("(id_dummy)")) {
            param.setServiceInstanceGuid(createserviceinstanceresponse.getMetadata().getId());
            procCatalogBindService(param, token);
        }
        return createserviceinstanceresponse;
    }

    /**
     * 카탈로그 앱 서비스를 바인드한다.
     *
     * @param param Catalog(모델클래스)
     * @param token   String(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public CreateServiceBindingResponse procCatalogBindService(Catalog param, String token) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> bindparameterMap = mapper.readValue(param.getApp_bind_parameter(), new TypeReference<Map<String, Object>>() {
        });
        CreateServiceBindingResponse createservicebindingresponse =
                Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).
                        serviceBindingsV2().create(
                        CreateServiceBindingRequest
                                .builder()
                                .applicationId(param.getAppGuid())
                                .serviceInstanceId(param.getServiceInstanceGuid())
                                .parameters(bindparameterMap)
                                .build()
                ).block();

        return createservicebindingresponse;
    }

    public ListServiceInstancesResponse listServiceInstancesResponse(String orgid, String spaceid, String token){
        ListServiceInstancesResponse listServiceInstancesResponse =
                Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).
                        serviceInstances().list(
                        ListServiceInstancesRequest
                                .builder()
                                .organizationId(orgid)
                                .spaceId(spaceid)
                                .build()
                ).block();
        return listServiceInstancesResponse;
    }

    /**
     * 서비스 전체 목록을 가져온다.
     *
     * @return ListServicesResponse
     */
    public ListServicesResponse getService()throws Exception{
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(this.getToken()))
                .services()
                .list(ListServicesRequest.builder()
                        .build()
                ).log()
                .block();
    }
}
