package org.openpaas.paasta.portal.api.mapper.cc;

import org.openpaas.paasta.portal.api.config.service.surport.Cc;

import java.util.List;

/**
 * org.openpaas.paasta.portal.api.cc.mapper
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.01
 */
@Cc
public interface CatalogCcMapper {

    int getDomainId(String domainName);

    List getRouteHostNameList(int domainId);
}
