package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.model.Support;
import org.openpaas.paasta.portal.api.service.SupportBoardService;
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
 * SupportBoardController.java
 * 커뮤니티 게시판 조회, 등록, 수정 등 커뮤니티 게시판 관리에 필요한 API를 호출 받는 컨트롤러
 *
 * @author yjkim
 * @version 1.0
 * @since 2016.07.28 최초작성
 */
@RestController
@RequestMapping(value = {"/support"})
public class SupportBoardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupportBoardController.class);

    @Autowired
    private SupportBoardService supportBoardService;

    /**
     * 게시판 목록 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map board list
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getBoardList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getBoardList(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getBoardList :: param :: {}, param.toString()");
        return supportBoardService.getBoardList(param);
    }

    /**
     * 게시판 게시글 상세 정보 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map board
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getBoard"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getBoard(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getBoard :: param :: {}, param.toString()");
        return supportBoardService.getBoard(param);
    }

    /**
     * 게시판 댓글 목록 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map board comment list
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getBoardCommentList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getBoardCommentList(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getBoardCommentList :: param :: {}, param.toString()");
        return supportBoardService.getBoardCommentList(param);
    }

    /**
     * 게시판 게시글 등록
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/insertBoard"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertBoard(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("insertBoard :: param :: {}, param.toString()");
        return supportBoardService.insertBoard(param);
    }

    /**
     * 게시판 게시글 수정
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateBoard"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateBoard(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("updateBoard :: param :: {}, param.toString()");
        return supportBoardService.updateBoard(param);
    }

    /**
     * 게시판 게시글 삭제
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteBoard"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteBoard(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("deleteBoard :: param :: {}, param.toString()");
        return supportBoardService.deleteBoard(param);
    }


    /**
     * 게시판 댓글 수 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map reply num
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getReplyNum"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getReplyNum(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getReplyNum :: param :: {}, param.toString()");
        return supportBoardService.getReplyNum(param);
    }

    /**
     * 게시판 댓글 수 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map comment reply num
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getCommentReplyNum"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCommentReplyNum(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("getCommentReplyNum :: param :: {}, param.toString()");
        return supportBoardService.getCommentReplyNum(param);
    }


    /**
     * 게시판 댓글 등록
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/insertBoardComment"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertBoardComment(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("insertBoardComment :: param :: {}, param.toString()");
        return supportBoardService.insertBoardComment(param);
    }

    /**
     * 게시판 댓글 수정
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateBoardComment"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateBoardComment(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("updateBoardComment :: param :: {}, param.toString()");
        return supportBoardService.updateBoardComment(param);
    }

    /**
     * 게시판 댓글 삭제
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteBoardComment"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteBoardComment(@RequestBody Support param, HttpServletResponse response) throws Exception{
        LOGGER.debug("deleteBoardComment :: param :: {}, param.toString()");
        return supportBoardService.deleteBoardComment(param);
    }



}
