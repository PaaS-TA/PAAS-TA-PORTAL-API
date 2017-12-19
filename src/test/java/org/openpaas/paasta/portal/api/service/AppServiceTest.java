package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CloudFoundryExceptionMatcher;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ApiApplication.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppServiceTest extends CommonTest {

    @Autowired
    private AppService appService;

    private static String token = "";
    private static String clientToken = "";
    private static UUID testAppGuid;
    private static Map testEnv = new HashMap();
    private static CustomCloudFoundryClient clientAdminCustom;
    private static CustomCloudFoundryClient client;
    private static CloudFoundryClient clientAdmin;

    public static String apiTarget;
    public static String clientUserName;
    public static String appTestOrg;
    public static String appTestSpace;
    public static String testApp;
    public static String nonexistentOrNoAuthOrg; /* 조직이 존재하지 않을때와 권한이 없을때의 결과가 같은 경우에 사용    */
    public static String nonexistentOrNoAuthSpace;
    public static String nonexistentApp;
    public static String domainName;
    public static String testHost;

    @BeforeClass
    public static void init() throws Exception {

        apiTarget = getPropertyValue("test.apiTarget");
        clientUserName = getPropertyValue("test.clientUserName");
        appTestOrg = getPropertyValue("test.appTestOrg");
        appTestSpace = getPropertyValue("test.appTestSpace");
        testApp = getPropertyValue("test.testApp");
        nonexistentOrNoAuthOrg = getPropertyValue("test.nonexistentOrNoAuthOrg");
        nonexistentOrNoAuthSpace = getPropertyValue("test.nonexistentOrNoAuthSpace");
        nonexistentApp = getPropertyValue("test.nonexistentApp");
        domainName = getPropertyValue("test.domainName");
        testHost = getPropertyValue("test.testHost");


        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        token = new CloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true).login().getValue();

        clientAdminCustom = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);
        clientAdminCustom.login();

        clientAdmin = new CloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);
        clientAdmin.login();


        CloudCredentials clientCredentials = new CloudCredentials(clientUserName, "1234");
        clientToken = new CustomCloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true).login().getValue();

        client = new CustomCloudFoundryClient(clientCredentials, getTargetURL(apiTarget), appTestOrg, appTestSpace, true);
        client.login();

        CloudApplication cloudApp = clientAdmin.getApplication(testApp);
        testAppGuid = cloudApp.getMeta().getGuid();

        //유저,조직,스페이스를 생성하고 app을 배포하여야 하는데, 현재는 유저 생성 및 app 배포를 할 방법이 없음.

        /*
        admin.setOrgRole(appTestOrg,clientUserName,"users");
        admin.setSpaceRole(appTestOrg,appTestSpace,clientUserName,"developers");


        client.createOrg(appTestOrg);
        client.createSpace(appTestOrg,appTestSpace);
        */

    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @AfterClass
    public static void testFinalize() throws Exception {
/*
        admin.deleteSpace(appTestOrg, appTestSpace);
       admin.deleteOrg(appTestOrg);
*/
    }


    @Test
    public void getAppSummary() throws Exception {

        App app = new App();
        app.setGuid(testAppGuid);
        String result;

        result = appService.getAppSummary(app, clientAdminCustom);

        assertTrue(result != null);

    }


    @Test
    @Ignore
    public void a_getAppStats() throws Exception {

        App app = new App();
        app.setGuid(testAppGuid);
        String result;

        result = appService.getAppStats(app, clientAdminCustom);

        assertTrue(result != null);
    }


    @Test
    public void getAppEvents() throws Exception {

        App app = new App();
        app.setGuid(testAppGuid);
        String result;

        result = appService.getAppEvents(app, clientAdminCustom);

        assertTrue(result != null);
    }


    @Test
    public void renameApp() throws Exception {

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);
        app.setNewName(testApp);

        appService.renameApp(app, clientAdmin);

    }


    @Test
    public void b_stopApp() throws Exception {

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);

        appService.stopApp(app, clientAdmin);
    }

    @Test
    public void z_startApp() throws Exception {

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);

        appService.startApp(app, clientAdmin);
    }


    @Test
    public void updateApp() throws Exception {

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);

        app.setInstances(1);
        appService.updateApp(app, clientAdmin);

        app.setInstances(0);
        app.setMemory(512);
        appService.updateApp(app, clientAdmin);

        app.setDiskQuota(1024);
        appService.updateApp(app, clientAdmin);
    }


    @Test
    public void restageApp() throws Exception {

        App app = new App();
        app.setGuid(testAppGuid);

        appService.restageApp(app, clientAdminCustom);

    }


    @Test
    public void getApplicationEnv_200() throws Exception {

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);

        Map result = appService.getApplicationEnv(app, clientToken);

        assertTrue(result.size() == 5);
    }

    @Test
    public void getApplicationEnv_400_EmptyBody() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        App app = new App();

        appService.getApplicationEnv(app, clientToken);
    }

    @Test
    public void getApplicationEnv_404_NonexistentApp() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.NOT_FOUND, "The app could not be found: env"));

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(nonexistentApp);

        appService.getApplicationEnv(app, clientToken);
    }

    @Test
    public void getApplicationEnv_400_InvalidOrgOrSpace() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "No matching organization and space found for org: " + nonexistentOrNoAuthOrg + " space: " + nonexistentOrNoAuthSpace);

        App app = new App();
        app.setOrgName(nonexistentOrNoAuthOrg);
        app.setSpaceName(nonexistentOrNoAuthSpace);
        app.setName(testApp);

        appService.getApplicationEnv(app, clientToken);

    }

    @Test
    public void updateApplicationEnv_200() throws Exception {

        testEnv.put("testEnv1", "testval1");

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);
        app.setEnvironment(testEnv);
        boolean result;

        result = appService.updateApplicationEnv(app, clientToken);

        assertTrue(result);
    }

    @Test
    public void updateApplicationEnv_400_EmptyBody() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        App app = new App();

        appService.updateApplicationEnv(app, clientToken);
    }

    @Test
    public void updateApplicationEnv_400_EmptyEnv() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);

        appService.updateApplicationEnv(app, clientToken);
    }

    @Test
    public void updateApplicationEnv_400_InvalidOrgOrSpace() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "No matching organization and space found for org: " + nonexistentOrNoAuthOrg + " space: " + nonexistentOrNoAuthSpace);

        App app = new App();
        app.setOrgName(nonexistentOrNoAuthOrg);
        app.setSpaceName(nonexistentOrNoAuthSpace);
        app.setName(testApp);
        app.setEnvironment(testEnv);

        appService.updateApplicationEnv(app, clientToken);
    }

    @Test
    public void updateApplicationEnv_404_NonexistentApp() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.NOT_FOUND, "Unknown request"));

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(nonexistentApp);
        app.setEnvironment(testEnv);

        appService.updateApplicationEnv(app, clientToken);
    }

    @Test
    public void addApplicationRoute_200() throws Exception {

        client.unbindRoute(testHost, domainName, testApp);

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);
        app.setDomainName(domainName);
        app.setHost(testHost);

        boolean result = appService.addApplicationRoute(app, clientToken);

        assertTrue(result);
    }

    @Test
    public void addApplicationRoute_400_EmptyBody() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        App app = new App();

        appService.addApplicationRoute(app, clientToken);
    }

    @Test
    public void addApplicationRoute_400_NonexistentOrgOrSpace() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "No matching organization and space found for org: " + nonexistentOrNoAuthOrg + " space: " + nonexistentOrNoAuthSpace);

        App app = new App();
        app.setOrgName(nonexistentOrNoAuthOrg);
        app.setSpaceName(nonexistentOrNoAuthSpace);
        app.setName(testApp);
        app.setDomainName(domainName);
        app.setHost(testHost);

        appService.addApplicationRoute(app, clientToken);
    }

    @Test
    public void addApplicationRoute_404_NonexistentApp() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.NOT_FOUND, "Unknown request"));

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(nonexistentApp);
        app.setDomainName(domainName);
        app.setHost(testHost);

        appService.addApplicationRoute(app, clientToken);
    }

    @Test
    public void removeApplicationRoute_200() throws Exception {

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(testApp);
        app.setDomainName(domainName);
        app.setHost(testHost);

        boolean result;
        try {
            result = appService.removeApplicationRoute(app, clientToken);
        } finally {
            client.bindRoute(testHost, domainName, testApp);
        }

        assertTrue(result);
    }

    @Test
    public void removeApplicationRoute_400_EmptyBody() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        App app = new App();

        appService.removeApplicationRoute(app, clientToken);
    }

    @Test
    public void removeApplicationRoute_400_NonexistentOrgOrSpace() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                "No matching organization and space found for org: " + nonexistentOrNoAuthOrg + " space: " + nonexistentOrNoAuthSpace);

        App app = new App();
        app.setOrgName(nonexistentOrNoAuthOrg);
        app.setSpaceName(nonexistentOrNoAuthSpace);
        app.setName(testApp);
        app.setDomainName(domainName);
        app.setHost(testHost);

        appService.removeApplicationRoute(app, clientToken);

    }

    @Test
    public void removeApplicationRoute_404_NonexistentApp() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.NOT_FOUND, "Unknown request"));

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(nonexistentApp);
        app.setDomainName(domainName);
        app.setHost(testHost);

        appService.removeApplicationRoute(app, clientToken);

    }

    @Test
    public void removeApplicationRoute_400_NonexistentRoute() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Host 'nonexistentRoute' not found for domain '" + domainName + "'.");

        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setName(nonexistentApp);
        app.setDomainName(domainName);
        app.setHost("nonexistentRoute");

        appService.removeApplicationRoute(app, clientToken);
    }

    @Test
    public void executeTerminateAppInstanceByIndex_200() throws Exception {
        App app = new App();
        app.setOrgName(appTestOrg);
        app.setSpaceName(appTestSpace);
        app.setGuid(testAppGuid);
        app.setAppInstanceIndex(0);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("cf-Authorization", clientToken);

        appService.executeTerminateAppInstanceByIndex(app, req);

    }
}
