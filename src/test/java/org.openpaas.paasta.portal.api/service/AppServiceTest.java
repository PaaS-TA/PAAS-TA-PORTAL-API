package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.applications.SummaryApplicationResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppServiceTest extends TestConfig {
    @MockBean
    AppService appService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAppSummary() throws Exception {
        SummaryApplicationResponse summaryApplicationResponse = SummaryApplicationResponse.builder().id("guid").name("name").build();
        when(appService.getAppSummary(anyString(), anyString())).thenReturn(summaryApplicationResponse);
        SummaryApplicationResponse result = appService.getAppSummary("guid", "token");
        Assert.assertEquals(summaryApplicationResponse, result);
    }

    @Test
    public void testGetAppStats() throws Exception {
        ApplicationStatisticsResponse applicationStatisticsResponse = ApplicationStatisticsResponse.builder().build();
        when(appService.getAppStats(any(),any())).thenReturn(applicationStatisticsResponse);
        ApplicationStatisticsResponse result = appService.getAppStats("guid", "token");
        Assert.assertEquals(applicationStatisticsResponse, result);
    }

//    @Test
//    public void testRenameApp() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        Map result = appService.renameApp(new App(), "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testStartApp() throws Exception {
//        when(loginService.cloudFoundryOperations(any(), any(), any(), any())).thenReturn(null);
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.startApp(new App(), "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testStopApp() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.stopApp(new App(), "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testDeleteApp() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        Map result = appService.deleteApp("guid");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testRestageApp() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.restageApp(new App(), "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testUpdateApp() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.updateApp(new App(), "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testBindService() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.bindService(new HashMap() {{
//            put("String", "String");
//        }}, "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testUnbindService() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.unbindService("serviceInstanceId", "applicationId", "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testGetAppEvents() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        ListEventsResponse result = appService.getAppEvents("guid", "token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetApplicationEnv() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        ApplicationEnvironmentResponse result = appService.getApplicationEnv("guid", "token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testAddApplicationRoute() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.addApplicationRoute(new HashMap() {{
//            put("String", "String");
//        }}, "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testRemoveApplicationRoute() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.removeApplicationRoute("guid", "route_guid", "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testDeleteRoute() throws Exception {
//        boolean result = appService.deleteRoute("orgName", "spaceName", Arrays.<String>asList("String"), "token");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testTerminateInstance() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = appService.terminateInstance("guid", "index", "token");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testGetRecentLog() throws Exception {
//        when(loginService.dopplerClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        List<Envelope> result = appService.getRecentLog("guid", "token");
//        Assert.assertEquals(Arrays.<Envelope>asList(null), result);
//    }
//
//    @Test
//    public void testGetTailLog() throws Exception {
//        when(loginService.cloudFoundryOperations(any(), any(), any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        List<LogMessage> result = appService.getTailLog("guid", "token");
//        Assert.assertEquals(Arrays.<LogMessage>asList(null), result);
//    }
//
//    @Test
//    public void testSocketTailLogs() throws Exception {
//        when(loginService.cloudFoundryOperations(any(), any(), any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        SocketIOClient result = appService.socketTailLogs(null, "appName", "orgName", "spaceName", "token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetToken() throws Exception {
//        when(loginService.login(any(), any())).thenReturn(null);
//
//        String result = appService.getToken();
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testGetTargetURL() throws Exception {
//        when(loginService.getTargetURI(any())).thenReturn(null);
//
//        URL result = appService.getTargetURL("target");
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
//        CloudFoundryClient result = appService.getCloudFoundryClient("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient2() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any())).thenReturn(null);
//        when(loginService.getOAuth2AccessToken(any())).thenReturn(null);
//
//        CloudFoundryClient result = appService.getCloudFoundryClient("token", "organization", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient3() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any(), any())).thenReturn(null);
//
//        CloudFoundryClient result = appService.getCloudFoundryClient("id", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient4() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any(), any())).thenReturn(null);
//
//        CloudFoundryClient result = appService.getCloudFoundryClient("id", "password", "organization", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudCredentials() throws Exception {
//        when(loginService.getOAuth2AccessToken(any())).thenReturn(null);
//
//        CloudCredentials result = appService.getCloudCredentials("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudCredentials2() throws Exception {
//        CloudCredentials result = appService.getCloudCredentials("id", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaUserOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaUserOperations result = appService.getUaaUserOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaGroupOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaGroupOperations result = appService.getUaaGroupOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaClientOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaClientOperations result = appService.getUaaClientOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testStringNullCheck() throws Exception {
//        boolean result = appService.stringNullCheck("params");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testStringContainsSpaceCheck() throws Exception {
//        boolean result = appService.stringContainsSpaceCheck("params");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testGetPropertyValue() throws Exception {
//        when(loginService.getProcPropertyValue(any(), any())).thenReturn("getProcPropertyValueResponse");
//        when(loginService.getPropertyValue(any(), any())).thenReturn("getPropertyValueResponse");
//
//        String result = AppService.getPropertyValue("key");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testGetPropertyValue2() throws Exception {
//        when(loginService.getProcPropertyValue(any(), any())).thenReturn("getProcPropertyValueResponse");
//
//        String result = AppService.getPropertyValue("key", "configFileName");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testConvertApiUrl() throws Exception {
//        String result = AppService.convertApiUrl("url");
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
//        DefaultCloudFoundryOperations result = AppService.cloudFoundryOperations(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations2() throws Exception {
//        DefaultCloudFoundryOperations result = AppService.cloudFoundryOperations(null, null, null);
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
//        DefaultCloudFoundryOperations result = AppService.cloudFoundryOperations(null, new TokenGrantTokenProvider("token"), "org", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations4() throws Exception {
//        DefaultCloudFoundryOperations result = AppService.cloudFoundryOperations(null, null, null, "org", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryClient() throws Exception {
//        ReactorCloudFoundryClient result = AppService.cloudFoundryClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testDopplerClient() throws Exception {
//        ReactorDopplerClient result = AppService.dopplerClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaClient() throws Exception {
//        ReactorUaaClient result = AppService.uaaClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaClient2() throws Exception {
//        ReactorUaaClient result = AppService.uaaClient(null, "clientId", "clientSecret");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaAdminClient() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.uaaClient(any(), any())).thenReturn(null);
//        when(loginService.peekConnectionContext()).thenReturn(null);
//        when(loginService.connectionContext(any(), anyBoolean())).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        ReactorUaaClient result = AppService.uaaAdminClient("apiTarget", "token", "uaaAdminClientId", "uaaAdminClientSecret");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testConnectionContext() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.peekConnectionContext()).thenReturn(null);
//        when(loginService.connectionContext(any(), anyBoolean())).thenReturn(null);
//
//        DefaultConnectionContext result = appService.connectionContext();
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testConnectionContext2() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.peekConnectionContext()).thenReturn(null);
//
//        DefaultConnectionContext result = AppService.connectionContext("apiUrl", true);
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testTokenProvider() throws Exception {
//        TokenGrantTokenProvider result = AppService.tokenProvider("token");
//        Assert.assertEquals(new TokenGrantTokenProvider("token"), result);
//    }
//
//    @Test
//    public void testTokenProviderWithDefault() throws Exception {
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        TokenProvider result = AppService.tokenProviderWithDefault("token", new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(new TokenGrantTokenProvider("token"), result);
//    }
//
//    @Test
//    public void testTokenProvider2() throws Exception {
//        PasswordGrantTokenProvider result = AppService.tokenProvider("username", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testPreDestroy() throws Exception {
//        when(loginService.peekConnectionContext()).thenReturn(null);
//
//        appService.preDestroy();
//    }
}

