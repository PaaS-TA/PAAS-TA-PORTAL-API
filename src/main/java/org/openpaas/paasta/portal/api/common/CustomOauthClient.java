package org.openpaas.paasta.portal.api.common;


import org.apache.commons.codec.binary.Base64;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.util.JsonUtil;
import org.cloudfoundry.identity.uaa.scim.ScimUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Client that can handle authentication against a UAA instance
 *
 * @author Dave Syer
 * @author Thomas Risberg
 */

/**
 * Created by mg on 2016-06-21.
 */
//NOSONAR
public class CustomOauthClient {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    private final URL authorizationUrl;

    private final RestTemplate restTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private OAuth2AccessToken token;
    public CustomOauthClient(URL authorizationUrl, RestTemplate restTemplate) {
        this.authorizationUrl = authorizationUrl;
        this.restTemplate = restTemplate;
    }

    public void init(CloudCredentials credentials) {
        if (credentials != null) {

            if (credentials.getToken() != null) {
                this.token = credentials.getToken();
            } else {
                this.token = createToken(credentials.getEmail(), credentials.getPassword(),
                        credentials.getClientId(), credentials.getClientSecret());
            }
        }

        restTemplate.setErrorHandler(new CustomCloudControllerResponseErrorHandler());
    }
    public void init(CloudCredentials credentials, String url) {
        if (credentials != null) {
            //    this.credentials = credentials;

            if (credentials.getToken() != null) {
                this.token = credentials.getToken();
            } else {
                this.token = createToken(credentials.getEmail(), credentials.getPassword(),
                        credentials.getClientId(), credentials.getClientSecret(), url);
            }
        }

        restTemplate.setErrorHandler(new CustomCloudControllerResponseErrorHandler());
    }
    public OAuth2AccessToken getToken() {
        if (token == null) {
            return null;
        }
        return token;
    }

    public String getAuthorizationHeader() {
        OAuth2AccessToken accessToken = getToken();
        if (accessToken != null) {
            return accessToken.getTokenType() + " " + accessToken.getValue();
        }
        return null;
    }

    private OAuth2AccessToken createToken(String username, String password, String clientId, String clientSecret) {
        OAuth2ProtectedResourceDetails resource = getResourceDetails(username, password, clientId, clientSecret);
        AccessTokenRequest request = createAccessTokenRequest(username, password);

        ResourceOwnerPasswordAccessTokenProvider provider = createResourceOwnerPasswordAccessTokenProvider();
        try {
            return provider.obtainAccessToken(resource, request);
        } catch (OAuth2AccessDeniedException oauthEx) {
            HttpStatus status = HttpStatus.valueOf(oauthEx.getHttpErrorCode());
            CloudFoundryException cfEx = new CloudFoundryException(status, oauthEx.getMessage());
            cfEx.setDescription(oauthEx.getSummary());
            throw cfEx;
        }
    }
    private OAuth2AccessToken createToken(String username, String password, String clientId, String clientSecret,String url) {
        OAuth2ProtectedResourceDetails resource = getResourceDetails(username, password, clientId, clientSecret, url);
        AccessTokenRequest request = createAccessTokenRequest(username, password);

        ResourceOwnerPasswordAccessTokenProvider provider = createResourceOwnerPasswordAccessTokenProvider();
        try {
            return provider.obtainAccessToken(resource, request);
        } catch (OAuth2AccessDeniedException oauthEx) {
            HttpStatus status = HttpStatus.valueOf(oauthEx.getHttpErrorCode());
            CloudFoundryException cfEx = new CloudFoundryException(status, oauthEx.getMessage());
            cfEx.setDescription(oauthEx.getSummary());
            throw cfEx;
        }
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void changePassword(String oldPassword, String newPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, token.getTokenType() + " " + token.getValue());
        HttpEntity info = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(authorizationUrl + "/userinfo", HttpMethod.GET, info, String.class);
        Map<String, Object> responseMap = JsonUtil.convertJsonToMap(response.getBody());
        String userId = (String) responseMap.get("user_id");
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("schemas", new String[]{"urn:scim:schemas:core:1.0"});
        body.put("password", newPassword);
        body.put("oldPassword", oldPassword);
        HttpEntity<Map> httpEntity = new HttpEntity<Map>(body, headers);
        restTemplate.put(authorizationUrl + "/Users/{id}/password", httpEntity, userId);
    }

