package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v2.applications.ListApplicationsResponse;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebindings.GetServiceBindingResponse;
import org.cloudfoundry.client.v2.serviceinstances.CreateServiceInstanceResponse;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansResponse;
import org.cloudfoundry.client.v2.services.ListServicesResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Catalog;
import org.openpaas.paasta.portal.api.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 서비스 카탈로그, 개발 환경 카탈로그, 앱 템플릿 카탈로그 정보 조회 및 관리 등의 API 를 호출 받는 컨트롤러이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.07.04 최초작성
 */
@RestController
public class CatalogController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogController.class);
    private final CatalogService catalogService;
    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * 카탈로그 서비스 이용사양 목록을 조회한다.
     *
     * @param servicename String(자바클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    //@RequestMapping(value = {"/getCatalogServicePlanList"}, method = RequestMethod.POST, consumes = "application/json")
    @GetMapping(Constants.V2_URL+"/catalogs/serviceplan/{servicename}")
    public ListServicePlansResponse getCatalogServicePlanList(@PathVariable String servicename, HttpServletRequest req) throws Exception {
        return catalogService.getCatalogServicePlanList(servicename, req);
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
    @GetMapping(Constants.V2_URL + "/catalogs/apps/{orgid}/{spaceid}")
    public ListApplicationsResponse getCatalogAppList(@PathVariable String orgid, @PathVariable String spaceid, HttpServletRequest req) throws Exception {
        return catalogService.getCatalogAppList(orgid, spaceid, req);
    }


    /**
     * 카탈로그 앱 이름 생성여부를 조회한다.
     *
     * @param name appname(앱이름)
     * @param req   HttpServletRequest(자바클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    //@RequestMapping(value = {"/getCheckCatalogApplicationNameExists"}, method = RequestMethod.POST, consumes = "application/json")
    @GetMapping(Constants.V2_URL+"/catalogs/apps/{name}")
    public Map<String, Object> getCheckCatalogApplicationNameExists(@PathVariable String name, @RequestParam String orgid, @RequestParam String spaceid, HttpServletRequest req, HttpServletResponse res) throws Exception {
        return catalogService.getCheckCatalogApplicationNameExists(name,orgid,spaceid, req, res);
    }



    /**
     * 카탈로그 앱 템플릿을 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/executeCatalogStarter"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> executeCatalogStarter(@RequestBody Catalog param, HttpServletRequest req,HttpServletResponse response) throws Exception {
        return catalogService.executeCatalogStarter(param, req,response);
    }


    /**
     * 카탈로그 앱 템플릿 내역을 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/insertCatalogHistoryStarter"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertCatalogHistoryStarter(@RequestBody Catalog param) {
        return catalogService.insertCatalogHistoryStarter(param);
    }


    /**
     * 카탈로그 앱 개발환경을 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/executeCatalogBuildPack"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> executeCatalogBuildPack(@RequestBody Catalog param, HttpServletRequest req, HttpServletResponse response) throws Exception {
        return catalogService.executeCatalogBuildPack(param, req, response);
    }


    /**
     * 카탈로그 앱 개발환경 내역을 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/insertCatalogHistoryBuildPack"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertCatalogHistoryBuildPack(@RequestBody Catalog param) {
        return catalogService.insertCatalogHistoryBuildPack(param);
    }

    @GetMapping(Constants.V2_URL+"/catalogs/servicepack/{orgid}/{spaceid}")
    public ListServiceInstancesResponse listServiceInstancesResponse(@PathVariable String orgid, @PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return catalogService.listServiceInstancesResponse(orgid, spaceid, token);
    }


    /**
     * 카탈로그 서비스를 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/executeCatalogServicePack"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> executeCatalogServicePack(@RequestBody Catalog param, HttpServletRequest req) throws Exception {
        return catalogService.executeCatalogServicePack(param, req);
    }

    /**
     * 카탈로그 서비스를 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/executeCatalogServicePackV2"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> executeCatalogServicePackV2(@RequestBody Catalog param, HttpServletRequest req) throws Exception {
        LOGGER.info("executeCatalogServicePackV2");
        LOGGER.info("parameter : " + param.getParameter());
        LOGGER.info("app_bind_parameter : " + param.getApp_bind_parameter());
        return catalogService.executeCatalogServicePackV2(param, req);
    }

    /**
     * 카탈로그 서비스 실행 내역을 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/insertCatalogHistoryServicePack"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertCatalogHistoryServicePack(@RequestBody Catalog param) {
        return catalogService.insertCatalogHistoryServicePack(param);
    }

    /**
     * 서비스 인스턴스와 앱 바인딩을생성한다.
     *
     * @param param Catalog(모델클래스)
     * @param token   String(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @PostMapping(Constants.V2_URL+"/catalogs/serviceinstances")
    public CreateServiceBindingResponse procCatalogCreateServiceInstanceV2(@RequestBody Catalog param, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return catalogService.procCatalogCreateServiceInstanceV2(param, token);
    }

    /**
     * 앱을 생성한다.
     *
     * @param param Catalog(모델클래스)
     * @param token,token2   HttpServletRequest(자바클래스)
     * @param response   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @PostMapping(Constants.V2_URL+"/catalogs/app")
    public Map<String, Object> createApp(@RequestBody Catalog param,   @RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestHeader("User-Agent") String token2, HttpServletResponse response) throws  Exception{
        return catalogService.createApp(param, token, token2, response);
    }

    /**
     * 서비스 전체 목록을 가져온다.
     *
     * @return ListServicesResponse
     * @throws Exception Exception(자바클래스)
     */
    @GetMapping(Constants.V2_URL+"/services")
    public ListServicesResponse getService() throws Exception {
        return catalogService.getService();
    }
}