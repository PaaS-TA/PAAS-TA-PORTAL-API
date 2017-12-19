package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.ConfigInfo;
import org.openpaas.paasta.portal.api.service.ConfigInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 설정정보 컨트롤러 - 포탈 설정정보를 수정 관리하는 컨트롤러이다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
@Transactional
@RequestMapping(value = {"/configInfo"})
public class ConfigInfoController extends Common {
    @Autowired
    private ConfigInfoService ConfigInfoService;

    /**
     * 설정 정보 값을 조회한다.
     *
     * @param ConfigInfo the config info
     * @return value value
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getValue"}, method = RequestMethod.POST)
    public Map<String, Object> getValue(@RequestBody ConfigInfo ConfigInfo) {

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("list", ConfigInfoService.getValue(ConfigInfo));

        return resultMap;

    }

    /**
     * 설정 정보 값을 수정한다.
     *
     * @param ConfigInfo the config info
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateValue"}, method = RequestMethod.POST)
    public Map<String, Object> updateValue(@RequestBody ConfigInfo ConfigInfo) {

        Map<String, Object> resultMap = new HashMap<>();

        ConfigInfoService.updateValue(ConfigInfo);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

}
