package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.identity.uaa.api.UaaConnectionFactory;
import org.cloudfoundry.identity.uaa.api.client.UaaClientOperations;
import org.cloudfoundry.identity.uaa.api.common.UaaConnection;
import org.cloudfoundry.identity.uaa.api.group.UaaGroupOperations;
import org.cloudfoundry.identity.uaa.api.user.UaaUserOperations;
import org.junit.runner.RunWith;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.util.SSLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;


public class CommonTest {

    public static String uaaTarget;
    public static String uaaAdminClientId;
    public static String uaaAdminClientSecret;
    public static String uaaLoginClientId;
    public static String uaaLoginClientSecret;

    private static final Logger LOG = LoggerFactory.getLogger(CommonTest.class);

    public static String apiTarget = "";//"https://api.115.68.46.29.xip.io";

    public static URL getTargetURL(String target) {
        try {
            return getTargetURI(target).toURL();
        } catch (MalformedURLException e) {
            LOG.error("The target URL is not valid: " + e.getMessage());
        }

        return null;
    }

    public static URI getTargetURI(String target) {
        try {

            return new URI(target);
        } catch (URISyntaxException e) {
            LOG.error("The target URL is not valid: " + e.getMessage());
        }

        return null;
    }



    public static Properties prop;

    static {
        InputStream is = null;
        try {
            prop = new Properties();
            is = ClassLoader.class.getResourceAsStream("/config.properties");
            prop.load(is);
            apiTarget = getPropertyValue("test.apiTarget");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String key){

        return prop.getProperty(key);
    }

    private DefaultOAuth2AccessToken getOAuth2AccessToken(String token) {
        return new DefaultOAuth2AccessToken(token);
    }

    public static UaaUserOperations getUaaUserOperations(String uaaClientId) throws Exception {
        UaaConnection connection = getUaaConnection(uaaClientId);
        return connection.userOperations();
    }

    public static UaaGroupOperations getUaaGroupOperations(String uaaClientId) throws Exception {
        UaaConnection connection = getUaaConnection(uaaClientId);
        return connection.groupOperations();
    }

    public static UaaClientOperations getUaaClientOperations(String uaaClientId) throws Exception{
        UaaConnection connection= getUaaConnection(uaaClientId);
        return connection.clientOperations();
    }

    //uaa 커넥션 생성
    private static UaaConnection getUaaConnection(String uaaClientId) throws Exception {
        uaaTarget  = getPropertyValue("test.uaaTarget");
        //ssl 유효성 체크 비활성
        if(getPropertyValue("test.skipSSLValidation").equals("true")){
            SSLUtils.turnOffSslChecking();
        }

        ResourceOwnerPasswordResourceDetails credentials = getCredentials(uaaClientId);
        URL uaaHost = new URL(uaaTarget);
        UaaConnection connection =  UaaConnectionFactory.getConnection(uaaHost, credentials);
        return connection;
    }


    private static ResourceOwnerPasswordResourceDetails getCredentials(String uaaClientId){

        uaaTarget  = getPropertyValue("test.uaaTarget");
        uaaAdminClientId = getPropertyValue("test.uaaAdminClientId");
        uaaAdminClientSecret = getPropertyValue("test.uaaAdminClientSecret");
        uaaLoginClientId = getPropertyValue("test.uaaLoginClientId");
        uaaLoginClientSecret= getPropertyValue("test.uaaLoginClientSecret");

        ResourceOwnerPasswordResourceDetails credentials = new ResourceOwnerPasswordResourceDetails();
        credentials.setAccessTokenUri(uaaTarget+"/oauth/token?grant_type=client_credentials&response_type=token");
        credentials.setClientAuthenticationScheme(AuthenticationScheme.header);

        credentials.setClientId(uaaClientId);

        if(uaaClientId.equals(uaaAdminClientId)){
            credentials.setClientSecret(uaaAdminClientSecret);
        } else if(uaaClientId.equals(uaaLoginClientId)){
            credentials.setClientSecret(uaaLoginClientSecret);
        }

        return credentials;
    }

}
