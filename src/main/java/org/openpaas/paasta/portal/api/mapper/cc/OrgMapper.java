package org.openpaas.paasta.portal.api.mapper.cc;

import org.openpaas.paasta.portal.api.config.service.surport.Cc;

import java.util.List;
import java.util.Map;

/**
 * Created by Dojun on 2016-09-06.
 */
@Cc
public interface OrgMapper {

    List<Object> getOrgsForAdmin();

    Map<String, Object> selectOrg(String orgName);
}
