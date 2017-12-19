package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Support;
import org.openpaas.paasta.portal.api.service.SupportQnAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * SupportQnAController.java
 * 문의 조회, 등록, 수정 등 문의 관리에 필요한 API 를 호출 받는 컨트롤러
 *
 * @author yjkim
 * @version 1.0
 * @since 2016.07.28 최초작성
 */
@RestController
@RequestMapping(value = {"/support"})
class SupportQnAController extends Common {

    private final SupportQnAService supportQnAService;

    /**
     * Instantiates a new Support qn a controller.
     *
     * @param supportQnAService the support qn a service
     */
    @Autowired
    public SupportQnAController(SupportQnAService supportQnAService) {
        this.supportQnAService = supportQnAService;
    }

    /**
     * 문의 목록 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map qn a list
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getQnAList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getQnAList(@RequestBody Support param, HttpServletResponse response) throws Exception {
        return supportQnAService.getQnAList(param);
    }


    /**
     * 문의 상세 정보 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map question
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getQuestion"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getQuestion(@RequestBody Support param, HttpServletResponse response) throws Exception {
        return supportQnAService.getQuestion(param);
    }


    /**
     * 답변 상세 정보 조회
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map answer
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getAnswer"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getAnswer(@RequestBody Support param, HttpServletResponse response) throws Exception {
        return supportQnAService.getAnswer(param);
    }


    /**
     * 답변 등록
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/insertAnswer"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertAnswer(@RequestBody Support param, HttpServletResponse response) throws Exception {
        return supportQnAService.insertAnswer(param);
    }


    /**
     * 답변 수정
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateAnswer"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateAnswer(@RequestBody Support param, HttpServletResponse response) throws Exception {
        return supportQnAService.updateAnswer(param);
    }


    /**
     * 답변 삭제
     *
     * @param param    Support
     * @param response HttpServletResponse
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteAnswer"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteAnswer(@RequestBody Support param, HttpServletResponse response) throws Exception {
        return supportQnAService.deleteAnswer(param);
    }


    /**
     * Gets my questions in my account.
     *
     * @param request the request
     * @return my questions in my account
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/myAccount/getMyQuestions"}, method = RequestMethod.POST)
    public List<Support> getMyQuestionsInMYAccount(HttpServletRequest request) throws Exception {
        return supportQnAService.getMyQuestionsInMyAccount(request.getHeader(AUTHORIZATION_HEADER_KEY));
    }


    /**
     * 내 문의 목록 조회
     *
     * @param param the param
     * @return my question list
     */
    @RequestMapping(value = {"/myQuestion/getMyQuestionList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getMyQuestionList(@RequestBody Support param) {
        return supportQnAService.getMyQuestionList(param);
    }


    /**
     * 내 문의 답변 조회
     *
     * @param param the param
     * @return my question answer
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/myQuestion/getMyQuestionAnswer"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getMyQuestionAnswer(@RequestBody Support param) throws Exception {
        return supportQnAService.getAnswer(new Support(){{setQuestionNo(param.getNo());}});
    }


    /**
     * 파일 업로드
     *
     * @param multipartFile the multipart file
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/myQuestion/uploadFile"}, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public Map<String, Object> uploadFile(@RequestParam(value="file", required=false) MultipartFile multipartFile) throws Exception {
        return supportQnAService.uploadFile(multipartFile);
    }


    /**
     * 파일 삭제
     *
     * @param param the param
     * @return map map
     */
    @RequestMapping(value = {"/myQuestion/deleteFile"}, method = RequestMethod.POST)
    public Map<String, Object> deleteFile(@RequestBody Support param) {
        return supportQnAService.deleteFile(param.getFilePath());
    }


    /**
     * 내 문의 저장
     *
     * @param param the param
     * @return map map
     */
    @RequestMapping(value = {"/myQuestion/insertMyQuestion"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertMyQuestion(@RequestBody Support param) {
        param.setStatus(Constants.MY_QUESTION_STATUS_WAITING);
        return supportQnAService.insertMyQuestion(param);
    }


    /**
     * 내 문의 수정
     *
     * @param param the param
     * @return map map
     */
    @RequestMapping(value = {"/myQuestion/updateMyQuestion"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateMyQuestion(@RequestBody Support param) {
        return supportQnAService.updateMyQuestion(param);
    }


    /**
     * 내 문의 삭제
     *
     * @param param the param
     * @return map map
     */
    @RequestMapping(value = {"/myQuestion/deleteMyQuestion"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteMyQuestion(@RequestBody Support param) {
        return supportQnAService.deleteMyQuestion(param);
    }
}
