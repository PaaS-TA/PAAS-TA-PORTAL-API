package org.openpaas.paasta.portal.api.mapper.portal;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.MyQuestion;


import java.util.List;

/**
 * org.openpaas.paasta.portal.api.mapper
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.08.22
 */
@Portal
public interface MyQuestionMapper {

    List<MyQuestion> getMyQuestionList(MyQuestion param);

    int insertMyQuestion(MyQuestion param);

    int updateMyQuestion(MyQuestion param);

    int deleteMyQuestion(MyQuestion param);
}
