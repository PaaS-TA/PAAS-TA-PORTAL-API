package org.openpaas.paasta.portal.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by hrjin on 2017-10-16.
 */
@Service
public class MonitoringService {
    private final MonitoringRestTemplateService monitoringRestTemplateService;
    private static final String url = "/app";

    @Autowired
    public MonitoringService(MonitoringRestTemplateService monitoringRestTemplateService) {
        this.monitoringRestTemplateService = monitoringRestTemplateService;
    }

    public Map getCpuUsage(String guid, long idx, String defaultTimeRange, String groupBy) {
        return monitoringRestTemplateService.send(url + "/" + guid + "/" + idx + "/cpuUsage?defaultTimeRange=" + defaultTimeRange + "&groupBy=" + groupBy, HttpMethod.GET, null);
    }

    public Map getMemoryUsage(String guid, long idx, String defaultTimeRange, String groupBy) {
        return monitoringRestTemplateService.send(url + "/" + guid + "/" + idx + "/memoryUsage?defaultTimeRange=" + defaultTimeRange + "&groupBy="+ groupBy, HttpMethod.GET, null);
    }

    public Map getNetworkIoKByte(String guid, long idx, String defaultTimeRange, String groupBy) {
        return monitoringRestTemplateService.send(url + "/" + guid + "/" + idx + "/networkIoKByte?defaultTimeRange=" + defaultTimeRange + "&groupBy="+ groupBy, HttpMethod.GET, null);
    }
}
