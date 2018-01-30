package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.mapper.portal.DocumentsMapper;
import org.openpaas.paasta.portal.api.model.Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * DocumentsService.java
 * 문서 조회, 등록, 수정 등 문서 관리에 필요한 기능을 구현한 서비스 클래스
 *
 * @author 김영지
 * @version 1.0
 * @since 2016.07.28 최초작성
 */
@Transactional
@Service
public class DocumentsService extends Common{


    @Autowired
    private DocumentsMapper documentsMapper;

    /**
     * 문서 목록 조회
     *
     * @param param Support
     * @return Map documents list
     * @throws Exception the exception
     */
    public Map<String, Object> getDocumentsList(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("list", documentsMapper.getDocumentsList(param));
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 문서 상세정보 조회
     *
     * @param param Support
     * @return Map document
     * @throws Exception the exception
     */
    public Map<String, Object> getDocument(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("info", documentsMapper.getDocument(param));
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 문서 등록
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> insertDocument(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        documentsMapper.insertDocument(param);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 문서 수정
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> updateDocument(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        documentsMapper.updateDocument(param);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 문서 삭제
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> deleteDocument(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        documentsMapper.deleteDocument(param);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


}
