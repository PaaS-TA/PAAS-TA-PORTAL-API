package org.openpaas.paasta.portal.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by indra on 2018-05-14.
 */
@Service
public class AppAutoscalingService {
    private final MonitoringRestTemplateService monitoringRestTemplateService;
    private static final String url = "/v2/paas/app/autoscaling";

    @Autowired
    public AppAutoscalingService(MonitoringRestTemplateService monitoringRestTemplateService) {
        this.monitoringRestTemplateService = monitoringRestTemplateService;
    }

    public Map getAutoscaling(String appGuid) {
        StringBuffer param = new StringBuffer();
        param.append("appGuid="+appGuid);

        return monitoringRestTemplateService.send(url + "/policy?"+param, HttpMethod.GET, null);
    }

    public Map updateAutoscaling(Map body) {

        return monitoringRestTemplateService.send(url + "/policy", HttpMethod.POST, body);
    }
}
