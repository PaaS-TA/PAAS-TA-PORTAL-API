package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
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
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/getAppSummary"}, method = RequestMethod.POST)
    public String getAppSummary(@RequestBody App app, HttpServletRequest request) throws Exception {

        String respApp = null;


        LOGGER.info("getAppSummary Start : " + app.getGuid());

        //token setting
//        CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        respApp = appService.getAppSummary(app, getToken());

        LOGGER.info("getAppSummary End ");


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
    @RequestMapping(value = {"/app/getAppStats"}, method = RequestMethod.POST)
    public String getAppStats(@RequestBody App app, HttpServletRequest request) throws Exception {

        String respAppStats = null;


        LOGGER.info("stopApp Start : " + app.getGuid());

        //token setting
        //CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        //String token = loginService.login("yschoi", "1qaz@WSX").getValue();
        respAppStats = appService.getAppStats(app, getToken());

        LOGGER.info("stopApp End ");


        return respAppStats;
    }


    /**
     * 앱을 변경한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/renameApp"}, method = RequestMethod.POST)
    public boolean renameApp(@RequestBody App app, HttpServletRequest request) throws Exception {


        LOGGER.info("Rename App Start : " + " : " + app.getName() + " : " + app.getNewName());

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        //service call
        appService.renameApp(app, getToken());

        LOGGER.info("Rename App End ");


        return true;
    }


    /**
     * 앱을 삭제한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/deleteApp"}, method = RequestMethod.POST)
    public boolean deleteApp(@RequestBody App app, HttpServletRequest request) throws Exception {


        LOGGER.info("delete App Start : " + app.getName());

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        appService.deleteApp(app, getToken());


        LOGGER.info("delete App End ");


        return true;
    }


    /**
     * 앱을 실행한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/startApp"}, method = RequestMethod.POST)
    public boolean startApp(@RequestBody App app, HttpServletRequest request) throws Exception {


        LOGGER.info("startApp Start : " + app.getName());

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        //service call
        appService.startApp(app, getToken());

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
    @RequestMapping(value = {"/app/stopApp"}, method = RequestMethod.POST)
    public boolean stopApp(@RequestBody App app, HttpServletRequest request) throws Exception {


        LOGGER.info("stopApp Start : " + app.getName());

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        //service call
        appService.stopApp(app, getToken());

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
    @RequestMapping(value = {"/app/restageApp"}, method = RequestMethod.POST)
    public boolean restageApp(@RequestBody App app, HttpServletRequest request) throws Exception {


        LOGGER.info("restageApp Start : " + app.getGuid());

        //token setting
        //CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        //service call
        appService.restageApp(app, getToken());

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
    @RequestMapping(value = {"/app/updateApp"}, method = RequestMethod.POST)
    public boolean updateApp(@RequestBody App app, HttpServletRequest request) throws Exception {

        ApplicationStats applicationStats = null;


        LOGGER.info("updateApp Start : " + app.getName());

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        //service call
        appService.updateApp(app, getToken());

        LOGGER.info("updateApp End ");

        return true;
    }

    /**
     * 앱-서비스를 바인드한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/bindService"}, method = RequestMethod.POST)
    public boolean bindService(@RequestBody App app, HttpServletRequest request) throws Exception {

        LOGGER.info("bindService Start : " + app.getName() + " / " + app.getServiceName());

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        //service call
        appService.bindService(app, getToken());

        LOGGER.info("bindService End ");

        return true;
    }


    /**
     * 앱-서비스를 언바인드한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/unbindService"}, method = RequestMethod.POST)
    public boolean unbindService(@RequestBody App app, HttpServletRequest request) throws Exception {

        LOGGER.info("unbindService Start : " + app.getName() + " / " + app.getServiceName());

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), app.getOrgName(), app.getSpaceName());

        //service call
        appService.unbindService(app, getToken());

        LOGGER.info("unbindService End ");

        return true;
    }


    /**
     * 앱 이벤트를 조회한다.
     *
     * @param app     the app
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/getAppEvents"}, method = RequestMethod.POST)
    public String getAppEvents(@RequestBody App app, HttpServletRequest request) throws Exception {

        String respAppEvents = null;

        LOGGER.info("getAppEvents Start : " + app.getGuid());

        CloudCredentials credentials = new CloudCredentials(new DefaultOAuth2AccessToken(request.getHeader(AUTHORIZATION_HEADER_KEY)), false);
        CustomCloudFoundryClient client = new CustomCloudFoundryClient(credentials, getTargetURL(apiTarget), true);
        client.login();

        respAppEvents = appService.getAppEvents(app, getToken());

        LOGGER.info("getAppEvents End ");

        return respAppEvents;
    }


    /**
     * 앱 환경변수를 조회한다.
     *
     * @param app     the app
     * @param request the request
     * @return map application env
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.29 최초작성
     */
    @RequestMapping(value = {"/app/getApplicationEnv"}, method = RequestMethod.POST)
    public Map getApplicationEnv(@RequestBody App app, HttpServletRequest request) throws Exception {

        LOGGER.info("getApplicationEnv Start : " + app.getName());
        //Map<String,String> appEnv = appService.getApplicationEnv(app, request.getHeader(AUTHORIZATION_HEADER_KEY));
        Map<String,String> appEnv = null;
        LOGGER.info("getApplicationEnv End ");

        return appEnv;
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

        appService.updateApplicationEnv(app, getToken());

        LOGGER.info("updateApplicationEnv End ");

        return true;
    }


    /**
     * 라우트 추가 및 라우트와 앱을 연결한다. (앱에 URI를 추가함)
     *
     * @param app     the app
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    @RequestMapping(value = {"/app/addApplicationRoute"}, method = RequestMethod.POST)
    public boolean addApplicationRoute(@RequestBody App app, HttpServletRequest request) throws Exception {

        LOGGER.info("addApplicationRoute Start : " + app.getName());

/*
            CloudRoute route = client.getRoutes("115.68.46.29.xip.io").stream()
                    .filter(existingRoute -> existingRoute.getHost().equals(host)).findAny().orElse(null);
*/

        appService.addApplicationRoute(app, getToken());

        LOGGER.info("addApplicationRoute End ");

        return true;
    }


    /**
     * 앱 라우트를 해제한다.
     *
     * @param app     the app
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    @RequestMapping(value = {"/app/removeApplicationRoute"}, method = RequestMethod.POST)
    public boolean removeApplicationRoute(@RequestBody App app, HttpServletRequest request) throws Exception {

        LOGGER.info("removeApplicationRoute Start : " + app.getName());

        appService.removeApplicationRoute(app, getToken());

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

        appService.deleteRoute(body.get("orgName").toString(), body.get("spaceName").toString(), (List)body.get("urls"), token);

        LOGGER.info("deleteRoute End ");
        return true;
    }

    /**
     * 인덱스에 의해 앱 인스턴스를 중지시킨다.
     *
     * @param param the param
     * @param req   the req
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/executeTerminateAppInstanceByIndex"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> executeTerminateAppInstanceByIndex(@RequestBody App param, HttpServletRequest req) throws Exception {
        LOGGER.info("executeTerminateAppInstanceByIndex:: param:: {}", param.toString());
        return appService.executeTerminateAppInstanceByIndex(param, req);
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
     * @param app     the app
     * @param request the request
     * @return Space respSpace
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/getRecentLogs"}, method = RequestMethod.POST)
    public Map getSpaceSummary(@RequestBody App app, HttpServletRequest request) throws Exception {

        LOGGER.info("getRecentLog Start : appGuid={}", app.getGuid().toString());

        // Get CloudFoundry class
//        TokenProvider tokenProvider = tokenProvider(request.getHeader(AUTHORIZATION_HEADER_KEY));
//        org.cloudfoundry.client.CloudFoundryClient cloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider);
//        ReactorDopplerClient reactorDopplerClient = dopplerClient(connectionContext(), tokenProvider);

        Map mapLog = new HashMap();
        try {
//            Stream<Envelope> list = appService.getRecentLog(reactorDopplerClient, app.getGuid().toString()).toStream();
//
//            mapLog.put("log", list.toArray());

        } catch (Exception e) {
            mapLog.put("log", "");
        }

        return mapLog;
    }

}
