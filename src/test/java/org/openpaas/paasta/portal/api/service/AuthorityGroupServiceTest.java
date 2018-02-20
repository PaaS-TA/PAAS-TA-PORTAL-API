package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.identity.uaa.api.group.UaaGroupOperations;
import org.cloudfoundry.identity.uaa.error.UaaException;
import org.cloudfoundry.identity.uaa.scim.ScimGroup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

/**
 * Created by Dojun on 2016-10-07.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorityGroupServiceTest extends CommonTest {

    @Autowired
    private AuthorityGroupService authorityGroupService;

    private static String token;
    private static CustomCloudFoundryClient client;

    private static String guid;
    private static String testClientUserName;
    private static String testClientUserPassword;

    private static String testAdminUserName;
    private static String testAdminUserPassword;

    private static String testAuthGroupDisplayName;
    private static String testAuthGroupGuid;
    private static UaaGroupOperations groupOperations;

    @BeforeClass
    public static void init() throws Exception {
        testClientUserName = getPropertyValue("test.clientUserName");
        testClientUserPassword = getPropertyValue("test.clientUserPassword");
        testAuthGroupDisplayName = getPropertyValue("test.authorityGroup");

        uaaAdminClientId = getPropertyValue("test.uaaAdminClientId");

        groupOperations = getUaaGroupOperations(uaaAdminClientId);
        ScimGroup groupToCreate = new ScimGroup();
        groupToCreate.setDisplayName(testAuthGroupDisplayName);
        ScimGroup testGroup = groupOperations.createGroup(groupToCreate);
        testAuthGroupGuid = testGroup.getId();
    }

    @AfterClass
    public static void testFinalize() throws Exception {
        LOGGER.info(testAuthGroupGuid);
        groupOperations.deleteGroup(testAuthGroupGuid);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityGroupServiceTest.class);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getAuthorityGroups_Ok() throws Exception {

        Collection<ScimGroup> result;
        result = authorityGroupService.getAuthorityGroups();

        Predicate<ScimGroup> predicate = scimGroup -> scimGroup.getDisplayName().equals(testAuthGroupDisplayName);
        assertTrue(result.stream().anyMatch(predicate));
    }

    @Test
    public void createAuthorityGroups_Ok() throws Exception {
        String groupDisplayName = getPropertyValue("test.authorityGroupForEachTest");
        ScimGroup createdGroup = new ScimGroup();

        try {
            createdGroup = authorityGroupService.createAuthorityGroup(groupDisplayName, null);
        } finally {
            authorityGroupService.deleteAuthorityGroup(createdGroup.getId());
        }

        assertTrue(createdGroup.getDisplayName().equals(groupDisplayName));
    }

    @Test
    public void createAuthorityGroups_EmptyParam() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        authorityGroupService.createAuthorityGroup(null, null);
    }

    @Test
    public void createAuthorityGroups_DuplicatedGroupName() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Duplicated group display name");

        authorityGroupService.createAuthorityGroup(testAuthGroupDisplayName, null);
    }


    @Test
    public void deleteAuthorityGroup_Ok() throws Exception {
        String groupDisplayName = getPropertyValue("test.authorityGroupForEachTest");
        ScimGroup createdGroup = authorityGroupService.createAuthorityGroup(groupDisplayName, null);

        authorityGroupService.deleteAuthorityGroup(createdGroup.getId());

        Collection<ScimGroup> result = authorityGroupService.getAuthorityGroups();
        Predicate<ScimGroup> predicate = scimGroup -> scimGroup.getDisplayName().equals(groupDisplayName);
        assertTrue(!result.stream().anyMatch(predicate));
    }

    @Test
    public void deleteAuthorityGroup_EmptyParam() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        authorityGroupService.deleteAuthorityGroup(null);
    }

    @Test
    public void deleteAuthorityGroup_NonexistentGroup() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Invalid group id");

        String groupGuid = "test-auth-group-nonexistentGroup-GUID";
        authorityGroupService.deleteAuthorityGroup(groupGuid);
    }

    @Test
    public void addGroupMembers_Ok() throws Exception {
        List<String> memberList = new ArrayList<>();
        memberList.add(getPropertyValue("test.clientUserName"));

        ScimGroup resultGroup = new ScimGroup();
        try {
            resultGroup = authorityGroupService.addGroupMembers(testAuthGroupGuid, memberList);
        } finally {
            authorityGroupService.deleteGroupMembers(testAuthGroupGuid, memberList);
        }

        assertTrue(resultGroup.getMembers().size() == 1);
    }

    @Test
    public void addGroupMembers_EmptyGuid() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        List<String> memberList = new ArrayList<>();
        memberList.add(getPropertyValue("test.clientUserName"));

        authorityGroupService.addGroupMembers("", memberList);
    }

    @Test
    public void addGroupMembers_EmptyMemberList() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        List<String> memberList = new ArrayList<>();

        authorityGroupService.addGroupMembers(testAuthGroupGuid, memberList);
    }

    @Test
    public void addGroupMembers_NullMemberList() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        authorityGroupService.addGroupMembers(testAuthGroupGuid, null);
    }

    @Test
    public void addGroupMembers_EmptyMemberName() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        List<String> memberList = new ArrayList<>();
        memberList.add("");

        authorityGroupService.addGroupMembers(testAuthGroupGuid, memberList);
    }


    @Test
    public void deleteGroupMembers_Ok() throws Exception {

        List<String> memberList = new ArrayList<>();
        memberList.add(getPropertyValue("test.clientUserName"));
        authorityGroupService.addGroupMembers(testAuthGroupGuid, memberList);
        ScimGroup group = authorityGroupService.deleteGroupMembers(testAuthGroupGuid, memberList);

        assertTrue(group.getMembers().size() == 0);
    }


    @Test
    public void deleteGroupMembers_EmptyGuid() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        List<String> memberList = new ArrayList<>();
        memberList.add(getPropertyValue("test.clientUserName"));

        authorityGroupService.deleteGroupMembers("", memberList);
    }

    @Test
    public void deleteGroupMembers_EmptyMemberList() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        List<String> memberList = new ArrayList<>();

        authorityGroupService.deleteGroupMembers(testAuthGroupGuid, memberList);
    }

    @Test
    public void deleteGroupMembers_NullMemberList() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        authorityGroupService.deleteGroupMembers(testAuthGroupGuid, null);
    }

    @Test
    public void deleteGroupMembers_EmptyMemberName() throws Exception {
        expectedException.expect(UaaException.class);
        expectedException.expectMessage("Required request body content is missing");

        List<String> memberList = new ArrayList<>();
        memberList.add("");

        authorityGroupService.deleteGroupMembers(testAuthGroupGuid, memberList);
    }


}
