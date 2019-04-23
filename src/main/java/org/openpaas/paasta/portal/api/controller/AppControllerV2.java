package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.v2.applications.ApplicationEnvironmentResponse;
import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.applications.SummaryApplicationResponse;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.doppler.Envelope;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.service.AppServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by indra on 2018-05-14.
 */
@RestController
public class AppControllerV2 extends Common {


    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV2.class);

    @Autowired
    private AppServiceV2 appServiceV2;


    /**
     * 앱 요약 정보를 조회한다.
     *
     * @param guid
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/summary"}, method = RequestMethod.GET)
    public SummaryApplicationResponse getAppSummary(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("getAppSummary Start : " + guid);

        SummaryApplicationResponse respApp = appServiceV2.getAppSummary(guid, token);

        return respApp;
    }

    /**
     * 앱 실시간 상태를 조회한다.
     *
     * @param guid the app guid
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/stats"}, method = RequestMethod.GET)
    public ApplicationStatisticsResponse getAppStats(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        //service call
        ApplicationStatisticsResponse applicationStatisticsResponse = appServiceV2.getAppStats(guid, token);
        return applicationStatisticsResponse;
    }

    /**
     * 앱을 변경한다.
     *
     * @param app the app
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/rename"}, method = RequestMethod.PUT)
    public Map renameApp(@PathVariable String guid, @RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("Rename App Start : " + guid + " : " + " : " + app.getName() + " : " + app.getNewName());
        //service call
        Map result = appServiceV2.renameApp(app, token);
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
        Map result = appServiceV2.deleteApp(guid);
        LOGGER.info("delete App End ");
        return result;
    }


    /**
     * 앱을 실행한다.
     *
     * @param app the app
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/startApp"}, method = RequestMethod.POST)
    public Map startApp(@RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("startApp Start ");

        Map resultMap = appServiceV2.startApp(app, token);

        LOGGER.info("startApp End ");
        return resultMap;
    }


    /**
     * 앱을 중지한다.
     *
     * @param app the app
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/stopApp"}, method = RequestMethod.POST)
    public Map stopApp(@RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("stopApp Start ");

        Map resultMap = appServiceV2.stopApp(app, token);

        LOGGER.info("stopApp End ");
        return resultMap;
    }


    /**
     * 앱을 리스테이징한다.
     *
     * @param app   the app
     * @param token the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/restageApp"}, method = RequestMethod.POST)
    public Map restageApp(@RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("restageApp Start ");

        Map resultMap = appServiceV2.restageApp(app, token);

        LOGGER.info("restageApp End ");
        return resultMap;
    }


    /**
     * 앱 인스턴스를 변경한다.
     *
     * @param app   the app
     * @param token the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/updateApp"}, method = RequestMethod.POST)
    public Map updateApp(@RequestBody App app, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("updateApp Start ");

        Map resultMap = appServiceV2.updateApp(app, token);

        LOGGER.info("updateApp End ");
        return resultMap;
    }

    /**
     * 앱-서비스를 바인드한다.
     *
     * @param body
     * @param token the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/service-bindings"}, method = RequestMethod.POST)
    public Map bindService(@RequestBody Map body, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("bindService Start ");

        Map resultMap = appServiceV2.bindService(body, token);

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
    public Map unbindService(@PathVariable String serviceInstanceId, @PathVariable String applicationId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("unbindService Start ");

        Map resultMap = appServiceV2.unbindService(serviceInstanceId, applicationId, token);

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
    public Map unbindUserProvideService(@PathVariable String serviceInstanceId, @PathVariable String applicationId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("unbindService Start ");

        Map resultMap = appServiceV2.unbindUserProvideService(serviceInstanceId, applicationId, token);

        LOGGER.info("unbindService End ");
        return resultMap;
    }


    /**
     * 앱 이벤트를 조회한다.
     *
     * @param guid
     * @param token the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/app-usage-events/{guid}"}, method = RequestMethod.GET)
    public ListEventsResponse getAppEvents(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("getAppEvents Start : " + guid);

        ListEventsResponse respAppEvents = appServiceV2.getAppEvents(guid, token);

        LOGGER.info("getAppEvents End ");

        return respAppEvents;
    }


    /**
     * 앱 환경변수를 조회한다.
     *
     * @param guid
     * @param token the request
     * @return map application env
     * @throws Exception the exception
     * @version 1.0
     * @since 2016.6.29 최초작성
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/env"}, method = RequestMethod.GET)
    public ApplicationEnvironmentResponse getApplicationEnv(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("getApplicationEnv Start : " + guid);

        ApplicationEnvironmentResponse respAppEvents = appServiceV2.getApplicationEnv(guid, token);

        LOGGER.info("getApplicationEnv End ");

        return respAppEvents;
    }

    /**
     * 라우트 추가 및 라우트와 앱을 연결한다. (앱에 URI를 추가함)
     *
     * @param body
     * @return boolean boolean
     * @throws Exception the exception
     * @version 1.0
     */
    @RequestMapping(value = {Constants.V2_URL + "/routes"}, method = RequestMethod.POST)
    public Map addApplicationRoute(@RequestBody Map body, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("addApplicationRoute Start ");

        Map resultMap = appServiceV2.addApplicationRoute(body, token);

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
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/routes/{route_guid}"}, method = RequestMethod.DELETE)
    public Map removeApplicationRoute(@PathVariable String guid, @PathVariable String route_guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("removeApplicationRoute Start ");

        Map resultMap = appServiceV2.removeApplicationRoute(guid, route_guid, token);

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
    public Map terminateInstance(@PathVariable String guid, @PathVariable String index, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("terminateInstance Start");

        Map resultMap = appServiceV2.terminateInstance(guid, index, token);

        LOGGER.info("terminateInstance End");
        return resultMap;
    }


    /**
     * 앱 최근 로그
     *
     * @param guid
     * @return Space respSpace
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL + "/apps/{guid}/recentlogs"}, method = RequestMethod.GET)
    public Map getRecentLog(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {


        LOGGER.info("getRecentLog Start : " + guid);

        Map mapLog = new HashMap();
        try {
            List<Envelope> respAppEvents = appServiceV2.getRecentLog(guid, token);
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
     * 유저 프로바이드 credentials을 가져온다.
     *
     * @param guid
     * @param token the token
     * @return Map
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL + "/apps/{guid}/credentials")
    public Map userProvideCredentials(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return appServiceV2.userProvideCredentials(guid, token);
    }

}
