package org.openpaas.paasta.portal.api.service;


import org.openpaas.paasta.portal.api.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

@Service
public class CommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);

    RestTemplate restTemplate;
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String CF_AUTHORIZATION_HEADER_KEY = "cf-Authorization";


    private String apiUrl;

    @Value("${paasta.portal.api.authorization.base64}")
    private String base64Authorization;

    @Value("${paasta.portal.api.zuulUrl.cfapi}")
    private String cfApiUrl;

    @Value("${paasta.portal.api.zuulUrl.commonapi}")
    private String commonApiUrl;

    @Value("${paasta.portal.api.zuulUrl.storageapi}")
    private String storageApiUrl;

    @Value("${paasta.portal.storageapi.type}")
    private String storageApiType;

    private final MessageSource messageSource;

    /**
     * Instantiates a new Common service.
     *
     * @param messageSource the message source
     */
    @Autowired
    public CommonService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    /**
     * 사용자 정의 에러메시지 전송
     *
     * @param res        the res
     * @param httpStatus the http status
     * @param reqMessage the req message
     * @throws Exception the exception
     */
    void getCustomSendError(HttpServletResponse res, HttpStatus httpStatus, String reqMessage) throws Exception {
        String[] reqMessageArray = reqMessage.split(Constants.DUPLICATION_SEPARATOR);

        if (reqMessageArray.length > 1) {
            res.sendError(httpStatus.value(), this.getCustomMessage(reqMessageArray[0])
                    + " " + Constants.DUPLICATION_SEPARATOR + " " + reqMessageArray[1]);

        } else {
            res.sendError(httpStatus.value(), this.getCustomMessage(reqMessage));
        }
    }


    /**
     * 사용자 정의 에러메시지 조회
     *
     * @param reqMessage the req message
     * @return custom message
     * @throws Exception the exception
     */
    String getCustomMessage(String reqMessage) throws Exception {
        return messageSource.getMessage(reqMessage, null, Locale.KOREA);
    }







    /**
     * REST TEMPLATE 처리 - CommonApi
     *
     * @param reqUrl     the req url
     * @param httpMethod the http method
     * @param obj        the obj
     * @param reqToken   the req token
     * @return map map
     */
    public Map<String, Object> procCommonApiRestTemplate(String reqUrl, HttpMethod httpMethod, Object obj, String reqToken) {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);

        LOGGER.info("CommonApiUrl::"+commonApiUrl + reqUrl);
        HttpEntity<Object> reqEntity = new HttpEntity<>(obj, reqHeaders);
        ResponseEntity<Map> resEntity = restTemplate.exchange(commonApiUrl + reqUrl, httpMethod, reqEntity, Map.class);
        Map<String, Object> resultMap = resEntity.getBody();

        LOGGER.info("procCommonApiRestTemplate reqUrl :: {} || resultMap :: {}", reqUrl, resultMap.toString());
        return resultMap;
    }

    /**
     * REST TEMPLATE 처리 - StorageApi
     *
     * @param reqUrl     the req url
     * @param httpMethod the http method
     * @param bodyObject        the obj
     * @param reqToken   the req token
     * @return map map
     */
    public <T> ResponseEntity<T> procStorageApiRestTemplate(String reqUrl, HttpMethod httpMethod, Object bodyObject, String reqToken, Class<T> resClazz) {
        restTemplate = new RestTemplate();

        // create url
        String storageRequestURL = storageApiUrl + "/v2/" + storageApiType + '/';
        if (null != reqUrl && false == "".equals( reqUrl ))
            storageRequestURL += reqUrl;

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);
        if (null == bodyObject)
            bodyObject = new LinkedMultiValueMap<>();
        HttpEntity<Object> reqEntity = new HttpEntity<>(bodyObject, reqHeaders);

        ResponseEntity<T> resEntity = restTemplate.exchange(storageRequestURL, httpMethod, reqEntity, resClazz);
        LOGGER.info("procRestStorageApiTemplate reqUrl :: {} || resultEntity type :: {}", storageRequestURL, resEntity.getHeaders().getContentType());
        LOGGER.info("procRestStorageApiTemplate response Http status code :: {}", resEntity.getStatusCode());
        return resEntity;
    }

}
