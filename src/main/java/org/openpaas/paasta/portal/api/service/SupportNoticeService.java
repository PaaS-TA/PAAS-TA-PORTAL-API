package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.mapper.portal.SupportNoticeMapper;
import org.openpaas.paasta.portal.api.model.Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * SupportNoticeService.java
 * 공지사항 조회, 등록, 수정 등 공지사항 관리에 필요한 기능을 구현한 서비스 클래스
 *
 * @author 김영지
 * @version 1.0
 * @since 2016.07.28 최초작성
 */
@Transactional
@Service
public class SupportNoticeService extends Common{

    @Autowired
    private SupportNoticeMapper supportNoticeMapper;


    /**
     * 공지 목록 조회
     *
     * @param param Support
     * @return Map notice list
     * @throws Exception the exception
     */
    public Map<String, Object> getNoticeList(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("list", supportNoticeMapper.getNoticeList(param));
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 공지 상세정보 조회
     *
     * @param param Support
     * @return Map notice
     * @throws Exception the exception
     */
    public Map<String, Object> getNotice(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("info", supportNoticeMapper.getNotice(param));
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 공지 등록
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> insertNotice(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        supportNoticeMapper.insertNotice(param);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 공지 수정
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> updateNotice(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        supportNoticeMapper.updateNotice(param);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 공지 삭제
     *
     * @param param Support
     * @return Map map
     * @throws Exception the exception
     */
    public Map<String, Object> deleteNotice(Support param) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        supportNoticeMapper.deleteNotice(param);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

}
