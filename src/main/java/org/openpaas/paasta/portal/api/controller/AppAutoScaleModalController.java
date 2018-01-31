package org.openpaas.paasta.portal.api.controller;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.AppAutoScaleModalService;
import org.openpaas.paasta.portal.api.service.AppService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 앱 Auto스케일 컨트롤러 - 앱 Auto스케일 정보를 조회하고 저장한다.
 *
 * @author 이인정
 * @version 1.0
 * @since 2016.7.11 최초작성
 */
@RestController
@Transactional
@JsonIgnoreProperties(ignoreUnknown = true)
@RequestMapping(value = {"/app"})
public class AppAutoScaleModalController extends Common {

    private final Logger LOGGER = getLogger(this.getClass());

    private final AppAutoScaleModalService appAutoScaleModalService;

    private final AppService appService;
    @Autowired
    public AppAutoScaleModalController(AppAutoScaleModalService appAutoScaleModalService, AppService appService) {
        this.appAutoScaleModalService = appAutoScaleModalService;
        this.appService = appService;
    }

    /**
     * Gets app auto scale info.
     *
     * @param appAutoScale the app auto scale
     * @param response     the response
     * @return the app auto scale info
     */
    @ResponseBody
    @RequestMapping(value = {"/getAppAutoScaleInfo"}, method = RequestMethod.POST)
    public Map<String, Object> getAppAutoScaleInfo(@RequestBody HashMap appAutoScale, HttpServletResponse response) {

        String guid = (null == appAutoScale.get("guid")) ? "" : appAutoScale.get("guid").toString();
        LOGGER.info("guid : "+ guid+" : request : "+ response.toString());


        Map<String, Object> resultMap;
        resultMap = appAutoScaleModalService.getAppAutoScaleInfo(appAutoScale);
        return resultMap;
    }

    /**
     * Gets app auto scale list.
     *
     * @param appAutoScale the app auto scale
     * @param response     the response
     * @return the app auto scale list
     */
    @ResponseBody
    @RequestMapping(value = {"/getAppAutoScaleList"}, method = RequestMethod.POST)
    public Map<String, Object> getAppAutoScaleList(@RequestBody HashMap appAutoScale, HttpServletResponse response) {

        String guid = (null == appAutoScale.get("guid")) ? "" : appAutoScale.get("guid").toString();
        LOGGER.info("guid : " + guid + " : request : " + response.toString());


        Map<String, Object> resultMap;
        resultMap = appAutoScaleModalService.getAppAutoScaleList(appAutoScale);
        return resultMap;
    }

    /**
     * Insert app auto scale map.
     *
     * @param appAutoScale the app auto scale
     * @param response     the response
     * @return the map
     */
    @ResponseBody
    @RequestMapping(value = {"/insertAppAutoScale"}, method = RequestMethod.POST)
    public Map<String, Object> insertAppAutoScale(@RequestBody HashMap<String, Object> appAutoScale, HttpServletResponse response) throws Exception {
        int iRst  = appAutoScaleModalService.insertAppAutoScale(appAutoScale);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * Update app auto scale map.
     *
     * @param appAutoScale the app auto scale
     * @param response     the response
     * @return the map
     */
    @ResponseBody
    @RequestMapping(value = {"/updateAppAutoScale"}, method = RequestMethod.POST)
    public Map<String, Object> updateAppAutoScale(@RequestBody HashMap<String, Object> appAutoScale, HttpServletResponse response) {
        int iRst  = appAutoScaleModalService.updateAppAutoScale(appAutoScale);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * Delete app auto scale map.
     *
     * @param appAutoScale the app auto scale
     * @param response     the response
     * @return the map
     */
    @ResponseBody
    @RequestMapping(value = {"/deleteAppAutoScale"}, method = RequestMethod.POST)
    public Map<String, Object> deleteAppAutoScale(@RequestBody HashMap<String, Object> appAutoScale, HttpServletResponse response) throws Exception {
        String guid = (null == appAutoScale.get("guid")) ? "" : appAutoScale.get("guid").toString();
        int iRst  = appAutoScaleModalService.deleteAppAutoScale(guid);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

}
