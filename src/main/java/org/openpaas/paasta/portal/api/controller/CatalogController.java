package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
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
     * 앱 개발환경 목록을 조회한다.
     *
     * @param req HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getBuildPackList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getBuildPackList(HttpServletRequest req) throws Exception {
        return catalogService.getBuildPackList(req);
    }


    /**
     * 서비스 목록을 조회한다.
     *
     * @param req HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getServicePackList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getServicePackList(HttpServletRequest req) throws Exception {
        return catalogService.getServicePackList(req);
    }


    /**
     * 앱 개발환경 카탈로그 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getBuildPackCatalogList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getBuildPackCatalogList(@RequestBody Catalog param) {
        return catalogService.getBuildPackCatalogList(param);
    }


    /**
     * 서비스 카탈로그 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getServicePackCatalogList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getServicePackCatalogList(@RequestBody Catalog param) {
        return catalogService.getServicePackCatalogList(param);
    }


    /**
     * 앱 개발환경 목록 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getBuildPackCatalogCount"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getBuildPackCatalogCount(@RequestBody Catalog param, HttpServletResponse res) throws Exception {
        return catalogService.getBuildPackCatalogCount(param, res);
    }


    /**
     * 서비스 목록 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getServicePackCatalogCount"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getServicePackCatalogCount(@RequestBody Catalog param, HttpServletResponse res) throws Exception {
        return catalogService.getServicePackCatalogCount(param, res);
    }


    /**
     * 앱 개발환경 카탈로그를 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/insertBuildPackCatalog"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertBuildPackCatalog(@RequestBody Catalog param) {
        return catalogService.insertBuildPackCatalog(param);
    }


    /**
     * 서비스 카탈로그를 저장한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/insertServicePackCatalog"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertServicePackCatalog(@RequestBody Catalog param) {
        return catalogService.insertServicePackCatalog(param);
    }


    /**
     * 앱 개발환경 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/updateBuildPackCatalog"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateBuildPackCatalog(@RequestBody Catalog param) {
        return catalogService.updateBuildPackCatalog(param);
    }


    /**
     * 서비스 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/updateServicePackCatalog"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateServicePackCatalo1g(@RequestBody Catalog param) {
        return catalogService.updateServicePackCatalog(param);
    }


    /**
     * 앱 개발환경 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/deleteBuildPackCatalog"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteBuildPackCatalog(@RequestBody Catalog param) {
        return catalogService.deleteBuildPackCatalog(param);
    }


    /**
     * 서비스 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/deleteServicePackCatalog"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteServicePackCatalog(@RequestBody Catalog param) {
        return catalogService.deleteServicePackCatalog(param);
    }


    /**
     * 앱 개발환경 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCheckDeleteBuildPackCatalogCount"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCheckDeleteBuildPackCatalogCount(@RequestBody Catalog param, HttpServletResponse res) throws Exception {
        return catalogService.getCheckDeleteBuildPackCatalogCount(param, res);
    }


    /**
     * 서비스 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCheckDeleteServicePackCatalogCount"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCheckDeleteServicePackCatalogCount(@RequestBody Catalog param, HttpServletResponse res) throws Exception {
        return catalogService.getCheckDeleteServicePackCatalogCount(param, res);
    }


    /**
     * 앱 템플릿 카탈로그 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getStarterCatalogCount"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getStarterCatalogCount(@RequestBody Catalog param, HttpServletResponse res) throws Exception {
        return catalogService.getStarterCatalogCount(param, res);
    }


    /**
     * 앱 템플릿명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getStarterNamesList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getStarterNames(@RequestBody Catalog param) {
        return catalogService.getStarterNamesList(param);
    }


    /**
     * 앱 개발환경명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getBuildPackNamesList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getBuildPackNamesList(@RequestBody Catalog param) {
        return catalogService.getBuildPackNamesList(param);
    }


    /**
     * 서비스명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getServicePackNamesList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getServicePackNamesList(@RequestBody Catalog param) {
        return catalogService.getServicePackNamesList(param);
    }


    /**
     * 앱 템플릿 카탈로그를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getOneStarterCatalog"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getOneStarterCatalog(@RequestBody Catalog param) {
        return catalogService.getOneStarterCatalog(param);
    }


    /**
     * 앱 템플릿 카탈로그를 등록한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/insertStarterCatalog"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertStarterCatalog(@RequestBody Catalog param) {
        return catalogService.insertStarterCatalog(param);
    }


    /**
     * 앱 템플릿 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/updateStarterCatalog"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateStarterCatalog(@RequestBody Catalog param) {
        return catalogService.updateStarterCatalog(param);
    }


    /**
     * 앱 템플릿 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/deleteStarterCatalog"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteStarterCatalog(@RequestBody Catalog param) {
        return catalogService.deleteStarterCatalog(param);
    }


    /**
     * 이미지 파일을 업로드한다.
     *
     * @param multipartFile MultipartFile(Spring 클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/uploadThumbnailImage"}, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadThumbnailImage(@RequestParam(value = "file", required = false) MultipartFile multipartFile) throws Exception {
        return catalogService.uploadFile(multipartFile);
    }


    /**
     * 이미지 파일을 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/deleteThumbnailImage"}, method = RequestMethod.POST)
    public Map<String, Object> deleteThumbnailImage(@RequestBody Catalog param) {
        return catalogService.deleteFile(param.getThumbImgPath());
    }


    /**
     * 앱 샘플 파일을 업로드한다.
     *
     * @param multipartFile MultipartFile(Spring 클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/uploadAppSampleFile"}, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadAppSampleFile(@RequestParam(value = "file", required = false) MultipartFile multipartFile) throws Exception {
        return catalogService.uploadFile(multipartFile);
    }


    /**
     * 앱 샘플 파일을 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/deleteAppSampleFile"}, method = RequestMethod.POST)
    public Map<String, Object> deleteAppSampleFile(@RequestBody Catalog param) {
        return catalogService.deleteFile(param.getAppSampleFilePath());
    }


    /**
     * 카탈로그 좌측 메뉴 목록을 조회한다.
     *
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCatalogLeftMenuList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCatalogLeftMenuList() throws Exception {
        return catalogService.getCatalogLeftMenuList();
    }


    /**
     * 카탈로그 내역 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getCatalogHistoryList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCatalogHistoryList(@RequestBody Catalog param) {
        return catalogService.getCatalogHistoryList(param);
    }

    /**
     * 카탈로그 공간 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCatalogSpaceList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCatalogSpaceList(@RequestBody Catalog param, HttpServletRequest req) throws Exception {
        return catalogService.getCatalogSpaceList(param, req);
    }


    /**
     * 카탈로그 도메인 목록을 조회한다.
     *
     * @param req HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCatalogDomainList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCatalogDomainList(HttpServletRequest req) throws Exception {
        return catalogService.getCatalogDomainList(req);
    }


    /**
     * 카탈로그 서비스 이용사양 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCatalogServicePlanList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCatalogServicePlanList(@RequestBody Catalog param, HttpServletRequest req) throws Exception {
        return catalogService.getCatalogServicePlanList(param, req);
    }


    /**
     * 카탈로그 서비스 이용사양 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCatalogMultiServicePlanList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCatalogMultiServicePlanList(@RequestBody Catalog param, HttpServletRequest req) throws Exception {
        return catalogService.getCatalogMultiServicePlanList(param, req);
    }


    /**
     * 카탈로그 앱 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCatalogAppList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCatalogAppList(@RequestBody Catalog param, HttpServletRequest req) throws Exception {
        return catalogService.getCatalogAppList(param, req);
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
    @RequestMapping(value = {"/getCheckCatalogApplicationNameExists"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCheckCatalogApplicationNameExists(@RequestBody Catalog param, HttpServletRequest req, HttpServletResponse res) throws Exception {
        return catalogService.getCheckCatalogApplicationNameExists(param, req, res);
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
    @RequestMapping(value = {"/getCheckCatalogServiceInstanceNameExists"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCheckCatalogServiceInstanceNameExists(@RequestBody Catalog param, HttpServletRequest req, HttpServletResponse res) throws Exception {
        return catalogService.getCheckCatalogServiceInstanceNameExists(param, req, res);
    }


    /**
     * 카탈로그 앱 URL 생성여부를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getCheckCatalogRouteExists"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCheckCatalogRouteExists(@RequestBody Catalog param, HttpServletResponse res) throws Exception {
        return catalogService.getCheckCatalogRouteExists(param, res);
    }


    /**
     * 카탈로그 앱 템플릿 구성을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getCatalogStarterRelationList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCatalogStarterRelationList(@RequestBody Catalog param) {
        return catalogService.getCatalogStarterRelationList(param);
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
     * 앱 서비스 바인드를 실행한다.
     *
     * @param param Catalog(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/appBindServiceV2"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> appBindServiceV2(@RequestBody Catalog param, HttpServletRequest req) throws Exception {
        return catalogService.procCatalogBindService(param, req);
    }

    @PostMapping("/v2/catalogs/app")
    public Map<String, Object> createApp(@RequestBody Catalog param,   HttpServletRequest req, HttpServletResponse response) throws  Exception{
        return catalogService.createApp(param, req, response);
    }
}