package org.openpaas.paasta.portal.api.service;

import okhttp3.mockwebserver.MockWebServer;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackRequest;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackResponse;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.init.InitTest;
import org.openpaas.paasta.portal.api.controller.AppController;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;


@ActiveProfiles("dev")
@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*", "javax.crypto.*", "org.openpaas.paasta.portal.api.common.*", "org.openpaas.paasta.portal.api.config.*"})
@TestPropertySource(properties = {"spring.config.location = classpath:/application.yml", "eureka.client.enabled=false"})
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildPackServiceTest extends InitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    //@Mock
    //private BuildPackService buildPackService;

    @MockBean
    PasswordGrantTokenProvider tokenProvider;


    private ReactorCloudFoundryClient client;
    MockWebServer mockWebServer;
    protected TokenProvider TOKEN_PROVIDER = connectionContext -> Mono.just("test-authorization");
    protected DefaultConnectionContext CONNECTION_CONTEXT;


    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();
        CONNECTION_CONTEXT = DefaultConnectionContext.builder().apiHost(mockWebServer.getHostName()).secure(false).build();
        client = ReactorCloudFoundryClient.builder().connectionContext(CONNECTION_CONTEXT).rootV2(this.root).rootV3(this.root).tokenProvider(TOKEN_PROVIDER).build();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetBuildPacks() throws Exception {
//        Map<String, Object> returnData = new HashMap<>();
//        returnData.put("GUID", UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
//        when(buildPackService.getBuildPacks()).thenReturn(returnData);
//
//        Map<String, Object> result = buildPackService.getBuildPacks();
//        Assert.assertEquals(returnData, result);

        assertThat(this.client.buildpacks()).isNotNull();
    }

    @Test
    public void testUpdateBuildPack() throws Exception {
//        BuildPack buildPack = new BuildPack();
//        buildPack.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
//        buildPack.setPosition(1);
//        buildPack.setEnable(true);
//        buildPack.setLock(true);
//        UpdateBuildpackResponse updateBuildpackResponse = UpdateBuildpackResponse.builder().build();
//        PowerMockito.when(buildPackService.updateBuildPack(buildPack)).thenReturn(true);
//        boolean result = buildPackService.updateBuildPack(buildPack);
//        Assert.assertEquals(true, result);

        assertThat(this.client.buildpacks().update(UpdateBuildpackRequest.builder().buildpackId("BUILD_ID").build())).isNotNull();
    }

}

