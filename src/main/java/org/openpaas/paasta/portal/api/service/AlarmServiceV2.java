package org.openpaas.paasta.portal.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by indra on 2018-05-11.
 */
@Service
public class AlarmServiceV2 {
    private final MonitoringRestTemplateService monitoringRestTemplateService;
    private static final String url = "/v2/paas/app/alarm";

    @Autowired
    public AlarmServiceV2(MonitoringRestTemplateService monitoringRestTemplateService) {
        this.monitoringRestTemplateService = monitoringRestTemplateService;
    }

    public Map getAlarmList(String appGuid, String pageItems, String pageIndex, String resourceType, String alarmLevel) {
        StringBuffer param = new StringBuffer();
        param.append("appGuid="+appGuid);
        param.append("&pageItems="+pageItems);
        param.append("&pageIndex="+pageIndex);

        if(resourceType != null && resourceType != "") {
            param.append("&resourceType="+resourceType);
        }
        if(alarmLevel != null && alarmLevel != "") {
            param.append("&alarmLevel="+alarmLevel);
        }
        return monitoringRestTemplateService.send(url + "/list?"+param, HttpMethod.GET, null);
    }

    public Map getAlarm(String appGuid) {
        StringBuffer param = new StringBuffer();
        param.append("appGuid="+appGuid);

        return monitoringRestTemplateService.send(url + "/policy?"+param, HttpMethod.GET, null);
    }

    public Map updateAlarm(Map body) {
        return monitoringRestTemplateService.send(url + "/policy", HttpMethod.POST, body);
    }
}