    /**
     * CloudFoundry 사용자를 삭제한다.
     * @param userId
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void deleteUser(String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("If-Match", "*");
        headers.add(AUTHORIZATION_HEADER_KEY, token.getTokenType() + " " + token.getValue());
        HttpEntity info = new HttpEntity(headers);
        restTemplate.exchange(authorizationUrl + "/Users/"+userId, HttpMethod.DELETE, info, String.class);
    }

    protected ResourceOwnerPasswordAccessTokenProvider createResourceOwnerPasswordAccessTokenProvider() {
        ResourceOwnerPasswordAccessTokenProvider resourceOwnerPasswordAccessTokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
        resourceOwnerPasswordAccessTokenProvider.setRequestFactory(restTemplate.getRequestFactory()); //copy the http proxy along
        return resourceOwnerPasswordAccessTokenProvider;
    }

    //NOSONAR
    private AccessTokenRequest createAccessTokenRequest(String username, String password) {
        LOGGER.info(this.getClass().getName()+" : "+"username"+username+", password" +password);
        AccessTokenRequest request = new DefaultAccessTokenRequest();
        return request;
    }

    private OAuth2ProtectedResourceDetails getResourceDetails(String username, String password, String clientId, String clientSecret) {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setUsername(username);
        resource.setPassword(password);

        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setId(clientId);
        resource.setClientAuthenticationScheme(AuthenticationScheme.header);
        resource.setAccessTokenUri(authorizationUrl + "/oauth/token");

        return resource;
    }

    private OAuth2ProtectedResourceDetails getResourceDetails(String username, String password, String clientId, String clientSecret, String url) {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setUsername(username);
        resource.setPassword(password);

        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setId(clientId);
        resource.setClientAuthenticationScheme(AuthenticationScheme.header);
        resource.setAccessTokenUri(url + "/oauth/token");

        return resource;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map resetPassword(String userId, String newPassword, String clientId, String clientSecret, String uaaTarget) throws MalformedURLException {

        Map<String, Object> requestMap = password_resets(userId,clientId,clientSecret,uaaTarget);
        String code = (String) requestMap.get("code");
        String token_type = (String) requestMap.get("token_type");
        String accesstoken = (String) requestMap.get("access_token");
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, token_type+" "+accesstoken );
        headers.add("Content-Type", "application/json" );
        Map<String, Object> body3 = new HashMap<String, Object>();
        body3.put("code", code);
        body3.put("new_password",newPassword);
        HttpEntity<Map> httpEntity = new HttpEntity<Map>(body3, headers);
        ResponseEntity<String>  response = restTemplate.exchange(uaaTarget + "/password_change", HttpMethod.POST, httpEntity, String.class);
        Map<String, Object> returnMap = JsonUtil.convertJsonToMap(response.getBody());
        return returnMap;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map password_resets(String userId, String clientId, String clientSecret, String uaaTarget) throws MalformedURLException {
        Map responseMap1 = getUaaAccessToken(clientId, clientSecret, uaaTarget);
        String token_type = (String) responseMap1.get("token_type");
        String accesstoken = (String) responseMap1.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, token_type+" "+accesstoken );
        HttpEntity<Map> info2 = new HttpEntity(userId, headers);
        System.out.println("username:"+userId);
        ResponseEntity<String> responseEntity2 = restTemplate.exchange(uaaTarget+ "/password_resets", HttpMethod.POST, info2, String.class);
        Map<String, Object> returnMap = JsonUtil.convertJsonToMap(responseEntity2.getBody());
        String code = (String) returnMap.get("code");
        returnMap.put("token_type", token_type);
        returnMap.put("access_token", accesstoken);
        LOGGER.debug("code:"+code);
        return returnMap;
    }

    /**
     * accessToken을 받아올수 있다.
     * @param clientId      uaa clientId
     * @param clientSecret uaa clientSecret
     * @param uaaTarget     uaa TargetUrl
     * @return Map
     *          String token_type = (String) responseMap.getOrDefault("token_type","");
     *          String accesstoken = (String) responseMap.getOrDefault("access_token","");
     * @throws MalformedURLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map getUaaAccessToken(String clientId, String clientSecret, String uaaTarget) throws MalformedURLException {

        HttpHeaders headers = new HttpHeaders();
        String str = clientId+":"+clientSecret;
        String encodeStr = new String(Base64.encodeBase64(str.getBytes()));

        headers.add(AUTHORIZATION_HEADER_KEY, " Basic "+encodeStr);
        HttpEntity info1 = new HttpEntity(headers);

        ResponseEntity responseEntity1 =  restTemplate.exchange(authorizationUrl +"/oauth/token?grant_type=client_credentials&response_type=token", HttpMethod.POST, info1, String.class);
        Map<String, Object> responseMap = JsonUtil.convertJsonToMap(responseEntity1.getBody().toString());

        return responseMap;
    }

    /**
     * 클라이언트 목록 조회
     * @param clientId      uaa clientId
     * @param clientSecret uaa clientSecret
     * @param uaaTarget     uaa TargetUrl
     * @return ResponseEntity
     * @throws MalformedURLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResponseEntity<String> getClientList(String clientId, String clientSecret, String uaaTarget) throws MalformedURLException {

        Map responseMap1 = getUaaAccessToken(clientId, clientSecret, uaaTarget);
        String token_type = (String) responseMap1.get("token_type");
        String accesstoken = (String) responseMap1.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, token_type+" "+accesstoken );
        HttpEntity<Map> header = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uaaTarget+ "/oauth/clients", HttpMethod.GET, header, String.class);
        return responseEntity;

    }

    /**
     * 클라이언트 정보 조회
     * @param clientId      uaa clientId
     * @param clientSecret uaa clientSecret
     * @param uaaTarget     uaa TargetUrl
     * @param param         the param
     * @return ResponseEntity
     * @throws MalformedURLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResponseEntity<String> getClient(String clientId, String clientSecret, String uaaTarget, Map<String, Object> param) throws MalformedURLException {

        Map responseMap1 = getUaaAccessToken(clientId, clientSecret, uaaTarget);
        String token_type = (String) responseMap1.get("token_type");
        String accesstoken = (String) responseMap1.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, token_type+" "+accesstoken );
        HttpEntity<Map> header = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uaaTarget+ "/oauth/clients/"+param.get("client_id"), HttpMethod.GET, header, String.class);
        return responseEntity;

    }

    /**
     * 클라이언트 등록
     * @param clientId      uaa clientId
     * @param clientSecret uaa clientSecret
     * @param uaaTarget     uaa TargetUrl
     * @param param         the param
     * @return ResponseEntity
     * @throws MalformedURLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResponseEntity<String> registerClient(String clientId, String clientSecret, String uaaTarget, Map<String, Object> param) throws MalformedURLException {

        Map responseMap1 = getUaaAccessToken(clientId, clientSecret, uaaTarget);

        String token_type = (String) responseMap1.get("token_type");
        String accesstoken = (String) responseMap1.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, token_type+" "+accesstoken );

        HttpEntity<Object> reqEntity = new HttpEntity<>(param, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uaaTarget+ "/oauth/clients", HttpMethod.POST, reqEntity, String.class);
        return responseEntity;

    }

    /**
     * 클라이언트 수정
     * @param clientId      uaa clientId
     * @param clientSecret uaa clientSecret
     * @param uaaTarget     uaa TargetUrl
     * @param param         the param
     * @return ResponseEntity
     * @throws MalformedURLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResponseEntity<String> updateClient(String clientId, String clientSecret, String uaaTarget, Map<String, Object> param) throws MalformedURLException {

        Map responseMap1 = getUaaAccessToken(clientId, clientSecret, uaaTarget);

        String token_type = (String) responseMap1.get("token_type");
        String accesstoken = (String) responseMap1.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, token_type+" "+accesstoken );

        HttpEntity<Object> reqEntity = new HttpEntity<>(param, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uaaTarget+ "/oauth/clients/"+param.get("client_id"), HttpMethod.PUT, reqEntity, String.class);
        return responseEntity;

    }

    /**
     * 클라이언트 삭제
     * @param clientId      uaa clientId
     * @param clientSecret uaa clientSecret
     * @param uaaTarget     uaa TargetUrl
     * @param param         the param
     * @return ResponseEntity
     * @throws MalformedURLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public ResponseEntity<String> deleteClient(String clientId, String clientSecret, String uaaTarget, Map<String, Object> param) throws MalformedURLException {

        Map responseMap1 = getUaaAccessToken(clientId, clientSecret, uaaTarget);

        String token_type = (String) responseMap1.get("token_type");
        String accesstoken = (String) responseMap1.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, token_type+" "+accesstoken );

        HttpEntity<Object> reqEntity = new HttpEntity<>(param, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uaaTarget+ "/oauth/clients/"+param.get("client_id"), HttpMethod.DELETE, reqEntity, String.class);
        return responseEntity;

    }


    public Map register(ScimUser scimUser) throws MalformedURLException {
        Map<String, Object> responseMap= new HashMap<String, Object>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("If-Match", "*");
        headers.add(AUTHORIZATION_HEADER_KEY, token.getTokenType() + " " + token.getValue());
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("username", scimUser.getUserName());
        body.put("name", scimUser.getName());
        body.put("emails", scimUser.getEmails());
        body.put("verified", true);
        body.put("password", scimUser.getPassword());

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(authorizationUrl + "/Users", HttpMethod.POST, httpEntity, String.class);
        responseMap = JsonUtil.convertJsonToMap(response.getBody());
        return responseMap;
    }



}

