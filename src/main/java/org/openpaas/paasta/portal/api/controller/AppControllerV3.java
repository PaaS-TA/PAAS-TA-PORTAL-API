package org.openpaas.paasta.portal.api.controller;

import com.unboundid.scim.wink.PATCH;
import org.cloudfoundry.client.v3.applications.StartApplicationResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.service.AppServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class AppControllerV3 extends Common {

    @Autowired
    private AppServiceV3 appServiceV3;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV3.class);



    ///////////////////////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 3                                    //////
    //////   Document : http://v3-apidocs.cloudfoundry.org/version/3.69.0/index.html //////
    ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 앱을 실행한다.
     *
     * @return ModelAndView model
     * @throws Exception the exception
     *
     */
    @PostMapping(value = {Constants.V3_URL + "/apps/{appGuid}/actions/start"})
    public void startApp(@PathVariable String appGuid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("startApp Start ");
        appServiceV3.startApp(appGuid,token);
        LOGGER.info("startApp End ");
    }

    /**
     * 앱을 중지한다.
     *
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @PostMapping(value = {Constants.V3_URL + "/apps/{appGuid}/actions/stop"})
    public void stopApp(@PathVariable String appGuid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("stopApp Start ");
        appServiceV3.stopApp(appGuid, token);
        LOGGER.info("stopApp End ");
    }

    /**
     * 앱 요약 정보를 조회한다.
     *
     * @param guid
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/apps/{guid}/summary"}, method = RequestMethod.GET)
    public void getAppSummary(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("getAppSummary Start : " + guid);
        appServiceV3.getSummary(token, guid);
    }


}
