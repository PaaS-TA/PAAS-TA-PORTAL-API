package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudCredentials;
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
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.UaaClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.config.service.EmailConfig;
import org.openpaas.paasta.portal.api.util.SSLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Common {

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

    @Value("${cloudfoundry.user.uaaClient.loginClientId}")
    public String uaaLoginClientId;

    @Value("${cloudfoundry.user.uaaClient.loginClientSecret}")
    public String uaaLoginClientSecret;

    @Value("${cloudfoundry.user.uaaClient.skipSSLValidation}")
    public boolean skipSSLValidation;


    @Value("${cloudfoundry.user.uaaClient.skipSSLValidation}")
    public boolean uaaskipSSLValidation;




    @Value("${monitoring.api.url}")
    public String monitoringApiTarget;

    @Autowired
    public EmailConfig emailConfig;

    public URL getTargetURL(String target) throws MalformedURLException, URISyntaxException {
        return getTargetURI(target).toURL();
    }

    private URI getTargetURI(String target) throws URISyntaxException {
        return new URI(target);
    }

    /**
     * get CloudFoundryClinet Object from token String
     *
     * @param token
     * @return CloudFoundryClinet
     */
    public CloudFoundryClient getCloudFoundryClient(String token) throws MalformedURLException, URISyntaxException {

        return cloudFoundryClient(connectionContext(),tokenProvider(token));
    }



    /**
     * get CloudFoundryClinet Object from id, password
     *
     * @param id
     * @param password
     * @return CloudFoundryClinet
     */
    public CloudFoundryClient getCloudFoundryClient(String id, String password) throws MalformedURLException, URISyntaxException {
        return cloudFoundryClient(connectionContext(),tokenProvider(id,password));
    }


    /**
     * get CloudFoundryClinet Object from token String
     *
     * @param token
     * @return CloudFoundryClinet
     */
    public CustomCloudFoundryClient getCustomCloudFoundryClient(String token) throws Exception {

        return new CustomCloudFoundryClient(getCloudCredentials(token), getTargetURL(apiTarget), true);
    }

    /**
     * get CloudFoundryClinet Object from token String
     * with organization, space
     *
     * @param token
     * @param organization
     * @param space
     * @return CloudFoundryClinet
     */
    public CustomCloudFoundryClient getCustomCloudFoundryClient(String token, String organization, String space) throws Exception {

        return new CustomCloudFoundryClient(getCloudCredentials(token), getTargetURL(apiTarget), organization, space, true);
    }

    /**
     * get CustomCloudFoundryClinet Object from id, password
     *
     * @param id
     * @param password
     * @return CustomCloudFoundryClinet
     */
    public CustomCloudFoundryClient getCustomCloudFoundryClient(String id, String password) throws Exception {
        return new CustomCloudFoundryClient(getCloudCredentials(id, password), getTargetURL(apiTarget), true);
    }

    /**
     * get CustomCloudFoundryClinet Object from token String
     * with organization, space
     *
     * @param id
     * @param password
     * @param organization
     * @param space
     * @return CustomCloudFoundryClinet
     */
    public CustomCloudFoundryClient getCustomCloudFoundryClient(String id, String password, String organization, String space) throws Exception {

        return new CustomCloudFoundryClient(getCloudCredentials(id, password), getTargetURL(apiTarget), organization, space, true);
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
        return new CloudCredentials(id, password);
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

    public UaaUserOperations getUaaUserOperations(String uaaClientId) throws Exception {
        UaaConnection connection = getUaaConnection(uaaClientId);
        return connection.userOperations();
    }

    public UaaGroupOperations getUaaGroupOperations(String uaaClientId) throws Exception {
        UaaConnection connection = getUaaConnection(uaaClientId);
        return connection.groupOperations();
    }

    public UaaClientOperations getUaaClientOperations(String uaaClientId) throws Exception{
        UaaConnection connection= getUaaConnection(uaaClientId);
        return connection.clientOperations();
    }

    //uaa 커넥션 생성
    private UaaConnection getUaaConnection(String uaaClientId) throws Exception {

        ResourceOwnerPasswordResourceDetails credentials = getCredentials(uaaClientId);
        URL uaaHost = new URL(uaaTarget);

        //ssl 유효성 체크 비활성
        if(uaaskipSSLValidation){
            SSLUtils.turnOffSslChecking();
        }

        UaaConnection connection =  UaaConnectionFactory.getConnection(uaaHost, credentials);
        return connection;
    }

    //credentials 세팅
    private ResourceOwnerPasswordResourceDetails getCredentials(String uaaClientId){
        ResourceOwnerPasswordResourceDetails credentials = new ResourceOwnerPasswordResourceDetails();
        credentials.setAccessTokenUri(uaaTarget+"/oauth/token?grant_type=client_credentials&response_type=token");
        credentials.setClientAuthenticationScheme(AuthenticationScheme.header);

        credentials.setClientId(uaaClientId);

        if(uaaClientId.equals(uaaAdminClientId)){
            credentials.setClientSecret(uaaAdminClientSecret);
        } else if(uaaClientId.equals(uaaLoginClientId)){
            credentials.setClientSecret(uaaLoginClientSecret);
        }

        //credentials.setUsername(adminUserName);
        //credentials.setPassword(adminPassword);
        return credentials;
    }


    /**
     * 요청 파라미터들의 빈값 또는 null값 확인을 하나의 메소드로 처리할 수 있도록 생성한 메소드
     * 요청 파라미터 중 빈값 또는 null값인 파라미터가 있는 경우, false를 리턴한다.
     *
     * @param params
     * @return
     */
    public boolean stringNullCheck(String... params) {
        return Arrays.stream(params).allMatch(param -> null != param && !param.equals(""));
    }

    //요청 문자열 파라미터 중, 공백을 포함하고 있는 파라미터가 있을 경우 false를 리턴
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

    public static int diffDay(Date d, Date accessDate) {
        /**
         * 날짜 계산
         */
        Calendar curC = Calendar.getInstance();
        Calendar accessC = Calendar.getInstance();
        curC.setTime(d);
        accessC.setTime(accessDate);
        accessC.compareTo(curC);
        int diffCnt = 0;
        while (!accessC.after(curC)) {
            diffCnt++;
            accessC.add(Calendar.DATE, 1); // 다음날로 바뀜

            System.out.println(accessC.get(Calendar.YEAR) + "년 " + (accessC.get(Calendar.MONTH) + 1) + "월 " + accessC.get(Calendar.DATE) + "일");
        }
        System.out.println("기준일로부터 " + diffCnt + "일이 지났습니다.");
        System.out.println(accessC.compareTo(curC));
        return diffCnt;
    }





    /**
     * body의 내용을 url의 parameter로 보내준다.
     * 이미지를 넣을 경우
     *
     * @param body{userId , refreshToken, ....}
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public boolean sendEmail(Map body) throws IOException, MessagingException {
        Boolean bRtn = false;
        try {
            String userId = (String) body.getOrDefault("userId", "");
            String refreshToken = (String) body.getOrDefault("refreshToken", "");

            // 메일 관련 정보
            Properties properties = emailConfig.properties();


//         메일 내용
            String username = (String) body.getOrDefault("username", properties.get("mail.smtp.username"));
            String userEmail = (String) body.getOrDefault("userEmail", properties.get("mail.smtp.userEmail"));
            ClassLoader classLoader = getClass().getClassLoader();
            String sFile = (String) body.getOrDefault("sFile", properties.get("mail.smtp.sFile"));
            String authUrl = (String) body.getOrDefault("authUrl", properties.get("mail.smtp.authUrl"));
            String subject = (String) body.getOrDefault("subject", properties.get("mail.smtp.subject"));
            String contextUrl = (String) body.getOrDefault("contextUrl", properties.get("mail.smtp.contextUrl"));
            // 인증
            Authenticator auth = emailConfig.auth();

            // 메일 세션
            Session session = Session.getInstance(properties, auth);

            File file = new File(classLoader.getResource(sFile).getFile());
            System.out.println(file.getAbsolutePath());
            Document doc = Jsoup.parse(file, "UTF-8");
            Elements elementAhref = doc.select("a[href]");
            Elements elementSpan = doc.select("span");

            if (elementAhref.size() != 0) {
                elementAhref.get(0).attr("href", authUrl + "/" + contextUrl + "?userId=" + userId + "&refreshToken=" + refreshToken);
            }
            if (elementSpan.size() != 0) {
                elementSpan.get(0).childNode(0).attr("text", userId);
            }

            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setDataHandler(new DataHandler(new ByteArrayDataSource(doc.outerHtml(), "text/html")));
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            msg.setContent(doc.outerHtml(), "text/html;charset=" + "EUC-KR");
            msg.setFrom(new InternetAddress(userEmail, username));
            msg.setReplyTo(InternetAddress.parse(userEmail, false));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userId, false));
            Transport.send(msg);
            bRtn = true;
        } catch (IOException | MessagingException e) {
            e.printStackTrace();

        }
        return bRtn;

    }


    /**
     * 기본적인 연결에 대한 설정값을 갖는 클레스
     *
     * @return
     */
    public DefaultConnectionContext connectionContext() {
        return DefaultConnectionContext.builder()
                .apiHost(apiTarget)
                .skipSslValidation(cfskipSSLValidation)
                .build();
    }

    /**
     * token을 제공하는 클레스 기본적으로 'cloudfoundry' client를 사용하며
     * user token을 얻는 것을 주 목적으로 사용한다.
     *
     * @param username
     * @param password
     * @return
     */
    public static PasswordGrantTokenProvider tokenProvider(String username,
                                                           String password) {
        return PasswordGrantTokenProvider.builder()
                .clientId("cf")
                .clientSecret("")
                .password(password)
                .username(username)
                .build();
    }

    /**
     * token을 제공하는 클레스 사용자 임의의 clientId를 사용하며,
     * user token, client token을 모두 얻을 수 있다.
     *
     * @param clientId
     * @param clientSecret
     * @param username
     * @param password
     * @return
     */
    public static PasswordGrantTokenProvider tokenProvider(String username,
                                                           String password,
                                                           String clientId,
                                                           String clientSecret) {
        return PasswordGrantTokenProvider.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .password(password)
                .username(username)
                .build();
    }

    /**
     * 이미 가지고 있는 token을 CF client에 제공하는 클래스
     *
     * @param token
     * @return
     */

    public static TokenGrantTokenProvider tokenProvider(String token) {
        return new TokenGrantTokenProvider(token);
    }

    /**
     * CloudFoudry의 기능을 제공한다.
     *
     * @param connectionContext
     * @param tokenProvider
     * @return
     */
    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }
    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, String username, String password, String clientId, String clientSecret) {
        return cloudFoundryClient(connectionContext, tokenProvider(username, password, clientId, clientSecret));
    }
    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, String username, String password) {
        return cloudFoundryClient(connectionContext, tokenProvider(username, password));
    }
    public static ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, String token) {
        return cloudFoundryClient(connectionContext, tokenProvider(token));
    }

    /**
     * Dropper의 기능을 제공한다.
     *
     * @param connectionContext
     * @param tokenProvider
     * @return
     */
    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorDopplerClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }
    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, String username, String password, String clientId, String clientSecret) {
        return dopplerClient(connectionContext, tokenProvider(username, password, clientId, clientSecret));
    }
    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, String username, String password) {
        return dopplerClient(connectionContext, tokenProvider(username, password));
    }
    public static ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, String token) {
        return dopplerClient(connectionContext, tokenProvider(token));
    }

    /**
     * Uaa 의 기능을 제공한다.
     *
     * @param connectionContext
     * @param tokenProvider
     * @return
     */
    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorUaaClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }
    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, String username, String password, String clientId, String clientSecret) {
        return uaaClient(connectionContext, tokenProvider(username, password, clientId, clientSecret));
    }
    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, String username, String password) {
        return uaaClient(connectionContext, tokenProvider(username, password));
    }
    public static ReactorUaaClient uaaClient(ConnectionContext connectionContext, String token) {
        return uaaClient(connectionContext, tokenProvider(token));
    }

    /**
     * CloudFoundry, Dropper, Uaa 의 기능을 함께 사용하여 구현가능한 기능을 제공한다.
     *
     * @param cloudFoundryClient
     * @param dopplerClient
     * @param uaaClient
     * @param organization
     * @param space
     * @return
     */
    public static DefaultCloudFoundryOperations cloudFoundryOperations(org.cloudfoundry.client.CloudFoundryClient cloudFoundryClient,
                                                                       DopplerClient dopplerClient,
                                                                       UaaClient uaaClient,
                                                                       String organization,
                                                                       String space) {
        return DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .dopplerClient(dopplerClient)
                .uaaClient(uaaClient)
                .organization(organization)
                .space(space)
                .build();
    }
    public static DefaultCloudFoundryOperations cloudFoundryOperations(ConnectionContext connectionContext,
                                                                       TokenProvider tokenProvider,
                                                                       String organization,
                                                                       String space) {
        return cloudFoundryOperations(cloudFoundryClient(connectionContext, tokenProvider),
                dopplerClient(connectionContext, tokenProvider),
                uaaClient(connectionContext, tokenProvider),
                organization,
                space);
    }
    public static DefaultCloudFoundryOperations cloudFoundryOperations(org.cloudfoundry.client.CloudFoundryClient cloudFoundryClient,
                                                                       DopplerClient dopplerClient,
                                                                       UaaClient uaaClient) {
        return DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .dopplerClient(dopplerClient)
                .uaaClient(uaaClient)
                .build();
    }
    public static DefaultCloudFoundryOperations cloudFoundryOperations(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return cloudFoundryOperations(cloudFoundryClient(connectionContext, tokenProvider),
                dopplerClient(connectionContext, tokenProvider),
                uaaClient(connectionContext, tokenProvider));
    }


    /**
     * 사용자 Uaa 등록
     * @param scimUser
     * @throws MalformedURLException
     */
