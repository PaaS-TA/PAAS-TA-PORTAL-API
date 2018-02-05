package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.*;
import org.cloudfoundry.client.lib.org.codehaus.jackson.map.ObjectMapper;
import org.cloudfoundry.client.lib.org.codehaus.jackson.type.TypeReference;
import org.cloudfoundry.client.lib.util.JsonUtil;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.mapper.cc.CatalogCcMapper;
import org.openpaas.paasta.portal.api.mapper.portal.CatalogMapper;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.model.Catalog;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.*;

import static java.util.Optional.*;
import static java.util.stream.Collectors.*;

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


    private final CatalogMapper catalogMapper;
    private final CatalogCcMapper catalogCcMapper;
    private final CommonService commonService;
    private final GlusterfsServiceImpl glusterfsService;
    private final CommonCodeService commonCodeService;
    private final SpaceService spaceService;
    private final DomainService domainService;
    private final AppService appService;

    @Value("${cloudfoundry.authorization}")
    private String cfAuthorizationHeaderKey;

    @Autowired
    public CatalogService(CatalogMapper catalogMapper,
                          CatalogCcMapper catalogCcMapper,
                          CommonCodeService commonCodeService,
                          SpaceService spaceService,
                          GlusterfsServiceImpl glusterfsService,
                          DomainService domainService,
                          AppService appService,
                          CommonService commonService) throws Exception {
        this.catalogMapper = catalogMapper;
        this.catalogCcMapper = catalogCcMapper;
        this.commonCodeService = commonCodeService;
        this.spaceService = spaceService;
        this.glusterfsService = glusterfsService;
        this.domainService = domainService;
        this.appService = appService;
        this.commonService = commonService;
    }


    /**
     * 앱 개발환경 목록을 조회한다.
     *
     * @param req HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @SuppressWarnings(value = "unchecked")
    public Map<String, Object> getBuildPackList(HttpServletRequest req) throws Exception {
        CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey));
        Map<String, Map<String, Object>> buildPacksMap = customCloudFoundryClient.getBuildPacks();
        List<Map<String, Map<String, Object>>> resourcesList = (List<Map<String, Map<String, Object>>>) buildPacksMap.get("resources");
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Map<String, Map<String, Object>> resource : resourcesList) {
            Map<String, Object> resourceMap = new HashMap<>();

            resourceMap.put("name", resource.get("entity").get("name"));
            resourceMap.put("value", resource.get("entity").get("name"));

            resultList.add(resourceMap);
        }

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }


    /**
     * 서비스 목록을 조회한다.
     *
     * @param req HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getServicePackList(HttpServletRequest req) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();

        CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey));
        List<CloudServiceOffering> serviceOfferingsList = cloudFoundryClient.getServiceOfferings();

        for (CloudServiceOffering cloudServiceOffering : serviceOfferingsList) {
            Map<String, Object> resourceMap = new HashMap<>();

            resourceMap.put("name", cloudServiceOffering.getName());
            resourceMap.put("value", cloudServiceOffering.getName());
            resourceMap.put("guid", cloudServiceOffering.getMeta().getGuid());

            resultList.add(resourceMap);
        }

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }


    /**
     * 앱 개발환경 카탈로그 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getBuildPackCatalogList(Catalog param) {
        return new HashMap<String, Object>() {{
            put("list", catalogMapper.getBuildPackCatalogList(param));
        }};
    }


    /**
     * 서비스 카탈로그 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getServicePackCatalogList(Catalog param) {
        return new HashMap<String, Object>() {{
            put("list", catalogMapper.getServicePackCatalogList(param));
        }};
    }


    /**
     * 앱 개발환경 카탈로그 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getBuildPackCatalogCount(Catalog param, HttpServletResponse res) throws Exception {
        if (catalogMapper.getBuildPackCatalogCount(param) > 0) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 서비스 카탈로그 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getServicePackCatalogCount(Catalog param, HttpServletResponse res) throws Exception {
        if (catalogMapper.getServicePackCatalogCount(param) > 0) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 앱 개발환경 카탈로그를 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertBuildPackCatalog(Catalog param) {
        catalogMapper.insertBuildPackCatalog(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 서비스 카탈로그를 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertServicePackCatalog(Catalog param) {
        catalogMapper.insertServicePackCatalog(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 앱 개발환경 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> updateBuildPackCatalog(Catalog param) {
        catalogMapper.updateBuildPackCatalog(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 서비스 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> updateServicePackCatalog(Catalog param) {
        catalogMapper.updateServicePackCatalog(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 앱 개발환경 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteBuildPackCatalog(Catalog param) {
        catalogMapper.deleteBuildPackCatalog(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 서비스 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteServicePackCatalog(Catalog param) {
        catalogMapper.deleteServicePackCatalog(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 앱 개발환경 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCheckDeleteBuildPackCatalogCount(Catalog param, HttpServletResponse res) throws Exception {
        if (catalogMapper.getCheckDeleteBuildPackCatalogCount(param) > 0) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.starter.delete");
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 서비스 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCheckDeleteServicePackCatalogCount(Catalog param, HttpServletResponse res) throws Exception {
        if (catalogMapper.getCheckDeleteServicePackCatalogCount(param) > 0) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.starter.delete");
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 앱 템플릿 카탈로그 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getStarterCatalogCount(Catalog param, HttpServletResponse res) throws Exception {
        if (catalogMapper.getStarterCatalogCount(param) > 0) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 앱 템플릿명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getStarterNamesList(Catalog param) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("list", catalogMapper.getStarterNamesList(param));
        return resultMap;
    }


    /**
     * 앱 개발환경명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getBuildPackNamesList(Catalog param) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("list", catalogMapper.getBuildPackNamesList(param));
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * 서비스명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getServicePackNamesList(Catalog param) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("list", catalogMapper.getServicePackNamesList(param));
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * 앱 템플릿 카탈로그 조회
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getOneStarterCatalog(Catalog param) {
        Map<String, Object> resultMap = new HashMap<>();

        List<Integer> paramForServices = catalogMapper.getSelectedServicePackList(param);
        Catalog paramForOneStarterCatalog = catalogMapper.getOneStarterCatalog(param);

        paramForOneStarterCatalog.setServicePackCategoryNoList(paramForServices);
        paramForOneStarterCatalog.setStarterCategoryNo(paramForOneStarterCatalog.getNo());

        resultMap.put("info", paramForOneStarterCatalog);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * 앱 템플릿 카탈로그 최대값울 조회한다.
     *
     * @return Map(자바클래스)
     */
    int getStarterCatalogMaxNumber() {
        return catalogMapper.getStarterCatalogMaxNumber();
    }


    /**
     * 앱 템플릿 카탈로그를 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertStarterCatalog(Catalog param) {
        Map<String, Object> resultMap = new HashMap<>();

        catalogMapper.insertStarterCatalog(param);

        for (int i = 0; i < param.getServicePackCategoryNoList().size(); i++) {
            catalogMapper.insertSelectedServicePackList(param.getServicePackCategoryNoList().get(i));
        }

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * 앱 템플릿 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> updateStarterCatalog(Catalog param) {
        Map<String, Object> resultMap = new HashMap<>();

        catalogMapper.updateStarterCatalog(param);
        catalogMapper.deleteSelectedServicePackList(param);

        for (int i = 0; i < param.getServicePackCategoryNoList().size(); i++) {
            catalogMapper.insertSelectedServicePackListForUpdate(param, param.getServicePackCategoryNoList().get(i));
        }

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * 앱 템플릿 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteStarterCatalog(Catalog param) {
        Map<String, Object> resultMap = new HashMap<>();

        catalogMapper.deleteSelectedServicePackList(param);
        catalogMapper.deleteStarterCatalog(param);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * 파일을 업로드한다.
     *
     * @param multipartFile MultipartFile(Spring 클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> uploadFile(MultipartFile multipartFile) throws Exception {
        return new HashMap<String, Object>() {{
            put("path", glusterfsService.upload(multipartFile));
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 파일을 삭제한다.
     *
     * @param fileUriPath String(파일 경로)
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteFile(String fileUriPath) {
        glusterfsService.delete(fileUriPath);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 좌측 메뉴 목록을 조회한다.
     *
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCatalogLeftMenuList() throws Exception {
        return new HashMap<String, Object>() {{
            put("starterList", commonCodeService.getCommonCodeById(Constants.STARTER_CATALOG_ID).get("list"));
            put("buildPackList", commonCodeService.getCommonCodeById(Constants.BUILD_PACK_CATALOG_ID).get("list"));
            put("servicePackList", commonCodeService.getCommonCodeById(Constants.SERVICE_PACK_CATALOG_ID).get("list"));
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 내역 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getCatalogHistoryList(Catalog param) {
        param.setLimitSize(Constants.CATALOG_HISTORY_LIMIT_SIZE);

        return new HashMap<String, Object>() {{
            put("list", catalogMapper.getCatalogHistoryList(param));
        }};
    }


    /**
     * 카탈로그 공간 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCatalogSpaceList(Catalog param, HttpServletRequest req) throws Exception {
        return new HashMap<String, Object>() {{
            put("list", spaceService.getSpaces(new Org() {{
                setOrgName(param.getOrgName());
            }}, req.getHeader(cfAuthorizationHeaderKey)));
        }};
    }


    /**
     * 카탈로그 도메인 목록을 조회한다.
     *
     * @param req HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCatalogDomainList(HttpServletRequest req) throws Exception {
        return new HashMap<String, Object>() {{
            put("list", domainService.getDomains(req.getHeader(cfAuthorizationHeaderKey), Constants.REQUEST_DOMAIN_STATUS_SHARED));
        }};
    }


    /**
     * 카탈로그 서비스 이용사양 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCatalogServicePlanList(Catalog param, HttpServletRequest req) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();

        CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
        List<CloudServiceOffering> serviceOfferingsList = cloudFoundryClient.getServiceOfferings();

        serviceOfferingsList.stream()
                .filter(cso -> of(param).map(Catalog::getServicePackName).orElse("").equals(cso.getName()))
                .flatMap(cos -> cos.getCloudServicePlans().stream())
                .collect(toList()).forEach(cloudServicePlan -> resultList.add(new HashMap<String, Object>() {{
            put("name", cloudServicePlan.getName());
            put("value", cloudServicePlan.getName());
            put("description", cloudServicePlan.getDescription());
            put("guid", cloudServicePlan.getMeta().getGuid());
        }}));

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }


    /**
     * 카탈로그 서비스 이용사양 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCatalogMultiServicePlanList(Catalog param, HttpServletRequest req) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Catalog> reqList = param.getServicePlanList();

        CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
        List<CloudServiceOffering> serviceOfferingsList = cloudFoundryClient.getServiceOfferings();

        for (Catalog reqCatalog : reqList) {
            List<Map<String, Object>> resultSubList = new ArrayList<>();

            serviceOfferingsList
                    .stream()
                    .filter(cso -> cso.getName().equals(reqCatalog.getServicePackName()))
                    .forEach(cso2 -> {
                        List<CloudServicePlan> cloudServicePlansList = cso2.getCloudServicePlans();

                        for (CloudServicePlan cloudServicePlan : cloudServicePlansList) {
                            Map<String, Object> resultMap = new HashMap<>();

                            resultMap.put("name", cloudServicePlan.getName());
                            resultMap.put("value", cloudServicePlan.getName());
                            resultMap.put("description", cloudServicePlan.getDescription());
                            resultMap.put("guid", cloudServicePlan.getMeta().getGuid());

                            resultSubList.add(resultMap);
                        }
                    });

            resultList.add(new HashMap<String, Object>() {{
                put("servicePackName", reqCatalog.getServicePackName());
                put("name", reqCatalog.getName());
                put("parameter", reqCatalog.getParameter());
                put("appBindYn",reqCatalog.getAppBindYn());
                put("app_bind_parameter", reqCatalog.getApp_bind_parameter());
                put("list", resultSubList);
            }});
        }

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }


    /**
     * 카탈로그 앱 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCatalogAppList(Catalog param, HttpServletRequest req) throws Exception {
        return new HashMap<String, Object>() {{
            put("list", spaceService.getSpaceSummary(new Space() {{
                setOrgName(param.getOrgName());
                setSpaceName(param.getSpaceName());
            }}, req.getHeader(cfAuthorizationHeaderKey)).getApps());
        }};
    }


    /**
     * 카탈로그 앱 이름 생성여부를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCheckCatalogApplicationNameExists(Catalog param, HttpServletRequest req, HttpServletResponse res) throws Exception {
        List<App> resultList = spaceService.getSpaceSummary(new Space() {{
            setOrgName(param.getOrgName());
            setSpaceName(param.getSpaceName());
        }}, req.getHeader(cfAuthorizationHeaderKey)).getApps();

        for (App app : resultList) {
            if (app.getName().equals(param.getName())) {
                commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");
            }
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 서비스 이름 생성여부를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCheckCatalogServiceInstanceNameExists(Catalog param, HttpServletRequest req, HttpServletResponse res) throws Exception {
        CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
        CloudService cloudService = cloudFoundryClient.getService(param.getServiceInstanceName());

        if (null != cloudService) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated.service.instance.name");
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 앱 URL 생성여부를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getCheckCatalogRouteExists(Catalog param, HttpServletResponse res) throws Exception {
        String reqDomainName = param.getDomainName();
        String reqRouteName = param.getRouteName();
        int resultCount = 0;
        Map resultMap;

        int domainId = catalogCcMapper.getDomainId(reqDomainName);
        List resultList = catalogCcMapper.getRouteHostNameList(domainId);

        for (Object resultObject : resultList) {
            resultMap = (HashMap) resultObject;

            if (reqRouteName.equals(resultMap.get("hostname") + "." + reqDomainName)) resultCount++;
        }

        if (resultCount > 0) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 카탈로그 앱 템플릿 구성을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getCatalogStarterRelationList(Catalog param) {
        List<Catalog> catalogs = catalogMapper.getCatalogStarterRelationServicePackList(param);
        return new HashMap<String, Object>() {{
            put("buildPackList", catalogMapper.getCatalogStarterRelationBuildPackList(param));
            put("servicePackList", catalogMapper.getCatalogStarterRelationServicePackList(param));
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
    public Map<String, Object> executeCatalogStarter(Catalog param, HttpServletRequest req) throws Exception {
        // CREATE APPLICATION
        this.procCatalogCreateApplication(param, req);

        // IF START APPLICATION
        // UPLOAD APPLICATION
        if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) {
            // UPLOAD APPLICATION
            this.procCatalogUploadApplication(param, req);
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
            Map<String, Object> tempMap = this.procCatalogCreateServiceInstanceV2(servicePlan, req);

            // FOR TEST CASE
            Catalog bindParam = new Catalog();
            bindParam.setServiceInstanceGuid(UUID.fromString(tempMap.get("SERVICE_INSTANCE_GUID").toString()));
            serviceInstanceGuidList.add(bindParam);

            // BIND SERVICE
            if (Constants.USE_YN_Y.equals(servicePlan.getAppBindYn())) {
                bindParam.setOrgName(param.getOrgName());
                bindParam.setSpaceName(param.getSpaceName());
                bindParam.setAppName(param.getName());
                bindParam.setApp_bind_parameter(servicePlan.getApp_bind_parameter());

                this.procCatalogBindService(bindParam, req);
            }
        }

        // IF START APPLICATION
        UUID resultAppGuid = null;
        if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) {
            // START APPLICATION
            Map<String, Object> resultMap = this.procCatalogStartApplication(param, req);
            resultAppGuid = (UUID) resultMap.get("APP_GUID");
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
        catalogMapper.insertCatalogHistory(param);

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
    public Map<String, Object> executeCatalogBuildPack(Catalog param, HttpServletRequest req) throws Exception {
        UUID resultAppGuid = null;

        // CREATE APPLICATION
        this.procCatalogCreateApplication(param, req);

        // UPLOAD APPLICATION
        if (!Constants.USE_YN_N.equals(param.getAppSampleFilePath())) this.procCatalogUploadApplication(param, req);

        if (Constants.USE_YN_Y.equals(param.getAppSampleStartYn())) {
            // START APPLICATION
            Map<String, Object> resultMap = this.procCatalogStartApplication(param, req);
            resultAppGuid = (UUID) resultMap.get("APP_GUID");
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
        catalogMapper.insertCatalogHistory(param);

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
            bindParam.setServiceInstanceGuid(UUID.fromString(resultMap.get("SERVICE_INSTANCE_GUID").toString()));
            bindParam.setOrgName(param.getOrgName());
            bindParam.setSpaceName(param.getSpaceName());
            bindParam.setAppName(param.getAppName());
            bindParam.setParameter(param.getParameter());

            this.procCatalogBindService(bindParam, req);
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
        try{
                // CREATE SERVICE INSTANCE
                Map<String, Object> resultMap = this.procCatalogCreateServiceInstanceV2(param, req);

                // BIND SERVICE
                if (Constants.USE_YN_Y.equals(param.getAppBindYn())) {
                    Catalog bindParam = new Catalog();
                    bindParam.setServiceInstanceGuid(UUID.fromString(resultMap.get("SERVICE_INSTANCE_GUID").toString()));
                    bindParam.setOrgName(param.getOrgName());
                    bindParam.setSpaceName(param.getSpaceName());
                    bindParam.setAppName(param.getAppName());
                    bindParam.setParameter(param.getApp_bind_parameter());

                    this.procCatalogBindService(bindParam, req);
                }
                return new HashMap<String, Object>() {{
                    put("SERVICE_INSTANCE_GUID", resultMap.get("SERVICE_INSTANCE_GUID")); // FOR TEST CASE
                    put("RESULT", Constants.RESULT_STATUS_SUCCESS);
                }};

            } catch (Exception e){
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
        catalogMapper.insertCatalogHistory(param);

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
        if (buildPackName.toLowerCase().contains(Constants.CATALOG_EGOV_BUILD_PACK_CHECK_STRING)) {
            CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
            customCloudFoundryClient.createApplicationV2(appName, staging, disk, memory, uris, new HashMap<String, Object>() {{
                put(Constants.CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_KEY, Constants.CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_VALUE);
            }});

        } else {
            CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
            cloudFoundryClient.createApplication(appName, staging, disk, memory, uris, null);
        }
    }


    /**
     * 카탈로그 앱을 업로드한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void procCatalogUploadApplication(Catalog param, HttpServletRequest req) throws Exception {
        CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
        String appName = param.getName();

        cloudFoundryClient.uploadApplication(appName, appName, new URL(param.getAppSampleFilePath()).openStream());
    }


    /**
     * 카탈로그 앱을 시작한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> procCatalogStartApplication(Catalog param, HttpServletRequest req) throws Exception {
        CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());
        String appName = param.getName();

//        // START APPLICATION
//        appService.startApp(new App() {{
//            setName(appName);
//        }}, cloudFoundryClient);
//
//        // GET APP GUID (FOR TEST CASE)
//        CloudApplication cloudApplication = cloudFoundryClient.getApplication(appName);
//        UUID resultAppGuid = cloudApplication.getMeta().getGuid();
//
//        return new HashMap<String, Object>() {{
//            put("APP_GUID", resultAppGuid);
//            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
//        }};
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
        CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());

        // CREATE SERVICE INSTANCE
        String resultString = customCloudFoundryClient.createService(param.getServiceInstanceName(), param.getServicePlan(), param.getOrgName(), param.getSpaceName());

        Map<String, Object> tempMap = JsonUtil.convertJsonToMap(resultString);
        Map tempSubMap = (Map) tempMap.get("metadata"); // FOR TEST CASE

        return new HashMap<String, Object>() {{
            put("SERVICE_INSTANCE_GUID", tempSubMap.get("guid"));
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
    private Map<String, Object> procCatalogCreateServiceInstanceV2(Catalog param, HttpServletRequest req) throws Exception {
        CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());

            String tempParameter = param.getParameter();
            if (null == tempParameter || "".equals(tempParameter)) param.setParameter("{}");

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> parameterMap = mapper.readValue(param.getParameter(), new TypeReference<Map<String, Object>>() {
            });

            // CREATE SERVICE INSTANCE
            String resultString = customCloudFoundryClient.createServiceV2(param.getServiceInstanceName(), param.getServicePlan(), param.getOrgName(), param.getSpaceName(), parameterMap);

            Map<String, Object> tempMap = JsonUtil.convertJsonToMap(resultString);
            Map tempSubMap = (Map) tempMap.get("metadata"); // FOR TEST CASE

            return new HashMap<String, Object>() {{
                put("SERVICE_INSTANCE_GUID", tempSubMap.get("guid"));
                put("RESULT", Constants.RESULT_STATUS_SUCCESS);
            }};

    }

    /**
     * 카탈로그 앱 서비스를 바인드한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> procCatalogBindService(Catalog param, HttpServletRequest req) throws Exception {
        CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(cfAuthorizationHeaderKey), param.getOrgName(), param.getSpaceName());

        String tempParameter = param.getApp_bind_parameter();
        if (null == tempParameter || "".equals(tempParameter)) param.setApp_bind_parameter("{}");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.readValue(param.getApp_bind_parameter(), new TypeReference<Map<String, Object>>() {
        });

        // BIND SERVICE
        customCloudFoundryClient.bindServiceV2(param.getServiceInstanceGuid(), param.getAppName(), parameterMap);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }
}
