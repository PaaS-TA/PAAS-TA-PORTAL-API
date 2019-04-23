package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.model.AppV3;
import org.openpaas.paasta.portal.api.service.AppServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class AppControllerV3 extends Common {

    @Autowired
    private AppServiceV3 appServiceV3;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV3.class);


    ///////////////////////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 3                                    //////
    //////   Document : http://v3-apidocs.cloudfoundry.org/version/3.69.0/index.html //////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 앱을 실행한다.
     *
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @PostMapping(value = {Constants.V3_URL + "/apps/{guid}/actions/start"})
    public Map startApp(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("startApp Start ");
        Map resultMap = appServiceV3.startApp(guid, token);
        LOGGER.info("startApp End ");
        return resultMap;
    }

    /**
     * 앱을 중지한다.
     *
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @PostMapping(value = {Constants.V3_URL + "/apps/{guid}/actions/stop"})
    public Map stopApp(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("stopApp Start ");
        Map resultMap = appServiceV3.stopApp(guid, token);
        LOGGER.info("stopApp End ");
        return resultMap;
    }

    /**
     * 앱 요약 정보를 조회한다.
     *
     * @param guid
     * @return AppV3 appV3
     * @throws Exception the exception
     * 권한 : 사용자 권한
     */
    @GetMapping(Constants.V3_URL + "/apps/{guid}/summary")
    public AppV3 getAppSummary(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        AppV3 appV3 = appService.getAppSummary(guid, token);
        return appV3;
    }


}

