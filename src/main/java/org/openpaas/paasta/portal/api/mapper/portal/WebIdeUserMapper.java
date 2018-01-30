package org.openpaas.paasta.portal.api.mapper.portal;


import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.WebIdeUser;

import java.util.List;

@Portal
public interface WebIdeUserMapper {

    WebIdeUser getUser(WebIdeUser webIdeUser);

    int insertUser(WebIdeUser webIdeUser);

    int updateUser(WebIdeUser webIdeUser);

    int deleteUser(WebIdeUser webIdeUser);

    List<WebIdeUser> getList(WebIdeUser webIdeUser);
}
