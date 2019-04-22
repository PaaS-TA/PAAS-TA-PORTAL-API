package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v3.applications.DeleteApplicationRequest;
import org.cloudfoundry.client.v3.applications.UpdateApplicationEnvironmentVariablesRequest;
import org.cloudfoundry.client.v3.applications.UpdateApplicationEnvironmentVariablesResponse;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AppServiceV3 extends Common{

    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceV3.class);

    /**
     *  App env 변수 업데이트
     *
     * @param app the app
     * @param token the token
     * @return UpdateApplicationEnvironmentVariablesResponse
     *
     * 권한 : 사용자 권한
     *
     */
    public UpdateApplicationEnvironmentVariablesResponse setAppEnv(App app, String token) {
        LOGGER.info("변경사항 있는 환경변수들 ::: " + app.getEnvironment().toString());

        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        UpdateApplicationEnvironmentVariablesResponse updatedAppEnvVar = reactorCloudFoundryClient.applicationsV3()
                .updateEnvironmentVariables(UpdateApplicationEnvironmentVariablesRequest.builder()
                        .applicationId(app.getGuid().toString())
                        .putAllVars(app.getEnvironment())
                        .build()).block();

    /**
     * 앱을 실행한다.
     *
     * @param app   the app
     * @param token the client
     * @throws Exception the exception
     */
    //@HystrixCommand(commandKey = "startApp")
    public Map startApp(App app, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            cloudFoundryClient.applicationsV3().start(org.cloudfoundry.client.v3.applications.StartApplicationRequest.builder().applicationId(app.getGuid().toString()).build()).block();
            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e.getMessage());
        }

        return resultMap;
    }

    /**
     * 앱을 중지한다.
     *
     * @param app     the app
     * @return ModelAndView model
     * @throws Exception the exception
     */
    //@HystrixCommand(commandKey = "stopApp")
    public Map stopApp(App app, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            cloudFoundryClient.applicationsV3().stop(org.cloudfoundry.client.v3.applications.StopApplicationRequest.builder().applicationId(app.getGuid().toString()).build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e.getMessage());
        }

        return resultMap;
    }



        LOGGER.info("변경사항 있는 환경변수들은요~~~ ::: " + updatedAppEnvVar.toString());
        return updatedAppEnvVar;
    }


    /**
     * 사용자 권한으로 app 삭제
     *
     * @param appGuid the appGuid
     * @param token the token
     *
     * 권한 : 사용자 권한
     *
     */
    public void deleteApp(String appGuid, String token) {
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        reactorCloudFoundryClient.applicationsV3()
                .delete(DeleteApplicationRequest.builder()
                        .applicationId(appGuid)
                        .build()).block();

        LOGGER.info("앱 삭제했다요!!!");
    }
}
