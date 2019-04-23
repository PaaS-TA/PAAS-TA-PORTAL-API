package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v3.applications.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppServiceV3 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceV3.class);



    /**
     * 앱을 실행한다.
     *
     * @param token the client
     * @throws Exception the exception
     * 권한:사용자 권한
     */
    public void startApp(String appGuid, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            cloudFoundryClient.applicationsV3().start(StartApplicationRequest.builder().applicationId(appGuid).build()).block();
            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e.getMessage());
        }

    }

    /**
     * 앱을 중지한다.
     *
     * @param token     the client
     * @return ModelAndView model
     * @throws Exception the exception
     * 권한:사용자 권한
     */
    public void stopApp(String appGuid, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            cloudFoundryClient.applicationsV3().stop(StopApplicationRequest.builder().applicationId(appGuid).build()).block();
            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e.getMessage());
        }

    }

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

        LOGGER.info("변경사항 있는 환경변수들은요~~~ ::: " + updatedAppEnvVar.toString());
        return updatedAppEnvVar;
    }



    public void getSummary(String token,String guid){

        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        GetApplicationResponse getApplicationResponse = reactorCloudFoundryClient.applicationsV3().get(org.cloudfoundry.client.v3.applications.GetApplicationRequest.builder().applicationId(guid).build()).block();
        GetApplicationCurrentDropletResponse getApplicationCurrentDropletResponse = reactorCloudFoundryClient.applicationsV3().getCurrentDroplet(GetApplicationCurrentDropletRequest.builder().applicationId(guid).build()).block();
        GetApplicationProcessResponse getApplicationProcessResponse = reactorCloudFoundryClient.applicationsV3().getProcess(GetApplicationProcessRequest.builder().applicationId(guid).build()).block();
        GetApplicationProcessStatisticsResponse processStatisticsResponse = reactorCloudFoundryClient.applicationsV3().getProcessStatistics(GetApplicationProcessStatisticsRequest.builder().applicationId(guid).build()).block();



    }



}
