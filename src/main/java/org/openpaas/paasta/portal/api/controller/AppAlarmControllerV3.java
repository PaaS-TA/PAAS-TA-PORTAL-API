package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.AlarmServiceV3;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by indra on 2018-05-11.
 */
@RestController
@RequestMapping(value = {"/app"})
public class AppAlarmControllerV3 {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private final Logger LOGGER = getLogger(AppAlarmControllerV3.class);

    @Autowired
    private AlarmServiceV3 alarmServiceV3;


    /**
     * 알람 정보 리스트를 가져온다.
     *
     * @param appGuid
     * @param pageItems
     * @param pageIndex
     * @param resourceType
     * @param alarmLevel
     * @return Map
     */
    @RequestMapping(value = {Constants.V3_URL + "/alarm/list"}, method = RequestMethod.GET)
    public Map getAlarmList(@RequestParam(value = "appGuid") String appGuid, @RequestParam(value = "pageItems") String pageItems, @RequestParam(value = "pageIndex") String pageIndex, @RequestParam(value = "resourceType") String resourceType, @RequestParam(value = "alarmLevel") String alarmLevel) {

        LOGGER.info("AlarmController Start");

        return alarmServiceV3.getAlarmList(appGuid, pageItems, pageIndex, resourceType, alarmLevel);
    }

    /**
     * 알람 정보를 가져온다.
     *
     * @param appGuid
     * @return Map
     */
    @RequestMapping(value = {Constants.V3_URL + "/alarm/policy"}, method = RequestMethod.GET)
    public Map getAlarm(@RequestParam(value = "appGuid") String appGuid) {
        LOGGER.info("AlarmController Get Start");

        return alarmServiceV3.getAlarm(appGuid);
    }

    /**
     * 알람 정보를 수정한다..
     *
     * @param body
     * @return Map
     * @throws Exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/alarm/policy"}, method = RequestMethod.POST)
    public Map updateAlarm(@RequestBody Map body) throws Exception {
        LOGGER.info("AlarmController Update Start");

        return alarmServiceV3.updateAlarm(body);
    }
}
