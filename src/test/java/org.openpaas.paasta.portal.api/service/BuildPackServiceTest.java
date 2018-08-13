package org.openpaas.paasta.portal.api.service;

import okhttp3.mockwebserver.MockWebServer;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackResponse;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.config.TestConfig;
import org.openpaas.paasta.portal.api.controller.AppController;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;


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

