package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.tokens.AbstractToken;
import org.cloudfoundry.uaa.tokens.GetTokenByClientCredentialsRequest;
import org.cloudfoundry.uaa.tokens.GetTokenByClientCredentialsResponse;
import org.cloudfoundry.uaa.tokens.RefreshTokenRequest;
import org.cloudfoundry.uaa.tokens.RefreshTokenResponse;
import org.cloudfoundry.uaa.tokens.TokenFormat;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson2Deserializer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 로그인 서비스 - 로그인를 처리한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Service
public class LoginService extends Common {
    
    ConcurrentHashMap<OAuth2AccessToken, ReactorCloudFoundryClient> tokenMap = new ConcurrentHashMap<>();
    
    TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>(){};
    
    ObjectMapper objectMapper = new ObjectMapper();

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
    
    public OAuth2AccessToken refresh(String token, String refreshToken) throws MalformedURLException, URISyntaxException {
        return new CloudFoundryClient(new CloudCredentials(getOAuth2Token(token, refreshToken), true), getTargetURL(apiTarget), true).login();
    }
    
    public OAuth2AccessToken login2(String id, String password) throws MalformedURLException, URISyntaxException {
        ReactorUaaClient uaaClient = uaaClient( connectionContext(), tokenProvider( id, password ) );
        GetTokenByClientCredentialsResponse tokenResponse = uaaClient
            .tokens()
            .getByClientCredentials( 
                GetTokenByClientCredentialsRequest.builder().clientId( id ).clientSecret( password ).build() )
            .onErrorReturn(
                GetTokenByClientCredentialsResponse.builder().scopes( "get_token_fail" ).expiresInSeconds( 0 ).build() )
            .block();
        
        OAuth2AccessToken accessToken = getOAuth2TokenFromTokenResponse(tokenResponse);
        return accessToken;
    }
    
    class Wrapper<T> {
        private ArrayList<T> objs = new ArrayList<>();
        
        public ArrayList<T> get() {
            return this.objs;
        }
        
        public void set(T obj) {
            this.objs.add( obj );
        }
    }
    
    public OAuth2AccessToken refresh2(String token, String refreshToken) {
        Wrapper<RefreshTokenResponse> tokenRefresh = new Wrapper<>();
        Mono<RefreshTokenResponse> mono = 
            uaaClient( connectionContext(), tokenProvider( token ) ).tokens()
            .refresh( RefreshTokenRequest.builder()
                .refreshToken( refreshToken ).tokenFormat( TokenFormat.OPAQUE ).clientId( "" ).clientSecret( "" ).build() )
            .onErrorReturn( RefreshTokenResponse.builder().refreshToken( "" ).accessToken( "" ).tokenId( "" ).tokenType( "" ).scopes( "refresh_fail" ).expiresInSeconds( 0 ).build() );
        Consumer<RefreshTokenResponse> consumer = new Consumer<RefreshTokenResponse>() {
            
            @Override
            public void accept( RefreshTokenResponse response ) {
                tokenRefresh.set( response );
            }
        };
        mono.subscribe( consumer ).dispose();
        
        OAuth2AccessToken accessToken = getOAuth2TokenFromTokenResponse(tokenRefresh.get().get( 0 ));
        return accessToken;
    }
    
    private final OAuth2AccessToken getOAuth2Token(String token) {
        return new DefaultOAuth2AccessToken( token );
    }
    
    private final OAuth2AccessToken getOAuth2Token(String token, String refreshToken) {
        DefaultOAuth2AccessToken oAuthToken = new DefaultOAuth2AccessToken( token );
        oAuthToken.setRefreshToken( new DefaultOAuth2RefreshToken( refreshToken ) );
        
        return oAuthToken;
    }
    
    private final OAuth2AccessToken getOAuth2TokenFromTokenResponse(AbstractToken tokenResponse) {
        final Map<String, String> tokenMap = objectMapper.convertValue( tokenResponse, typeRef );
        return DefaultOAuth2AccessToken.valueOf( tokenMap );
    }
    
    /*
    private class ClientInfo {
        public ClientInfo(final Reactor) {
            // TODO Auto-generated constructor stub
        }
    }
    */
}
