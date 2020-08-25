package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.AlarmService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by indra on 2018-05-11.
 */
@RestController
public class AppAlarmController {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private final Logger LOGGER = getLogger(AppAlarmController.class);
    private final AlarmService alarmService;

    @Autowired
    public AppAlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

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
    @GetMapping(Constants.EXTERNAL_URL + "/alarm/list")
    public Map getAlarmList(@RequestParam(value = "appGuid") String appGuid, @RequestParam(value = "pageItems") String pageItems, @RequestParam(value = "pageIndex") String pageIndex, @RequestParam(value = "resourceType") String resourceType, @RequestParam(value = "alarmLevel") String alarmLevel) {

        LOGGER.info("AlarmController Start");

        return alarmService.getAlarmList(appGuid, pageItems, pageIndex, resourceType, alarmLevel);
    }

    /**
     * 알람 정보를 가져온다.
     *
     * @param appGuid
     * @return Map
     */
    @GetMapping(Constants.EXTERNAL_URL + "/alarm/policy")
    public Map getAlarm(@RequestParam(value = "appGuid") String appGuid) {
        LOGGER.info("AlarmController Get Start");

        return alarmService.getAlarm(appGuid);
    }

    /**
     * 알람 정보를 수정한다..
     *
     * @param body
     * @return Map
     * @throws Exception
     */
    @PostMapping(Constants.EXTERNAL_URL + "/alarm/policy")
    public Map updateAlarm(@RequestBody Map body) throws Exception {
        LOGGER.info("AlarmController Update Start");

        return alarmService.updateAlarm(body);
    }
}
