package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.applications.UpdateApplicationEnvironmentVariablesResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.service.AppServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
public class AppControllerV3 extends Common {

    @Autowired
    private AppServiceV3 appService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV3.class);

    /**
     * 앱을 실행한다.
     *
     * @param app     the app
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/apps/{guid}/actions/start"}, method = RequestMethod.POST)
    public Map startApp(@RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("startApp Start ");
        Map resultMap = appService.startApp(app, token);
        LOGGER.info("startApp End ");
        return resultMap;
    }

    /**
     * 앱을 중지한다.
     *
     * @param app     the app
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/apps/{guid}/actions/stop"}, method = RequestMethod.POST)
    public Map stopApp(@RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("stopApp Start ");
        Map resultMap = appService.stopApp(app, token);
        LOGGER.info("stopApp End ");
        return resultMap;
    }

    /**
     * App env 변수 업데이트 (PATCH /v3/apps/:guid/environment_variables)
     * The updated environment variables will not take effect until the app is restarted.
     *
     * @param appGuid the appGuid
     * @param app the app
     * @param token the token
     * @return UpdateApplicationEnvironmentVariablesResponse
     *
     * 권한 : 사용자 권한
     *
     */
    @PutMapping(value = Constants.V3_URL + "/apps/{appGuid}")
    public UpdateApplicationEnvironmentVariablesResponse setAppEnv(@PathVariable String appGuid, @RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        LOGGER.info("사용자 토큰 :::: " + token);

        // TODO ::: AppV3 모델 쓰면 appGuid 가 UUID -> String 으로 바뀔 예정.
        app.setGuid(UUID.fromString(appGuid));

        return appService.setAppEnv(app, token);
    }


    /**
     * App 삭제
     *
     * @param appGuid the appGuid
     * @param token the token
     * 권한 : 사용자 권한
     *
     */
    @DeleteMapping(value = Constants.V3_URL + "/apps/{appGuid}")
    public void deleteApp(@PathVariable String appGuid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        appService.deleteApp(appGuid, token);
    }
}
