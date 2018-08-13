package org.openpaas.paasta.portal.api.service;

import okhttp3.mockwebserver.MockWebServer;
import org.cloudfoundry.client.lib.org.codehaus.jackson.map.ObjectMapper;
import org.cloudfoundry.client.lib.org.codehaus.jackson.type.TypeReference;
import org.cloudfoundry.client.v2.OrderDirection;
import org.cloudfoundry.client.v2.applications.*;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackResponse;
import org.cloudfoundry.client.v2.events.ListEventsRequest;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.client.v2.routemappings.CreateRouteMappingRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteResponse;
import org.cloudfoundry.client.v2.routes.DeleteRouteRequest;
import org.cloudfoundry.client.v2.routes.Route;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.DeleteServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.DeleteServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebindings.ServiceBindingResource;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstanceServiceBindingsRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstanceServiceBindingsResponse;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.LogMessage;
import org.cloudfoundry.doppler.RecentLogsRequest;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.applications.LogsRequest;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
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
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppServiceTest extends TestConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @InjectMocks
    Common common;

    @Mock
    private AppService appService;


    String GUID = "f89b1ef6-7416-4d12-b492-c10fdaaff632";
    String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI1NmFjZGZmMzlmZDQ0ZTdiOTc0MGM1ZTMwNDI2MDFiMSIsInN1YiI6ImI4ZDkwZDQ3LWM5YzItNDdlZi1hOTE1LWQzOGI5MDk2OWFkNiIsInNjb3BlIjpbInVhYS51c2VyIl0sImNsaWVudF9pZCI6ImNmIiwiY2lkIjoiY2YiLCJhenAiOiJjZiIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfaWQiOiJiOGQ5MGQ0Ny1jOWMyLTQ3ZWYtYTkxNS1kMzhiOTA5NjlhZDYiLCJvcmlnaW4iOiJ1YWEiLCJ1c2VyX25hbWUiOiJtYXJpc3NhIiwiZW1haWwiOiJtYXJpc3NhQHRlc3Qub3JnIiwiYXV0aF90aW1lIjoxNTI5NjkwNDI2LCJyZXZfc2lnIjoiNGI1NzYzNWIiLCJpYXQiOjE1Mjk2OTA0MjYsImV4cCI6MTUyOTczMzYyNiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3VhYS9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsInVhYSJdfQ.erbXOVkGb0M2x9cEnP10nxfXY0iFENSurvo3tAKAzeE";

    App APP;

    @Before
    public void setUp() {
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

        List<String> urls = new ArrayList<>();
        urls.add("url1");
        APP.setUrls(urls);

        App.Staging staging = null;
        APP.setStaging(staging);

        Map env = new HashMap();
        APP.setEnvironment(env);

        List<String> services = new ArrayList<>();
        services.add("service1");
        APP.setServices(services);


    }


    @Test
    public void testGetAppSummary() {
        SummaryApplicationResponse summaryApplicationResponse = SummaryApplicationResponse.builder().buildpack("buildpacks").build();
        PowerMockito.when(appService.getAppSummary(anyString(), anyString())).thenReturn(summaryApplicationResponse);
        SummaryApplicationResponse result = appService.getAppSummary(GUID, TOKEN);
        Assert.assertEquals(summaryApplicationResponse.getBuildpack(), result.getBuildpack());
    }

    @Test
    public void testGetAppStats() {
        ApplicationStatisticsResponse applicationStatisticsResponse = ApplicationStatisticsResponse.builder().build();
        PowerMockito.when(appService.getAppStats(anyString(), anyString())).thenReturn(applicationStatisticsResponse);
        ApplicationStatisticsResponse result = appService.getAppStats(GUID, TOKEN);
        Assert.assertEquals(applicationStatisticsResponse, result);
    }

    @Test
    public void testRenameApp() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.renameApp(any(), anyString())).thenReturn(map);

        Map result = appService.renameApp(APP, TOKEN);
        Assert.assertEquals(map, result);


    }

    @Test
    public void testStartApp() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.startApp(any(), anyString())).thenReturn(map);

        Map result = appService.startApp(APP, TOKEN);
        Assert.assertEquals(map, result);

    }

    @Test
    public void testStopApp() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.stopApp(any(), anyString())).thenReturn(map);

        Map result = appService.stopApp(APP, TOKEN);
        Assert.assertEquals(map, result);
    }

    @Test
    public void testDeleteApp() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.deleteApp(anyString())).thenReturn(map);

        Map result = appService.deleteApp(GUID);
        Assert.assertEquals(map, result);
    }

    @Test
    public void testRestageApp() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.restageApp(any(), anyString())).thenReturn(map);

        Map result = appService.restageApp(APP, TOKEN);
        Assert.assertEquals(map, result);
    }

    @Test
    public void testUpdateApp() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.updateApp(any(), anyString())).thenReturn(map);

        Map result = appService.updateApp(APP, TOKEN);
        Assert.assertEquals(map, result);
    }

    @Test
    public void testBindService() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.bindService(anyMap(), anyString())).thenReturn(map);

        Map data = new HashMap();
        map.put("parameter", "{name:name}");
        map.put("applicationId", "applicationId");
        map.put("serviceInstanceId", "serviceInstanceId");
        Map result = appService.bindService(data, TOKEN);
        Assert.assertEquals(map, result);
    }

    @Test
    public void testUnbindService() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.unbindService(anyString(), anyString(), anyString())).thenReturn(map);

        Map result = appService.unbindService("serviceInstanceId", "applicationId", TOKEN);
        Assert.assertEquals(map, result);
    }

    @Test
    public void getAppEvents() {
        ListEventsResponse listEventsResponse = ListEventsResponse.builder().build();
        PowerMockito.when(appService.getAppEvents(anyString(), anyString())).thenReturn(listEventsResponse);

        ListEventsResponse result = appService.getAppEvents(GUID, TOKEN);

        Assert.assertEquals(listEventsResponse, result);
    }

    @Test
    public void testGetApplicationEnv() {
        ApplicationEnvironmentResponse applicationEnvironmentResponse = ApplicationEnvironmentResponse.builder().build();
        PowerMockito.when(appService.getApplicationEnv(anyString(), anyString())).thenReturn(applicationEnvironmentResponse);

        ApplicationEnvironmentResponse result = appService.getApplicationEnv(GUID, TOKEN);
        Assert.assertEquals(applicationEnvironmentResponse, result);
    }

    @Test
    public void testAddApplicationRoute() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.addApplicationRoute(anyMap(), anyString())).thenReturn(map);

        Map data = new HashMap();
        data.put("host", "host");
        data.put("domainId", "domainId");
        data.put("spaceId", "spaceId");
        data.put("applicationId", "applicationId");

        Map result = appService.addApplicationRoute(data, TOKEN);
        Assert.assertEquals(map, result);
    }

    @Test
    public void testRemoveApplicationRoute() {
        Map map = new HashMap();
        map.put("result", true);
        map.put("msg", "You have successfully completed the task.");
        PowerMockito.when(appService.removeApplicationRoute(anyString(), anyString(), anyString())).thenReturn(map);

        Map result = appService.removeApplicationRoute(GUID, GUID, TOKEN);
        Assert.assertEquals(map, result);
    }

    @Test
    public void testTerminateInstance() {
        Map map = new HashMap();
        map.put("result", true);
        PowerMockito.when(appService.terminateInstance(anyString(),anyString(),anyString())).thenReturn(map);

        Map result = appService.terminateInstance(GUID,"1",TOKEN);
        Assert.assertEquals(map, result);
    }


    @Test
    public void testGetRecentLog() {
        List<Envelope> envelopes = new ArrayList<>();
        PowerMockito.when(appService.getRecentLog(anyString(),anyString())).thenReturn(envelopes);

        List<Envelope> result = appService.getRecentLog(GUID,TOKEN);
        Assert.assertEquals(envelopes, result);
    }

    @Test
    public void testGetTailLog() {
        List<LogMessage> logMessages = new ArrayList<>();
        PowerMockito.when(appService.getTailLog(anyString(),anyString())).thenReturn(logMessages);

        List<LogMessage> result = appService.getTailLog(GUID,TOKEN);
        Assert.assertEquals(logMessages, result);
    }

}

