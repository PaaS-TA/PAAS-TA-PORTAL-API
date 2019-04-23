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

public class SpaceServiceV2Test {

    @Mock
    SpaceServiceV2 spaceServiceV2;

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
        when(spaceServiceV2.getSpaces(anyString(), anyObject())).thenReturn(listSpacesResponse);

        ListSpacesResponse result = spaceServiceV2.getSpaces("orgId", null);
        Assert.assertEquals(listSpacesResponse, result);
    }

    @Test
    public void testGetSpacesWithOrgName() throws Exception {
        ListSpacesResponse listSpacesResponse = ListSpacesResponse.builder().build();
        when(spaceServiceV2.getSpacesWithOrgName(anyString(), anyObject(), anyString())).thenReturn(listSpacesResponse);

        ListSpacesResponse result = spaceServiceV2.getSpacesWithOrgName("orgName", null, "token");
        Assert.assertEquals(listSpacesResponse, result);
    }

    @Test
    public void testGetSpaces2() throws Exception {
        ListSpacesResponse listSpacesResponse = ListSpacesResponse.builder().build();
        when(spaceServiceV2.getSpaces(any(Org.class), anyString())).thenReturn(listSpacesResponse);

        ListSpacesResponse result = spaceServiceV2.getSpaces(new Org(), "token");
        Assert.assertEquals(listSpacesResponse, result);
    }

    @Test
    public void testCreateSpace() throws Exception {
        when(spaceServiceV2.createSpace(any(), anyString())).thenReturn(thenReturn);
        Map result = spaceServiceV2.createSpace(new Space(), "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testGetSpace() throws Exception {
        GetSpaceResponse getSpaceResponse = GetSpaceResponse.builder().build();
        when(spaceServiceV2.getSpace(anyString(), anyString())).thenReturn(getSpaceResponse);

        GetSpaceResponse result = spaceServiceV2.getSpace("spaceId", "token");
        Assert.assertEquals(getSpaceResponse, result);
    }

    @Test
    public void testGetSpace2() throws Exception {
        GetSpaceResponse getSpaceResponse = GetSpaceResponse.builder().build();
        when(spaceServiceV2.getSpace(anyString())).thenReturn(getSpaceResponse);

        GetSpaceResponse result = spaceServiceV2.getSpace("spaceId");
        Assert.assertEquals(getSpaceResponse, result);
    }

    @Test
    public void testGetSpaceUsingName() throws Exception {
        SpaceResource spaceResource = SpaceResource.builder().build();
        when(spaceServiceV2.getSpaceUsingName(anyString(), anyString(), anyString())).thenReturn(spaceResource);

        SpaceResource result = spaceServiceV2.getSpaceUsingName("orgName", "spaceName", "token");
        Assert.assertEquals(spaceResource, result);
    }

    @Test
    public void testIsExistSpace() throws Exception {
        when(spaceServiceV2.isExistSpace(anyString())).thenReturn(true);

        boolean result = spaceServiceV2.isExistSpace("spaceId");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testRenameSpace() throws Exception {
        when(spaceServiceV2.renameSpace(any(), anyString())).thenReturn(thenReturn);

        Map result = spaceServiceV2.renameSpace(new Space(), "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testDeleteSpace() throws Exception {

        when(spaceServiceV2.deleteSpace(anyString(), anyBoolean(), anyString())).thenReturn(thenReturn);

        Map result = spaceServiceV2.deleteSpace("guid", true, "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testGetSpaceSummary() throws Exception {
        GetSpaceSummaryResponse getSpaceSummaryResponse = GetSpaceSummaryResponse.builder().build();
        when(spaceServiceV2.getSpaceSummary(anyString(), anyObject())).thenReturn(getSpaceSummaryResponse);

        GetSpaceSummaryResponse result = spaceServiceV2.getSpaceSummary("spaceId", null);
        Assert.assertEquals(getSpaceSummaryResponse, result);
    }

    @Test
    public void testGetSpaceServices() throws Exception {
        ListSpaceServicesResponse listSpaceServicesResponse = ListSpaceServicesResponse.builder().build();
        when(spaceServiceV2.getSpaceServices(anyString())).thenReturn(listSpaceServicesResponse);

        ListSpaceServicesResponse result = spaceServiceV2.getSpaceServices("spaceId");
        Assert.assertEquals(listSpaceServicesResponse, result);
    }

    @Test
    public void testGetSpaceUserRoles() throws Exception {
        ListSpaceUserRolesResponse listSpaceUserRolesResponse = ListSpaceUserRolesResponse.builder().build();
        when(spaceServiceV2.getSpaceUserRoles(anyString(), anyString())).thenReturn(listSpaceUserRolesResponse);

        ListSpaceUserRolesResponse result = spaceServiceV2.getSpaceUserRoles("spaceId", "token");
        Assert.assertEquals(listSpaceUserRolesResponse, result);
    }

    @Test
    public void testAssociateSpaceUserRole() throws Exception {
        when(spaceServiceV2.associateSpaceUserRole(anyString(), anyString(), anyString())).thenReturn(null);

        AbstractSpaceResource result = spaceServiceV2.associateSpaceUserRole("spaceId", "userId", "role");
        Assert.assertEquals(null, result);
    }

    @Test
    public void testAssociateAllSpaceUserRolesByOrgId() throws Exception {
        List<AbstractSpaceResource> abstractSpaceResourceList = new ArrayList<>();
        when(spaceServiceV2.associateAllSpaceUserRolesByOrgId(anyString(), anyString(), any(), anyObject())).thenReturn(abstractSpaceResourceList);

        List<AbstractSpaceResource> result = spaceServiceV2.associateAllSpaceUserRolesByOrgId("orgId", "userId", null, null);
        Assert.assertEquals(abstractSpaceResourceList, result);
    }

    @Test
    public void testRemoveSpaceUserRole() throws Exception {
        spaceServiceV2.removeSpaceUserRole("spaceId", "userId", "role");
    }

    @Test
    public void testRemoveAllSpaceUserRolesByOrgId() throws Exception {
        spaceServiceV2.removeAllSpaceUserRolesByOrgId("orgId", "userId", null);
    }

    @Test
    public void testAssociateSpaceUserRoles() throws Exception {
        when(spaceServiceV2.associateSpaceUserRoles(anyString(), anyList(), anyString())).thenReturn(true);
        boolean result = spaceServiceV2.associateSpaceUserRoles("spaceid", Arrays.<UserRole>asList(UserRole.builder().build()), "token");
        Assert.assertEquals(true, result);
    }

}
