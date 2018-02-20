package org.openpaas.paasta.portal.api.service;

import org.apache.commons.collections.map.HashedMap;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openpaas.paasta.portal.api.common.CloudFoundryExceptionMatcher;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by Dojun on 2016-05-31.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class SpaceServiceTest extends CommonTest {

    @Autowired
    private SpaceService spaceService;
    @Autowired
    private OrgService orgService;
    private static String adminToken = "";
    private static String clientToken = "";
    private static String clientUserGuid;
    private static CustomCloudFoundryClient admin;
    private static CustomCloudFoundryClient client;


    private static String apiTarget;
    private static String clientUserName = "";
    private static String adminUserName = "";
    private static String testOrg = "";
    private static String noAuthSpaceTestOrg = "";
    private static String noContentTestOrg = "";
    private static String testSpace = "";
    private static String createTestSpace = "";
    private static String noAuthTestSpace = "";
    //스페이스가 존재하지 않을때와 권한이 없을때의 결과가 같은 경우에 사용
    private static String nonexistentOrNoAuthSpace = "";
    //조직이 존재하지 않을때와 권한이 없을때의 결과가 같은 경우에 사용
    private static String nonexistentOrNoAuthOrg = "";

    @BeforeClass
    public static void init() throws Exception {

        apiTarget = getPropertyValue("test.apiTarget");
        clientUserName = getPropertyValue("test.clientUserName");
        adminUserName = getPropertyValue("test.adminUserName");
        testOrg = getPropertyValue("test.testOrg");
        testSpace = getPropertyValue("test.testSpace");
        noAuthSpaceTestOrg = getPropertyValue("test.noAuthSpaceTestOrg");
        nonexistentOrNoAuthOrg = getPropertyValue("test.nonexistentOrNoAuthOrg");
        createTestSpace = getPropertyValue("test.createTestSpace");
        noAuthTestSpace = getPropertyValue("test.noAuthTestSpace");
        nonexistentOrNoAuthSpace = getPropertyValue("test.nonexistentOrNoAuthSpace");
        noContentTestOrg = getPropertyValue("test.noContentTestOrg");


        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        adminToken = new CloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true).login().getValue();
        admin = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);

        CloudCredentials clientCredentials = new CloudCredentials(clientUserName,  getPropertyValue("test.clientUserPassword"));
        clientToken = new CloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true).login().getValue();
        client = new CustomCloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true);
        clientUserGuid = client.getUserGuid();

        //유저생성을 추가해야하나 아직 유저 생성 기능이 없음.
        client.createOrg(testOrg);
        client.createSpace(testOrg, testSpace);
        admin.createSpace(testOrg, noAuthTestSpace);

        admin.createOrg(noAuthSpaceTestOrg);
        admin.createSpace(noAuthSpaceTestOrg, noAuthTestSpace);

        client.createOrg(noContentTestOrg);
    }

    @AfterClass
    public static void testFinalize() throws Exception {

        //조직 삭제 이전에 해당 조직의 스페이스를 모두 삭제하여야 함
        //조직 삭제는 admin으로만 가능
        client.deleteSpace(testOrg, testSpace);
        admin.deleteSpace(testOrg, noAuthTestSpace);
        admin.deleteOrg(testOrg);

        admin.deleteSpace(noAuthSpaceTestOrg, noAuthTestSpace);
        admin.deleteOrg(noAuthSpaceTestOrg);

        admin.deleteOrg(noContentTestOrg);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void getSpaces_200() throws Exception {
        Org org = new Org();
        org.setOrgName(testOrg);

        List result = spaceService.getSpaces(org, clientToken);
        assertTrue(result.size() > 0);
    }

    @Test
    public void getSpaces_400_EmptyParam() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Org org = new Org();
        spaceService.getSpaces(org, clientToken);
    }

    @Test
    public void getSpaces_204_NonexistentOrNoAuthOrg() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.NO_CONTENT, "Space not found"));

        Org org = new Org();
        org.setOrgName(nonexistentOrNoAuthOrg);
        spaceService.getSpaces(org, clientToken);
    }


    @Test
    public void getSpaces_204_NoSpace() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.NO_CONTENT, "Space not found"));

        Org org = new Org();
        org.setOrgName(noContentTestOrg);
        spaceService.getSpaces(org, clientToken);
    }

    @Test
    public void createSpace_200() throws Exception {
        Space space = new Space();
        space.setOrgName(testOrg);
        space.setNewSpaceName(createTestSpace);
        boolean result;

        try {
            result = spaceService.createSpace(space, clientToken);
        } finally {
            admin.deleteSpace(testOrg, createTestSpace);
        }

        assertTrue(result);
    }

    @Test
    public void createSpace_409_DuplicatedSpace() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.CONFLICT, "Space name already exists"));

        Space space = new Space();
        space.setOrgName(testOrg);
        space.setNewSpaceName(testSpace);

        spaceService.createSpace(space, clientToken);
    }

    @Test
    public void createSpace_400_EmptyParam() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Space space = new Space();

        spaceService.createSpace(space, clientToken);
    }

    @Test
    public void renameSpace_200() throws Exception {
        Space space = new Space();
        space.setOrgName(testOrg);
        space.setSpaceName(testSpace);
        space.setNewSpaceName("junit-test-space-renamed");
        boolean result;

        try {
            result = spaceService.renameSpace(space, clientToken);
        } finally {
            client.renameSpace(testOrg, "junit-test-space-renamed", testSpace);
        }

        assertTrue(result);

    }

    @Test
    public void renameSpace_400_InvalidOrgOrSpace() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrNoAuthOrg + "' not found.");

        Space space = new Space();
        space.setOrgName(nonexistentOrNoAuthOrg);
        space.setSpaceName(nonexistentOrNoAuthSpace);
        space.setNewSpaceName("junit-test-space-renamed");

        spaceService.renameSpace(space, clientToken);
    }

    @Test
    public void renameSpace_403_NoAuthSpace() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.FORBIDDEN, "You are not authorized to perform the requested action"));

        admin.setOrgRole(noAuthSpaceTestOrg, clientUserName, "users");
        admin.setSpaceRole(noAuthSpaceTestOrg, noAuthTestSpace, clientUserName, "developers");

        Space space = new Space();
        space.setOrgName(noAuthSpaceTestOrg);
        space.setSpaceName(noAuthTestSpace);
        space.setNewSpaceName("junit-space-test-space-renamed");

        try {
            spaceService.renameSpace(space, clientToken);
        } finally {
            admin.unsetSpaceRole(noAuthSpaceTestOrg, noAuthTestSpace, client.getUserGuid(), "developers");
            admin.unsetOrgRole(noAuthSpaceTestOrg, client.getUserGuid(), "users");
        }
    }

    @Test
    public void renameSpace_400_EmptyBody() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Space space = new Space();
        space.setOrgName(noAuthSpaceTestOrg);
        space.setSpaceName(noAuthTestSpace);

        spaceService.renameSpace(space, clientToken);
    }

    @Test
    public void deleteSpace_200() throws Exception {
        Space space = new Space();
        space.setOrgName(testOrg);
        space.setSpaceName(testSpace);
        boolean result;

        try {
            result = spaceService.deleteSpace(space, clientToken);
        } finally {
            admin.createSpace(testOrg, testSpace);
        }

        assertTrue(result);
    }


    @Test
    public void deleteSpace_400_InvalidOrgOrSpace() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrNoAuthOrg + "' not found.");

        Space space = new Space();
        space.setOrgName(nonexistentOrNoAuthOrg);
        space.setSpaceName(nonexistentOrNoAuthSpace);

        spaceService.deleteSpace(space, clientToken);
    }

    @Test
    public void deleteSpace_403_NoAuthSpace() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.FORBIDDEN, "You are not authorized to perform the requested action"));

        admin.setOrgRole(noAuthSpaceTestOrg, clientUserName, "users");
        admin.setSpaceRole(noAuthSpaceTestOrg, noAuthTestSpace, clientUserName, "developers");

        Space space = new Space();
        space.setOrgName(noAuthSpaceTestOrg);
        space.setSpaceName(noAuthTestSpace);

        try {
            spaceService.deleteSpace(space, clientToken);
        } finally {
            admin.unsetSpaceRole(noAuthSpaceTestOrg, noAuthTestSpace, client.getUserGuid(), "developers");
            admin.unsetOrgRole(noAuthSpaceTestOrg, client.getUserGuid(), "users");
        }
    }

    @Test
    public void deleteSpace_EmptyParam_200() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Space space = new Space();

        spaceService.deleteSpace(space, clientToken);
    }


    @Test
    public void getSpaceSummary_200() throws Exception {
        Space space = new Space();
        space.setOrgName(testOrg);
        space.setSpaceName(testSpace);

        Space result = spaceService.getSpaceSummary(space, clientToken);
        assertTrue(result.getGuid() != null);
    }

    @Test
    public void getSpaceSummary_404_NonexistentOrNoAuthSpace() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.NOT_FOUND, "The app space could not be found: summary"));

        Space space = new Space();
        space.setOrgName(testOrg);
        space.setSpaceName(nonexistentOrNoAuthSpace);

        spaceService.getSpaceSummary(space, clientToken);
    }


    @Test
    public void getSpaceSummary_400_EmptyParam() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Space space = new Space();

        spaceService.getSpaceSummary(space, clientToken);
    }

    @Test
    public void getSpaceSummary_400_NonexistentOrNoAuthOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrNoAuthOrg + "' not found.");

        Space space = new Space();
        space.setOrgName(nonexistentOrNoAuthOrg);
        space.setSpaceName(testSpace);

        spaceService.getSpaceSummary(space, clientToken);
    }

    @Test
    public void setSpaceRole() throws Exception {
        admin.setOrgRole(testOrg, clientUserName, "managers");
        boolean result;
        try {
            result = spaceService.setSpaceRole(testOrg, testSpace, clientUserName, "SpaceDeveloper", clientToken);
        } finally {
            spaceService.unsetSpaceRole(testOrg, testSpace, clientUserGuid, "SpaceDeveloper", clientToken);
        }
        assertTrue(result);
    }

    @Test
    public void setSpaceRole_EmptyParam() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        spaceService.setSpaceRole("", "", "", "", clientToken);
    }

    @Test
    public void setSpaceRole_NonexistentOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrNoAuthOrg + "' not found.");

        spaceService.setSpaceRole(nonexistentOrNoAuthOrg, nonexistentOrNoAuthSpace, clientUserName, "SpaceAuditor", clientToken);
    }

    @Test
    public void setSpaceRole_NoAuthOrg() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Invalid relation: " + clientUserGuid));

        spaceService.setSpaceRole(noAuthSpaceTestOrg, noAuthTestSpace, clientUserName, "SpaceAuditor", adminToken);
    }

    @Test
    public void setSpaceRole_NoAuthSpace() throws Exception {
        boolean result;
        try {
            result = spaceService.setSpaceRole(testOrg, noAuthTestSpace, clientUserName, "SpaceAuditor", clientToken);
        } finally {
            spaceService.unsetSpaceRole(testOrg, noAuthTestSpace, clientUserGuid, "SpaceAuditor", clientToken);
        }
        assertTrue(result);
    }

    @Test
    public void setSpaceRole_NonexistentSpace() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.NOT_FOUND, "The app space could not be found: auditors"));
        //spaceGuid가 null 이라서 spaceGuid 자리에 입력한 role값을 넣어 요청을 보내기 때문에 에러메시지가 위와 같이 나옴

        spaceService.setSpaceRole(testOrg, nonexistentOrNoAuthSpace, clientUserName, "SpaceAuditor", clientToken);
    }

    @Test
    public void setSpaceRole_InvalidRole() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Invalid userRole."));

        spaceService.setSpaceRole(testOrg, testSpace, clientUserName, "InvalidRole", clientToken);
    }

    @Test
    public void getUsersForSpaceRole_Ok() throws Exception {
        List<Map<String, Object>> userList = new ArrayList<>();
        Map<String, Object> userMap = new HashedMap();
        userMap.put("userName", clientUserName);
        userList.add(userMap);
        List<Map<String, Object>> result = spaceService.getUsersForSpaceRole(testOrg, testSpace, userList, clientToken);
        assertTrue(result.size() > 0);
    }

    @Test
    public void getUsersForSpaceRole_InvalidOrg() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Organization '" + nonexistentOrNoAuthOrg + "' not found.");

        List<Map<String, Object>> userList = new ArrayList<>();
        Map<String, Object> userMap = new HashedMap();
        userMap.put("userName", clientUserName);
        userList.add(userMap);
        spaceService.getUsersForSpaceRole(nonexistentOrNoAuthOrg, testSpace, userList, clientToken);
    }

    @Test
    public void getUsersForSpaceRole_EmptyParam() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        List<Map<String, Object>> userList = new ArrayList<>();
        Map<String, Object> userMap = new HashedMap();
        userMap.put("userName", clientUserName);
        userList.add(userMap);
        spaceService.getUsersForSpaceRole("", "", userList, clientToken);
    }

    @Test
    public void getSpaceForAdmin_Ok() throws Exception {

        List<Object> resultList = spaceService.getSpacesForAdmin(testOrg);
        assertTrue(resultList.size() > 0);
    }

    @Test
    public void getSpacesInfo() throws Exception {
        int orgId = orgService.getOrgId(testOrg);
        List<Space> resultList = spaceService.getSpacesInfo(testSpace, orgId);
        assertTrue(resultList.size() > 0);
    }

    @Test
    public void getSpacesInfoById() throws Exception {
        int orgId = orgService.getOrgId(testOrg);
        List<Space> resultList = spaceService.getSpacesInfo(testSpace, orgId);
        int spaceId = resultList.get(0).getSpaceId();
        List spaceList = spaceService.getSpacesInfoById(spaceId);
        assertTrue(spaceList.size() > 0);
    }
}
