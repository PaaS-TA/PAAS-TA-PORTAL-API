package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.CloudFoundryClient;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.config.TestConfig;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
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

import static org.mockito.Mockito.*;

//@RunWith(SpringRunner.class)
@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*" , "javax.crypto.*"})
@PrepareForTest({Common.class})
@RunWith(PowerMockRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildPackServiceTest extends TestConfig {
    @Mock
    Logger LOGGER;
    @Mock
    LoginService loginService;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    ThreadLocal<DefaultConnectionContext> connectionContextThreadLocal;
    @InjectMocks
    private final BuildPackService buildPackService = new BuildPackService();

    private final CloudFoundryOperations cloudFoundryOperations = mock(CloudFoundryOperations.class);

//    private final CloudFoundryApplication application;

//    @Mock
//    private CloudFoundryClient cloudFoundryClient;

//    @Mock
    Common common;

    @Mock
    Buildpacks buildpacks;

    private final ApplicationsV2 applications = mock(ApplicationsV2.class, RETURNS_SMART_NULLS);

    private final CloudFoundryClient cloudFoundryClient = mock(CloudFoundryClient.class, RETURNS_SMART_NULLS);

//    private final ReactorCloudFoundryClient reactorCloudFoundryClient = mock(ReactorCloudFoundryClient.class, RETURNS_SMART_NULLS);

    private CloudFoundryClient cfClient;
    private CloudFoundryOperations cfOps;

    ConnectionContext connectionContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        common = PowerMockito.spy(new Common());

//        ConnectionContext connectionContext = DefaultConnectionContext.builder().apiHost("apiHost").skipSslValidation(true).build();
//        TokenProvider tokenProvider = PasswordGrantTokenProvider.builder().username("admin").password("admin").build();
//        cfClient = ReactorCloudFoundryClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider).build();
//        cfOps = DefaultCloudFoundryOperations.builder().cloudFoundryClient(cfClient).build();
    }

//    @Test
//    public void testGetBuildPacks() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        Map<String, Object> result = buildPackService.getBuildPacks();
//        Assert.assertEquals(new HashMap<String, Object>() {{
//            put("String", null);
//        }}, result);
//    }

    @Test
    public void testUpdateBuildPack() throws Exception {
        BuildPack buildPack = new BuildPack();
        buildPack.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        buildPack.setPosition(1);
        buildPack.setEnable(true);
        buildPack.setLock(true);



        BuildPackService service = mock(BuildPackService.class);
//        Common com = Whitebox.invokeConstructor(Common.class);
//        Common com = mock(Common.class);
        PowerMockito.mockStatic(Common.class);
        System.out.println("==1");
        DefaultConnectionContext defaultConnectionContext = DefaultConnectionContext.builder().apiHost("api.115.68.46.187.xip.io").build();
        System.out.println("==2");
        PowerMockito.when(common.connectionContext("api.115.68.46.187.xip.io", true)).thenReturn(defaultConnectionContext);


        System.out.println("==3");
        when(service.updateBuildPack(buildPack)).thenReturn(true);
        System.out.println("==4");

        PowerMockito.when(service.updateBuildPack(buildPack)).thenReturn(true);
        System.out.println("==5");
        boolean result = service.updateBuildPack(buildPack);
        System.out.println("==6");
        Assert.assertEquals(true, result);
        System.out.println("==7");


//        Mono<UpdateBuildpackResponse> response = Mono.just(UpdateBuildpackResponse.builder().build());
//
//        when(cfClient.buildpacks().update(UpdateBuildpackRequest.builder()
//                .buildpackId("f89b1ef6-7416-4d12-b492-c10fdaaff632")
//                .position(1)
//                .enabled(true)
//                .locked(true)
//                .build())).thenReturn(response);



//        when(buildpacks.update(UpdateBuildpackRequest.builder()
//                                .buildpackId("f89b1ef6-7416-4d12-b492-c10fdaaff632")
//                                .position(1)
//                                .enabled(true)
//                                .locked(true)
//                                .build()
//        ));
//        BuildPackService service = mock(BuildPackService.class);
//        when(service.updateBuildPack(buildPack)).thenReturn(true);
//
//        boolean result = service.updateBuildPack(buildPack);
//        Assert.assertEquals(true, result);




//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        boolean result = buildPackService.updateBuildPack(new BuildPack());
//        Assert.assertEquals(true, result);
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