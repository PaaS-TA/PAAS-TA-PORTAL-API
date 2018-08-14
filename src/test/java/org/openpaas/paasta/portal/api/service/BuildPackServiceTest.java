package org.openpaas.paasta.portal.api.service;

import okhttp3.mockwebserver.MockWebServer;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackRequest;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.init.InitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PrepareForTest(Common.class)
public class BuildPackServiceTest extends InitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildPackServiceTest.class);

    @InjectMocks
    private BuildPackService buildPackService;


    private ReactorCloudFoundryClient REACTOR_CLOUDFOUDRYCLINET;
    MockWebServer mockWebServer;
    protected TokenProvider TOKEN_PROVIDER = connectionContext -> Mono.just("test-authorization");
    protected DefaultConnectionContext CONNECTION_CONTEXT;




    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockWebServer = new MockWebServer();
        CONNECTION_CONTEXT = DefaultConnectionContext.builder().apiHost(mockWebServer.getHostName()).secure(false).build();
        REACTOR_CLOUDFOUDRYCLINET = ReactorCloudFoundryClient.builder().connectionContext(CONNECTION_CONTEXT).rootV2(this.root).rootV3(this.root).tokenProvider(TOKEN_PROVIDER).build();


    }

    @Test
    public void testGetBuildPacks() throws Exception {
//        Map<String, Object> returnData = new HashMap<>();
//        returnData.put("GUID", UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
//
//        when(buildPackService.connectionContext()).thenReturn(CONNECTION_CONTEXT);
//        when(buildPackService.tokenProvider(any(),any())).thenReturn((PasswordGrantTokenProvider)TOKEN_PROVIDER);
//        when(buildPackService.cloudFoundryClient(any(),any())).thenReturn(REACTOR_CLOUDFOUDRYCLINET);
//
//
//        when(buildPackService.getBuildPacks()).thenReturn(returnData);
//
//        assertThat(buildPackService.getBuildPacks()).isNotNull();

//        Map<String, Object> result = buildPackService.getBuildPacks();
//        Assert.assertEquals(returnData, result);

//        assertThat(this.REACTOR_CLOUDFOUDRYCLINET.buildpacks()).isNotNull();
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

        assertThat(this.REACTOR_CLOUDFOUDRYCLINET.buildpacks().update(UpdateBuildpackRequest.builder().buildpackId("BUILD_ID").build())).isNotNull();
    }

}
