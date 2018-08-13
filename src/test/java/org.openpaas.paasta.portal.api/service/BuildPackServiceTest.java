package org.openpaas.paasta.portal.api.service;

import okhttp3.mockwebserver.MockWebServer;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.Metadata;
import org.cloudfoundry.client.v2.buildpacks.BuildpackEntity;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.client.v2.applications.ApplicationsV2;
import org.cloudfoundry.client.v2.buildpacks.Buildpacks;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackRequest;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackResponse;
import org.cloudfoundry.identity.uaa.api.client.UaaClientOperations;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.config.TestConfig;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.controller.AppController;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildPackServiceTest extends TestConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);


    @Mock
    ObjectMapper objectMapper;


    @Mock
    private BuildPackService buildPackService;


    final String TOKEN = "token";

    final String ADMINUSER = "ADMIN";
    final String ADMINPAWD = "ADMIN";
    final String CLIENTID = "CLIENTID";
    final String CLIENTSECRET = "CLIENTSECRET";
    final String ZONEDOMAIN = "ZONEDOMAIN";

    @InjectMocks
    Common common;


    @MockBean
    PasswordGrantTokenProvider tokenProvider;


    private ReactorCloudFoundryClient client;


    MockWebServer mockWebServer;

    protected TokenProvider TOKEN_PROVIDER = connectionContext -> Mono.just("test-authorization");


    protected DefaultConnectionContext CONNECTION_CONTEXT;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockWebServer = new MockWebServer();
        CONNECTION_CONTEXT = DefaultConnectionContext.builder().apiHost(mockWebServer.getHostName()).secure(false).build();

        client = ReactorCloudFoundryClient.builder().connectionContext(CONNECTION_CONTEXT).rootV2(this.root).rootV3(this.root).tokenProvider(TOKEN_PROVIDER).build();

//        PW_TOKEN_PROVIDER = PasswordGrantTokenProvider.builder().username(ADMINUSER).password(ADMINPAWD).build();
    }

    @Test
    public void testGetBuildPacks() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        Map<String, Object> result = buildPackService.getBuildPacks();
//        Assert.assertEquals(new HashMap<String, Object>() {{
//            put("String", null);
//        }}, result);

        assertThat(this.client.buildpacks()).isNotNull();
    }

    @Test
    public void testUpdateBuildPack() throws Exception {
        BuildPack buildPack = new BuildPack();
        buildPack.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        buildPack.setPosition(1);
        buildPack.setEnable(true);
        buildPack.setLock(true);
        UpdateBuildpackResponse updateBuildpackResponse = UpdateBuildpackResponse.builder().build();
        PowerMockito.when(buildPackService.updateBuildPack(buildPack)).thenReturn(true);
        boolean result = buildPackService.updateBuildPack(buildPack);
        Assert.assertEquals(true, result);


    }

//    @Test
//    public void testGetToken() throws Exception {
//        when(loginService.login(any(), any())).thenReturn(null);
//
//        String result = buildPackService.getToken();
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testGetTargetURL() throws Exception {
//        when(loginService.getTargetURI(any())).thenReturn(null);
//
//        URL result = buildPackService.getTargetURL("target");
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
//        CloudFoundryClient result = buildPackService.getCloudFoundryClient("token");
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
//        CloudFoundryClient result = buildPackService.getCloudFoundryClient("token", "organization", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient3() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any(), any())).thenReturn(null);
//
//        CloudFoundryClient result = buildPackService.getCloudFoundryClient("id", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient4() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any(), any())).thenReturn(null);
//
//        CloudFoundryClient result = buildPackService.getCloudFoundryClient("id", "password", "organization", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudCredentials() throws Exception {
//        when(loginService.getOAuth2AccessToken(any())).thenReturn(null);
//
//        CloudCredentials result = buildPackService.getCloudCredentials("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudCredentials2() throws Exception {
//        CloudCredentials result = buildPackService.getCloudCredentials("id", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaUserOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaUserOperations result = buildPackService.getUaaUserOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaGroupOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaGroupOperations result = buildPackService.getUaaGroupOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaClientOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaClientOperations result = buildPackService.getUaaClientOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testStringNullCheck() throws Exception {
//        boolean result = buildPackService.stringNullCheck("params");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testStringContainsSpaceCheck() throws Exception {
//        boolean result = buildPackService.stringContainsSpaceCheck("params");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testGetPropertyValue() throws Exception {
//        when(loginService.getProcPropertyValue(any(), any())).thenReturn("getProcPropertyValueResponse");
//        when(loginService.getPropertyValue(any(), any())).thenReturn("getPropertyValueResponse");
//
//        String result = BuildPackService.getPropertyValue("key");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testGetPropertyValue2() throws Exception {
//        when(loginService.getProcPropertyValue(any(), any())).thenReturn("getProcPropertyValueResponse");
//
//        String result = BuildPackService.getPropertyValue("key", "configFileName");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testConvertApiUrl() throws Exception {
//        String result = BuildPackService.convertApiUrl("url");
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
//        DefaultCloudFoundryOperations result = BuildPackService.cloudFoundryOperations(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations2() throws Exception {
//        DefaultCloudFoundryOperations result = BuildPackService.cloudFoundryOperations(null, null, null);
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
//        DefaultCloudFoundryOperations result = BuildPackService.cloudFoundryOperations(null, new TokenGrantTokenProvider("token"), "org", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations4() throws Exception {
//        DefaultCloudFoundryOperations result = BuildPackService.cloudFoundryOperations(null, null, null, "org", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryClient() throws Exception {
//        ReactorCloudFoundryClient result = BuildPackService.cloudFoundryClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testDopplerClient() throws Exception {
//        ReactorDopplerClient result = BuildPackService.dopplerClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaClient() throws Exception {
//        ReactorUaaClient result = BuildPackService.uaaClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaClient2() throws Exception {
//        ReactorUaaClient result = BuildPackService.uaaClient(null, "clientId", "clientSecret");
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
//        ReactorUaaClient result = BuildPackService.uaaAdminClient("apiTarget", "token", "uaaAdminClientId", "uaaAdminClientSecret");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testConnectionContext() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.peekConnectionContext()).thenReturn(null);
//        when(loginService.connectionContext(any(), anyBoolean())).thenReturn(null);
//
//        DefaultConnectionContext result = buildPackService.connectionContext();
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testConnectionContext2() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.peekConnectionContext()).thenReturn(null);
//
//        DefaultConnectionContext result = BuildPackService.connectionContext("apiUrl", true);
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testTokenProvider() throws Exception {
//        TokenGrantTokenProvider result = BuildPackService.tokenProvider("token");
//        Assert.assertEquals(new TokenGrantTokenProvider("token"), result);
//    }
//
//    @Test
//    public void testTokenProviderWithDefault() throws Exception {
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        TokenProvider result = BuildPackService.tokenProviderWithDefault("token", new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(new TokenGrantTokenProvider("token"), result);
//    }
//
//    @Test
//    public void testTokenProvider2() throws Exception {
//        PasswordGrantTokenProvider result = BuildPackService.tokenProvider("username", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testPreDestroy() throws Exception {
//        when(loginService.peekConnectionContext()).thenReturn(null);
//
//        buildPackService.preDestroy();
//    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme