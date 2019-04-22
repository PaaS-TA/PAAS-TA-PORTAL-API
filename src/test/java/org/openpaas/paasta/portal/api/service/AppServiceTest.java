package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.applications.ApplicationEnvironmentResponse;
import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.applications.SummaryApplicationResponse;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.LogMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.controller.AppControllerV2;
import org.openpaas.paasta.portal.api.model.App;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.Matchers.*;


@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV2.class);

    @Mock
    private AppServiceV2 appServiceV2;


    App APP;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        setApp();
        MockitoAnnotations.initMocks(this);
    }

    public void setApp() {
        APP = new App();
        APP.setAppInstanceIndex(1);
        APP.setBuildPack("buildpack");
        APP.setUpdatedAt("20180813");
        APP.setCreatedAt("20180813");
        APP.setDiskQuota(1024);
        APP.setDomainName("xxx.xxx.xxx.xxx.xip.io");
        APP.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        APP.setHost("APP");
        APP.setInstances(1);
        APP.setMemory(1024);
        APP.setName("APP");
        APP.setNewName("NEW_APP");
        APP.setOrgName("ORG");
        APP.setServiceGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        APP.setServiceName("SERVICE");
        APP.setServiceNewName("NEW_SERVICE");
        APP.setSpaceName("SPACE");
        APP.setStackName("STACK");
        APP.setState("STATE");
        APP.setTotalUserCount(1);

        APP.setUrls(Arrays.<String>asList("url"));

        APP.setStaging(null);

        Map env = new HashMap();
        APP.setEnvironment(env);

        APP.setServices(Arrays.<String>asList("service"));


    }


    @Test
    public void testGetAppSummary() {
        SummaryApplicationResponse summaryApplicationResponse = SummaryApplicationResponse.builder().buildpack("buildpacks").build();
        PowerMockito.when(appServiceV2.getAppSummary(anyString(), anyString())).thenReturn(summaryApplicationResponse);
        SummaryApplicationResponse result = appServiceV2.getAppSummary("guid", "token");
        Assert.assertEquals(summaryApplicationResponse.getBuildpack(), result.getBuildpack());
    }

    @Test
    public void testGetAppStats() {
        ApplicationStatisticsResponse applicationStatisticsResponse = ApplicationStatisticsResponse.builder().build();
        PowerMockito.when(appServiceV2.getAppStats(anyString(), anyString())).thenReturn(applicationStatisticsResponse);
        ApplicationStatisticsResponse result = appServiceV2.getAppStats("guid", "token");
        Assert.assertEquals(applicationStatisticsResponse, result);
    }

    @Test
    public void testRenameApp() {

        PowerMockito.when(appServiceV2.renameApp(any(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.renameApp(APP, "token");
        Assert.assertEquals(thenReturn, result);


    }

    @Test
    public void testStartApp() {
        PowerMockito.when(appServiceV2.startApp(any(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.startApp(APP, "token");
        Assert.assertEquals(thenReturn, result);

    }

    @Test
    public void testStopApp() {
        PowerMockito.when(appServiceV2.stopApp(any(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.stopApp(APP, "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testDeleteApp() {
        PowerMockito.when(appServiceV2.deleteApp(anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.deleteApp("token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testRestageApp() {
        PowerMockito.when(appServiceV2.restageApp(any(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.restageApp(APP, "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testUpdateApp() {
        PowerMockito.when(appServiceV2.updateApp(any(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.updateApp(APP, "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testBindService() {
        PowerMockito.when(appServiceV2.bindService(anyMap(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.bindService(new HashMap() {{
            put("parameter","{name:name}");
            put("applicationId","applicationId");
            put("serviceInstanceId","serviceInstanceId");
        }}, "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testUnbindService() {
        PowerMockito.when(appServiceV2.unbindService(anyString(), anyString(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.unbindService("serviceInstanceId", "applicationId", "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void getAppEvents() {
        ListEventsResponse listEventsResponse = ListEventsResponse.builder().build();
        PowerMockito.when(appServiceV2.getAppEvents(anyString(), anyString())).thenReturn(listEventsResponse);

        ListEventsResponse result = appServiceV2.getAppEvents("guid", "token");

        Assert.assertEquals(listEventsResponse, result);
    }

    @Test
    public void testGetApplicationEnv() {
        ApplicationEnvironmentResponse applicationEnvironmentResponse = ApplicationEnvironmentResponse.builder().build();
        PowerMockito.when(appServiceV2.getApplicationEnv(anyString(), anyString())).thenReturn(applicationEnvironmentResponse);

        ApplicationEnvironmentResponse result = appServiceV2.getApplicationEnv("guid", "token");
        Assert.assertEquals(applicationEnvironmentResponse, result);
    }

    @Test
    public void testAddApplicationRoute() {
        PowerMockito.when(appServiceV2.addApplicationRoute(anyMap(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.addApplicationRoute(new HashMap(){{
            put("host", "host");
            put("domainId", "domainId");
            put("spaceId", "spaceId");
            put("applicationId", "applicationId");
        }},"token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testRemoveApplicationRoute() {
        PowerMockito.when(appServiceV2.removeApplicationRoute(anyString(), anyString(), anyString())).thenReturn(thenReturn);

        Map result = appServiceV2.removeApplicationRoute("guid", "route_guid","token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testTerminateInstance() {
        Map map = new HashMap();
        map.put("result", true);
        PowerMockito.when(appServiceV2.terminateInstance(anyString(), anyString(), anyString())).thenReturn(map);

        Map result = appServiceV2.terminateInstance("goid", "1", "token");
        Assert.assertEquals(map, result);
    }


    @Test
    public void testGetRecentLog() {
        List<Envelope> envelopes = new ArrayList<>();
        PowerMockito.when(appServiceV2.getRecentLog(anyString(), anyString())).thenReturn(envelopes);

        List<Envelope> result = appServiceV2.getRecentLog("guid", "token");
        Assert.assertEquals(envelopes, result);
    }

    @Test
    public void testGetTailLog() {
        List<LogMessage> logMessages = new ArrayList<>();
        PowerMockito.when(appServiceV2.getTailLog(anyString(), anyString())).thenReturn(logMessages);

        List<LogMessage> result = appServiceV2.getTailLog("guid", "token");
        Assert.assertEquals(logMessages, result);
    }

}

