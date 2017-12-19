package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.model.Support;
import org.openpaas.paasta.portal.api.service.SupportNoticeService;
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
 * SupportNoticeController.java
 * 공지사항 조회, 등록, 수정 등 공지사항 관리에 필요한 API 를 호출 받는 컨트롤러
 *
 * @author yjkim
 * @version 1.0
 * @since 2016.07.28 최초작성
 */
@RestController
@RequestMapping(value = {"/support"})
public class SupportNoticeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupportNoticeController.class);

    @Autowired
    private SupportNoticeService supportNoticeService;

    /**
     * 공지 목록 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map notice list
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getNoticeList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getNoticeList(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getNoticeList :: param :: {}, param.toString()");
        return supportNoticeService.getNoticeList(param);
    }

    /**
     * 공지 상세 정보 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map notice
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getNotice"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getNotice(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getNoticeList :: param :: {}, param.toString()");
        return supportNoticeService.getNotice(param);
    }

    /**
     * 공지 등록
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/insertNotice"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertNotice(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("insertNotice :: param :: {}, param.toString()");
        return supportNoticeService.insertNotice(param);
    }

    /**
     * 공지 수정
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateNotice"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateNotice(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("updateNotice :: param :: {}, param.toString()");
        return supportNoticeService.updateNotice(param);
    }

    /**
     * 공지 삭제
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteNotice"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteNotice(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("deleteNotice :: param :: {}, param.toString()");
        return supportNoticeService.deleteNotice(param);
    }

}
