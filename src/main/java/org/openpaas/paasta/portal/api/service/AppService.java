package org.openpaas.paasta.portal.api.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.applications.*;
import org.cloudfoundry.client.v3.applications.GetApplicationEnvironmentResponse;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.applications.*;
import org.cloudfoundry.operations.applications.DeleteApplicationRequest;
import org.cloudfoundry.operations.applications.RestageApplicationRequest;
import org.cloudfoundry.operations.services.BindServiceInstanceRequest;
import org.cloudfoundry.operations.services.UnbindServiceInstanceRequest;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.mapper.cc.AppCcMapper;
import org.openpaas.paasta.portal.api.mapper.portal.AppMapper;
import org.openpaas.paasta.portal.api.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 앱 서비스 - 애플리케이션 정보 조회, 구동, 정지 등의 API 를 호출 하는 서비스이다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Service
public class AppService extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    @Autowired
    private AppCcMapper appCcMapper;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private AppAutoScaleModalService appAutoScaleModalService;

    @Autowired
    ReactorCloudFoundryClient reactorCloudFoundryClient;

    //ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));
    //DefaultCloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token));
    //DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());

    public String getAppSummary(App app, String token) throws IOException{

        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(token));

        //SummaryApplicationResponse summaryApplicationResponse = cloudFoundryClient.applicationsV2().summary(SummaryApplicationRequest.builder().applicationId(app.getGuid().toString()).build()).block();
        // 로그 추가(이후 .log() 제거)
        SummaryApplicationResponse summaryApplicationResponse = cloudFoundryClient.applicationsV2().summary(SummaryApplicationRequest.builder().applicationId(app.getGuid().toString()).build()).log().block();

        ObjectMapper objectMapper = new ObjectMapper();
        //return summaryApplicationResponse.toString();
        return objectMapper.writeValueAsString(summaryApplicationResponse);
    }


    /**
     * 앱 실시간 상태를 조회한다.
     *
     * @param app   the app
     * @param token the client
     * @return the app stats
     */
    public String getAppStats(App app, String token) {
        ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));
        ApplicationStatisticsResponse applicationStatisticsResponse = cloudFoundryClient.applicationsV2().statistics(ApplicationStatisticsRequest.builder().applicationId(app.getGuid().toString()).build()).block();
        return applicationStatisticsResponse.toString();
    }


    /**
     * 앱을 변경한다.
     *
     * @param app   the app
     * @param token the client
     * @throws Exception the exception
     */
    public void renameApp(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().rename(RenameApplicationRequest.builder().name(app.getName()).newName(app.getNewName()).build());
    }


    /**
     * 앱을 실행한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void startApp(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().start(StartApplicationRequest.builder().name(app.getName()).build());
    }


    /**
     * 앱을 중지한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void stopApp(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().stop(StopApplicationRequest.builder().name(app.getName()).build());
    }


    /**
     * 앱을 삭제한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void deleteApp(App app, String token) throws Exception {

        //앱 삭제
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().delete(DeleteApplicationRequest.builder().name(app.getName()).build());
        //AutoScale 설정 삭제
        try {
            HashMap map = new HashMap();
            map.put("guid", String.valueOf(app.getGuid()));
            if (null != appAutoScaleModalService.getAppAutoScaleInfo(map).get("list")) {
                appAutoScaleModalService.deleteAppAutoScale(String.valueOf(app.getGuid()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 앱을 리스테이징한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void restageApp(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().restage(RestageApplicationRequest.builder().name(app.getName()).build());
    }

    /**
     * 앱 인스턴스를 변경한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void updateApp(App app, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));
        if (app.getInstances() > 0) {
            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).instances(app.getInstances()).build());
        }
        if (app.getMemory() > 0) {
            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).memory(app.getMemory()).build());
        }
        if (app.getDiskQuota() > 0) {
            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).diskQuota(app.getDiskQuota()).build());
        }
    }

    /**
     * 앱-서비스를 바인드한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void bindService(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.services().bind(BindServiceInstanceRequest.builder().applicationName(app.getName()).serviceInstanceName(app.getServiceName()).build());

    }


    /**
     * 앱-서비스를 언바인드한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void unbindService(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.services().unbind(UnbindServiceInstanceRequest.builder().applicationName(app.getName()).serviceInstanceName(app.getServiceName()).build());
    }

    /**
     * 앱 이벤트를 조회한다.
     *
     * @param app    the app
     * @param token the client
     * @return the app events
     * @throws Exception the exception
     */
    public String getAppEvents(App app, String token) throws Exception {

        DefaultCloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token),app.getOrgName(),app.getSpaceName());

