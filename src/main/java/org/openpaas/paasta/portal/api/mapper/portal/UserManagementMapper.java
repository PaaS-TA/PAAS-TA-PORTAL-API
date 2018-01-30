package org.openpaas.paasta.portal.api.mapper.portal;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.UserManagement;

import java.util.List;

/**
 * org.openpaas.paasta.portal.api.mapper
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.08.31
 */
@Portal
public interface UserManagementMapper {

    /**
     * 사용자 정보 목록을 조회한다.
     *
     * @param param UserManagement(모델클래스)
     * @return List(자바클래스)
     */
    List<UserManagement> getUserInfoList(UserManagement param);

    /**
     * 운영자 권한 부여를 수정한다.
     *
     * @param param UserManagement(모델클래스)
     * @return Integer(자바클래스)
     */
    int updateOperatingAuthority(UserManagement param);
}
