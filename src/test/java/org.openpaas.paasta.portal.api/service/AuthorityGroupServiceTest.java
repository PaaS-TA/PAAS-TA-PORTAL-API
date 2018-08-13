package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.identity.uaa.scim.ScimGroup;
import org.cloudfoundry.identity.uaa.scim.ScimGroupMember;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.config.TestConfig;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthorityGroupServiceTest extends TestConfig {
    @Mock
    Logger LOGGER;
    @Mock
    LoginService loginService;

    @Mock
    AuthorityGroupService authorityGroupService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAuthorityGroups() throws Exception {
        Collection<ScimGroup> collection = new ArrayList<>();

        when(loginService.getUaaGroupOperations(any())).thenReturn(null);
        Collection<ScimGroup> result = authorityGroupService.getAuthorityGroups();
        Assert.assertEquals(collection, result);
    }

    @Test
    public void testCreateAuthorityGroup() throws Exception {
        when(loginService.getUaaGroupOperations(any())).thenReturn(null);
        when(loginService.stringNullCheck(anyVararg())).thenReturn(true);
        List<ScimGroupMember> memberList = new ArrayList<>();
        ScimGroup result = authorityGroupService.createAuthorityGroup("displayName", memberList);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testDeleteAuthorityGroup() throws Exception {
        when(loginService.getUaaGroupOperations(any())).thenReturn(null);
        when(loginService.stringNullCheck(anyVararg())).thenReturn(true);

        authorityGroupService.deleteAuthorityGroup("groupGuid");
    }

    @Test
    public void testAddGroupMembers() throws Exception {
        when(loginService.getUaaGroupOperations(any())).thenReturn(null);
        when(loginService.stringNullCheck(anyVararg())).thenReturn(true);

        ScimGroup result = authorityGroupService.addGroupMembers("groupGuid", Arrays.<String>asList("String"));
        Assert.assertEquals(null, result);
    }

    @Test
    public void testDeleteGroupMembers() throws Exception {
        when(loginService.getUaaGroupOperations(any())).thenReturn(null);
        when(loginService.stringNullCheck(anyVararg())).thenReturn(true);

        ScimGroup result = authorityGroupService.deleteGroupMembers("groupGuid", Arrays.<String>asList("String"));
        Assert.assertEquals(null, result);
    }


}
