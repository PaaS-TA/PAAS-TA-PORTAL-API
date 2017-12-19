package org.openpaas.paasta.portal.api.service;

import org.junit.Test;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MonitoringServiceTest extends Common {

    @Test
    public void test01_getClientList() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<Object>(null, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.exchange("http://monitapi.211.239.163.240.xip.io/app/4e958052-8d17-4aa0-88d7-0bdd796b16a7/0/cpuUsage?defaultTimeRange=2000000m&groupBy=2m", HttpMethod.GET , entity, Object.class);
        Object object = response.getBody();
    }

}
