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
    }

    @Test
    public void testGetBuildPacks() throws Exception {
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("GUID", UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        when(buildPackService.getBuildPacks()).thenReturn(returnData);

        Map<String, Object> result = buildPackService.getBuildPacks();
        Assert.assertEquals(returnData, result);
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

}

