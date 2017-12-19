package org.openpaas.paasta.portal.api.mapper;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.Support;

import java.util.List;

/**
 * SupportQnAMapper.java
 * Mybatis Mapper Inteface 클래스로 문의 관련 메소드 정의만 한다
 *
 * @author yjkim
 * @version 1.0
 * @since 2016.07.28
 */

@Portal
public interface SupportQnAMapper {

    /**
     * 문의사항 목록 조회
     *
     * @param param Support
     * @return List<Support>
     */
    List<Support> getQnAList(Support param);

    /**
     * 문의사항 상세정보 조회
     *
     * @param param Support
     * @return Support
     */
    Support getQuestion(Support param);

    /**
     * 답변 상세정보 조회
     *
     * @param param Support
     * @return Support
     */
    Support getAnswer(Support param);

    /**
     * 답변 등록
     *
     * @param param Support
     * @return int
     */
    int insertAnswer(Support param);

    /**
     * 답변 수정
     *
     * @param param Support
     * @return int
     */
    int updateAnswer(Support param);

    /**
     * 답변 삭제
     *
     * @param param Support
     * @return int
     */
    int deleteAnswer(Support param);

    int updateQuestionStatus(Support param);

    List<Support> getMyQuestionsInMyAccount(String userId);

    List<Support> getMyQuestionList(Support param);

    int insertMyQuestion(Support param);

    int updateMyQuestion(Support param);

    int deleteMyQuestion(Support param);

}

