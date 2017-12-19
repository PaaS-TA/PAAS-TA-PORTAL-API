package org.openpaas.paasta.portal.api.mapper.uaa;

import org.apache.ibatis.annotations.Param;
import org.cloudfoundry.identity.uaa.scim.ScimUser;
import org.openpaas.paasta.portal.api.config.service.surport.Uaa;

import java.util.List;
import java.util.Map;

/**
 * Login Mapper
 *
 * @author mingu
 * @version 1.0
 * @since 2016.5.18 최초작성
 */
@Uaa
public interface UserMapper {
    /**
     * update User's email and username
     *
     * @param oldUsername the old username
     * @param username    the username
     * @return int int
     */
    int updateUserNameAndEmail(@Param("oldUsername") String oldUsername, @Param("username") String username);


    /**
     * check user
     *
     * @param username the username
     * @return int int
     */
    int isExist(@Param("username") String username);


    ScimUser createUser(org.cloudfoundry.identity.uaa.scim.ScimUser user, String password);


    /**
     * Gets user guid.
     *
     * @param username the username (=) user id
     * @return user guid
     */
    String getUserGuid(String username);


    /**
     * get userName for auto-complete
     *
     * @return userName
     */
    List<Map<String,String>> getUserInfo();
}