package org.openpaas.paasta.portal.api.controller;

/**
 * Created by swmoon on 2018-03-15.
 */

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.openpaas.paasta.portal.api.service.LoginServiceV1;
import org.openpaas.paasta.portal.api.service.UserServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class LoginControllerV1 extends Common {


    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 1                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginControllerV1.class);

    @Autowired
    private LoginServiceV1 loginServiceV1;

    @Autowired
    private UserServiceV2 userServiceV2;

    /**
     * Login map.
     *
     * @param body the body
     * @return the map
     * @throws Exception the exception
     */
    @CrossOrigin
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> login(@RequestBody Map<String, Object> body) throws Exception {
        String id = (String) body.get("id");
        String password = (String) body.get("password");

        LOGGER.info("> into login ...");
        LOGGER.info("id: {}", id);
        LOGGER.info("password: {}", password);

        Map<String, Object> result = new HashMap<>();
        OAuth2AccessToken token = loginServiceV1.login(id, password);
        long currentTime = System.currentTimeMillis();

        UserDetail user = null;

        List auths = new ArrayList();
        auths.add("ROLE_USER");

        //Start 테스트용(ASIS:DB조회 데이터) 임시 데이터 생성(아래 참조부분만 셋팅)
        user = new UserDetail();

        if ("Y".equals(user.getAdminYn())) auths.add("ROLE_ADMIN");
        //USER_GUID
        result.put("user_id", token.getAdditionalInformation().get("user_id"));
        result.put("scope", token.getScope());
        result.put("token_type", token.getTokenType());
        result.put("token", token.getValue());
        result.put("refresh_token_type", token.getTokenType());
        result.put("refresh_token", token.getRefreshToken().getValue());
        result.put("expireDate", token.getExpiration().getTime() - 10000);
        result.put("expire_in", token.getExpiresIn());
        result.put("expiredAt-cal", (token.getExpiration().getTime() - currentTime) / 1000);
        //USER_ID -- UAA 통일
        result.put("user_name", id);
        result.put("id", id);
        result.put("password", password);
        result.put("auth", auths);
        LOGGER.info("Token :: " + token.getValue());

        return result;
    }


    /**
     * token Refresh.
     *
     * @param body the body
     * @return the map
     * @throws Exception the exception
     */
    @PostMapping("/token/refresh")
    public Map<String, Object> refresh(@RequestBody Map<String, Object> body) throws Exception {
        String tokenStr = (String) body.get("token");
        String refreshTokenStr = (String) body.get("refresh_token");

        Map<String, Object> result = new HashMap<>();
        OAuth2AccessToken token = loginServiceV1.refresh("", refreshTokenStr);
        long currentTime = System.currentTimeMillis();

        result.put("token", token.getValue());
        result.put("access_token", token.getValue());
        result.put("token_type", token.getTokenType());
        result.put("refresh_token", token.getRefreshToken().getValue());
        result.put("expireDate", token.getExpiration().getTime() - 10000);
        result.put("expireIn", token.getExpiresIn());
        result.put("expires_in", token.getExpiresIn());
        result.put("scope", token.getScope());
        result.put("expiredAt-cal", (token.getExpiration().getTime() - currentTime) / 1000);
        result.put("real_token", token);


        return result;
    }


}
