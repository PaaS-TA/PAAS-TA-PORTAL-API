package org.openpaas.paasta.portal.api.controller;

import org.apache.commons.collections.map.HashedMap;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.openpaas.paasta.portal.api.service.GlusterfsServiceImpl;
import org.openpaas.paasta.portal.api.service.LoginService;
import org.openpaas.paasta.portal.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    @Autowired
    private GlusterfsServiceImpl glusterfsService;

    /**
     * 사용자 총 명수
     *
     * @param
     * @return App user count
     */
    @RequestMapping(value = {"/getUserCount"}, method = RequestMethod.GET)
    public UserDetail getUserCount() {

        int userCnt = userService.getUserCount();

        LOGGER.info("count : " + userCnt);

        UserDetail user = new UserDetail();
        user.setCount(userCnt);

        return user;
    }

    /**
     * Update user map.
     *
     * @param userId   the user id
     * @param body     the body
     * @param response the response
     * @return Map { "result": updateCount}
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateUser/{userId:.+}"}, method = RequestMethod.PUT, consumes="application/json")
    public Map updateUser(@PathVariable String userId, @RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception{

        LOGGER.info("> into updateUser...");

        //UserDetail user = new UserDetail(body);
        UserDetail user = null;
        Map<String, Object> result = new HashMap<>();

        user = userService.getUser(userId);

        if( user == null ) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User does not exist.");
        } else {

            if ( body.containsKey("userName") )         user.setUserName((String)body.get("userName"));
            if ( body.containsKey("status") )           user.setStatus((String) body.get("status"));
            if ( body.containsKey("addressDetail") )    user.setAddressDetail((String) body.get("addressDetail"));
            if ( body.containsKey("address") )          user.setAddress((String) body.get("address"));
            if ( body.containsKey("tellPhone") )        user.setTellPhone((String) body.get("tellPhone"));
            if ( body.containsKey("zipCode") )          user.setZipCode((String) body.get("zipCode"));
            if ( body.containsKey("adminYn") )          user.setAdminYn((String) body.get("adminYn"));
            if ( body.containsKey("imgPath") ) {
                if (user.getImgPath() != null) glusterfsService.delete(user.getImgPath());
                user.setImgPath((String) body.get("imgPath"));
            }

            int cnt = userService.updateUser(userId, user);
            result.put("result", cnt);
        }

        return result;
    }

    /**
     * Update user email map.
     *
     * @param userId   the user id
     * @param body     the body
     * @param response the response
     * @return the map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateUserEmail/{userId:.+}"}, method = RequestMethod.PUT)
    public Map updateUserEmail(@PathVariable String userId, @RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {

        LOGGER.info("> into updateUserEmail");

        Map<String, Object> result = new HashMap<>();

        String searchUserId = userId;
        String newUserId    = (String)body.get("userId");

        int cnt = userService.updateUserId(searchUserId, newUserId);

        if ( cnt < 0 ) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "User already exist.");
        } else if (cnt == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User does not exist.");
        } else {
            result.put("result", cnt);
        }

        return result;
    }

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

        try {
            userService.updateUserPassword(getCustomCloudFoundryClient(token), getCloudCredentials(userId, oldPassword), newPassword);
        } catch(CloudFoundryException cfe) {
            System.out.println(cfe);
            response.sendError(cfe.getStatusCode().value(), cfe.getDescription());
        }

        return result;
    }

    /**
     * Gets user.
     *
     * @param userId the user id
     * @return Map user
     */
    @RequestMapping(value = {"/getUser/{userId:.+}"}, method = RequestMethod.GET)
    public Map getUser(@PathVariable String userId) {
        LOGGER.info("> into getUser...");

        UserDetail user = userService.getUser(userId);

        Map<String, Object> result = new HashMap<>();

        result.put("User", user);

        return result;
    }

    /**
     * Gets user and orgs.
     *
     * @param userId   the user id
     * @param request  the request
     * @param response the response
     * @return Map user and orgs
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getUserAndOrgs/{userId:.+}"}, method = RequestMethod.GET)
    public Map getUserAndOrgs(@PathVariable String userId , HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOGGER.info(">getUserAndOrgs...");

        Map<String, Object> result = new HashMap<>();

        UserDetail user = userService.getUser(userId);

        if( user == null ) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User does not exist.");
        } else {
            CloudCredentials credentials = new CloudCredentials(new DefaultOAuth2AccessToken(request.getHeader(AUTHORIZATION_HEADER_KEY)),false);
            CloudFoundryClient client = new CloudFoundryClient(credentials, getTargetURL(apiTarget), true);

            List<CloudOrganization> orgs = client.getOrganizations();

            result.put("Orgs", orgs);
            result.put("User", user);
        }

        return result;
    }

    /**
     * Insert user map.
     *
     * @param body     the body
     * @param response the response
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/insertUser"})
    public Map insertUser(@RequestBody Map<String, Object> body, HttpServletResponse response) throws Exception {
        LOGGER.info("> into insertUser...");

        UserDetail user = new UserDetail(body);
        Map<String, Object> result = new HashMap<>();

        int createResult = 0;

        if ( userService.getUser(user.getUserId()) != null && adminUserName != user.getUserId()) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "User already exists.");
        } else {
            if(adminUserName.equals(user.getUserId())){
                user.setAdminYn("Y");
            }
            createResult = userService.createUser(user);
            result.put("result", createResult);
        }
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
            CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
            CustomCloudFoundryClient ccfc = getCustomCloudFoundryClient(userId, password);

            deleteResult = userService.deleteUser(adminCcfc, ccfc, userId);

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
     * 이메일 인증 사용자 확인
     *
     * @param userDetail the user detail
     * @param response   the response
     * @return the map
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    @RequestMapping(value = {"/confirmAuthUser"})
    public Map<String, Object> confirmAuthUser(@RequestBody UserDetail userDetail, HttpServletResponse response) throws IOException, MessagingException {
        HashMap body = new HashMap();
        Map<String, Object> resultMap = new HashMap();
        HashMap<String, Object> requestMap = new HashMap();

        body.put("userId", userDetail.getUserId());
        body.put("refreshToken", userDetail.getRefreshToken());
        body.put("status", userDetail.getStatus());

        List listUser = userService.getUserDetailInfo(body);
        UserDetail userDetail1 = (UserDetail)listUser.get(0);
        requestMap.put("userId",userDetail1.getUserId());
        requestMap.put("authAccessCnt",userDetail1.getAuthAccessCnt());
        userService.authAddAccessCnt(requestMap);
        resultMap.put("resultUser",listUser.size());
        resultMap.put("listResultUser",listUser);
        return resultMap;
    }


    /**
     * 계정등록
     *
     * @param userDetail the user detail
     * @param response   the response
     * @return map
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    @RequestMapping(value = {"/authUser"})
    public Map<String, Object> authUser(@RequestBody UserDetail userDetail, HttpServletResponse response) throws IOException, MessagingException {
        HashMap body = new HashMap();
        Map<String, Object> resultMap = new HashMap();

        body.put("userId", userDetail.getUserId());
        body.put("password", userDetail.getPassword());
        body.put("refreshToken", userDetail.getRefreshToken());
        body.put("password", userDetail.getPassword());
        body.put("password", userDetail.getPassword());

        LOGGER.info("userId : " + userDetail.getUserId() + " : request : " + response.toString());
        List<UserDetail> listUser = userService.getUserDetailInfo(body);

        resultMap.put("resultUser",listUser.size());
        return resultMap;
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
        List<UserDetail> listUser = userService.getUserDetailInfo(body);
        if(listUser.size() >= 1) {
            HashMap map = body;
            map.put("searchUserId", userDetail.getUserId());
            Boolean resultCreateUser = userService.resetPassword(map);
            resultMap.put("resetPassword", resultCreateUser);

        }
        resultMap.put("resultUser",listUser.size());
        return resultMap;
    }

    /**
     * 이메일 인증을 통한 CF 사용자 추가
     *
     * @param userDetail the user detail
     * @return map
     * @throws Exception the exception
     */
    @Transactional
    @RequestMapping(value = {"/authAddUser"})
    public Map<String, Object> authAddUser(@RequestBody HashMap userDetail) throws Exception {
        UserDetail updateUser = new UserDetail();
        Map<String, Object> resultMap = new HashMap();
        HashMap<String, Object> paramMap = userDetail;
        String userId = (String)userDetail.getOrDefault("userId","");
        updateUser.setUserId(userId);
        updateUser.setUserName((String)userDetail.getOrDefault("username",""));
        updateUser.setPassword((String)userDetail.getOrDefault("password",""));
        if(adminUserName.equals(updateUser.getUserId())){
            updateUser.setAdminYn("Y");
        } else {
            updateUser.setAdminYn("N");
        }
        updateUser.setStatus("1");
        int resultCreateUser = userService.updateUser(userId, updateUser); //일단 status를 1로 만들어준다.
        if(resultCreateUser > 0) {
            CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
            boolean resultUaa = userService.create(userDetail);
        }
        paramMap.put("searchUserId", paramMap.get("userId"));
        paramMap.put("refreshToken", "");
        paramMap.put("authAccessTime", new Date());
        paramMap.put("authAccessCnt", 0);
        userService.authAddUser(paramMap);
        resultMap.put("bRtn", true);
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
        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
        boolean resultCreateUser = userService.updateAuthUserPassword(adminCcfc, userDetail);
        resultMap.put("bRtn", resultCreateUser);

        return resultMap;
    }

    /**
     * 파일 업로드
     *
     * @param multipartFile the multipart file
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/uploadFile"}, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public Map<String, Object> uploadFile(@RequestParam(value="file", required=false) MultipartFile multipartFile) throws Exception {
        return userService.uploadFile(multipartFile);
    }

    /**
     * Gets users.
     *
     * @param map the map
     * @return the users
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getUser/"}, method = RequestMethod.POST )
    public Map<String, Object> getUsers(@RequestBody HashMap map) throws Exception {
        LOGGER.info("> into getUser...");
        String userId = (String)map.getOrDefault("userId","");
        UserDetail user = userService.getUser(userId);

        Map<String, Object> result = new HashMap<>();

        result.put("User", user);

        return result;
    }

    /**
     * 모든 Uaa 유저의 이름과 Guid를 목록으로 가져온다.
     *
     * @return map all user name
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getUserInfo"}, method = RequestMethod.GET)
    public Map<String, Object> getAllUserName() throws Exception {
        List<Map<String,String>> userInfo = userService.getUserInfo();
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("userInfo", userInfo);
        return resultMap;
    }

}