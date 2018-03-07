package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
//import org.openpaas.paasta.portal.api.mapper.portal.SupportQnAMapper;
import org.openpaas.paasta.portal.api.model.Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SupportQnAService.java
 * 문의 조회, 등록, 수정 등 나의 문의 관리에 필요한 기능을 구현한 서비스 클래스
 *
 * @author 김영지
 * @version 1.0
 * @since 2016.07.28 최초작성
 */
@Transactional
@Service
public class SupportQnAService extends Common {

//    private final SupportQnAMapper supportQnAMapper;
//    private final GlusterfsServiceImpl glusterfsService;

    /**
     * Instantiates a new Support qn a service.
     *
     * //@param supportQnAMapper the support qn a mapper
     //* @param glusterfsService the glusterfs service
     */
    @Autowired
    public SupportQnAService(
//            SupportQnAMapper supportQnAMapper,
            //GlusterfsServiceImpl glusterfsService
     ) {
//        this.supportQnAMapper = supportQnAMapper;
//        this.glusterfsService = glusterfsService;
    }


    /**
     * 문의 목록 조회
     *
     * @param param Support
     * @return Map qn a list
     * @throws Exception the exception
     */
    public Map<String, Object> getQnAList(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

//        resultMap.put("list", supportQnAMapper.getQnAList(param));
        resultMap.put("list", "");
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 문의 상세정보 조회
     *
     * @param param Support
     * @return Map question
     * @throws Exception the exception
     */
    public Map<String, Object> getQuestion(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

//        resultMap.put("info", supportQnAMapper.getQuestion(param));
        resultMap.put("list", "");
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 답변 조회
     *
     * @param param Support
     * @return Map answer
     * @throws Exception the exception
     */
    public Map<String, Object> getAnswer(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

//        resultMap.put("info", supportQnAMapper.getAnswer(param));
        resultMap.put("list", "");
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 답변 등록
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> insertAnswer(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

//        supportQnAMapper.insertAnswer(param);
//        supportQnAMapper.updateQuestionStatus(param);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 답변 수정
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> updateAnswer(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

//        supportQnAMapper.updateAnswer(param);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 답변 삭제
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> deleteAnswer(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

//        supportQnAMapper.deleteAnswer(param);
//        supportQnAMapper.updateQuestionStatus(param);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * Gets my questions in my account.
     *
     * @param token the token
     * @return my questions in my account
     * @throws Exception the exception
     */
    public List<Support> getMyQuestionsInMyAccount(String token) throws Exception {
//        return supportQnAMapper.getMyQuestionsInMyAccount(getCustomCloudFoundryClient(token).getCloudInfo().getUser());
        return null;
    }


    /**
     * 내 문의 목록 조회
     *
     * @param param the param
     * @return the my question list
     */
    public Map<String, Object> getMyQuestionList(Support param) {
        int pageNo = Constants.PAGE_NO;
        float pageSize = Constants.PAGE_SIZE;

        // SET PAGING INIT VALUE
        if (param.getPageNo() > 0) pageNo = param.getPageNo();
        if (param.getPageSize() > 0) pageSize = param.getPageSize();

        param.setPageNo((int) ((pageSize * (pageNo - 1))));
//        return new HashMap<String, Object>(){{put("list", supportQnAMapper.getMyQuestionList(param));}};
        return null;
    }


    /**
     * 파일 업로드
     *
     * @param multipartFile MultipartFile
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> uploadFile(MultipartFile multipartFile) throws Exception {
        return new HashMap<String, Object>() {{
            //put("path", glusterfsService.upload(multipartFile));
            put("path", "");
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 파일 삭제
     *
     * @param fileUriPath String
     * @return Map map
     */
    public Map<String, Object> deleteFile(String fileUriPath) {
        //glusterfsService.delete(fileUriPath);

        return new HashMap<String, Object>(){{put("RESULT", Constants.RESULT_STATUS_SUCCESS);}};
    }


    /**
     * 내 문의 저장
     *
     * @param param the param
     * @return map map
     */
    public Map<String, Object> insertMyQuestion(Support param) {
//        supportQnAMapper.insertMyQuestion(param);

        return new HashMap<String, Object>(){{put("RESULT", Constants.RESULT_STATUS_SUCCESS);}};
    }


    /**
     * 내 문의 수정
     *
     * @param param the param
     * @return map map
     */
    public Map<String, Object> updateMyQuestion(Support param) {
//        supportQnAMapper.updateMyQuestion(param);

        return new HashMap<String, Object>(){{put("RESULT", Constants.RESULT_STATUS_SUCCESS);}};
    }


    /**
     * 내 문의 삭제
     *
     * @param param the param
     * @return map map
     */
    public Map<String, Object> deleteMyQuestion(Support param) {
//        supportQnAMapper.deleteMyQuestion(param);
//        supportQnAMapper.deleteAnswer(param);


        return new HashMap<String, Object>(){{put("RESULT", Constants.RESULT_STATUS_SUCCESS);}};
    }

}
