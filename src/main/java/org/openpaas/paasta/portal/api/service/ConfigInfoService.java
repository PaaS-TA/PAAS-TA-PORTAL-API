package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
//import org.openpaas.paasta.portal.api.mapper.portal.ConfigInfoMapper;
import org.openpaas.paasta.portal.api.model.ConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 설정정보 서비스 - 포탈 설정정보를 수정 관리하는 서비스이다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Transactional
@Service
public class ConfigInfoService extends Common {

//    @Autowired
//    private ConfigInfoMapper configInfoMapper;

    /**
     * 설정 정보 값을 조회한다.
     *
     * @param configInfo the config info
     * @return value value
     * @throws Exception the exception
     */
    public List<ConfigInfo> getValue(ConfigInfo configInfo)  {

//        return configInfoMapper.getValue(configInfo);
        return null;
    }

    /**
     * 설정 정보 값을 수정한다.
     *
     * @param configInfo the config info
     * @return map
     * @throws Exception the exception
     */
    public void updateValue(ConfigInfo configInfo) {

//        configInfoMapper.updateValue(configInfo);
    }

}
