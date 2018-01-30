package org.openpaas.paasta.portal.api.mapper.portal;

import org.apache.ibatis.annotations.Param;
import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.UserDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Login Mapper
 *
 * @author mingu
 * @version 1.0
 * @since 2016.5.18 최초작성
 */
@Portal
public interface UserDetailMapper {

    /**
     * Gets list apps.
     *
     * @param userId the userid
     * @return the list users
     */
    UserDetail selectOne(String userId);

    /**
     *
     * update User Detail
     *
     * @param userId
     * @param userDetail
     * @return
     */
    int update(@Param("userId")String userId,  @Param("userDetail")UserDetail userDetail);

    /**
     *
     * @param oldUserId
     * @param userId
     * @return
     */
//    int updateUserId(@Param("oldUserId") String oldUserId, @Param("newUserId") String userId);

    /**
     * insert User Detail
     *
     * @param userDetail
     * @return
     */
    int insert(UserDetail userDetail);

    int delete(@Param("userId") String userId);

    /**
     * 총 사용자 수 조회
     *
     * @return int
     */
    int getUserDetailCount();

    int createRequestUser(Map map);

    List<UserDetail> getUserDetailInfo(HashMap map);

    int upadteUserParam(HashMap map);

}