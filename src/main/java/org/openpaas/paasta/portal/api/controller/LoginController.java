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
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> login(@RequestBody Map<String, Object> body) throws Exception {
        String id = (String) body.get("id");
        String password = (String) body.get("password");

        LOGGER.info("> into login ...");
        LOGGER.info("id: {}", id);

        Map<String, Object> result = new HashMap<>();
        OAuth2AccessToken token = loginService.login(id, password);
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
        //result.put("token", token.getValue());
        LOGGER.info(token.getRefreshToken().getValue());
        result.put("token", token.getRefreshToken().getValue());
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
        return result;
    }

    @PostMapping("/token/refresh")
    public Map<String, Object> refresh(@RequestBody Map<String, Object> body) throws Exception {
        String tokenStr = (String) body.get("token");
        String refreshTokenStr = (String) body.get("refresh_token");

        Map<String, Object> result = new HashMap<>();
        //OAuth2AccessToken token = loginService.refresh( tokenStr, refreshTokenStr );
        OAuth2AccessToken token = loginService.refresh("", refreshTokenStr);
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

        LOGGER.info("token realization : {}", token.getExpiresIn());

        return result;
    }


}
