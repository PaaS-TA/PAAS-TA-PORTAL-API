package org.openpaas.paasta.portal.api.mapper.portal;

import org.apache.ibatis.annotations.Param;
import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.Question;

import java.util.List;

/**
 * Login Mapper
 *
 * @author kimdojun
 * @version 1.0
 * @since 2016.6.7 최초작성
 */
@Portal
public interface QuestionMapper {


    /**
     *
     * update question
     *
     * @param question
     * @return
     */
    boolean updateMyQuestion(@Param("question") Question question);


    /**
     * insert question
     *
     * @param question
     * @return
     */
    boolean insertQuestion(@Param("question") Question question);

    /**
     * get my questions
     *
     * @param userId
     * @return List<Question>
     */
    List<Question> getMyQuestions(String userId);


    /**
     * delete my question
     *
     * @param question
     * @return
     */
    boolean deleteMyQuestion(@Param("question") Question question);
}