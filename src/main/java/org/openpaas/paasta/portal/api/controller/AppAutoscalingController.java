package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.service.AppAutoscalingService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by indra on 2018-05-14.
 */
@RestController
@RequestMapping(value = {"/app"})
public class AppAutoscalingController {

    private final Logger LOGGER = getLogger(AppAutoscalingController.class);
    private final AppAutoscalingService appAutoscalingService;

    @Autowired
    public AppAutoscalingController(AppAutoscalingService appAutoscalingService) { this.appAutoscalingService = appAutoscalingService; }

    /**
     * 오토 스케일링 정보를 가져온다.
     *
     * @param appGuid
     * @return Map
     */
    @RequestMapping(value = {"/autoscaling/policy"}, method = RequestMethod.GET)
    public Map getAutoscaling(@RequestParam(value = "appGuid") String appGuid) {
        LOGGER.info("AppAutoscalingController Get Start");

        return appAutoscalingService.getAutoscaling(appGuid);
    }

    /**
     * 오토 스케일링 정보를 수정한다.
     *
     * @param body
     * @return Map
     * @throws Exception
     */
    @RequestMapping(value = {"/autoscaling/policy"}, method = RequestMethod.POST)
    public Map updateAutoscaling(@RequestBody Map body) throws Exception {
        LOGGER.info("AppAutoscalingController Update Start");

        return appAutoscalingService.updateAutoscaling(body);
    }
}
