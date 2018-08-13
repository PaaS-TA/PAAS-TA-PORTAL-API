package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.identity.uaa.scim.ScimGroup;
import org.cloudfoundry.identity.uaa.scim.ScimGroupMember;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*", "javax.crypto.*", "org.openpaas.paasta.portal.api.common.*", "org.openpaas.paasta.portal.api.config.*"})
@TestPropertySource(properties = {"spring.config.location = classpath:/application.yml", "eureka.client.enabled=false"})
// Push 용
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthorityGroupServiceTest {

    @Mock
    AuthorityGroupService authorityGroupService;


    String GUID = "f89b1ef6-7416-4d12-b492-c10fdaaff632";
    String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsImtpZCI6ImxlZ2FjeS10b2tlbi1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI1NmFjZGZmMzlmZDQ0ZTdiOTc0MGM1ZTMwNDI2MDFiMSIsInN1YiI6ImI4ZDkwZDQ3LWM5YzItNDdlZi1hOTE1LWQzOGI5MDk2OWFkNiIsInNjb3BlIjpbInVhYS51c2VyIl0sImNsaWVudF9pZCI6ImNmIiwiY2lkIjoiY2YiLCJhenAiOiJjZiIsImdyYW50X3R5cGUiOiJwYXNzd29yZCIsInVzZXJfaWQiOiJiOGQ5MGQ0Ny1jOWMyLTQ3ZWYtYTkxNS1kMzhiOTA5NjlhZDYiLCJvcmlnaW4iOiJ1YWEiLCJ1c2VyX25hbWUiOiJtYXJpc3NhIiwiZW1haWwiOiJtYXJpc3NhQHRlc3Qub3JnIiwiYXV0aF90aW1lIjoxNTI5NjkwNDI2LCJyZXZfc2lnIjoiNGI1NzYzNWIiLCJpYXQiOjE1Mjk2OTA0MjYsImV4cCI6MTUyOTczMzYyNiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3VhYS9vYXV0aC90b2tlbiIsInppZCI6InVhYSIsImF1ZCI6WyJjZiIsInVhYSJdfQ.erbXOVkGb0M2x9cEnP10nxfXY0iFENSurvo3tAKAzeE";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAuthorityGroups() throws Exception {
        Collection<ScimGroup> collection = new ArrayList<>();
        when(authorityGroupService.getAuthorityGroups()).thenReturn(collection);

        Collection<ScimGroup> result = authorityGroupService.getAuthorityGroups();
        Assert.assertEquals(collection, result);
    }

    @Test
    public void testCreateAuthorityGroup() throws Exception {
        ScimGroup scimGroup = new ScimGroup();
        scimGroup.setId(GUID);
        when(authorityGroupService.createAuthorityGroup(anyString(), anyList())).thenReturn(scimGroup);
        ScimGroup result = authorityGroupService.createAuthorityGroup("displayName", Arrays.<ScimGroupMember>asList(new ScimGroupMember()));
        Assert.assertEquals(scimGroup.getId(), result.getId());
    }

    @Test
    public void testDeleteAuthorityGroup() throws Exception {
        authorityGroupService.deleteAuthorityGroup("groupGuid");
    }

    @Test
    public void testAddGroupMembers() throws Exception {
        when(authorityGroupService.addGroupMembers(anyString(), anyList())).thenReturn(null);
        ScimGroup result = authorityGroupService.addGroupMembers("groupGuid", Arrays.<String>asList("String"));
        Assert.assertEquals(null, result);
    }

    @Test
    public void testDeleteGroupMembers() throws Exception {
        when(authorityGroupService.deleteGroupMembers(anyString(), anyList())).thenReturn(null);
        ScimGroup result = authorityGroupService.deleteGroupMembers("groupGuid", Arrays.<String>asList("String"));
        Assert.assertEquals(null, result);
    }


}
