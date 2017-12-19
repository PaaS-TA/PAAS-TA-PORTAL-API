package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.ApplicationLog;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 로그 컨트롤러 - 앱-최근로그를 조회한다.
 *
 * @author 이인정
 * @version 1.0
 * @since 2016.7.11 최초작성
 */
@RestController
//@RequestMapping(value = {"/log"})
public class LogController extends Common {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private LogService logService;

    /**
     * Gets recent logs.
     *
     * @param app      the app
     * @param request  the request
     * @param response the response
     * @return the recent logs
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/log/getRecentLogs"}, method = RequestMethod.POST)
    public Map getRecentLogs(@RequestBody App app, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String respApp = "";

        Map mapLog = new HashMap();
        LOGGER.info("getRecentLogs Start : " + app.getGuid());

        //token setting
        CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        List<ApplicationLog> listLog = logService.getLog(app, client);
        List slistLog = new ArrayList();
        for (int i =0; i < listLog.size(); i++){
            slistLog.add(listLog.get(i).toString());
        }
        LOGGER.info("getRecentLogs End ");
        mapLog.put("log",slistLog);
        return mapLog;
    }
}
