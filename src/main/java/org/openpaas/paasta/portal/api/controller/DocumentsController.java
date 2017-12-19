package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.model.Support;
import org.openpaas.paasta.portal.api.service.DocumentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * DocumentsController.java
 * 문서 조회, 등록, 수정 등 문서 관리에 필요한 API 를 호출 받는 컨트롤러
 *
 * @author 김영지
 * @version 1.0
 * @since 2016.07.28 최초작성
 */
@RestController
@RequestMapping(value = {"/documents"})
public class DocumentsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentsController.class);

    @Autowired
    private DocumentsService documentsService;


    /**
     * 문서 목록 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map documents list
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getDocumentsList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getDocumentsList(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getDocumentsList :: param :: {}, param.toString()");
        return documentsService.getDocumentsList(param);
    }

    /**
     * 문서 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map document
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getDocument"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getDocument(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getDocument :: param :: {}, param.toString()");
        return documentsService.getDocument(param);
    }

    /**
     * 문서 등록
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/insertDocument"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertDocument(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("insertDocument :: param :: {}, param.toString()");
        return documentsService.insertDocument(param);
    }

    /**
     * 문서 수정
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateDocument"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateDocument(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("updateDocument :: param :: {}, param.toString()");
        return documentsService.updateDocument(param);
    }

    /**
     * 문서 삭제
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteDocument"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteDocument(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("deleteDocument :: param :: {}, param.toString()");
        return documentsService.deleteDocument(param);
    }

}
