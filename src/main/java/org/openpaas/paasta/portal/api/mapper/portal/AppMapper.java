package org.openpaas.paasta.portal.api.mapper.portal;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;

@Portal
public interface AppMapper {

    String getAppImageUrl(String buildPack);

}
