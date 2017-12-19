package org.openpaas.paasta.portal.api.mapper;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.ConfigInfo;
import org.openpaas.paasta.portal.api.model.WebIdeUser;

import java.util.List;

@Portal
public interface ConfigInfoMapper {

    List<ConfigInfo> getValue(ConfigInfo configInfo);

    int updateValue(ConfigInfo configInfo);

}
