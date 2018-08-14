//package org.openpaas.paasta.portal.api.common;
//
//import org.cloudfoundry.client.lib.CloudCredentials;
//import org.cloudfoundry.client.lib.CloudFoundryClient;
//import org.cloudfoundry.identity.uaa.api.common.UaaConnection;
//import org.cloudfoundry.identity.uaa.api.group.UaaGroupOperations;
//import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
//import org.cloudfoundry.reactor.DefaultConnectionContext;
//import org.cloudfoundry.reactor.TokenProvider;
//import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
//import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
//import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
//import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
//import org.openpaas.paasta.portal.api.service.LoginService;
//import org.slf4j.Logger;
//import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//
//import java.net.URL;
//
//import static org.mockito.Mockito.*;
//
//public class CommonTest {
//    @Mock
//    Logger LOGGER;
//    @Mock
//    LoginService loginService;
//    //Field connectionContext of type DefaultConnectionContext - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    //Field tokenProvider of type PasswordGrantTokenProvider - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    @Mock
//    ObjectMapper objectMapper;
//    @Mock
//    ThreadLocal<DefaultConnectionContext> connectionContextThreadLocal;
//    @InjectMocks
//    Common common;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testGetToken() throws Exception {
//        when(loginService.login(any(), any())).thenReturn(null);
//
//        String result = common.getToken();
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetTargetURL() throws Exception {
//        when(loginService.getTargetURI(any())).thenReturn(null);
//
//        URL result = common.getTargetURL("target");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any())).thenReturn(null);
//        when(loginService.getOAuth2AccessToken(any())).thenReturn(null);
//
//        CloudFoundryClient result = common.getCloudFoundryClient("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudCredentials() throws Exception {
//        when(loginService.getOAuth2AccessToken(any())).thenReturn(null);
//
//        CloudCredentials result = common.getCloudCredentials("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudCredentials2() throws Exception {
//        CloudCredentials result = common.getCloudCredentials("id", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetOAuth2AccessToken() throws Exception {
//        DefaultOAuth2AccessToken result = common.getOAuth2AccessToken("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaGroupOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaGroupOperations result = common.getUaaGroupOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaConnection() throws Exception {
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaConnection result = common.getUaaConnection("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCredentials() throws Exception {
//        ResourceOwnerPasswordResourceDetails result = common.getCredentials("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testStringNullCheck() throws Exception {
//        boolean result = common.stringNullCheck("params");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testStringContainsSpaceCheck() throws Exception {
//        boolean result = common.stringContainsSpaceCheck("params");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testGetPropertyValue() throws Exception {
//        when(loginService.getProcPropertyValue(any(), any())).thenReturn("getProcPropertyValueResponse");
//        when(loginService.getPropertyValue(any(), any())).thenReturn("getPropertyValueResponse");
//
//        String result = Common.getPropertyValue("key");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testGetProcPropertyValue() throws Exception {
//        String result = Common.getProcPropertyValue("key", "configFileName");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testGetPropertyValue2() throws Exception {
//        when(loginService.getProcPropertyValue(any(), any())).thenReturn("getProcPropertyValueResponse");
//
//        String result = Common.getPropertyValue("key", "configFileName");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testConvertApiUrl() throws Exception {
//        String result = Common.convertApiUrl("url");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations() throws Exception {
//        when(loginService.cloudFoundryOperations(any(), any(), any())).thenReturn(null);
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.dopplerClient(any(), any())).thenReturn(null);
//        when(loginService.uaaClient(any(), any())).thenReturn(null);
//
//        DefaultCloudFoundryOperations result = Common.cloudFoundryOperations(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations2() throws Exception {
//        DefaultCloudFoundryOperations result = Common.cloudFoundryOperations(null, null, null);
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations3() throws Exception {
//        when(loginService.cloudFoundryOperations(any(), any(), any(), any(), any())).thenReturn(null);
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.dopplerClient(any(), any())).thenReturn(null);
//        when(loginService.uaaClient(any(), any())).thenReturn(null);
//
//        DefaultCloudFoundryOperations result = Common.cloudFoundryOperations(null, new TokenGrantTokenProvider("token"), "org", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations4() throws Exception {
//        DefaultCloudFoundryOperations result = Common.cloudFoundryOperations(null, null, null, "org", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryClient() throws Exception {
//        ReactorCloudFoundryClient result = Common.cloudFoundryClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testDopplerClient() throws Exception {
//        ReactorDopplerClient result = Common.dopplerClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaClient() throws Exception {
//        ReactorUaaClient result = Common.uaaClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaClient2() throws Exception {
//        ReactorUaaClient result = Common.uaaClient(null, "clientId", "clientSecret");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaAdminClient() throws Exception {
//        when(loginService.uaaClient(any(), any())).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        ReactorUaaClient result = Common.uaaAdminClient(null, "apiTarget", "token", "uaaAdminClientId", "uaaAdminClientSecret");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testConnectionContext() throws Exception {
//        DefaultConnectionContext result = common.connectionContext();
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCrateConnectionContext() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.peekConnectionContext()).thenReturn(null);
//
//        DefaultConnectionContext result = Common.crateConnectionContext("apiUrl", true);
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testPeekConnectionContext() throws Exception {
//        DefaultConnectionContext result = Common.peekConnectionContext();
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testPushConnectionContext() throws Exception {
//        Common.pushConnectionContext(null);
//    }
//
//    @Test
//    public void testRemoveConnectionContext() throws Exception {
//        Common.removeConnectionContext();
//    }
//
//    @Test
//    public void testDisposeConnectionContext() throws Exception {
//        Common.disposeConnectionContext(null);
//    }
//
//    @Test
//    public void testTokenProvider() throws Exception {
//        TokenGrantTokenProvider result = Common.tokenProvider("token");
//        Assert.assertEquals(new TokenGrantTokenProvider("token"), result);
//    }
//
//    @Test
//    public void testTokenProviderWithDefault() throws Exception {
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        TokenProvider result = Common.tokenProviderWithDefault("token", new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(new TokenGrantTokenProvider("token"), result);
//    }
//
//    @Test
//    public void testTokenProvider2() throws Exception {
//        PasswordGrantTokenProvider result = Common.tokenProvider("username", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testTokenProvider3() throws Exception {
//        PasswordGrantTokenProvider result = common.tokenProvider();
//        Assert.assertEquals(null, result);
//    }
//}
//
////Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme