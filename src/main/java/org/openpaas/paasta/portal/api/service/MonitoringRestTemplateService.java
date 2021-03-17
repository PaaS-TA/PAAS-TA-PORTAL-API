package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by hrjin on 2017-10-16.
 */
@Service
public class MonitoringRestTemplateService extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringRestTemplateService.class);

    public Map send(String reqUrl, HttpMethod httpMethod, Object bodyObject){
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add("Accept", "application/json");
        reqHeaders.add("Content-Type", "application/json; charset=utf-8");
        HttpEntity<Object> reqEntity = new HttpEntity<>(bodyObject, reqHeaders);
//        //LOGGER.info("####### monitoringApiTarget Url ::: " + monitoringApiTarget + reqUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> resEntity = restTemplate.exchange(monitoringApiTarget + reqUrl, httpMethod , reqEntity, Map.class);
        Map body = resEntity.getBody();
        //LOGGER.info("Response Type: {}", resEntity.getBody().getClass());

        return body;
    }
}