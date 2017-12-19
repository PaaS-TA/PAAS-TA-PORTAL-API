package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.WebIdeUser;
import org.openpaas.paasta.portal.api.service.WebIdeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * WEB IDE 관리 컨트롤러 - WEB IDE 신청자를 관리하는 컨트롤러이다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
@Transactional
@RequestMapping(value = {"/webIdeUser"})
public class WebIdeUserController extends Common {

    @Autowired
    private WebIdeUserService webIdeUserService;

    /**
     * WEB IDE 사용자 정보를 조회한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    @RequestMapping(value = {"/getUser"}, method = RequestMethod.POST)
    public WebIdeUser getUser(@RequestBody WebIdeUser webIdeUser) {

        WebIdeUser rstWebIdeUser = new WebIdeUser();
        rstWebIdeUser = webIdeUserService.getUser(webIdeUser);
        if(rstWebIdeUser == null) {
            return webIdeUser;
        }
        return rstWebIdeUser;
    }

    /**
     * WEB IDE 사용자를 저장한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    @RequestMapping(value = {"/insertUser"}, method = RequestMethod.POST)
    public Map<String, Object>  insertUser(@RequestBody WebIdeUser webIdeUser){
        Map<String, Object> resultMap = new HashMap<>();
        webIdeUserService.insertUser(webIdeUser);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * WEB IDE 사용자를 수정한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    @RequestMapping(value = {"/updateUser"}, method = RequestMethod.POST)
    public Map<String, Object>  updateUser(@RequestBody WebIdeUser webIdeUser) {
        Map<String, Object> resultMap = new HashMap<>();
        webIdeUserService.updateUser(webIdeUser);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        return resultMap;
    }

    /**
     * WEB IDE 사용자를 삭제한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    @RequestMapping(value = {"/deleteUser"}, method = RequestMethod.POST)
    public Map<String, Object>  deleteUser(@RequestBody WebIdeUser webIdeUser) {
        Map<String, Object> resultMap = new HashMap<>();
        webIdeUserService.deleteUser(webIdeUser);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * WEB IDE 사용자 리스트를 조회한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    @RequestMapping(value = {"/getList"}, method = RequestMethod.POST)
    public Map<String, Object> getList(@RequestBody WebIdeUser webIdeUser) {

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("list", webIdeUserService.getList(webIdeUser));

        return resultMap;
    }
}
