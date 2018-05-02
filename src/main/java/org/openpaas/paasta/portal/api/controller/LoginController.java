package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.openpaas.paasta.portal.api.service.LoginService;
import org.openpaas.paasta.portal.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 로그인 컨트롤러 - 로그인를 처리한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
@Transactional

public class LoginController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    /**
     * Login map.
     *
     * @param body the body
     * @return the map
     * @throws Exception the exception
     */
    @CrossOrigin
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST, consumes="application/json")
    public Map<String, Object> login(@RequestBody Map<String, Object> body) throws Exception {
        String id = (String)body.get("id");
        String password = (String)body.get("password");

        LOGGER.info("> into login ...");
        LOGGER.info("id: {}", id);

        Map<String, Object> result = new HashMap<>();
        OAuth2AccessToken token = loginService.login(id, password);
        long currentTime = System.currentTimeMillis();

        UserDetail user = null;

        if (!userService.isExist(id)) {
            LOGGER.info("UserDetail info of {} was not found. create {}'s Userdetail info...", id, id);
            user = new UserDetail();
            user.setUserId(id);
            user.setStatus("1");
            if (adminUserName.equals(id)){
                user.setAdminYn("Y");
            }
            userService.createUser(user);
        } else {
            user = userService.getUser(id);

            if (adminUserName.equals(id)){
                user.setAdminYn("Y");
            }
            userService.updateUser(id, user);
        }

        user = userService.getUser(id);

        List auths = new ArrayList();
        auths.add("ROLE_USER");

        //Start 테스트용(ASIS:DB조회 데이터) 임시 데이터 생성(아래 참조부분만 셋팅)
        user = new UserDetail();
        user.setUserName("yschoi");
        //End 테스트용 임시 계정데이터 생성(아래 참조부분만 셋팅)

        if ("Y".equals(user.getAdminYn())) auths.add("ROLE_ADMIN");

        result.put("token", token.getValue());
        result.put("refresh_token", token.getRefreshToken().getValue());
        result.put("expireDate", token.getExpiration().getTime()-10000);
        result.put("expireIn", token.getExpiresIn());
        result.put("expiredAt-cal", (token.getExpiration().getTime() - currentTime) / 1000);
        result.put("id", id);
        result.put("password", password);
        result.put("name", user.getUserName());
        result.put("imgPath", user.getImgPath());
        result.put("auth", auths);
        return result;
    }
    
    @RequestMapping(value = {"/login2"}, method = RequestMethod.POST, consumes="application/json")
    public Map<String, Object> login2(@RequestBody Map<String, Object> body) throws Exception {
        String id = (String)body.get("id");
        String password = (String)body.get("password");

        LOGGER.info("> into login ...");
        LOGGER.info("id: {}", id);

        Map<String, Object> result = new HashMap<>();
        // uaa client login 
        OAuth2AccessToken token = loginService.login2(id, password);
        long currentTime = System.currentTimeMillis();

        UserDetail user = null;
        //TODO : 나중에 꼭 수정해야함....Commonapi에서 정보가져오도록
        user = new UserDetail();
        user.setUserId(id);
        user.setStatus("1");
        user.setAdminYn("Y");


        if (!userService.isExist(id)) {
            LOGGER.info("UserDetail info of {} was not found. create {}'s Userdetail info...", id, id);
            user = new UserDetail();
            user.setUserId(id);
            user.setStatus("1");
            if (adminUserName.equals(id)) {
                user.setAdminYn("Y");
            }
            userService.createUser(user);
        } else {
            user = userService.getUser(id);

            if (adminUserName.equals(id)) {
                user.setAdminYn("Y");
            }
            userService.updateUser(id,user);
        }


        List auths = new ArrayList();
        //TODO: common 서비스 에서 가져오도록 수정되어야함
        user = userService.getUser(id);
        if ("Y".equals(user.getAdminYn())) auths.add("ROLE_ADMIN");
        else auths.add("ROLE_USER");

        result.put("token", token.getValue());
        result.put("refresh_token", token.getRefreshToken().getValue());
        result.put("expireDate", token.getExpiration().getTime()-10000);
        result.put("expireIn", token.getExpiresIn());
        result.put("expiredAt-cal", (token.getExpiration().getTime() - currentTime) / 1000);
        result.put("id", id);
        result.put("password", password);
        result.put("name", user.getUserName());
        result.put("imgPath", user.getImgPath());
        result.put("auth", auths);
        return result;
    }
    
    @PostMapping("/token/refresh")
    public Map<String, Object> refresh(@RequestBody Map<String, Object> body) throws Exception {
        String tokenStr = (String) body.get( "token" );
        String refreshTokenStr = (String) body.get( "refresh_token" );
        
        Map<String, Object> result = new HashMap<>();
        //OAuth2AccessToken token = loginService.refresh( tokenStr, refreshTokenStr );
        OAuth2AccessToken token = loginService.refresh( "", refreshTokenStr );
        long currentTime = System.currentTimeMillis();
        
        result.put("token", token.getValue());
        result.put("refresh_token", token.getRefreshToken().getValue());
        result.put("expireDate", token.getExpiration().getTime()-10000);
        result.put("expireIn", token.getExpiresIn());
        result.put("expiredAt-cal", (token.getExpiration().getTime() - currentTime) / 1000);
        result.put( "real_token", token );
        
        LOGGER.info( "token realization : {}", token );
        
        return result;
    }
    
    @PostMapping("/token/refresh2")
    public Map<String, Object> refresh2(@RequestBody Map<String, Object> body) throws Exception {
        String tokenStr = (String) body.get( "token" );
        String refreshTokenStr = (String) body.get( "refresh_token" );
        
        Map<String, Object> result = new HashMap<>();
        //OAuth2AccessToken token = loginService.refresh( tokenStr, refreshTokenStr );
        OAuth2AccessToken token = loginService.refresh2( tokenStr, refreshTokenStr );
        long currentTime = System.currentTimeMillis();
        
        result.put("token", token.getValue());
        result.put("refresh_token", token.getRefreshToken().getValue());
        result.put("expireDate", token.getExpiration().getTime()-10000);
        result.put("expireIn", token.getExpiresIn());
        result.put("expiredAt-cal", (token.getExpiration().getTime() - currentTime) / 1000);
        result.put( "real_token", token );
        
        LOGGER.info( "token realization : {}", token );
        
        return result;
    }
    

    /**
     * Request email authentication map.
     *
     * @param userDetail the user detail
     * @param response   the response
     * @return the map
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    @RequestMapping(value = {"/requestEmailAuthentication"}, method = RequestMethod.POST)
    public Map<String, Object> requestEmailAuthentication(@RequestBody UserDetail userDetail, HttpServletResponse response) throws IOException, MessagingException {
        HashMap body = new HashMap();
        Map<String, Object> resultMap = new HashMap();

        body.put("userId", userDetail.getUserId());

        LOGGER.info("userId : " + userDetail.getUserId() + " : request : " + response.toString());

        List<UserDetail> listUser = userService.getUserDetailInfo(body);
        resultMap.put("resultUserDetail", listUser);

        if (listUser.size() > 0) {
            UserDetail userDetail1 = listUser.get(0);
            if (!"0".equals(userDetail1.getStatus())) {
                resultMap.put("bRtn", false);
                resultMap.put("error", "계정이 이미 존재합니다.");
                return resultMap;
            }
        }
        boolean resultSendEmail = userService.createRequestUser(body);
        resultMap.put("bRtn", resultSendEmail);

        return resultMap;
    }

}
