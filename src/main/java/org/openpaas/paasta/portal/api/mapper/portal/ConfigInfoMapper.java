package org.openpaas.paasta.portal.api.mapper.portal;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.ConfigInfo;

import java.util.List;

@Portal
public interface ConfigInfoMapper {

    List<ConfigInfo> getValue(ConfigInfo configInfo);

    int updateValue(ConfigInfo configInfo);

}
