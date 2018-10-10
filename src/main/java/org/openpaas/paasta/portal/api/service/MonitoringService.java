package org.openpaas.paasta.portal.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hrjin on 2017-10-16.
 */
@Service
public class MonitoringService {
    private final MonitoringRestTemplateService monitoringRestTemplateService;
    private static final String url = "/v2/paas/app/instance";

    @Autowired
    public MonitoringService(MonitoringRestTemplateService monitoringRestTemplateService) {
        this.monitoringRestTemplateService = monitoringRestTemplateService;
    }

    public Map getCpuUsage(String guid, long idx, String defaultTimeRange, String groupBy, String type) {
        Map returnMap = new HashMap();
        List<Map> dataList = new ArrayList<Map>();

        if(type.equals("All")) {
            for(int i = 0 ; i <= idx; i++){
                Map data = new HashMap();
                Map result = monitoringRestTemplateService.send(url + "/" + guid + "/" + i + "/cpu/usages?defaultTimeRange=" + defaultTimeRange + "&groupBy=" + groupBy, HttpMethod.GET, null);

                data.put("name", i);
                data.put("data", result);
                dataList.add(data);
            }
        } else {
            Map data = new HashMap();
            Map result = monitoringRestTemplateService.send(url + "/" + guid + "/" + type + "/cpu/usages?defaultTimeRange=" + defaultTimeRange + "&groupBy=" + groupBy, HttpMethod.GET, null);

            data.put("name", type);
            data.put("data", result);
            dataList.add(data);
        }

        returnMap.put("data", dataList);
        return returnMap;
    }

    public Map getMemoryUsage(String guid, long idx, String defaultTimeRange, String groupBy, String type) {
        Map returnMap = new HashMap();
        List<Map> dataList = new ArrayList<Map>();

        if(type.equals("All")) {
            for(int i = 0 ; i <= idx; i++){
                Map data = new HashMap();
                Map result = monitoringRestTemplateService.send(url + "/" + guid + "/" + idx + "/memory/usages?defaultTimeRange=" + defaultTimeRange + "&groupBy=" + groupBy, HttpMethod.GET, null);

                data.put("name", type);
                data.put("data", result);
                dataList.add(data);
            }
        } else {
            Map data = new HashMap();
            Map result = monitoringRestTemplateService.send(url + "/" + guid + "/" + type + "/memory/usages?defaultTimeRange=" + defaultTimeRange + "&groupBy=" + groupBy, HttpMethod.GET, null);

            data.put("name", type);
            data.put("data", result);
            dataList.add(data);
        }

        returnMap.put("data", dataList);
        return returnMap;
    }

    public Map getNetworkByte(String guid, long idx, String defaultTimeRange, String groupBy, String type) {
        Map returnMap = new HashMap();
        List<Map> dataList = new ArrayList<Map>();

        if(type.equals("All")) {
            for(int i = 0 ; i <= idx; i++){
                Map data = new HashMap();
                Map result = monitoringRestTemplateService.send(url + "/" + guid + "/" + i + "/network/bytes?defaultTimeRange=" + defaultTimeRange + "&groupBy=" + groupBy, HttpMethod.GET, null);

                data.put("name", i);
                data.put("data", result);
                dataList.add(data);
            }
        } else {
            Map data = new HashMap();
            Map result = monitoringRestTemplateService.send(url + "/" + guid + "/" + type + "/network/bytes?defaultTimeRange=" + defaultTimeRange + "&groupBy=" + groupBy, HttpMethod.GET, null);

            data.put("name", type);
            data.put("data", result);
            dataList.add(data);
        }

        returnMap.put("data", dataList);
        return returnMap;
    }
}