//        List<> = cloudFoundryOperations.applications().getEvents(GetApplicationEventsRequest.builder().name(app.getName()).build()).buffer();
//
//        String respAppEvents = client.getAppEvents(app.getGuid());

        return null;
    }

    /**
     * 앱 환경변수를 조회한다.
     *
     * @param app   the app
     * @param token the token
     * @return the application env
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.29 최초작성
     */
    public String getApplicationEnv(App app, String token) throws Exception {

        String orgName = app.getOrgName();
        String spaceName = app.getSpaceName();
        String appName = app.getName();

        if (!stringNullCheck(orgName, spaceName, appName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
        }

        DefaultCloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token),app.getOrgName(),app.getSpaceName());
        ApplicationEnvironments resp = cloudFoundryOperations.applications().getEnvironments(GetApplicationEnvironmentsRequest.builder().name(appName).build()).block();


        return resp.toString();

    }

    /**
     * 앱 환경변수 중 사용자 정의 환경변수를 추가,수정한다.
     *
     * @param app   the app
     * @param token the token
     * @return the boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.30 최초작성
     */
    public boolean updateApplicationEnv(App app, String token) throws Exception {


//        String orgName = app.getOrgName();
//        String spaceName = app.getSpaceName();
//        String appName = app.getName();
//        Map<String, String> appEnvironment = app.getEnvironment();
//
//        if (!stringNullCheck(orgName, spaceName, appName) || appEnvironment == null) {
//            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
//        }
//
//        CloudFoundryClient client = getCloudFoundryClient(token, orgName, spaceName);
//
//        client.updateApplicationEnv(appName, appEnvironment);

        return true;

    }


    /**
     * 앱 라우트를 조회한다.
     *
     * @param domainName,client
     * @return map
     * @author 김도준
     * @version 1.0
     * @since 2016.7.5 최초작성
     */
/*
    public List<CloudRoute> getRoute(String domainName, CloudFoundryClient client) throws Exception {

        List<CloudRoute> routes = client.getRoutes(domainName);

        return routes;
    }
*/


    /**
     * 라우트 추가 및 라우트와 앱을 연결한다. (앱에 URI를 추가함)
     *
     * @param app   the app
     * @param token the token
     * @return the boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    public boolean addApplicationRoute(App app, String token) throws Exception {

        String orgName = app.getOrgName();
        String spaceName = app.getSpaceName();
        String appName = app.getName();
        String host = app.getHost();
        String domainName = app.getDomainName();

        if (!stringNullCheck(orgName, spaceName, appName, host, domainName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);

        client.bindRoute(host, domainName, appName);

        return true;
    }

    /**
     * 앱 라우트를 해제한다.
     *
     * @param app   the app
     * @param token the token
     * @return the boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    public boolean removeApplicationRoute(App app, String token) throws Exception {


        String orgName = app.getOrgName();
        String spaceName = app.getSpaceName();
        String appName = app.getName();
        String host = app.getHost();
        String domainName = app.getDomainName();

        if (!stringNullCheck(orgName, spaceName, appName, host, domainName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);

        client.unbindRoute(host, domainName, appName);
        client.deleteRoute(host, domainName);

        return true;
    }

    /**
     * 라우트를 삭제한다.
     *
     * @param orgName   the org name
     * @param spaceName the space name
     * @param urls      the urls
     * @param token     the token
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean deleteRoute(String orgName, String spaceName, List<String> urls, String token) throws Exception {

        if (!stringNullCheck(orgName, spaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
        }

        if (urls == null) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);

        for (String url : urls) {
            String[] array = url.split("\\.", 2);
            client.deleteRoute(array[0], array[1]);
        }


        return true;
    }

    /**
     * 인덱스로 앱 인스턴스를 종료한다.
     *
     * @param param the param
     * @param req   the req
     * @return the map
     * @throws Exception the exception
     */
    public Map<String, Object> executeTerminateAppInstanceByIndex(App param, HttpServletRequest req) throws Exception {
        LOGGER.info("SERVICE executeTerminateAppInstanceByIndex param :: {}", param.toString());

        CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(AUTHORIZATION_HEADER_KEY), param.getOrgName(), param.getSpaceName());
        customCloudFoundryClient.terminateAppInstanceByIndex(param.getGuid(), param.getAppInstanceIndex(), param.getOrgName(), param.getSpaceName());

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 앱 이미지 URL를 조회한다.
     *
     * @param app the app
     * @return the app image url
     */
    public String getAppImageUrl(App app) {

        String buildPack = appCcMapper.getAppBuildPack(String.valueOf(app.getGuid()));

        String appImageUrl = appMapper.getAppImageUrl(buildPack);

        return appImageUrl;

    }


//    public List<ApplicationSummary> getAppSummery(DefaultCloudFoundryOperations cloudFoundryOperations) {
//        return cloudFoundryOperations.applications().list().collectList().block();
//    }
//
//    public ApplicationDetail getAppDetail(DefaultCloudFoundryOperations cloudFoundryOperations, String appName) {
//        return cloudFoundryOperations.applications().get(GetApplicationRequest.builder().name(appName).build()).block();
//    }
//
//    public Flux<Envelope> getRecentLog(ReactorDopplerClient reactorDopplerClient, String appId) {
//
//        RecentLogsRequest recentLogsRequest = RecentLogsRequest.builder().applicationId(appId).build();
//
//        Flux<Envelope> getRecentLog = reactorDopplerClient.recentLogs(recentLogsRequest);
//
//        return getRecentLog;
//    }

}
