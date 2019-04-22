package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.applications.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AppServiceV3 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceV3.class);

    // music-war 의 appGuid = b2d995ee-c2db-478e-86b5-f3901c000d35

    /**
     * 사용자 권한으로 Env 조회
     *
     * @param appGuid the appGuid
     * @param token the token
     * @return
     */
    public GetApplicationEnvironmentResponse getAppEnv(String appGuid, String token) {
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));

        GetApplicationEnvironmentResponse appEnv = reactorCloudFoundryClient.applicationsV3()
                .getEnvironment(GetApplicationEnvironmentRequest.builder()
                        .applicationId(appGuid)
                        .build()).block();

        LOGGER.info("앱 환경 다 내놔~~~ ::: " + appEnv.toString());

        return appEnv;
    }


    /**
     * 사용자 권한으로 Env 변수 조회
     *
     * @param appGuid the appGuid
     * @param token the token
     * @return
     */
    public GetApplicationEnvironmentVariablesResponse getAppEnvVariables(String appGuid, String token) {
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));

        GetApplicationEnvironmentVariablesResponse appEnvVariables = reactorCloudFoundryClient.applicationsV3()
                .getEnvironmentVariables(GetApplicationEnvironmentVariablesRequest.builder()
                        .applicationId(appGuid)
                        .build()).block();

        LOGGER.info("앱 환경변수 다 내놔~~~ ::: " + appEnvVariables.toString());

        return appEnvVariables;
    }


    /**
     * 사용자 권한으로 app create 시 환경변수 추가(임시!!!)
     *
     * @param app the app
     * @param token the token
     * @return
     */
    public CreateApplicationResponse createApp(App app, String token) {
        LOGGER.info("넘어온 환경변수들 ::: " + app.getEnvironment().toString());

        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        CreateApplicationResponse appResponse = reactorCloudFoundryClient.applicationsV3()
                .create(CreateApplicationRequest.builder()
                        .name(app.getName())
                        .relationships(ApplicationRelationships.builder()
                                .space(ToOneRelationship.builder()
                                        .data(Relationship.builder()
                                                .id("203b7773-f9ca-410c-8111-46db6a4ff585") // TODO :: SPACE ID 는 화면에서 받아오던가, APP 에 spaceGuid 추가해서 body 로 받아오던가.
                                                .build())
                                        .build())
                                .build())
                        .environmentVariables(app.getEnvironment())
                        .build()).block();
        LOGGER.info("생성된 app 은요~~~ ::: " + appResponse.toString());
        return appResponse;
    }


    /**
     *  사용자 권한으로 app env 변수 업데이트
     *
     * @param app the app
     * @param token the token
     * @return
     */
    public UpdateApplicationEnvironmentVariablesResponse setAppEnv(App app, String token) {
        LOGGER.info("추가될 환경변수들 ::: " + app.getEnvironment().toString());

        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        UpdateApplicationEnvironmentVariablesResponse updatedAppEnvVar = reactorCloudFoundryClient.applicationsV3()
                .updateEnvironmentVariables(UpdateApplicationEnvironmentVariablesRequest.builder()
                        .applicationId(app.getGuid().toString())
                        .putAllVars(app.getEnvironment())
                        .build()).block();


        LOGGER.info("추가될 환경변수들은요~~~ ::: " + updatedAppEnvVar.toString());
        return updatedAppEnvVar;
    }
}
