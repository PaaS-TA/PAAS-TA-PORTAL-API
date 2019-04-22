package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.applications.CreateApplicationResponse;
import org.cloudfoundry.client.v3.applications.GetApplicationEnvironmentResponse;
import org.cloudfoundry.client.v3.applications.GetApplicationEnvironmentVariablesResponse;
import org.cloudfoundry.client.v3.applications.UpdateApplicationEnvironmentVariablesResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.service.AppServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class AppControllerV3 extends Common {

    @Autowired
    private AppServiceV3 appService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV3.class);


    /**
     * 사용자 권한으로 Env 조회
     * GET /v3/apps/:guid/env
     *
     * @param appGuid
     * @return
     */
    @GetMapping(value = "/v3/apps/{appGuid}/env")
    public GetApplicationEnvironmentResponse getAppEnv(@PathVariable String appGuid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        LOGGER.info("사용자 토큰 :::: " + token);
        return appService.getAppEnv(appGuid, token);
    }

    /**
     * 사용자 권한으로 Env 변수 조회
     * GET /v3/apps/:guid/environment_variables
     *
     * @param appGuid
     * @return
     */
    @GetMapping(value = "/v3/apps/{appGuid}/environment_variables")
    public GetApplicationEnvironmentVariablesResponse getAppEnvVariables(@PathVariable String appGuid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        LOGGER.info("사용자 토큰 :::: " + token);
        return appService.getAppEnvVariables(appGuid, token);
    }


    // 1. App Create 시 환경변수 넣을 수 있음. (POST /v3/apps)
    /**
     * 사용자 권한으로 app create 시 환경변수 추가
     *
     * @param app
     * @return
     */
    @PostMapping(value = "/v3/apps")
    public CreateApplicationResponse createApp(@RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        LOGGER.info("사용자 토큰 :::: " + token);
        return appService.createApp(app, token);
    }



    // 2. Update environment variables for an app (PATCH /v3/apps/:guid/environment_variables)
    /**
     * 사용자 권한으로 app env 변수 업데이트
     *
     * The updated environment variables will not take effect until the app is restarted.
     *
     */
    @PatchMapping(value = "/v3/apps/{appGuid}")
    public UpdateApplicationEnvironmentVariablesResponse setAppEnv(@PathVariable String appGuid, @RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        LOGGER.info("사용자 토큰 :::: " + token);
        app.setGuid(UUID.fromString(appGuid));

        return appService.setAppEnv(app, token);
    }


}
