package org.openpaas.paasta.portal.api.service;


import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.lib.org.codehaus.jackson.map.ObjectMapper;
import org.cloudfoundry.client.lib.org.codehaus.jackson.type.TypeReference;
import org.cloudfoundry.client.v2.applications.*;
import org.cloudfoundry.client.v2.routemappings.CreateRouteMappingRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteRequest;
import org.cloudfoundry.client.v2.routes.DeleteRouteRequest;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingResponse;
import org.cloudfoundry.client.v2.serviceinstances.*;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansRequest;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansResponse;
import org.cloudfoundry.client.v2.serviceplanvisibilities.ListServicePlanVisibilitiesRequest;
import org.cloudfoundry.client.v2.serviceplanvisibilities.ListServicePlanVisibilitiesResponse;
import org.cloudfoundry.client.v2.services.ListServicesRequest;
import org.cloudfoundry.client.v2.services.ListServicesResponse;
import org.cloudfoundry.client.v2.services.ServiceResource;
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.applications.SetApplicationCurrentDropletRequest;
import org.cloudfoundry.client.v3.applications.StartApplicationRequest;
import org.cloudfoundry.client.v3.builds.CreateBuildRequest;
import org.cloudfoundry.client.v3.builds.CreateBuildResponse;
import org.cloudfoundry.client.v3.builds.GetBuildRequest;
import org.cloudfoundry.client.v3.packages.CreatePackageRequest;
import org.cloudfoundry.client.v3.packages.GetPackageRequest;
import org.cloudfoundry.client.v3.packages.PackageRelationships;
import org.cloudfoundry.client.v3.packages.PackageType;
import org.cloudfoundry.client.v3.packages.UploadPackageRequest;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Catalog;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Service
public class CatalogServiceV3 extends Common {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CatalogServiceV3.class);

    private final CommonService commonService;
    private final SpaceServiceV2 spaceServiceV2;
    private final DomainServiceV2 domainServiceV2;
    private final AppServiceV2 appServiceV2;
    private final AppServiceV3 appServiceV3;

    @Value("${cloudfoundry.authorization}")
    private String cfAuthorizationHeaderKey;

    @Autowired
    public CatalogServiceV3(SpaceServiceV2 spaceServiceV2, DomainServiceV2 domainServiceV2, AppServiceV2 appServiceV2, CommonService commonService, AppServiceV3 appServiceV3) throws Exception {

        this.spaceServiceV2 = spaceServiceV2;
        this.domainServiceV2 = domainServiceV2;
        this.appServiceV2 = appServiceV2;
        this.commonService = commonService;
        this.appServiceV3 = appServiceV3;
    }

    /**
     * 카탈로그 서비스 이용사양 목록을 조회한다.
     *
     * @param servicename ServiceName(자바클래스)
     * @param token       HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public ListServicePlansResponse getCatalogServicePlanList(String servicename, String token) throws Exception {
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient( tokenProvider(token));
        ListServicesResponse listServicesResponse = reactorCloudFoundryClient.services().list(ListServicesRequest.builder().build()).block();
        Optional<ServiceResource> serviceResource = listServicesResponse.getResources().stream().filter(a -> a.getEntity().getLabel().equals(servicename)).findFirst();
        ListServicePlansResponse listServicePlansResponse = reactorCloudFoundryClient.servicePlans().list(ListServicePlansRequest.builder().serviceId(serviceResource.get().getMetadata().getId()).build()).block();
        return listServicePlansResponse;
    }

    /**
     * 카탈로그 서비스 이용사양 목록을 조회한다.
     *
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map getCatalogServicePlanAdMinList() throws Exception {
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient();
        List<Map> List_service = new ArrayList<>();
        ListServicesResponse listServicesResponse = reactorCloudFoundryClient.services().list(ListServicesRequest.builder().build()).block();
        listServicesResponse.getResources().forEach(resource -> {
            Map list_result = new HashMap();
            try {
                list_result.put("Plan", reactorCloudFoundryClient.servicePlans().list(ListServicePlansRequest.builder().serviceId(resource.getMetadata().getId()).build()).block());
                list_result.put("Service", resource);
            } catch (Exception e) {

            } finally {
                List_service.add(list_result);
            }
        });
        ListServicePlanVisibilitiesResponse listServicePlanVisibilitiesResponse = reactorCloudFoundryClient.servicePlanVisibilities().list(ListServicePlanVisibilitiesRequest.builder().build()).block();
        return new HashMap<String, Object>() {{
            put("RESULT", List_service);
            put("Visibilities", listServicePlanVisibilitiesResponse);
        }};
    }


    /**
     * 카탈로그 앱 목록을 조회한다.
     *
     * @param orgid   String(자바클래스)
     * @param spaceid String(자바클래스)
     * @param token   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public ListApplicationsResponse getCatalogAppList(String orgid, String spaceid, String token) throws Exception {
        ListApplicationsResponse listApplicationsResponse = cloudFoundryClient(tokenProvider(token)).applicationsV2().list(ListApplicationsRequest.builder().organizationId(orgid).spaceId(spaceid).build()).block();
        return listApplicationsResponse;
    }


    /**
     * 카탈로그 앱 이름 생성여부를 조회한다.
     *
     * @param name  name(앱 이름)
     * @param token HttpServletRequest(자바클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCheckCatalogApplicationNameExists(String name, String orgid, String spaceid, String token, HttpServletResponse res) throws Exception {

        ListApplicationsResponse listApplicationsResponse = cloudFoundryClient(tokenProvider(token)).applicationsV2().list(ListApplicationsRequest.builder().organizationId(orgid).spaceId(spaceid).build()).block();

        for (ApplicationResource applicationResource : listApplicationsResponse.getResources()) {
            if (applicationResource.getEntity().getName().equals(name)) {
                commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");
            }
        }
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
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
     * @param applicationid             applicationid
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> procCatalogStartApplication(String applicationid, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        try {
            // 시작
            reactorCloudFoundryClient.applicationsV3().start(StartApplicationRequest.builder().applicationId(applicationid).build()).block();
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 카탈로그 앱을 생성한다.
     *
     * @param param  Catalog
     * @param token  token
     * @param token2 token
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> createApp(Catalog param, String token, String token2, HttpServletResponse response) {
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        String applicationid = "applicationID";
        String routeid = "route ID";
        File file = null;
        try {
            file = createTempFile(param, token2, response); // 임시파일을 생성합니다.
            applicationid = createApplication(param, reactorCloudFoundryClient); // App을 만들고 guid를 return 합니다.
            routeid = createRoute(param, reactorCloudFoundryClient); //route를 생성후 guid를 return 합니다.
            routeMapping(applicationid, routeid, reactorCloudFoundryClient); // app와 route를 mapping합니다.
            String packageId = fileUpload(file, applicationid, reactorCloudFoundryClient); // app에 파일 업로드 작업을 합니다.
            final String APPLICATION_ID = applicationid;

            //Build와 Start 병렬실행
            Thread th = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                appServiceV3.createBuild(APPLICATION_ID,packageId,reactorCloudFoundryClient);
                                //앱 실행버튼이 on일때
                                if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) {
                                    procCatalogStartApplication(APPLICATION_ID, reactorCloudFoundryClient); //앱 시작
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    );
            th.start();
            commonService.procCommonApiRestTemplate("/v2/history", HttpMethod.POST, param, null);
            return new HashMap<String, Object>() {{
                put("RESULT", Constants.RESULT_STATUS_SUCCESS);
            }};
        } catch (Exception e) {
            if (!applicationid.equals("applicationID")) {
                if (!routeid.equals("route ID")) {
                    reactorCloudFoundryClient.routes().delete(DeleteRouteRequest.builder().routeId(routeid).build()).block();
                }
                reactorCloudFoundryClient.applicationsV2().delete(DeleteApplicationRequest.builder().applicationId(applicationid).build()).block();
            }
            return new HashMap<String, Object>() {{
                put("RESULT", Constants.RESULT_STATUS_FAIL);
                put("msg", e.getMessage());
            }};
        } finally {
            if (file != null) {
                boolean fileDeleteResult = file.delete();
            }
        }
    }


    /**
     * 카탈로그 앱 템플릿을 생성한다.
     *
     * @param param  Catalog
     * @param token  token
     * @param token2 token
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> createAppTemplate(Catalog param, String token, String token2, HttpServletResponse response) {
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        String applicationid = "applicationID";
        String routeid = "route ID";
        boolean appcreated = false;
        File file = null;
        try {
            file = createTempFile(param, token2, response); // 임시파일을 생성합니다.
            applicationid = createApplication(param, reactorCloudFoundryClient); // App을 만들고 guid를 return 합니다.
            routeid = createRoute(param, reactorCloudFoundryClient); //route를 생성후 guid를 return 합니다.
            routeMapping(applicationid, routeid, reactorCloudFoundryClient); // app와 route를 mapping합니다.
            String packageId = fileUpload(file, applicationid, reactorCloudFoundryClient); // app에 파일 업로드 작업을 합니다.
            final String APPLICATION_ID = applicationid;
            //Build와 Start 병렬실행
            Thread th = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                appServiceV3.createBuild(APPLICATION_ID, packageId, reactorCloudFoundryClient);
                                //앱 실행버튼이 on일때
                                if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) {
                                    procCatalogStartApplication(APPLICATION_ID, reactorCloudFoundryClient); //앱 시작
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    );
            th.start();
            appcreated = true;
            String appid = applicationid;
            if (param.getServicePlanList() != null) {
                param.getServicePlanList().forEach(serviceplan -> {
                    try {
                        serviceplan.setSpaceId(param.getSpaceId());
                        if (!serviceplan.getAppGuid().equals("(id_dummy)")) {
                            serviceplan.setAppGuid(appid);
                        }
                        serviceplan.setServiceInstanceGuid("");
                        serviceplan.setServiceInstanceGuid(procCatalogCreateServiceInstanceV2(true,serviceplan, reactorCloudFoundryClient).get("SERVICEID").toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            commonService.procCommonApiRestTemplate("/v2/history", HttpMethod.POST, param, null);
            return new HashMap<String, Object>() {{
                put("RESULT", Constants.RESULT_STATUS_SUCCESS);
            }};
        } catch (Exception e) {
            if (appcreated) {
                param.getServicePlanList().forEach(serviceplan -> {
                    if (!serviceplan.getServiceInstanceGuid().equals("")) {
                        reactorCloudFoundryClient.serviceInstances().delete(DeleteServiceInstanceRequest.builder().serviceInstanceId(serviceplan.getServiceInstanceGuid()).recursive(true).build()).block();
                    }
                });
            }
            if (!applicationid.equals("applicationID")) {
                if (!routeid.equals("route ID")) {
                    reactorCloudFoundryClient.routes().delete(DeleteRouteRequest.builder().routeId(routeid).build()).block();
                }
                reactorCloudFoundryClient.applicationsV2().delete(DeleteApplicationRequest.builder().applicationId(applicationid).build()).block();
            }
            return new HashMap<String, Object>() {{
                put("RESULT", Constants.RESULT_STATUS_FAIL);
                put("msg", e.getMessage());
            }};
        } finally {
            if (file != null) {
                boolean fileDeleteResult = file.delete();
            }
        }
    }

    /**
     * 앱을 생성한다.
     *
     * @param param                     Catalog
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private String createApplication(Catalog param, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        if (param.getBuildPackName().toLowerCase().contains(Constants.CATALOG_EGOV_BUILD_PACK_CHECK_STRING)) {
            return reactorCloudFoundryClient.
                    applicationsV2().create(CreateApplicationRequest.builder().buildpack(param.getBuildPackName()).memory(param.getMemorySize()).name(param.getAppName()).diskQuota(param.getDiskSize()).spaceId(param.getSpaceId()).environmentJsons(new HashMap<String, Object>() {{
                put(Constants.CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_KEY, Constants.CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_VALUE);
            }}).build()).block().getMetadata().getId();
        }

        return reactorCloudFoundryClient.
                applicationsV2().create(CreateApplicationRequest.builder().buildpack(param.getBuildPackName()).memory(param.getMemorySize()).name(param.getAppName()).diskQuota(param.getDiskSize()).spaceId(param.getSpaceId()).build()).block().getMetadata().getId();

    }

    /**
     * 라우트를 생성한다..
     *
     * @param param                     Catalog
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private String createRoute(Catalog param, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        return reactorCloudFoundryClient.
                routes().create(CreateRouteRequest.builder().host(param.getHostName()).domainId(param.getDomainId()).spaceId(param.getSpaceId()).build()).block().getMetadata().getId();
    }

    /**
     * 라우트를 앱에 매핑한다.
     *
     * @param applicationid             String
     * @param routeid                   String
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void routeMapping(String applicationid, String routeid, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        reactorCloudFoundryClient.
                routeMappings().create(CreateRouteMappingRequest.builder().routeId(routeid).applicationId(applicationid).build()).block();
    }

    /**
     * 파일을 업로드한다.
     *
     * @param file                      File
     * @param applicationid             String
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private String fileUpload(File file, String applicationid, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        try {
            // package 생성
            String packageId = reactorCloudFoundryClient.
                                packages().create(CreatePackageRequest.builder().type(PackageType.BITS).relationships(PackageRelationships.builder().application(ToOneRelationship.builder().data(Relationship.builder().id(applicationid).build()).build()).build()).build()).block().getId();
            // package 업로드
                reactorCloudFoundryClient.
                    packages().upload(UploadPackageRequest.builder()
                    .packageId(packageId)
                    .bits(file.toPath()).build()).block();
                
            // package 업로드 확인
            while(true){
                if(reactorCloudFoundryClient.
                packages().get(GetPackageRequest.builder()
                .packageId(packageId).build()).block().getState().getValue().equals("READY"))
                    break;
                Thread.sleep(1000);
            }
            return packageId;
        } catch (Exception e) {
            return null;
        }
        
    }

    /**
     * 임시 파일을 생성한다.
     *
     * @param param  Catalog(모델클래스)
     * @param token2 String(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private File createTempFile(Catalog param, String token2, HttpServletResponse response) throws Exception {

        try {
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
        } catch (Exception e) {
            //LOGGER.info(e.getMessage());
        }
        return null;
    }

    /**
     * 카탈로그 서비스 인스턴스를 생성한다.
     *
     * @param param                     Catalog(모델클래스)
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map procCatalogCreateServiceInstanceV2(boolean tmp, Catalog param, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        LOGGER.info(param.toString());
        LOGGER.info(parameterMap.toString());
        try {
            parameterMap = mapper.readValue(param.getParameter(), new TypeReference<Map<String, Object>>() {});
            if(tmp){
                parameterMap.put("app_guid",param.getAppGuid());
            }
            CreateServiceInstanceResponse createserviceinstanceresponse = reactorCloudFoundryClient.serviceInstances()
                    .create(CreateServiceInstanceRequest.builder()
                            .name(param.getName())
                            .spaceId(param.getSpaceId())
                            .parameters(parameterMap)
                            .servicePlanId(param.getServicePlan())
                            .build()).block();
            if (param.getOnDemandYn().equals("N") && !param.getAppGuid().equals("(id_dummy)")) {
                param.setServiceInstanceGuid(createserviceinstanceresponse.getMetadata().getId());
                procCatalogBindService(param, reactorCloudFoundryClient);
            } if (param.getCatalogType() != null) {
                commonService.procCommonApiRestTemplate("/v2/history", HttpMethod.POST, param, null);
            } return new HashMap() {{
                put("RESULT", Constants.RESULT_STATUS_SUCCESS);
                put("SERVICEID", createserviceinstanceresponse.getEntity().getServiceId());
            }};
        } catch (Exception e) {
            return new HashMap() {{
                put("RESULT", Constants.RESULT_STATUS_FAIL);
                put("MSG", e.getMessage());
                put("SERVICEID", "");
            }};
        }
    }

    /**
     * 카탈로그 앱 서비스를 바인드한다.
     *
     * @param param                     Catalog(모델클래스)
     * @param reactorCloudFoundryClient ReactorCloudFoundryClient
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public CreateServiceBindingResponse procCatalogBindService(Catalog param, ReactorCloudFoundryClient reactorCloudFoundryClient) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> bindparameterMap = mapper.readValue(param.getApp_bind_parameter(), new TypeReference<Map<String, Object>>() {
        });
        return reactorCloudFoundryClient.
                serviceBindingsV2().create(CreateServiceBindingRequest.builder().applicationId(param.getAppGuid()).serviceInstanceId(param.getServiceInstanceGuid()).parameters(bindparameterMap).build()).block();


    }

    /**
     * 서비스 인스턴스 목록들을 가져온다.
     *
     * @param orgid   org guid(조직 Guid)
     * @param spaceid space guid(공간 Guid)
     * @param token   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public ListServiceInstancesResponse listServiceInstancesResponse(String orgid, String spaceid, String token) {
        ListServiceInstancesResponse listServiceInstancesResponse = cloudFoundryClient(connectionContext(), tokenProvider(token)).
                serviceInstances().list(ListServiceInstancesRequest.builder().organizationId(orgid).spaceId(spaceid).build()).block();
        return listServiceInstancesResponse;
    }

    /**
     * 서비스 전체 목록을 가져온다.
     *
     * @return ListServicesResponse
     */
    public ListServicesResponse getService() throws Exception {
        return cloudFoundryClient(connectionContext(), tokenProvider()).services().list(ListServicesRequest.builder().build()).block();
    }
}
