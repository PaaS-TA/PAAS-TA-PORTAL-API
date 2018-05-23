package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.service.MonitoringService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by hrjin on 2017-10-16.
 */
@RestController
@RequestMapping(value = {"/app"})
public class MonitoringController{

    private final Logger LOGGER = getLogger(MonitoringController.class);
    private final MonitoringService monitoringService;

    @Autowired
    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @RequestMapping(value = {"/{guid}/{idx}/cpuUsage"}, method = RequestMethod.GET)
    public Map getCpuUsage(@PathVariable String guid, @PathVariable long idx, @RequestParam(value = "defaultTimeRange") String defaultTimeRange, @RequestParam(value = "groupBy") String groupBy, @RequestParam(value = "type") String type) {
        LOGGER.info("### get guid ::: {}, idx :::{}, defaultTimeRange :::{}, groupBy :::{}", guid, idx, defaultTimeRange, groupBy);
        return monitoringService.getCpuUsage(guid, idx, defaultTimeRange, groupBy, type);
    }

    @RequestMapping(value = {"/{guid}/{idx}/memoryUsage"}, method = RequestMethod.GET)
    public Map getMemoryUsage(@PathVariable String guid, @PathVariable long idx, @RequestParam(value = "defaultTimeRange") String defaultTimeRange, @RequestParam(value = "groupBy") String groupBy, @RequestParam(value = "type") String type){
        return monitoringService.getMemoryUsage(guid, idx, defaultTimeRange, groupBy, type);
    }

    @RequestMapping(value = {"/{guid}/{idx}/getNetworkByte"}, method = RequestMethod.GET)
    public Map getNetworkByte(@PathVariable String guid, @PathVariable long idx, @RequestParam(value = "defaultTimeRange") String defaultTimeRange, @RequestParam(value = "groupBy") String groupBy, @RequestParam(value = "type") String type){
        return monitoringService.getNetworkByte(guid, idx, defaultTimeRange, groupBy, type);
    }
}
