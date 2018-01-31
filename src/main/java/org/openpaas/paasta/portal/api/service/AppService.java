package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.RecentLogsRequest;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.applications.ApplicationDetail;
import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.cloudfoundry.operations.applications.GetApplicationRequest;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
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
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 앱 요약 정보를 조회한다.
     *
     * @param app    the app
     * @param client the client
     * @return the app summary
     */
    public String getAppSummary(App app,  CustomCloudFoundryClient client) {

        String respApp = client.getAppSummary(app.getGuid());

        return respApp;

    }


    /**
     * 앱 실시간 상태를 조회한다.
     *
     * @param app    the app
     * @param client the client
     * @return the app stats
     */
    public String getAppStats(App app,    CustomCloudFoundryClient client) {

        String respAppStats = client.getAppStats(app.getGuid());

        return respAppStats;
    }


    /**
     * 앱을 변경한다.
     *
     * @param app    the app
     * @param client the client
     * @throws Exception the exception
     */
    public void renameApp(App app, CloudFoundryClient client) throws Exception {

            client.rename(app.getName(), app.getNewName());

    }


    /**
     * 앱을 실행한다.
     *
     * @param app    the app
     * @param client the client
     * @throws Exception the exception
     */
    public void startApp(App app,  CloudFoundryClient client) throws Exception {

            client.startApplication(app.getName());

    }


    /**
     * 앱을 중지한다.
     *
     * @param app    the app
     * @param client the client
     * @throws Exception the exception
     */
    public void stopApp(App app,  CloudFoundryClient client) throws Exception {

        client.stopApplication(app.getName());
    }


    /**
     * 앱을 삭제한다.
     *
     * @param app    the app
     * @param client the client
     * @throws Exception the exception
     */
    public void deleteApp(App app,  CloudFoundryClient client) throws Exception {

        //앱 삭제
        client.deleteApplication(app.getName());

        //AutoScale 설정 삭제
        try {
            HashMap map = new HashMap();
            map.put("guid", String.valueOf(app.getGuid()));
            if (null != appAutoScaleModalService.getAppAutoScaleInfo(map).get("list")) {
                appAutoScaleModalService.deleteAppAutoScale(String.valueOf(app.getGuid()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 앱을 리스테이징한다.
     *
     * @param app    the app
     * @param client the client
     * @throws Exception the exception
     */
    public void restageApp(App app,  CustomCloudFoundryClient client) throws Exception {

        client.restageApp(app.getGuid());

    }

    /**
     * 앱 인스턴스를 변경한다.
     *
     * @param app    the app
     * @param client the client
     * @throws Exception the exception
     */
    public void updateApp(App app,  CloudFoundryClient client) throws Exception {

            if(app.getInstances() > 0) {
                client.updateApplicationInstances(app.getName(), app.getInstances());
            }
            if(app.getMemory() > 0) {
                client.updateApplicationMemory(app.getName(), app.getMemory());
            }
            if(app.getDiskQuota() > 0) {
                client.updateApplicationDiskQuota(app.getName(), app.getDiskQuota());
            }
    }

    /**
     * 앱-서비스를 바인드한다.
     *
     * @param app    the app
     * @param client the client
     * @throws Exception the exception
     */
    public void bindService(App app,  CloudFoundryClient client) throws Exception {

            client.bindService(app.getName(), app.getServiceName());

    }


    /**
     * 앱-서비스를 언바인드한다.
     *
     * @param app    the app
     * @param client the client
     * @throws Exception the exception
     */
    public void unbindService(App app,  CloudFoundryClient client) throws Exception {

            client.unbindService(app.getName(), app.getServiceName());

    }

    /**
     * 앱 이벤트를 조회한다.
     *
     * @param app    the app
     * @param client the client
     * @return the app events
     * @throws Exception the exception
     */
    public String getAppEvents(App app,  CustomCloudFoundryClient client) throws Exception {

        String respAppEvents =  client.getAppEvents(app.getGuid());

        return respAppEvents;
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
    public Map getApplicationEnv(App app, String token) throws Exception {

        String orgName = app.getOrgName();
        String spaceName = app.getSpaceName();
        String appName = app.getName();

        if (!stringNullCheck(orgName,spaceName,appName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CloudFoundryClient client = getCloudFoundryClient(token, orgName, spaceName);

        Map appEnv = client.getApplicationEnvironment(appName);

        return appEnv;

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


        String orgName = app.getOrgName();
        String spaceName = app.getSpaceName();
        String appName = app.getName();
        Map<String, String> appEnvironment = app.getEnvironment();

        if (!stringNullCheck(orgName,spaceName,appName) || appEnvironment == null) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CloudFoundryClient client = getCloudFoundryClient(token, orgName, spaceName);

        client.updateApplicationEnv(appName, appEnvironment);

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

        if (!stringNullCheck(orgName,spaceName,appName,host,domainName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token,orgName, spaceName);

        client.bindRoute(host,domainName,appName);

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

        if (!stringNullCheck(orgName,spaceName,appName,host,domainName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);

        client.unbindRoute(host,domainName,appName);
        client.deleteRoute(host,domainName);

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

        if (!stringNullCheck(orgName,spaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        if (urls == null) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);

        for(String url : urls){
            String[] array = url.split("\\.",2);
            client.deleteRoute(array[0],array[1]);
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

        return new HashMap<String, Object>(){{put("RESULT", Constants.RESULT_STATUS_SUCCESS);}};
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


    public List<ApplicationSummary> getAppSummery(DefaultCloudFoundryOperations cloudFoundryOperations) {
        return cloudFoundryOperations.applications().list().collectList().block();
    }

    public ApplicationDetail getAppDetail(DefaultCloudFoundryOperations cloudFoundryOperations, String appName) {
        return cloudFoundryOperations.applications().get(
                GetApplicationRequest.builder()
                        .name(appName).build())
                .block();
    }

    public Flux<Envelope> getRecentLog(ReactorDopplerClient reactorDopplerClient, String appId) {

        RecentLogsRequest recentLogsRequest = RecentLogsRequest.builder()
                .applicationId(appId)
                .build();

        Flux<Envelope> getRecentLog = reactorDopplerClient.recentLogs(recentLogsRequest);

        return getRecentLog;
    }

}
