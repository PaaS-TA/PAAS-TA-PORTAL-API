package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.v2.applications.ApplicationEnvironmentResponse;
import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.applications.SummaryApplicationResponse;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.LogMessage;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by indra on 2018-05-14.
 */
@RestController
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
     * @param guid    the app guid
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/stats"}, method = RequestMethod.GET)
    public ApplicationStatisticsResponse getAppStats(@PathVariable String guid, HttpServletRequest request) throws Exception {

        LOGGER.info("stopApp Start : " + guid);

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
    public Map startApp(@RequestBody App app, HttpServletRequest request) throws Exception {
        LOGGER.info("startApp Start ");

        Map resultMap = appService.startApp(app, this.getToken());

        LOGGER.info("startApp End ");
        return resultMap;
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
    public Map stopApp(@RequestBody App app, HttpServletRequest request) throws Exception {
        LOGGER.info("stopApp Start ");

        Map resultMap = appService.stopApp(app, this.getToken());

        LOGGER.info("stopApp End ");
        return resultMap;
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
    public Map restageApp(@RequestBody App app, HttpServletRequest request) throws Exception {
        LOGGER.info("restageApp Start ");

        Map resultMap = appService.restageApp(app, this.getToken());

        LOGGER.info("restageApp End ");
        return resultMap;
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
    public Map updateApp(@RequestBody App app, HttpServletRequest request) throws Exception {
        LOGGER.info("updateApp Start ");

        Map resultMap = appService.updateApp(app, this.getToken());

        LOGGER.info("updateApp End ");
        return resultMap;
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
    public Map bindService(@RequestBody Map body, HttpServletRequest request) throws Exception {
        LOGGER.info("bindService Start ");

        Map resultMap = appService.bindService(body, this.getToken());

        LOGGER.info("bindService End ");
        return resultMap;
    }


    /**
     * 앱-서비스를 언바인드한다.
     *
     * @param serviceInstanceId
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/service-bindings/{serviceInstanceId}/apps/{applicationId}"}, method = RequestMethod.DELETE)
    public Map unbindService(@PathVariable String serviceInstanceId, @PathVariable String applicationId) throws Exception {
        LOGGER.info("unbindService Start ");

        Map resultMap = appService.unbindService(serviceInstanceId, applicationId, this.getToken());

        LOGGER.info("unbindService End ");
        return resultMap;
    }

    /**
     * 앱-서비스를 언바인드한다.
     *
     * @param serviceInstanceId
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V2_URL + "/user-provide-service-bindings/{serviceInstanceId}/apps/{applicationId}")
    public Map unbindUserProvideService(@PathVariable String serviceInstanceId, @PathVariable String applicationId) throws Exception {
        LOGGER.info("unbindService Start ");

        Map resultMap = appService.unbindUserProvideService(serviceInstanceId, applicationId, this.getToken());

        LOGGER.info("unbindService End ");
        return resultMap;
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
    public Map addApplicationRoute(@RequestBody Map body, HttpServletRequest request) throws Exception {
        LOGGER.info("addApplicationRoute Start ");

        Map resultMap = appService.addApplicationRoute(body, this.getToken());

        LOGGER.info("addApplicationRoute End ");
        return resultMap;
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
    public Map removeApplicationRoute(@PathVariable String guid, @PathVariable String route_guid) throws Exception {
        LOGGER.info("removeApplicationRoute Start ");

        Map resultMap = appService.removeApplicationRoute(guid, route_guid, this.getToken());

        LOGGER.info("removeApplicationRoute End ");
        return resultMap;
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
    public Map terminateInstance(@PathVariable String guid, @PathVariable String index) throws Exception {
        LOGGER.info("terminateInstance Start");

        Map resultMap = appService.terminateInstance(guid, index, this.getToken());

        LOGGER.info("terminateInstance End");
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
    public Map getRecentLog(@PathVariable String guid, HttpServletRequest request) throws Exception {

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

    /**
     * 앱 최근 로그를 가져온다.
     *
     * @param guid
     * @param request the request
     * @return Map
     * @throws Exception the exception
     */
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

//    public SocketIOClient socketTailLogs(SocketIOClient client, String appName, String orgName, String spaceName) {
//        try {
//            LOGGER.info("Starting TailLog :::::");
//            client = appService.socketTailLogs(client, appName, orgName, spaceName, this.getToken());
//        } catch (Exception e) {
//            LOGGER.error(e.toString());
//        }
//        return client;
//    }


}
