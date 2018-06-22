package org.openpaas.paasta.portal.api.controller;

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
@RequestMapping(value = {"/app"})
public class AppAlarmController {

    private final Logger LOGGER = getLogger(AppAlarmController.class);
    private final AlarmService alarmService;

    @Autowired
    public AppAlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @RequestMapping(value = {"/alarm/list"}, method = RequestMethod.GET)
    public Map getAlarmList(@RequestParam(value = "appGuid") String appGuid
            , @RequestParam(value = "pageItems") String pageItems
            , @RequestParam(value = "pageIndex") String pageIndex
            , @RequestParam(value = "resourceType") String resourceType
            , @RequestParam(value = "alarmLevel") String alarmLevel) {

        LOGGER.info("AlarmController Start");

        return alarmService.getAlarmList(appGuid, pageItems, pageIndex, resourceType, alarmLevel);
    }

    @RequestMapping(value = {"/alarm/policy"}, method = RequestMethod.GET)
    public Map getAlarm(@RequestParam(value = "appGuid") String appGuid) {
        LOGGER.info("AlarmController Get Start");

        return alarmService.getAlarm(appGuid);
    }

    @RequestMapping(value = {"/alarm/policy"}, method = RequestMethod.POST)
    public Map updateAlarm(@RequestBody Map body) throws Exception {
        LOGGER.info("AlarmController Update Start");

        return alarmService.updateAlarm(body);
    }
}
