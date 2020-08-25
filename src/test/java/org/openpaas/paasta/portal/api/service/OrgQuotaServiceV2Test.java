//package org.openpaas.paasta.portal.api.service;
//
//import org.cloudfoundry.client.v2.organizationquotadefinitions.*;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.openpaas.paasta.portal.api.model.Quota;
//
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//
//public class OrgQuotaServiceV2Test {
//
//    @Mock
//    OrgQuotaServiceV2 orgQuotaServiceV2;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testGetOrgQuotaDefinitionsList() throws Exception {
//        ListOrganizationQuotaDefinitionsResponse listOrganizationQuotaDefinitionsResponse = ListOrganizationQuotaDefinitionsResponse.builder().build();
//        when(orgQuotaServiceV2.getOrgQuotaDefinitionsList(anyString())).thenReturn(listOrganizationQuotaDefinitionsResponse);
//
//        ListOrganizationQuotaDefinitionsResponse result = orgQuotaServiceV2.getOrgQuotaDefinitionsList("token");
//        Assert.assertEquals(listOrganizationQuotaDefinitionsResponse, result);
//    }
//
//    @Test
//    public void testGetOrgQuotaDefinitions() throws Exception {
//        GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = GetOrganizationQuotaDefinitionResponse.builder().build();
//        when(orgQuotaServiceV2.getOrgQuotaDefinitions(anyString(), anyString())).thenReturn(getOrganizationQuotaDefinitionResponse);
//
//        GetOrganizationQuotaDefinitionResponse result = orgQuotaServiceV2.getOrgQuotaDefinitions("quotaGuid", "token");
//        Assert.assertEquals(getOrganizationQuotaDefinitionResponse, result);
//    }
//
//    @Test
//    public void testCreateOrgQuotaDefinitions() throws Exception {
//        CreateOrganizationQuotaDefinitionResponse createOrganizationQuotaDefinitionResponse = CreateOrganizationQuotaDefinitionResponse.builder().build();
//        when(orgQuotaServiceV2.createOrgQuotaDefinitions(any(), anyString())).thenReturn(createOrganizationQuotaDefinitionResponse);
//
//        CreateOrganizationQuotaDefinitionResponse result = orgQuotaServiceV2.createOrgQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0), "token");
//        Assert.assertEquals(createOrganizationQuotaDefinitionResponse, result);
//    }
//
//    @Test
//    public void testUpdateOrgQuotaDefinitions() throws Exception {
//        UpdateOrganizationQuotaDefinitionResponse updateOrganizationQuotaDefinitionResponse = UpdateOrganizationQuotaDefinitionResponse.builder().build();
//        when(orgQuotaServiceV2.updateOrgQuotaDefinitions(any(), anyString())).thenReturn(updateOrganizationQuotaDefinitionResponse);
//
//        UpdateOrganizationQuotaDefinitionResponse result = orgQuotaServiceV2.updateOrgQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0), "token");
//        Assert.assertEquals(updateOrganizationQuotaDefinitionResponse, result);
//    }
//
//    @Test
//    public void testDeleteOrgQuotaDefinitions() throws Exception {
//        DeleteOrganizationQuotaDefinitionResponse deleteOrganizationQuotaDefinitionResponse = DeleteOrganizationQuotaDefinitionResponse.builder().build();
//        when(orgQuotaServiceV2.deleteOrgQuotaDefinitions(anyString(), anyString())).thenReturn(deleteOrganizationQuotaDefinitionResponse);
//
//        DeleteOrganizationQuotaDefinitionResponse result = orgQuotaServiceV2.deleteOrgQuotaDefinitions("quotaGuid", "token");
//        Assert.assertEquals(deleteOrganizationQuotaDefinitionResponse, result);
//    }
//
//    @Test
//    public void testSetOrgQuotaDefinitions() throws Exception {
//
//        when(orgQuotaServiceV2.setOrgQuotaDefinitions(any())).thenReturn(true);
//
//        boolean result = orgQuotaServiceV2.setOrgQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0));
//        Assert.assertEquals(true, result);
//    }
//
//}
//
