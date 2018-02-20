package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.App;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;


/**
 * 외부에서 들어오는 요청에 관련된 서비스에 대한 앱 테스트 케이스이다.
 */

/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ApiApplication.class})
@WebAppConfiguration
public class ExternalAppServiceTest  extends CommonTest {
    private final ExternalAppService externalAppService;

    @Autowired
    public ExternalAppServiceTest() {
    }

    @Autowired
    public ExternalAppServiceTest(ExternalAppService externalAppService){
        this.externalAppService = externalAppService;
    }

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
    public static String nonexistentOrNoAuthOrg; */
/* 조직이 존재하지 않을때와 권한이 없을때의 결과가 같은 경우에 사용    *//*

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
        testApp = "hello-spring";//getPropertyValue("test.testApp");
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

        */
/*
        admin.setOrgRole(appTestOrg,clientUserName,"users");
        admin.setSpaceRole(appTestOrg,appTestSpace,clientUserName,"developers");


        client.createOrg(appTestOrg);
        client.createSpace(appTestOrg,appTestSpace);
        *//*


    }


    @Test
    public void getAppInfo() throws Exception {

        App app = new App();
        externalAppService.getAppInfo(app);

    }
    @Test
    public void callUpdategAppInfo() throws Exception {
        Map map = new HashMap();
        externalAppService.callUpdategAppInfo();

    }
}*/
