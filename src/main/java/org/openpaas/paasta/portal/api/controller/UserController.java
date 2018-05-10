package org.openpaas.paasta.portal.api.controller;

import org.apache.commons.collections.map.HashedMap;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.openpaas.paasta.portal.api.service.LoginService;
import org.openpaas.paasta.portal.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.openpaas.paasta.portal.api.service.GlusterfsServiceImpl;

/**
 * 유저 컨트롤러 - 마이페이지의 유저의 조회 수정을 처리한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.5.23 최초작성
 */
@RestController
@Transactional
@RequestMapping(value = {"/user"})
public class UserController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;


    /**
     * Update user password map.
     *
     * @param userId   the user id
     * @param body     the body
     * @param request  the request
     * @param response the response
     * @return the map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateUserPassword/{userId:.+}"}, method = RequestMethod.PUT)
    public Map updateUserPassword(@PathVariable String userId, @RequestBody Map<String, Object> body,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOGGER.info("> into updateUserPassword");

        Map<String, Object> result = new HashMap<>();

        String oldPassword  = (String)body.get("oldPassword");
        String newPassword  = (String)body.get("password");
        String token        = request.getHeader(AUTHORIZATION_HEADER_KEY);

//        try {
//            userService.updateUserPassword(getCustomCloudFoundryClient(token), getCloudCredentials(userId, oldPassword), newPassword);
//        } catch(CloudFoundryException cfe) {
//            System.out.println(cfe);
//            response.sendError(cfe.getStatusCode().value(), cfe.getDescription());
//        }

        return result;
    }



    /**
     * Delete user map.
     *
     * @param userId   the user id
     * @param body     the body
     * @param response the response
     * @return the map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteUser/{userId:.+}"}, method = RequestMethod.PUT)
    public Map deleteUser(@PathVariable String userId, @RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        LOGGER.info("> into deleteUser");

        String password = (String)body.get("password");

        Map<String, Object> result = new HashMap<>();
        int deleteResult = -1;
        try {
//            CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
//            CustomCloudFoundryClient ccfc = getCustomCloudFoundryClient(userId, password);
//
//            deleteResult = userService.deleteUser(adminCcfc, ccfc, userId);

            if (deleteResult < 1) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User does not exist.");
            }

        } catch (CloudFoundryException cfe) {
            System.out.println(cfe);
            response.sendError(cfe.getStatusCode().value(), cfe.getDescription());
        }

        result.put("result", deleteResult);
        return result;
    }


    /**
     * Gets list for the user.
     *
     * @param keyOfRole the key of role
     * @param request   the request
     * @param response  the response
     * @return the list for the user
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getListForTheUser/{keyOfRole}"}, method = RequestMethod.POST)
    public List<Map> getListForTheUser(@PathVariable String keyOfRole, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //to return
        List<Map> listOrgOrSpace =new ArrayList<>();

        listOrgOrSpace = userService.getListForTheUser(keyOfRole, request.getHeader(AUTHORIZATION_HEADER_KEY));

        return listOrgOrSpace;
    }

    /**
     * 계정생성 화면
     *
     * @param request  the request
     * @param response the response
     * @return map
     */
    @RequestMapping(value = "/addUser")
    @ResponseBody
    public Map<String, Object> addUser(@RequestBody HashMap request, HttpServletResponse response) {
        String userId = (null == request.get("userId")) ? "" : request.get("userId").toString();
        LOGGER.info("userId : "+ userId+" : request : "+ response.toString());
        Map<String, Object> result = new HashedMap();
        result.put("status",0);
        return  result;
    }






    /**
     * Reset password map.
     *
     * @param userDetail the user detail
     * @param response   the response
     * @return map
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    @RequestMapping(value = {"/resetPassword"})
    public Map<String, Object> resetPassword(@RequestBody UserDetail userDetail, HttpServletResponse response) throws IOException, MessagingException {
        HashMap body = new HashMap();
        Map<String, Object> resultMap = new HashMap();

        body.put("userId", userDetail.getUserId());

        LOGGER.info("userId : " + userDetail.getUserId() + " : request : " + response.toString());
//        body.put("status", "1");
//        List<UserDetail> listUser = userService.resetPassword(userDetail.getUserId(),userDetail);
//        if(listUser.size() >= 1) {
//            HashMap map = body;
//            map.put("searchUserId", userDetail.getUserId());
//            Boolean resultCreateUser = userService.resetPassword(map);
//            resultMap.put("resetPassword", resultCreateUser);
//
//        }
//        resultMap.put("resultUser",listUser.size());
        return resultMap;
    }



    /**
     * 비밀번호 재설정을 한다.
     *
     * @param userDetail the user detail
     * @return map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/authResetPassword"})
    public Map<String, Object> authResetPassword(@RequestBody HashMap userDetail) throws Exception {
        Map<String, Object> resultMap = new HashMap();
//        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
//        boolean resultCreateUser = userService.updateAuthUserPassword(adminCcfc, userDetail);
//        resultMap.put("bRtn", resultCreateUser);

        return resultMap;
    }



    /**
     * 모든 Uaa 유저의 이름과 Guid를 목록으로 가져온다.
     *
     * @return map all user name
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getUserInfo"}, method = RequestMethod.GET)
    public Map<String, Object> getAllUserName() throws Exception {
//        List<Map<String,String>> userInfo = userService.getUserInfo();
//        Map<String, Object> resultMap = new HashMap();
//        resultMap.put("userInfo", userInfo);
        return null;
    }

}