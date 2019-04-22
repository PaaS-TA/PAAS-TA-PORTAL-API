package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.organizationquotadefinitions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.model.Quota;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class OrgQuotaServiceTest {

    @Mock
    OrgQuotaService orgQuotaService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOrgQuotaDefinitionsList() throws Exception {
        ListOrganizationQuotaDefinitionsResponse listOrganizationQuotaDefinitionsResponse = ListOrganizationQuotaDefinitionsResponse.builder().build();
        when(orgQuotaService.getOrgQuotaDefinitionsList(anyString())).thenReturn(listOrganizationQuotaDefinitionsResponse);

        ListOrganizationQuotaDefinitionsResponse result = orgQuotaService.getOrgQuotaDefinitionsList("token");
        Assert.assertEquals(listOrganizationQuotaDefinitionsResponse, result);
    }

    @Test
    public void testGetOrgQuotaDefinitions() throws Exception {
        GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = GetOrganizationQuotaDefinitionResponse.builder().build();
        when(orgQuotaService.getOrgQuotaDefinitions(anyString(), anyString())).thenReturn(getOrganizationQuotaDefinitionResponse);

        GetOrganizationQuotaDefinitionResponse result = orgQuotaService.getOrgQuotaDefinitions("quotaGuid", "token");
        Assert.assertEquals(getOrganizationQuotaDefinitionResponse, result);
    }

    @Test
    public void testCreateOrgQuotaDefinitions() throws Exception {
        CreateOrganizationQuotaDefinitionResponse createOrganizationQuotaDefinitionResponse = CreateOrganizationQuotaDefinitionResponse.builder().build();
        when(orgQuotaService.createOrgQuotaDefinitions(any(), anyString())).thenReturn(createOrganizationQuotaDefinitionResponse);

        CreateOrganizationQuotaDefinitionResponse result = orgQuotaService.createOrgQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0), "token");
        Assert.assertEquals(createOrganizationQuotaDefinitionResponse, result);
    }

    @Test
    public void testUpdateOrgQuotaDefinitions() throws Exception {
        UpdateOrganizationQuotaDefinitionResponse updateOrganizationQuotaDefinitionResponse = UpdateOrganizationQuotaDefinitionResponse.builder().build();
        when(orgQuotaService.updateOrgQuotaDefinitions(any(), anyString())).thenReturn(updateOrganizationQuotaDefinitionResponse);

        UpdateOrganizationQuotaDefinitionResponse result = orgQuotaService.updateOrgQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0), "token");
        Assert.assertEquals(updateOrganizationQuotaDefinitionResponse, result);
    }

    @Test
    public void testDeleteOrgQuotaDefinitions() throws Exception {
        DeleteOrganizationQuotaDefinitionResponse deleteOrganizationQuotaDefinitionResponse = DeleteOrganizationQuotaDefinitionResponse.builder().build();
        when(orgQuotaService.deleteOrgQuotaDefinitions(anyString(), anyString())).thenReturn(deleteOrganizationQuotaDefinitionResponse);

        DeleteOrganizationQuotaDefinitionResponse result = orgQuotaService.deleteOrgQuotaDefinitions("quotaGuid", "token");
        Assert.assertEquals(deleteOrganizationQuotaDefinitionResponse, result);
    }

    @Test
    public void testSetOrgQuotaDefinitions() throws Exception {

        when(orgQuotaService.setOrgQuotaDefinitions(any())).thenReturn(true);

        boolean result = orgQuotaService.setOrgQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0));
        Assert.assertEquals(true, result);
    }

}

