package org.openpaas.paasta.portal.api.controller;

import com.corundumstudio.socketio.SocketIOClient;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.v2.applications.ApplicationEnvironmentResponse;
import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.applications.SummaryApplicationResponse;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.LogMessage;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 앱 컨트롤러 - 애플리케이션 정보 조회, 구동, 정지 등의 API 를 호출 하는 컨트롤러이다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
@Transactional
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private AppService appService;

//    @Autowired
//    private LoginService loginService;

    /**
     * 앱 요약 정보를 조회한다.
     *
     * @param guid
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/summary"}, method = RequestMethod.GET)
    public SummaryApplicationResponse getAppSummary(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getAppSummary Start : " + guid);

        SummaryApplicationResponse respApp = appService.getAppSummary(guid, this.getToken());

        return respApp;
    }


    /**
     * 앱 실시간 상태를 조회한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
//    @RequestMapping(value = {"/app/getAppStats"}, method = RequestMethod.POST)
//    public String getAppStats(@RequestBody App app, HttpServletRequest request) throws Exception {
//
//        String respAppStats = null;
//
//        //LOGGER.info("getAppStats Start : " + app.getGuid());
//        LOGGER.info("getAppStats Start : ");
//
//        //token setting
//        //CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));
//
//        //service call
//        //String token = loginService.login("yschoi", "1qaz@WSX").getValue();
//
////        respAppStats = appService.getAppStats(app, this.getToken());
//
//
//        CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());
//        respAppStats = appService.getAppStats(app,client);
//
//
//        LOGGER.info("stopApp End ");
//
//
//        return respAppStats;
//    }

    /**
     * 앱 실시간 상태를 조회한다.
     *
     * @param guid    the app guid
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/stats"}, method = RequestMethod.GET)
    public ApplicationStatisticsResponse getAppStats(@PathVariable String guid, HttpServletRequest request) throws Exception {

        LOGGER.info("stopApp Start : " + guid);

        //token setting
        //CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        ApplicationStatisticsResponse applicationStatisticsResponse = appService.getAppStats(guid, this.getToken());

        LOGGER.info("stopApp End ");

        return applicationStatisticsResponse;
    }

    /**
     * 앱을 변경한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/rename"}, method = RequestMethod.PUT)
    public Map renameApp(@PathVariable String guid, @RequestBody App app, HttpServletRequest request) throws Exception {
        LOGGER.info("Rename App Start : " + guid + " : " + " : " + app.getName() + " : " + app.getNewName());
        //service call
        Map result = appService.renameApp(app,guid);
        LOGGER.info("Rename App End ");
        return result;
    }


    /**
     * 앱을 삭제한다.
     *
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}"}, method = RequestMethod.DELETE)
    public Map deleteApp(@PathVariable String guid) throws Exception {
        LOGGER.info("delete App Start : " + guid);
        Map result = appService.deleteApp(guid);
        LOGGER.info("delete App End ");
        return result;
    }


    /**
     * 앱을 실행한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/apps/startApp"}, method = RequestMethod.POST)
    public boolean startApp(@RequestBody App app, HttpServletRequest request) throws Exception {
        LOGGER.info("startApp Start ");

        appService.startApp(app, this.getToken());

        LOGGER.info("startApp End ");
        return true;
    }


    /**
     * 앱을 중지한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/apps/stopApp"}, method = RequestMethod.POST)
    public boolean stopApp(@RequestBody App app, HttpServletRequest request) throws Exception {
        LOGGER.info("stopApp Start ");

        appService.stopApp(app, this.getToken());

        LOGGER.info("stopApp End ");
        return true;
    }


    /**
     * 앱을 리스테이징한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/restageApp"}, method = RequestMethod.POST)
    public boolean restageApp(@RequestBody App app, HttpServletRequest request) throws Exception {
        LOGGER.info("restageApp Start ");

        appService.restageApp(app, this.getToken());

        LOGGER.info("restageApp End ");
        return true;
    }


    /**
     * 앱 인스턴스를 변경한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/updateApp"}, method = RequestMethod.POST)
    public boolean updateApp(@RequestBody App app, HttpServletRequest request) throws Exception {

        ApplicationStats applicationStats = null;


        LOGGER.info("updateApp Start : " + app.getGuid());
        LOGGER.info("getInstances : " + app.getInstances());
        LOGGER.info("getMemory : " + app.getMemory());
        LOGGER.info("getDiskQuota : " + app.getDiskQuota());
        LOGGER.info("getName : " + app.getName());
        LOGGER.info("getEnvironment : " + app.getEnvironment());

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        //service call
        appService.updateApp(app, this.getToken());

        LOGGER.info("updateApp End ");

        return true;
    }

    /**
     * 앱-서비스를 바인드한다.
     *
     * @param body
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/service-bindings"}, method = RequestMethod.POST)
    public boolean bindService(@RequestBody Map body, HttpServletRequest request) throws Exception {
        LOGGER.info("bindService Start ");

        appService.bindService(body, this.getToken());

        LOGGER.info("bindService End ");
        return true;
    }


    /**
     * 앱-서비스를 언바인드한다.
     *
     * @param serviceInstanceId
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/service-bindings/{serviceInstanceId}/apps/{applicationId}"}, method = RequestMethod.DELETE)
    public boolean unbindService(@PathVariable String serviceInstanceId, @PathVariable String applicationId) throws Exception {
        LOGGER.info("unbindService Start ");

        appService.unbindService(serviceInstanceId, applicationId, this.getToken());

        LOGGER.info("unbindService End ");
        return true;
    }


    /**
     * 앱 이벤트를 조회한다.
     *
     * @param guid
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/app-usage-events/{guid}"}, method = RequestMethod.GET)
    public ListEventsResponse getAppEvents(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getAppEvents Start : " + guid);

        ListEventsResponse respAppEvents = appService.getAppEvents(guid, this.getToken());

        LOGGER.info("getAppEvents End ");

        return respAppEvents;
    }


    /**
     * 앱 환경변수를 조회한다.
     *
     * @param guid
     * @param request the request
     * @return map application env
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.29 최초작성
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/env"}, method = RequestMethod.GET)
    public ApplicationEnvironmentResponse getApplicationEnv(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getApplicationEnv Start : " + guid);

        ApplicationEnvironmentResponse respAppEvents = appService.getApplicationEnv(guid, this.getToken());

        LOGGER.info("getApplicationEnv End ");

        return respAppEvents;
    }

    /**
     * 앱 환경변수 중 사용자 정의 환경변수 추가,수정한다.
     *
     * @param app     the app
     * @param request the request
     * @return map boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.30 최초작성
     */
    @RequestMapping(value = {"/app/updateApplicationEnv"}, method = RequestMethod.POST)
    public boolean updateApplicationEnv(@RequestBody App app, HttpServletRequest request) throws Exception {

        LOGGER.info("updateApplicationEnv Start : " + app.getName());

        appService.updateApplicationEnv(app, this.getToken());

        LOGGER.info("updateApplicationEnv End ");

        return true;
    }


    /**
     * 라우트 추가 및 라우트와 앱을 연결한다. (앱에 URI를 추가함)
     *
     * @param body
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    @RequestMapping(value = {Constants.V2_URL + "/routes"}, method = RequestMethod.POST)
    public boolean addApplicationRoute(@RequestBody Map body, HttpServletRequest request) throws Exception {
        LOGGER.info("addApplicationRoute Start ");

        appService.addApplicationRoute(body, this.getToken());

        LOGGER.info("addApplicationRoute End ");

        return true;
    }


    /**
     * 앱 라우트를 해제한다.
     *
     * @param guid
     * @param route_guid
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/routes/{route_guid}"}, method = RequestMethod.DELETE)
    public boolean removeApplicationRoute(@PathVariable String guid, @PathVariable String route_guid) throws Exception {

        LOGGER.info("removeApplicationRoute Start ");

        appService.removeApplicationRoute(guid, route_guid, this.getToken());

        LOGGER.info("removeApplicationRoute End ");
        return true;
    }

    /**
     * 앱 라우트를 삭제한다.
     *
     * @param token the token
     * @param body  the body
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    @RequestMapping(value = {"/app/deleteRoute"}, method = RequestMethod.POST)
    public boolean removeApplicationRoute(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map body) throws Exception {

        LOGGER.info("deleteRoute Start");

        appService.deleteRoute(body.get("orgName").toString(), body.get("spaceName").toString(), (List) body.get("urls"), token);

        LOGGER.info("deleteRoute End ");
        return true;
    }

    /**
     * 인덱스에 의해 앱 인스턴스를 중지시킨다.
     *
     * @param guid
     * @param index
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/instances/{index}"}, method = RequestMethod.DELETE)
    public boolean terminateInstance(@PathVariable String guid, @PathVariable String index) throws Exception {
        LOGGER.info("terminateInstance Start");
        appService.terminateInstance(guid, index, this.getToken());
        LOGGER.info("terminateInstance End");
        return true;
    }


    /**
     * 앱 이미지를 조회한다.
     *
     * @param app the app
     * @return app image url
     */
    @RequestMapping(value = {"/app/getAppImageUrl"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getAppImageUrl(@RequestBody App app) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("appImageUrl", appService.getAppImageUrl(app));

        return resultMap;
    }


    /**
     * 앱 최근 로그
     *
     * @param guid
     * @param request the request
     * @return Space respSpace
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/recentlogs"}, method = RequestMethod.GET)
    public Map getSpaceSummary(@PathVariable String guid, HttpServletRequest request) throws Exception {

        LOGGER.info("getRecentLog Start : " + guid);

        Map mapLog = new HashMap();
        try {
            List<Envelope> respAppEvents = appService.getRecentLog(guid, this.getToken());
            mapLog.put("log", respAppEvents);
        } catch (Exception e) {
            LOGGER.info("################ ");
            LOGGER.error(e.toString());
            mapLog.put("log", "");
        }

        LOGGER.info("getRecentLog End");

        return mapLog;
    }

    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/taillogs"}, method = RequestMethod.GET)
    public Map getTailLogs(@PathVariable String guid, HttpServletRequest request) throws Exception {

        LOGGER.info("getTailLogs Start ");

        Map mapLog = new HashMap();
        try {
            List<LogMessage> respAppEvents = appService.getTailLog(guid, this.getToken());
            mapLog.put("log", respAppEvents);
        } catch (Exception e) {
            LOGGER.info("################ ");
            LOGGER.error(e.toString());
            mapLog.put("log", "");
        }

        LOGGER.info("getTailLogs End ");

        return mapLog;
    }

    public SocketIOClient socketTailLogs(SocketIOClient client, String appName, String orgName, String spaceName) {
        try {
            client = appService.socketTailLogs(client, appName, orgName, spaceName, this.getToken());
        } catch (Exception e) {
            LOGGER.error(e.toString());

        }
        return client;
    }

}
