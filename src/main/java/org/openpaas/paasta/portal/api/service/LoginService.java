package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * 로그인 서비스 - 로그인를 처리한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Service
public class LoginService extends Common {

    /**
     * id, password 방식으로 CloudFoundry 인증 토큰을 OAuth2AccessToken 형태로 반환한다.
     *
     * @param id       the id
     * @param password the password
     * @return OAuth2AccessToken o auth 2 access token
     * @throws MalformedURLException the malformed url exception
     * @throws URISyntaxException    the uri syntax exception
     */
    public OAuth2AccessToken login(String id, String password) throws MalformedURLException, URISyntaxException {
        return new CloudFoundryClient(new CloudCredentials(id, password), getTargetURL(apiTarget), true).login();
    }
}
