package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.spaces.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.openpaas.paasta.portal.api.model.UserRole;

import java.util.*;

import static org.mockito.Mockito.*;

public class SpaceServiceTest {

    @Mock
    SpaceService spaceService;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSpaces() throws Exception {
        ListSpacesResponse listSpacesResponse = ListSpacesResponse.builder().build();
        when(spaceService.getSpaces(anyString(),anyObject())).thenReturn(listSpacesResponse);

        ListSpacesResponse result = spaceService.getSpaces("orgId", null);
        Assert.assertEquals(listSpacesResponse, result);
    }

    @Test
    public void testGetSpacesWithOrgName() throws Exception {
        ListSpacesResponse listSpacesResponse = ListSpacesResponse.builder().build();
        when(spaceService.getSpacesWithOrgName(anyString(),anyObject(),anyString())).thenReturn(listSpacesResponse);

        ListSpacesResponse result = spaceService.getSpacesWithOrgName("orgName", null, "token");
        Assert.assertEquals(listSpacesResponse, result);
    }

    @Test
    public void testGetSpaces2() throws Exception {
        ListSpacesResponse listSpacesResponse = ListSpacesResponse.builder().build();
        when(spaceService.getSpaces(any(Org.class),anyString())).thenReturn(listSpacesResponse);

        ListSpacesResponse result = spaceService.getSpaces(new Org(), "token");
        Assert.assertEquals(listSpacesResponse, result);
    }

    @Test
    public void testCreateSpace() throws Exception {
        when(spaceService.createSpace(any(),anyString())).thenReturn(thenReturn);
        Map result = spaceService.createSpace(new Space(), "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testGetSpace() throws Exception {
        GetSpaceResponse getSpaceResponse = GetSpaceResponse.builder().build();
        when(spaceService.getSpace(anyString(),anyString())).thenReturn(getSpaceResponse);

        GetSpaceResponse result = spaceService.getSpace("spaceId", "token");
        Assert.assertEquals(getSpaceResponse, result);
    }

    @Test
    public void testGetSpace2() throws Exception {
        GetSpaceResponse getSpaceResponse = GetSpaceResponse.builder().build();
        when(spaceService.getSpace(anyString())).thenReturn(getSpaceResponse);

        GetSpaceResponse result = spaceService.getSpace("spaceId");
        Assert.assertEquals(getSpaceResponse, result);
    }

    @Test
    public void testGetSpaceUsingName() throws Exception {
        SpaceResource spaceResource = SpaceResource.builder().build();
        when(spaceService.getSpaceUsingName(anyString(),anyString(),anyString())).thenReturn(spaceResource);

        SpaceResource result = spaceService.getSpaceUsingName("orgName", "spaceName", "token");
        Assert.assertEquals(spaceResource, result);
    }

    @Test
    public void testIsExistSpace() throws Exception {
        when(spaceService.isExistSpace(anyString())).thenReturn(true);

        boolean result = spaceService.isExistSpace("spaceId");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRenameSpace() throws Exception {
        when(spaceService.renameSpace(any(),anyString())).thenReturn(thenReturn);

        Map result = spaceService.renameSpace(new Space(), "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testDeleteSpace() throws Exception {

        when(spaceService.deleteSpace(anyString(),anyBoolean(),anyString())).thenReturn(thenReturn);

        Map result = spaceService.deleteSpace("guid", true, "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testGetSpaceSummary() throws Exception {
        GetSpaceSummaryResponse getSpaceSummaryResponse = GetSpaceSummaryResponse.builder().build();
        when(spaceService.getSpaceSummary(anyString(),anyObject())).thenReturn(getSpaceSummaryResponse);

        GetSpaceSummaryResponse result = spaceService.getSpaceSummary("spaceId", null);
        Assert.assertEquals(getSpaceSummaryResponse, result);
    }

    @Test
    public void testGetSpaceServices() throws Exception {
        ListSpaceServicesResponse listSpaceServicesResponse = ListSpaceServicesResponse.builder().build();
        when(spaceService.getSpaceServices(anyString(),anyString())).thenReturn(listSpaceServicesResponse);

        ListSpaceServicesResponse result = spaceService.getSpaceServices("spaceId", "token");
        Assert.assertEquals(listSpaceServicesResponse, result);
    }

    @Test
    public void testGetSpaceUserRoles() throws Exception {
        ListSpaceUserRolesResponse listSpaceUserRolesResponse = ListSpaceUserRolesResponse.builder().build();
        when(spaceService.getSpaceUserRoles(anyString(),anyString())).thenReturn(listSpaceUserRolesResponse);

        ListSpaceUserRolesResponse result = spaceService.getSpaceUserRoles("spaceId", "token");
        Assert.assertEquals(listSpaceUserRolesResponse, result);
    }

    @Test
    public void testAssociateSpaceUserRole() throws Exception {
        when(spaceService.associateSpaceUserRole(anyString(),anyString(),anyString())).thenReturn(null);

        AbstractSpaceResource result = spaceService.associateSpaceUserRole("spaceId", "userId", "role");
        Assert.assertEquals(null, result);
    }

    @Test
    public void testAssociateAllSpaceUserRolesByOrgId() throws Exception {
        List<AbstractSpaceResource> abstractSpaceResourceList = new ArrayList<>();
        when(spaceService.associateAllSpaceUserRolesByOrgId(anyString(),anyString(),any(),anyObject())).thenReturn(abstractSpaceResourceList);

        List<AbstractSpaceResource> result = spaceService.associateAllSpaceUserRolesByOrgId("orgId", "userId", null, null);
        Assert.assertEquals(abstractSpaceResourceList, result);
    }

    @Test
    public void testRemoveSpaceUserRole() throws Exception {
        spaceService.removeSpaceUserRole("spaceId", "userId", "role");
    }

    @Test
    public void testRemoveAllSpaceUserRolesByOrgId() throws Exception {
        spaceService.removeAllSpaceUserRolesByOrgId("orgId", "userId", null);
    }

    @Test
    public void testAssociateSpaceUserRoles() throws Exception {
        when(spaceService.associateSpaceUserRoles(anyString(), anyList(), anyString())).thenReturn(true);
        boolean result = spaceService.associateSpaceUserRoles("spaceid", Arrays.<UserRole>asList(UserRole.builder().build()), "token");
        Assert.assertEquals(true, result);
    }

}
