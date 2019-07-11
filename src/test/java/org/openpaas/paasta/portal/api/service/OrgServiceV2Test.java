//package org.openpaas.paasta.portal.api.service;
//
//import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionResponse;
//import org.cloudfoundry.client.v2.organizations.*;
//import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
//import org.cloudfoundry.client.v2.users.UserResource;
//import org.cloudfoundry.operations.organizations.OrganizationDetail;
//import org.cloudfoundry.operations.organizations.OrganizationQuota;
//import org.cloudfoundry.operations.useradmin.OrganizationUsers;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.openpaas.paasta.portal.api.model.Org;
//import org.openpaas.paasta.portal.api.model.UserRole;
//
//import java.util.*;
//
//import static org.mockito.Mockito.*;
//
//public class OrgServiceV2Test {
//
//    @Mock
//    OrgServiceV2 orgServiceV2;
//
//    Map thenReturn;
//
//    @Before
//    public void setUp() {
//        thenReturn = new HashMap();
//        thenReturn.put("result", true);
//        thenReturn.put("msg", "You have successfully completed the task.");
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testCreateOrg() throws Exception {
//
//        when(orgServiceV2.createOrg(any(), any())).thenReturn(thenReturn);
//
//        Map result = orgServiceV2.createOrg(new Org(), "token");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testIsExistOrgName() throws Exception {
//
//        when(orgServiceV2.isExistOrgName(anyString())).thenReturn(true);
//
//        boolean result = orgServiceV2.isExistOrgName("orgName");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testIsExistOrg() throws Exception {
//
//        when(orgServiceV2.isExistOrg(anyString())).thenReturn(true);
//
//        boolean result = orgServiceV2.isExistOrg("orgId");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testGetOrg() throws Exception {
//        GetOrganizationResponse getOrganizationResponse = GetOrganizationResponse.builder().build();
//        when(orgServiceV2.getOrg(anyString())).thenReturn(getOrganizationResponse);
//
//        GetOrganizationResponse result = orgServiceV2.getOrg("orgId");
//        Assert.assertEquals(getOrganizationResponse, result);
//    }
//
//    @Test
//    public void testGetOrg2() throws Exception {
//        GetOrganizationResponse getOrganizationResponse = GetOrganizationResponse.builder().build();
//        when(orgServiceV2.getOrg(anyString(), anyObject())).thenReturn(getOrganizationResponse);
//
//        GetOrganizationResponse result = orgServiceV2.getOrg("orgId", null);
//        Assert.assertEquals(getOrganizationResponse, result);
//    }
//
//    @Test
//    public void testGetOrgSummary() throws Exception {
//        SummaryOrganizationResponse summaryOrganizationResponse = SummaryOrganizationResponse.builder().build();
//        when(orgServiceV2.getOrgSummary(anyString(), anyString())).thenReturn(summaryOrganizationResponse);
//
//        SummaryOrganizationResponse result = orgServiceV2.getOrgSummary("orgId", "token");
//        Assert.assertEquals(summaryOrganizationResponse, result);
//    }
//
//    @Test
//    public void testGetOrgSummaryMap() throws Exception {
//        when(orgServiceV2.getOrgSummaryMap(anyString(), anyObject())).thenReturn(thenReturn);
//
//        Map result = orgServiceV2.getOrgSummaryMap("orgId", null);
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testGetOrgs() throws Exception {
//        List<OrganizationResource> organizationResourceList = new ArrayList<>();
//        when(orgServiceV2.getOrgs(anyString())).thenReturn(organizationResourceList);
//
//
//        List<OrganizationResource> result = orgServiceV2.getOrgs("token");
//        Assert.assertEquals(organizationResourceList, result);
//    }
//
//    @Test
//    public void testGetOrgsForUser() throws Exception {
//        ListOrganizationsResponse listOrganizationsResponse = ListOrganizationsResponse.builder().build();
//        when(orgServiceV2.getOrgsForUser(anyObject(), anyInt())).thenReturn(listOrganizationsResponse);
//
//        ListOrganizationsResponse result = orgServiceV2.getOrgsForUser(null, 0);
//        Assert.assertEquals(listOrganizationsResponse, result);
//    }
//
//    @Test
//    public void testGetOrgsForAdmin() throws Exception {
//        ListOrganizationsResponse listOrganizationsResponse = ListOrganizationsResponse.builder().build();
//        when(orgServiceV2.getOrgsForAdmin()).thenReturn(listOrganizationsResponse);
//
//        ListOrganizationsResponse result = orgServiceV2.getOrgsForAdmin();
//        Assert.assertEquals(listOrganizationsResponse, result);
//    }
//
//    @Test
//    public void testGetOrgId() throws Exception {
//        when(orgServiceV2.getOrgId(anyString(), anyString())).thenReturn("msg");
//
//        String result = orgServiceV2.getOrgId("orgName", "token");
//        Assert.assertEquals("msg", result);
//    }
//
//    @Test
//    public void testGetOrgUsingName() throws Exception {
//        OrganizationDetail organizationDetail = OrganizationDetail.builder().id("id").name("name").quota(OrganizationQuota.builder().id("id").instanceMemoryLimit(1024).name("name").organizationId("organizationId").paidServicePlans(true).totalMemoryLimit(10240).totalRoutes(100).totalServiceInstances(100).build()).build();
//        when(orgServiceV2.getOrgUsingName(anyString())).thenReturn(organizationDetail);
//
//        OrganizationDetail result = orgServiceV2.getOrgUsingName("name");
//        Assert.assertEquals(organizationDetail, result);
//    }
//
//    @Test
//    public void testGetOrgUsingName2() throws Exception {
//        OrganizationDetail organizationDetail = OrganizationDetail.builder().id("id").name("name").quota(OrganizationQuota.builder().id("id").instanceMemoryLimit(1024).name("name").organizationId("organizationId").paidServicePlans(true).totalMemoryLimit(10240).totalRoutes(100).totalServiceInstances(100).build()).build();
//        when(orgServiceV2.getOrgUsingName(anyString(), anyString())).thenReturn(organizationDetail);
//
//        OrganizationDetail result = orgServiceV2.getOrgUsingName("name", "token");
//        Assert.assertEquals(organizationDetail, result);
//    }
//
//    @Test
//    public void testRenameOrg() throws Exception {
//        when(orgServiceV2.renameOrg(any(), anyString())).thenReturn(thenReturn);
//
//        Map result = orgServiceV2.renameOrg(new Org(), "token");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testDeleteOrg() throws Exception {
//        when(orgServiceV2.deleteOrg(anyString(), anyBoolean(), anyString())).thenReturn(thenReturn);
//
//        Map result = orgServiceV2.deleteOrg("orgId", true, "token");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testGetOrgSpaces() throws Exception {
//        ListSpacesResponse listSpacesResponse = ListSpacesResponse.builder().build();
//        when(orgServiceV2.getOrgSpaces(anyString(), anyObject())).thenReturn(listSpacesResponse);
//
//        ListSpacesResponse result = orgServiceV2.getOrgSpaces("orgId", null);
//        Assert.assertEquals(listSpacesResponse, result);
//    }
//
//    @Test
//    public void testGetOrgQuota() throws Exception {
//        GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = GetOrganizationQuotaDefinitionResponse.builder().build();
//        when(orgServiceV2.getOrgQuota(anyString(), anyObject())).thenReturn(getOrganizationQuotaDefinitionResponse);
//
//        GetOrganizationQuotaDefinitionResponse result = orgServiceV2.getOrgQuota("orgId", null);
//        Assert.assertEquals(getOrganizationQuotaDefinitionResponse, result);
//    }
//
//    @Test
//    public void testUpdateOrgQuota() throws Exception {
//        when(orgServiceV2.updateOrgQuota(anyString(), any(), anyString())).thenReturn(thenReturn);
//
//        Map result = orgServiceV2.updateOrgQuota("orgId", new Org(), "token");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testListAllOrgUsers() throws Exception {
//        List<UserResource> userResourceList = new ArrayList<>();
//        when(orgServiceV2.listAllOrgUsers(anyString(), anyObject())).thenReturn(userResourceList);
//
//        List<UserResource> result = orgServiceV2.listAllOrgUsers("orgId", null);
//        Assert.assertEquals(userResourceList, result);
//    }
//
//    @Test
//    public void testGetOrgUserRoles() throws Exception {
//        when(orgServiceV2.getOrgUserRoles(anyString(), anyObject())).thenReturn(null);
//
//        Map<String, Collection<UserRole>> result = orgServiceV2.getOrgUserRoles("orgId", null);
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetOrgUserRolesByOrgName() throws Exception {
//        OrganizationUsers organizationUsers = OrganizationUsers.builder().build();
//        when(orgServiceV2.getOrgUserRolesByOrgName(anyString(), anyString())).thenReturn(organizationUsers);
//
//        OrganizationUsers result = orgServiceV2.getOrgUserRolesByOrgName("orgName", "token");
//        Assert.assertEquals(organizationUsers, result);
//    }
//
//    @Test
//    public void testIsOrgManagerUsingOrgName() throws Exception {
//        when(orgServiceV2.isOrgManagerUsingOrgName(anyString(), anyString())).thenReturn(true);
//
//        boolean result = orgServiceV2.isOrgManagerUsingOrgName("orgName", "token");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testIsOrgManagerUsingToken() throws Exception {
//        when(orgServiceV2.isOrgManagerUsingToken("orgId", "token")).thenReturn(true);
//
//        boolean result = orgServiceV2.isOrgManagerUsingToken("orgId", "token");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testIsOrgManager() throws Exception {
//        when(orgServiceV2.isOrgManager(anyString(), anyString())).thenReturn(true);
//
//        boolean result = orgServiceV2.isOrgManager("orgId", "userId");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testIsBillingManager() throws Exception {
//        when(orgServiceV2.isBillingManager(anyString(), anyString())).thenReturn(true);
//
//        boolean result = orgServiceV2.isBillingManager("orgId", "userId");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testIsOrgAuditor() throws Exception {
//        when(orgServiceV2.isOrgAuditor(anyString(), anyString())).thenReturn(true);
//
//        boolean result = orgServiceV2.isOrgAuditor("orgId", "userId");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testAssociateOrgUserRole() throws Exception {
//        when(orgServiceV2.associateOrgUserRole(anyString(), anyString(), anyString(), anyString())).thenReturn(null);
//
//        AbstractOrganizationResource result = orgServiceV2.associateOrgUserRole("orgId", "userId", "role", "token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testRemoveOrgUserRole() throws Exception {
//        orgServiceV2.removeOrgUserRole("orgId", "userId", "role", "token");
//    }
//
//    @Test
//    public void testInviteUser() throws Exception {
//        orgServiceV2.inviteUser("orgId", "userId", "token");
//    }
//
//    @Test
//    public void testCancelInvitionUser() throws Exception {
//        orgServiceV2.cancelInvitionUser();
//    }
//
//    @Test
//    public void testCancelOrganizationMember() throws Exception {
//        when(orgServiceV2.cancelOrganizationMember(anyString(), anyString(), anyString())).thenReturn(true);
//
//        boolean result = orgServiceV2.cancelOrganizationMember("orgId", "userId", "token");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testAssociateOrgUserRole2() throws Exception {
//        when(orgServiceV2.associateOrgUserRole2(anyMap())).thenReturn(true);
//
//        boolean result = orgServiceV2.associateOrgUserRole2(new HashMap() {{
//            put("String", "String");
//        }});
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testGetOrgsForAdminAll() throws Exception {
//        ListOrganizationsResponse listOrganizationsResponse = ListOrganizationsResponse.builder().build();
//        when(orgServiceV2.getOrgsForAdminAll(anyInt())).thenReturn(listOrganizationsResponse);
//
//        ListOrganizationsResponse result = orgServiceV2.getOrgsForAdminAll(0);
//        Assert.assertEquals(listOrganizationsResponse, result);
//    }
//
//    @Test
//    public void testOrgFlag() throws Exception {
//        when(orgServiceV2.orgFlag(anyString(), anyString())).thenReturn(thenReturn);
//
//        Map result = orgServiceV2.orgFlag("flagname", "token");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//
//}
//
