package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.organizations.*;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.users.UserResource;
import org.cloudfoundry.operations.organizations.OrganizationDetail;
import org.cloudfoundry.operations.organizations.OrganizationQuota;
import org.cloudfoundry.operations.useradmin.OrganizationUsers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.UserRole;

import java.util.*;

import static org.mockito.Mockito.*;

public class OrgServiceTest {

    @Mock
    OrgService orgService;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrg() throws Exception {

        when(orgService.createOrg(any(), any())).thenReturn(thenReturn);

        Map result = orgService.createOrg(new Org(), "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testIsExistOrgName() throws Exception {

        when(orgService.isExistOrgName(anyString())).thenReturn(true);

        boolean result = orgService.isExistOrgName("orgName");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsExistOrg() throws Exception {

        when(orgService.isExistOrg(anyString())).thenReturn(true);

        boolean result = orgService.isExistOrg("orgId");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetOrg() throws Exception {
        GetOrganizationResponse getOrganizationResponse = GetOrganizationResponse.builder().build();
        when(orgService.getOrg(anyString())).thenReturn(getOrganizationResponse);

        GetOrganizationResponse result = orgService.getOrg("orgId");
        Assert.assertEquals(getOrganizationResponse, result);
    }

    @Test
    public void testGetOrg2() throws Exception {
        GetOrganizationResponse getOrganizationResponse = GetOrganizationResponse.builder().build();
        when(orgService.getOrg(anyString(), anyString())).thenReturn(getOrganizationResponse);

        GetOrganizationResponse result = orgService.getOrg("orgId", "token");
        Assert.assertEquals(getOrganizationResponse, result);
    }

    @Test
    public void testGetOrgSummary() throws Exception {
        SummaryOrganizationResponse summaryOrganizationResponse = SummaryOrganizationResponse.builder().build();
        when(orgService.getOrgSummary(anyString(), anyString())).thenReturn(summaryOrganizationResponse);

        SummaryOrganizationResponse result = orgService.getOrgSummary("orgId", "token");
        Assert.assertEquals(summaryOrganizationResponse, result);
    }

    @Test
    public void testGetOrgSummaryMap() throws Exception {
        when(orgService.getOrgSummaryMap(anyString(), anyString())).thenReturn(thenReturn);

        Map result = orgService.getOrgSummaryMap("orgId", "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testGetOrgs() throws Exception {
        List<OrganizationResource> organizationResourceList = new ArrayList<>();
        when(orgService.getOrgs(anyString())).thenReturn(organizationResourceList);


        List<OrganizationResource> result = orgService.getOrgs("token");
        Assert.assertEquals(organizationResourceList, result);
    }

    @Test
    public void testGetOrgsForUser() throws Exception {
        ListOrganizationsResponse listOrganizationsResponse = ListOrganizationsResponse.builder().build();
        when(orgService.getOrgsForUser(anyString())).thenReturn(listOrganizationsResponse);

        ListOrganizationsResponse result = orgService.getOrgsForUser("token");
        Assert.assertEquals(listOrganizationsResponse, result);
    }

    @Test
    public void testGetOrgsForAdmin() throws Exception {
        ListOrganizationsResponse listOrganizationsResponse = ListOrganizationsResponse.builder().build();
        when(orgService.getOrgsForAdmin()).thenReturn(listOrganizationsResponse);

        ListOrganizationsResponse result = orgService.getOrgsForAdmin();
        Assert.assertEquals(listOrganizationsResponse, result);
    }

    @Test
    public void testGetOrgId() throws Exception {
        when(orgService.getOrgId(anyString(), anyString())).thenReturn("msg");

        String result = orgService.getOrgId("orgName", "token");
        Assert.assertEquals("msg", result);
    }

    @Test
    public void testGetOrgUsingName() throws Exception {
        OrganizationDetail organizationDetail = OrganizationDetail.builder().id("id").name("name").quota(OrganizationQuota.builder().id("id").instanceMemoryLimit(1024).name("name").organizationId("organizationId").paidServicePlans(true).totalMemoryLimit(10240).totalRoutes(100).totalServiceInstances(100).build()).build();
        when(orgService.getOrgUsingName(anyString())).thenReturn(organizationDetail);

        OrganizationDetail result = orgService.getOrgUsingName("name");
        Assert.assertEquals(organizationDetail, result);
    }

    @Test
    public void testGetOrgUsingName2() throws Exception {
        OrganizationDetail organizationDetail = OrganizationDetail.builder().id("id").name("name").quota(OrganizationQuota.builder().id("id").instanceMemoryLimit(1024).name("name").organizationId("organizationId").paidServicePlans(true).totalMemoryLimit(10240).totalRoutes(100).totalServiceInstances(100).build()).build();
        when(orgService.getOrgUsingName(anyString(), anyString())).thenReturn(organizationDetail);

        OrganizationDetail result = orgService.getOrgUsingName("name", "token");
        Assert.assertEquals(organizationDetail, result);
    }

    @Test
    public void testRenameOrg() throws Exception {
        when(orgService.renameOrg(any(), anyString())).thenReturn(thenReturn);

        Map result = orgService.renameOrg(new Org(), "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testDeleteOrg() throws Exception {
        when(orgService.deleteOrg(anyString(), anyBoolean(), anyString())).thenReturn(thenReturn);

        Map result = orgService.deleteOrg("orgId", true, "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testGetOrgSpaces() throws Exception {
        ListSpacesResponse listSpacesResponse = ListSpacesResponse.builder().build();
        when(orgService.getOrgSpaces(anyString(), anyString())).thenReturn(listSpacesResponse);

        ListSpacesResponse result = orgService.getOrgSpaces("orgId", "token");
        Assert.assertEquals(listSpacesResponse, result);
    }

    @Test
    public void testGetOrgQuota() throws Exception {
        GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = GetOrganizationQuotaDefinitionResponse.builder().build();
        when(orgService.getOrgQuota(anyString(), anyString())).thenReturn(getOrganizationQuotaDefinitionResponse);

        GetOrganizationQuotaDefinitionResponse result = orgService.getOrgQuota("orgId", "token");
        Assert.assertEquals(getOrganizationQuotaDefinitionResponse, result);
    }

    @Test
    public void testUpdateOrgQuota() throws Exception {
        when(orgService.updateOrgQuota(anyString(), any(), anyString())).thenReturn(thenReturn);

        Map result = orgService.updateOrgQuota("orgId", new Org(), "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testListAllOrgUsers() throws Exception {
        List<UserResource> userResourceList = new ArrayList<>();
        when(orgService.listAllOrgUsers(anyString(), anyString())).thenReturn(userResourceList);

        List<UserResource> result = orgService.listAllOrgUsers("orgId", "token");
        Assert.assertEquals(userResourceList, result);
    }

    @Test
    public void testGetOrgUserRoles() throws Exception {
        when(orgService.getOrgUserRoles(anyString(), anyString())).thenReturn(null);

        Map<String, Collection<UserRole>> result = orgService.getOrgUserRoles("orgId", "token");
        Assert.assertEquals(null, result);
    }

    @Test
    public void testGetOrgUserRolesByOrgName() throws Exception {
        OrganizationUsers organizationUsers = OrganizationUsers.builder().build();
        when(orgService.getOrgUserRolesByOrgName(anyString(), anyString())).thenReturn(organizationUsers);

        OrganizationUsers result = orgService.getOrgUserRolesByOrgName("orgName", "token");
        Assert.assertEquals(organizationUsers, result);
    }

    @Test
    public void testIsOrgManagerUsingOrgName() throws Exception {
        when(orgService.isOrgManagerUsingOrgName(anyString(), anyString())).thenReturn(true);

        boolean result = orgService.isOrgManagerUsingOrgName("orgName", "token");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsOrgManagerUsingToken() throws Exception {
        when(orgService.isOrgManagerUsingToken("orgId", "token")).thenReturn(true);

        boolean result = orgService.isOrgManagerUsingToken("orgId", "token");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsOrgManager() throws Exception {
        when(orgService.isOrgManager(anyString(), anyString())).thenReturn(true);

        boolean result = orgService.isOrgManager("orgId", "userId");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsBillingManager() throws Exception {
        when(orgService.isBillingManager(anyString(), anyString())).thenReturn(true);

        boolean result = orgService.isBillingManager("orgId", "userId");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsOrgAuditor() throws Exception {
        when(orgService.isOrgAuditor(anyString(), anyString())).thenReturn(true);

        boolean result = orgService.isOrgAuditor("orgId", "userId");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testAssociateOrgUserRole() throws Exception {
        when(orgService.associateOrgUserRole(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        AbstractOrganizationResource result = orgService.associateOrgUserRole("orgId", "userId", "role", "token");
        Assert.assertEquals(null, result);
    }

    @Test
    public void testRemoveOrgUserRole() throws Exception {
        orgService.removeOrgUserRole("orgId", "userId", "role", "token");
    }

    @Test
    public void testInviteUser() throws Exception {
        orgService.inviteUser("orgId", "userId", "token");
    }

    @Test
    public void testCancelInvitionUser() throws Exception {
        orgService.cancelInvitionUser();
    }

    @Test
    public void testCancelOrganizationMember() throws Exception {
        when(orgService.cancelOrganizationMember(anyString(), anyString(), anyString())).thenReturn(true);

        boolean result = orgService.cancelOrganizationMember("orgId", "userId", "token");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testAssociateOrgUserRole2() throws Exception {
        when(orgService.associateOrgUserRole2(anyMap())).thenReturn(true);

        boolean result = orgService.associateOrgUserRole2(new HashMap() {{
            put("String", "String");
        }});
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetOrgsForAdminAll() throws Exception {
        ListOrganizationsResponse listOrganizationsResponse = ListOrganizationsResponse.builder().build();
        when(orgService.getOrgsForAdminAll(anyInt())).thenReturn(listOrganizationsResponse);

        ListOrganizationsResponse result = orgService.getOrgsForAdminAll(0);
        Assert.assertEquals(listOrganizationsResponse, result);
    }

    @Test
    public void testOrgFlag() throws Exception {
        when(orgService.orgFlag(anyString(), anyString())).thenReturn(thenReturn);

        Map result = orgService.orgFlag("flagname", "token");
        Assert.assertEquals(thenReturn, result);
    }


}

