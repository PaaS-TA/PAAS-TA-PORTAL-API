package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.identity.uaa.api.UaaConnectionFactory;
import org.cloudfoundry.identity.uaa.api.client.UaaClientOperations;
import org.cloudfoundry.identity.uaa.api.common.UaaConnection;
import org.cloudfoundry.identity.uaa.api.group.UaaGroupOperations;
import org.cloudfoundry.identity.uaa.api.user.UaaUserOperations;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.ClientCredentialsGrantTokenProvider;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.UaaClient;
import org.cloudfoundry.uaa.tokens.GetTokenByClientCredentialsRequest;
import org.cloudfoundry.uaa.tokens.GetTokenByClientCredentialsResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.service.LoginService;
import org.openpaas.paasta.portal.api.util.SSLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(Common.class);

    @Value("${cloudfoundry.cc.api.url}")
    public String apiTarget;

    @Value("${cloudfoundry.cc.api.uaaUrl}")
    public String uaaTarget;

    @Value("${cloudfoundry.cc.api.sslSkipValidation}")
    public boolean cfskipSSLValidation;

    @Value("${cloudfoundry.user.admin.username}")
    public String adminUserName;

    @Value("${cloudfoundry.user.admin.password}")
    public String adminPassword;

    public static final String AUTHORIZATION_HEADER_KEY = "cf-Authorization";

    @Value("${cloudfoundry.user.uaaClient.clientId}")
    public String uaaClientId;

    @Value("${cloudfoundry.user.uaaClient.clientSecret}")
    public String uaaClientSecret;

    @Value("${cloudfoundry.user.uaaClient.adminClientId}")
    public String uaaAdminClientId;

    @Value("${cloudfoundry.user.uaaClient.adminClientSecret}")
    public String uaaAdminClientSecret;

    @Value("${cloudfoundry.user.uaaClient.skipSSLValidation}")
    public boolean skipSSLValidation;

    @Value("${monitoring.api.url}")
    public String monitoringApiTarget;

    @Autowired
    private LoginService loginService;

    @Autowired
    DefaultConnectionContext connectionContext;

    @Autowired
    PasswordGrantTokenProvider tokenProvider;


    public ObjectMapper objectMapper = new ObjectMapper();

    private static ReactorCloudFoundryClient adminReactorCloudFoundryClient;

    private static long adminTime;

    /**
     * 관리자 토큰을 가져온다.
     *
     * @return String token
     * @throws Exception the exception
     */
    public String getToken() {
        try {
            return loginService.login(adminUserName, adminPassword).getValue();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * CF Target URL을 가져온다.
     * @param target cf target
     * @return URL target
     * @throws MalformedURLException, URISyntaxException the exception
     */
    public URL getTargetURL(String target) throws MalformedURLException, URISyntaxException {
        return getTargetURI(target).toURL();
    }

    /**
     * CF Target URL을 가져온다.
     * @param target cf target
     * @return URL target
     * @throws URISyntaxException
     */
    private URI getTargetURI(String target) throws URISyntaxException {
        return new URI(target);
    }

    /**
     * CF Client Object를 가져온다.
     *
     * @param token cf token
     * @return CloudFoundryClinet
     * @throws MalformedURLException, URISyntaxException
     */
    public CloudFoundryClient getCloudFoundryClient(String token) throws MalformedURLException, URISyntaxException {

        return new CloudFoundryClient(getCloudCredentials(token), getTargetURL(apiTarget), true);
    }


    /**
     * get CloudCredentials Object from token String
     *
     * @param token
     * @return CloudCredentials
     */
    public CloudCredentials getCloudCredentials(String token) {
        return new CloudCredentials(getOAuth2AccessToken(token), false);
    }

    /**
     * get CloudCredentials Object from id, password
     *
     * @param id
     * @param password
     * @return CloudCredentials
     */
    public CloudCredentials getCloudCredentials(String id, String password) {

        LOGGER.info("============getCloudCredentials==============");
        CloudCredentials test = new CloudCredentials(id, password);
        LOGGER.info("getToken       :" + test.getToken());
        LOGGER.info("getClientId    :" + test.getClientId());
        LOGGER.info("getClientSecret:" + test.getClientSecret());
        LOGGER.info("getEmail       :" + test.getEmail());
        LOGGER.info("getPassword    :" + test.getPassword());
        LOGGER.info("getProxyUser   :" + test.getProxyUser());
        return test;

    }

    /**
     * get DefailtOAuth2AccessToken Object from token String
     *
     * @param token
     * @return
     */
    private DefaultOAuth2AccessToken getOAuth2AccessToken(String token) {
        return new DefaultOAuth2AccessToken(token);
    }



    /**
     * 권한그룹 조회 등록시 사용
     *
     * @param uaaClientId
     * @return UaaGroupOperations
     * @throws Exception
     */
    public UaaGroupOperations getUaaGroupOperations(String uaaClientId) throws Exception {
        UaaConnection connection = getUaaConnection(uaaClientId);
        return connection.groupOperations();
    }

    /**
     * UAA 커넥션 생성
     *
     * @param uaaClientId
     * @return UaaConnection
     * @throws Exception
     */
    private UaaConnection getUaaConnection(String uaaClientId) throws Exception {
        ResourceOwnerPasswordResourceDetails credentials = getCredentials(uaaClientId);
        URL uaaHost = new URL(uaaTarget);

        //ssl 유효성 체크 비활성
        if (skipSSLValidation) {
            SSLUtils.turnOffSslChecking();
        }

        UaaConnection connection = UaaConnectionFactory.getConnection(uaaHost, credentials);
        return connection;
    }

    /**
     * credentials 세팅
     *
     * @param uaaClientId
     * @return ResourceOwnerPasswordResourceDetails
     */
    private ResourceOwnerPasswordResourceDetails getCredentials(String uaaClientId) {
        ResourceOwnerPasswordResourceDetails credentials = new ResourceOwnerPasswordResourceDetails();
        credentials.setAccessTokenUri(uaaTarget + "/oauth/token?grant_type=client_credentials&response_type=token");
        credentials.setClientAuthenticationScheme(AuthenticationScheme.header);

        credentials.setClientId(uaaClientId);

        if (uaaClientId.equals(uaaAdminClientId)) {
            credentials.setClientSecret(uaaAdminClientSecret);
        }
        return credentials;
    }


    /**
     * 요청 파라미터들의 빈값 또는 null값 확인을 하나의 메소드로 처리할 수 있도록 생성한 메소드
     * 요청 파라미터 중 빈값 또는 null값인 파라미터가 있는 경우, false를 리턴한다.
     *
     * @param params
     * @return boolean
     */
    public boolean stringNullCheck(String... params) {
        return Arrays.stream(params).allMatch(param -> null != param && !param.equals(""));
    }

    /**
     * 요청 문자열 파라미터 중, 공백을 포함하고 있는 파라미터가 있을 경우 false를 리턴
     *
     * @param params
     * @return boolean
     */
    public boolean stringContainsSpaceCheck(String... params) {
        return Arrays.stream(params).allMatch(param -> !param.contains(" "));
    }

    /**
     * Gets property value.
     *
     * @param key the key
     * @return property value
     * @throws Exception the exception
     */
    public static String getPropertyValue(String key) throws Exception {
        return getPropertyValue(key, "/config.properties");
    }


    /**
     * Gets process property value.
     *
     * @param key            the key
     * @param configFileName the config file name
     * @return property value
     * @throws Exception the exception
     */
    private static String getProcPropertyValue(String key, String configFileName) throws Exception {
        if (Constants.NONE_VALUE.equals(configFileName)) return "";

        Properties prop = new Properties();

        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(configFileName)) {
            prop.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prop.getProperty(key);
    }

    /**
     * Gets property value.
     *
     * @param key            the key
     * @param configFileName the config file name
     * @return property value
     * @throws Exception the exception
     */
    public static String getPropertyValue(String key, String configFileName) throws Exception {
        return getProcPropertyValue(key, Optional.ofNullable(configFileName).orElse(Constants.NONE_VALUE));
    }


    public static String convertApiUrl(String url) {
        return url.replace("https://", "").replace("http://", "");
    }


    /**
     * DefaultCloudFoundryOperations을 생성하여, 반환한다.
     *
     * @param connectionContext
     * @param tokenProvider
     * @return DefaultCloudFoundryOperations
     */
    public static DefaultCloudFoundryOperations cloudFoundryOperations(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return cloudFoundryOperations(cloudFoundryClient(connectionContext, tokenProvider), dopplerClient(connectionContext, tokenProvider), uaaClient(connectionContext, tokenProvider));
    }

    /**
     * DefaultCloudFoundryOperations을 생성하여, 반환한다.
     *
     * @param cloudFoundryClient
     * @param dopplerClient
     * @param uaaClient
     * @return DefaultCloudFoundryOperations
     */
    public static DefaultCloudFoundryOperations cloudFoundryOperations(org.cloudfoundry.client.CloudFoundryClient cloudFoundryClient, DopplerClient dopplerClient, UaaClient uaaClient) {
        return DefaultCloudFoundryOperations.builder().cloudFoundryClient(cloudFoundryClient).dopplerClient(dopplerClient).uaaClient(uaaClient).build();
    }

    /**
     * DefaultCloudFoundryOperations을 생성하여, 반환한다.
     *
     * @param connectionContext
     * @param tokenProvider
     * @param org
     * @param space
     * @return DefaultCloudFoundryOperations
     */
    public static DefaultCloudFoundryOperations cloudFoundryOperations(ConnectionContext connectionContext, TokenProvider tokenProvider, String org, String space) {
        return cloudFoundryOperations(cloudFoundryClient(connectionContext, tokenProvider), dopplerClient(connectionContext, tokenProvider), uaaClient(connectionContext, tokenProvider), org, space);
    }

    /**
     * DefaultCloudFoundryOperations을 생성하여, 반환한다.
     *
     * @param cloudFoundryClient
     * @param dopplerClient
     * @param uaaClient
     * @param org
     * @param space
     * @return DefaultCloudFoundryOperations
     */
    public static DefaultCloudFoundryOperations cloudFoundryOperations(org.cloudfoundry.client.CloudFoundryClient cloudFoundryClient, DopplerClient dopplerClient, UaaClient uaaClient, String org, String space) {
        return DefaultCloudFoundryOperations.builder().cloudFoundryClient(cloudFoundryClient).dopplerClient(dopplerClient).uaaClient(uaaClient).organization(org).space(space).build();
    }

    /**
     * ReactorCloudFoundryClient 생성하여, 반환한다.
     *
     * @param connectionContext
     * @param tokenProvider
     * @return DefaultCloudFoundryOperations
     */
    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider).build();
    }

    public ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext){
        try {
            if (adminReactorCloudFoundryClient == null) {
                return adminReactorCloudFoundryClient = ReactorCloudFoundryClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider(getToken())).build();
            } else {
                if(System.currentTimeMillis() - adminTime >= 180000){
                    LOGGER.info("관리자 클라이언트 재생산");
                    return adminReactorCloudFoundryClient = ReactorCloudFoundryClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider(getToken())).build();
                }
                return adminReactorCloudFoundryClient;
            }
        } catch (Exception e){
            return adminReactorCloudFoundryClient = ReactorCloudFoundryClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider(getToken())).build();
        } finally {
            adminTime = System.currentTimeMillis();
        }
    }


    /**
     * ReactorDopplerClient 생성하여, 반환한다.
     *
     * @param connectionContext
     * @param tokenProvider
     * @return ReactorDopplerClient
     */
    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorDopplerClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider).build();
    }

    /**
     * ReactorUaaClient 생성하여, 반환한다.
     *
     * @param connectionContext
     * @param tokenProvider
     * @return ReactorUaaClient
     */
    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorUaaClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider).build();
    }

    /**
     * ReactorUaaClient 생성하여, 반환한다.
     *
     * @param connectionContext
     * @param clientId
     * @param clientSecret
     * @return ReactorUaaClient
     */
    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, String clientId, String clientSecret) {
        return ReactorUaaClient.builder().connectionContext(connectionContext).tokenProvider(ClientCredentialsGrantTokenProvider.builder().clientId(clientId).clientSecret(clientSecret).build()).build();
    }

    /**
     * ReactorUaaClient(관리자) 생성하여, 반환한다.
     *
     * @param connectionContext
     * @param apiTarget
     * @param token
     * @param uaaAdminClientId
     * @param uaaAdminClientSecret
     * @return ReactorUaaClient
     */
    public static ReactorUaaClient uaaAdminClient(ConnectionContext connectionContext, String apiTarget, String token, String uaaAdminClientId, String uaaAdminClientSecret) {
        ReactorUaaClient reactorUaaClient = Common.uaaClient(connectionContext, tokenProvider(token));
        GetTokenByClientCredentialsResponse getTokenByClientCredentialsResponse = reactorUaaClient.tokens().getByClientCredentials(GetTokenByClientCredentialsRequest.builder().clientId(uaaAdminClientId).clientSecret(uaaAdminClientSecret).build()).block();
        return Common.uaaClient(connectionContext, tokenProvider(getTokenByClientCredentialsResponse.getAccessToken()));
    }

    private static final ThreadLocal<DefaultConnectionContext> connectionContextThreadLocal = new ThreadLocal<>();


    /**
     * DefaultConnectionContext 가져온다.
     *
     * @return DefaultConnectionContext
     */
    public DefaultConnectionContext connectionContext() {
        return connectionContext;
    }

    /**
     * DefaultConnectionContext 생성하여, 반환한다.
     *
     * @param apiUrl
     * @param skipSSLValidation
     * @return DefaultConnectionContext
     */
    public static DefaultConnectionContext crateConnectionContext(String apiUrl, boolean skipSSLValidation) {
        DefaultConnectionContext connectionContext = peekConnectionContext();
        if (null != connectionContext) {
            boolean isEqual = connectionContext.getApiHost().equals(convertApiUrl(apiUrl)) && connectionContext.getSkipSslValidation().get() == skipSSLValidation;
            if (!isEqual) {
                removeConnectionContext();
                connectionContext = null;
            }
        }

        if (null == connectionContext) {
            connectionContext = DefaultConnectionContext.builder().apiHost(convertApiUrl(apiUrl)).skipSslValidation(skipSSLValidation).keepAlive(true).build();
            pushConnectionContext(connectionContext);
        }

        return connectionContext;
    }

    private static DefaultConnectionContext peekConnectionContext() {
        return connectionContextThreadLocal.get();
    }

    private static void pushConnectionContext(DefaultConnectionContext connectionContext) {
        connectionContextThreadLocal.set(connectionContext);
        LOGGER.info("Create connection context and push thread local : DefalutConnectionContext@{}", Integer.toHexString(connectionContext.hashCode()));
    }

    private static void removeConnectionContext() {
        disposeConnectionContext(connectionContextThreadLocal.get());
        connectionContextThreadLocal.remove();
    }



    private static void disposeConnectionContext(DefaultConnectionContext connectionContext) {
        try {
            if (null != connectionContext) connectionContext.dispose();
        } catch (Exception ignore) {
        }
    }

    /**
     * TokenGrantTokenProvider 생성하여, 반환한다.
     *
     * @param token
     * @return DefaultConnectionContext
     * @throws Exception
     */
    public static TokenGrantTokenProvider tokenProvider(String token) {
        try {
            if (token.indexOf("bearer") < 0) {
                token = "bearer " + token;
            }
            return new TokenGrantTokenProvider(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * TokenProvider 생성하여, 반환한다. 토큰이 null일경우 defaultTokenProvider을 반환한다.
     *
     * @param token
     * @param defaultTokenProvider
     * @return TokenProvider
     * @throws Exception
     */
    public static TokenProvider tokenProviderWithDefault(String token, TokenProvider defaultTokenProvider) {
        if (null == token) return defaultTokenProvider;
        else if (token.trim().length() <= 0) return defaultTokenProvider;

        return tokenProvider(token);
    }

    /**
     * token을 제공하는 클레스 사용자 임의의 clientId를 사용하며,
     * user token, client token을 모두 얻을 수 있다.
     *
     * @param username
     * @param password
     * @return PasswordGrantTokenProvider
     */
    public static PasswordGrantTokenProvider tokenProvider(String username, String password) {
        return PasswordGrantTokenProvider.builder().password(password).username(username).build();
    }

    public PasswordGrantTokenProvider tokenProvider() {
        return tokenProvider;
    }
}
