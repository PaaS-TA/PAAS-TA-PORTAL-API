package org.openpaas.paasta.portal.api.service;

import org.apache.commons.collections.map.HashedMap;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CloudFoundryExceptionMatcher;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.Org;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrgServiceTest extends CommonTest {

    @Autowired
    private OrgService orgService;

    private static String adminToken = "";
    private static String clientToken = "";
    private static String clientUserGuid;
    private static String noOrgClientToken = "";

    private static CustomCloudFoundryClient admin;
    private static CustomCloudFoundryClient client;

    public static String apiTarget;
    public static String clientUserName;
    public static String orgTestOrg;
    public static String appTestOrg;
    public static String testSpace;
    public static String nonexistentOrNoAuthOrg; /* 조직이 존재하지 않을때와 권한이 없을때의 결과가 같은 경우에 사용    */
    public static String noAuthTestOrg;
    public static String nonexistentOrg;
    public static String createTestOrg;
    public static String noOrgClientUserName;
    public static String inviteId;
    public static String userId;
    public static String orgRoleUser;
    public static String orgRoleUserGuid;

    @BeforeClass
    public static void init() throws Exception {

        apiTarget = getPropertyValue("test.apiTarget");
        noOrgClientUserName = getPropertyValue("test.noOrgClientUserName");
        orgTestOrg = getPropertyValue("test.orgTestOrg");
        appTestOrg = getPropertyValue("test.appTestOrg");
        clientUserName = getPropertyValue("test.clientUserName");
        testSpace = getPropertyValue("test.testSpace");
        noAuthTestOrg = getPropertyValue("test.noAuthTestOrg");
        nonexistentOrg = getPropertyValue("test.nonexistentOrg");
        nonexistentOrNoAuthOrg = getPropertyValue("test.nonexistentOrNoAuthOrg");
        createTestOrg = getPropertyValue("test.createTestOrg");
        inviteId = getPropertyValue("test.inviteId");
        userId = getPropertyValue("test.userId");
        orgRoleUser = getPropertyValue("test.org-role-user");
        orgRoleUserGuid = getPropertyValue("test.org-role-user-guid");


        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        adminToken = new CloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true).login().getValue();
        admin = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);

        //CloudCredentials clientCredentials = new CloudCredentials(clientUserName, "1234");
        CloudCredentials clientCredentials = new CloudCredentials(getPropertyValue("test.clientUserName"), getPropertyValue("test.clientUserPassword"));
        clientToken = new CloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true).login().getValue();
        client = new CustomCloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true);
        clientUserGuid = client.getUserGuid();

        CloudCredentials noOrgClientCredentials = new CloudCredentials(noOrgClientUserName, getPropertyValue("test.clientUserPassword"));
        noOrgClientToken = new CloudFoundryClient(noOrgClientCredentials, getTargetURL(apiTarget), true).login().getValue();

        //유저생성을 추가해야하나 아직 유저 생성 기능이 없음.
        client.createOrg(orgTestOrg);
        admin.createOrg(noAuthTestOrg);
    }


    @AfterClass
    public static void testFinalize() throws Exception {
        admin.deleteOrg(orgTestOrg);
        admin.deleteOrg(noAuthTestOrg);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getOrgSummary_200() throws Exception {

        Org org = new Org();
        org.setOrgName(appTestOrg);
        Org result;

        try {
            result = orgService.getOrgSummary(org, clientToken);
        } finally {

        }
        assertTrue(result.getGuid() != null);
    }

    @Test
    public void getOrgSummary_400_NonexistentAndNoAuthOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrNoAuthOrg + "' not found.");

        Org org = new Org();
        org.setOrgName(nonexistentOrNoAuthOrg);
        Org result;

        try {
            result = orgService.getOrgSummary(org, clientToken);
        } finally {

        }
    }


    @Test
    public void getOrgByName_200() throws Exception {

        Org org = new Org();
        org.setOrgName(orgTestOrg);
        CloudOrganization result;

        try {
            result = orgService.getOrgByName(org, clientToken);
        } finally {

        }
        assertTrue(result.getMeta().getGuid() != null);
    }


    @Test
    public void getOrgByName_400_NonexistentAndNoAuthOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrNoAuthOrg + "' not found.");

        Org org = new Org();
        org.setOrgName(nonexistentOrNoAuthOrg);
        CloudOrganization result;

        try {
            result = orgService.getOrgByName(org, clientToken);
        } finally {

        }
    }


    @Test
    public void renameOrg_200() throws Exception {

        Org org = new Org();
        org.setOrgName(orgTestOrg);
        org.setNewOrgName("junit-org-test-org-renamed");
        boolean result;

        try {
            result = orgService.renameOrg(org, clientToken);
        } finally {
            client.renameOrg("junit-org-test-org-renamed", orgTestOrg);
        }
        assertTrue(result);
    }


    @Test
    public void renameOrg_400_NonexistentAndNoAuthOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrNoAuthOrg + "' not found.");

        Org org = new Org();
        org.setOrgName(nonexistentOrNoAuthOrg);
        org.setNewOrgName(nonexistentOrNoAuthOrg);
        boolean result;

        try {
            result = orgService.renameOrg(org, clientToken);
        } finally {

        }
    }

    @Test
    public void deleteOrg() throws Exception {

        client.createOrg("delete_test_org");

        Org org = new Org();
        org.setOrgName("delete_test_org");
        boolean result;

        try {
            result = orgService.deleteOrg(org, clientToken);
        } finally {

        }

        assertTrue(result);
    }


    @Test
    public void deleteOrg_403__NonexistentAndNoAuthOrg() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.FORBIDDEN, "You are not authorized to perform the requested action."));

        Org org = new Org();
        org.setOrgName(nonexistentOrNoAuthOrg);
        boolean result;

        try {
            result = orgService.deleteOrg(org, clientToken);
        } finally {

        }
    }


    @Test
    public void getOrgs_200() throws Exception {

        List<CloudOrganization> result;

        try {
            result = orgService.getOrgs(clientToken);
        } finally {

        }

        assertTrue(result.size() > 0);
    }

    @Test
    public void getOrgs_204_NoOrg() throws Exception {

        List<CloudOrganization> result;

        try {
            result = orgService.getOrgs(noOrgClientToken);
        } finally {

        }

        assertTrue(result.size() == 0);
    }


    @Test
    public void createOrg_200() throws Exception {

        Org org = new Org();
        org.setNewOrgName(createTestOrg);
        boolean result;

        try {
            result = orgService.createOrg(org, clientToken);
        } finally {
            admin.deleteOrg(createTestOrg);
        }

        assertTrue(result);
    }

    @Test
    public void createOrg_200_Admin() throws Exception {

        Org org = new Org();
        org.setNewOrgName(createTestOrg);
        boolean result;

        try {
            result = orgService.createOrg(org, adminToken);
        } finally {
            admin.deleteOrg(createTestOrg);
        }

        assertTrue(result);
    }


    @Test
    public void createOrg_400_DuplicatedOrgName() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "The organization name is taken: " + orgTestOrg));

        Org org = new Org();
        org.setNewOrgName(orgTestOrg);
        boolean result;

        try {
            result = orgService.createOrg(org, clientToken);
        } finally {

        }
    }

    @Test
    public void createOrg_400_EmptyBody() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Org org = new Org();
        boolean result;

        try {
            result = orgService.createOrg(org, clientToken);
        } finally {

        }
    }


    @Test
    public void removeUserFromOrg_200() throws Exception {

        Org org = new Org();
        org.setOrgName(orgTestOrg);
        boolean result;
        client.createSpace(orgTestOrg, "test-space");
        try {
            result = orgService.removeUserFromOrg(org, clientToken);
        } finally {
            admin.deleteSpace(orgTestOrg, "test-space");
            admin.setOrgRole(orgTestOrg, clientUserName, "managers");
        }
        assertTrue(result);
    }

    @Test
    public void removeUserFromOrg_400_NonexistentOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrg + "' not found.");

        Org org = new Org();
        org.setOrgName(nonexistentOrg);
        boolean result;

        try {
            result = orgService.removeUserFromOrg(org, clientToken);
        } finally {

        }
    }

    @Test
    public void removeUserFromOrg_200_NoAuthOrg() throws Exception {

        Org org = new Org();
        org.setOrgName(noAuthTestOrg);
        boolean result;

        try {
            result = orgService.removeUserFromOrg(org, clientToken);
        } finally {

        }
        assertTrue(result);
    }

    @Test
    public void removeUserFromOrg_400_EmptyBody() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Org org = new Org();
        orgService.removeUserFromOrg(org, clientToken);
    }

    @Test
    public void setOrgRole() throws Exception {

        boolean result;
        try {
            result = orgService.setOrgRole(orgTestOrg, clientUserName, "OrgAuditor", clientToken);
        } finally {
            orgService.unsetOrgRole(orgTestOrg, clientUserGuid, "OrgAuditor", clientToken);
        }
        assertTrue(result);
    }

    @Test
    public void setOrgRole_EmptyParam() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        orgService.setOrgRole("", "", "", clientToken);
    }

    @Test
    public void setOrgRole_NonexistentOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrg + "' not found.");

        orgService.setOrgRole(nonexistentOrg, clientUserName, "OrgAuditor", clientToken);
    }

    @Test
    public void setOrgRole_NoAuthOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + noAuthTestOrg + "' not found.");

        orgService.setOrgRole(noAuthTestOrg, clientUserName, "OrgAuditor", clientToken);
    }

    @Test
    public void setOrgRole_InvalidRole() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Invalid userRole."));

        orgService.setOrgRole(orgTestOrg, clientUserName, "InvalidRole", clientToken);
    }

    @Test
    public void getAllUsers_Ok() throws Exception {
        List<Map<String, Object>> result = orgService.getAllUsers(orgTestOrg, clientToken);
        assertTrue(result.size() > 0);
    }

    @Test
    public void getAllUsers_NonexistentOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrg + "' not found.");

        orgService.getAllUsers(nonexistentOrg, clientToken);
    }

    @Test
    public void getUsersForOrgRole_Ok() throws Exception {
        List<Map<String, Object>> userList = new ArrayList<>();
        Map<String, Object> userMap = new HashedMap();
        userMap.put("userName", clientUserName);
        userList.add(userMap);
        List<Map<String, Object>> result = orgService.getUsersForOrgRole(orgTestOrg, userList, clientToken);
        assertTrue(result.size() > 0);
    }

    @Test
    public void getUsersForOrgRole_NonexistentOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrg + "' not found.");

        List<Map<String, Object>> userList = new ArrayList<>();
        Map<String, Object> userMap = new HashedMap();
        userMap.put("userName", clientUserName);
        userList.add(userMap);

        orgService.getUsersForOrgRole(nonexistentOrg, userList, clientToken);
    }


    @Test
    @Ignore
    public void getOrgMember() throws Exception {

        Map map = new HashMap();
        List listOrg = new ArrayList();
        List org = new ArrayList();
        List listSpace = new ArrayList();
        List spaces = new ArrayList();

        org.add("OCP");
        org.add(true);
        org.add(false);
        org.add(true);
        listOrg.add(org);

        spaces.add("dev");
        spaces.add(true);
        spaces.add(false);
        spaces.add(true);
        listSpace.add(spaces);

        spaces = new ArrayList();

        spaces.add("stg");
        spaces.add(true);
        spaces.add(false);
        spaces.add(true);
        listSpace.add(spaces);

        map.put("listOrg", listOrg);
        map.put("listSpace", listSpace);
        map.put("userId", orgRoleUser);

        orgService.setOrgSpaceUser(map);
        map.put("userId", orgRoleUserGuid);

    }

    @Test
    @Ignore
    public void addOrgMember() throws Exception {

        Map map = new HashMap();
        List listOrg = new ArrayList();
        List org = new ArrayList();
        List listSpace = new ArrayList();
        List spaces = new ArrayList();

        org.add("OCP");
        org.add(true);
        org.add(false);
        org.add(true);
        listOrg.add(org);

        spaces.add("dev");
        spaces.add(true);
        spaces.add(false);
        spaces.add(true);
        listSpace.add(spaces);

        spaces = new ArrayList();

        spaces.add("stg");
        spaces.add(true);
        spaces.add(false);
        spaces.add(true);
        listSpace.add(spaces);

        map.put("listOrg", listOrg);
        map.put("listSpace", listSpace);
        map.put("userId", orgRoleUser);

        orgService.setOrgSpaceUser(map);
        map.put("userId", orgRoleUserGuid);

    }

    @Test
    @Ignore
    public void deleteOrgMember() throws Exception {

        Map map = new HashMap();
        List listOrg = new ArrayList();
        List org = new ArrayList();
        List listSpace = new ArrayList();
        List spaces = new ArrayList();

        org.add("OCP");
        org.add(true);
        org.add(false);
        org.add(true);
        listOrg.add(org);

        spaces.add("dev");
        spaces.add(true);
        spaces.add(false);
        spaces.add(true);
        listSpace.add(spaces);

        spaces = new ArrayList();

        spaces.add("stg");
        spaces.add(true);
        spaces.add(false);
        spaces.add(true);
        listSpace.add(spaces);

        map.put("listOrg", listOrg);
        map.put("listSpace", listSpace);
        map.put("userId", orgRoleUserGuid);
        orgService.unsetOrgSpaceUser(map);

    }

    @Test
    public void inviteMemberEmail() throws Exception {

        Map<String, Object> userMap = new HashedMap();
        userMap.put("userId", userId);
        userMap.put("inviteUserId", inviteId);
        List dataList = new ArrayList();
        dataList.add(Arrays.asList("org", orgTestOrg, true, true, true));
        dataList.add(Arrays.asList("space", testSpace, true, false, true));
        userMap.put("dataList", dataList);

        orgService.inviteMemberEmail(userMap);

    }

    @Test
    public void getOrgId() throws Exception {
        int id = orgService.getOrgId(appTestOrg);
    }

    @Test
    public void updateInviteY() throws Exception {
        int cnt = orgService.updateInviteY("ARFTCJO9");
        assertTrue(cnt == 0);
    }

    @Test
    public void updateAccessCnt() throws Exception {
        int cnt = orgService.updateAccessCnt("ARFTCJO9", 0);
        assertTrue(cnt == 0);
    }


    @Test
    public void selectInviteInfo() throws Exception {
        List map = orgService.selectInviteInfo("ARFTCJO9");
        assertTrue(map.size() == 0);
        System.out.print("map::" + map.toString());
    }


    @Test
    public void getUsersByInvite() throws Exception {
        List map = orgService.getUsersByInvite("AAA", inviteId, "0");
        assertTrue(map.size() == 0);
        System.out.print("map::" + map.toString());
    }

    @Test
    public void cancelInvite() throws Exception {
        Map<String, Object> userMap = new HashedMap();
        userMap.put("token", "ARFTCJO9");
        int cnt = orgService.cancelInvite(userMap);
        assertTrue(cnt == 0);
    }

    @Test
    @Ignore
    public void unsetUserOrg() throws Exception {
        Map<String, Object> body = new HashedMap();
        body.put("orgName", "jasmin_ORG");
        body.put("userGuid", "af1b2963-07c3-4942-b78f-5c5625f3daa3");
        orgService.unsetUserOrg(body, adminToken);
    }


}