/**
 public void register(ScimUser scimUser) throws MalformedURLException {


 ResourceOwnerPasswordResourceDetails credentials = new ResourceOwnerPasswordResourceDetails();
 credentials.setAccessTokenUri("https://uaa.115.68.46.29.xip.io/oauth/token/61a208dd-6e56-4afe-8f0e-9b6a9b0492ef?grant_type=client_credentials&response_type=token");

 credentials.setClientAuthenticationScheme(AuthenticationScheme.header);

 credentials.setClientId("admin");
 credentials.setClientSecret("admin");

 URL uaaHost = new URL("http://uaa.115.68.46.29.xip.io");
 UaaConnection connection = UaaConnectionFactory.getConnection(uaaHost, credentials);
 UaaUserOperations uaaUserOperations = connection.userOperations();
 uaaUserOperations.createUser(scimUser);

 }

 @SuppressWarnings({"rawtypes", "unchecked"})
 public void resetPassword(String userId, String newPassword, String type) throws MalformedURLException {
 ResourceOwnerPasswordResourceDetails credentials = new ResourceOwnerPasswordResourceDetails();
 credentials.setAccessTokenUri(uaaTarget + "/oauth/token?grant_type=client_credentials&response_type=token");

 credentials.setClientAuthenticationScheme(AuthenticationScheme.header);

 credentials.setClientId("admin");
 credentials.setClientSecret("admin");


 URL uaaHost = new URL(uaaTarget);
 UaaConnection connection = UaaConnectionFactory.getConnection(uaaHost, credentials);
 //        connection.userOperations().changeUserPassword();createUser(scimUser);
 }
 */

}
