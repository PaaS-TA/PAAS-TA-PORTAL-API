package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.spacequotadefinitions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.model.Quota;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class SpaceQuotaServiceTest {

    @Mock
    SpaceQuotaService spaceQuotaService;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSpaceQuotaDefinitionsList() throws Exception {
        ListSpaceQuotaDefinitionsResponse listSpaceQuotaDefinitionsResponse = ListSpaceQuotaDefinitionsResponse.builder().build();
        when(spaceQuotaService.getSpaceQuotaDefinitionsList(anyString())).thenReturn(listSpaceQuotaDefinitionsResponse);

        ListSpaceQuotaDefinitionsResponse result = spaceQuotaService.getSpaceQuotaDefinitionsList("token");
        Assert.assertEquals(listSpaceQuotaDefinitionsResponse, result);
    }

    @Test
    public void testGetSpaceQuotaDefinitions() throws Exception {
        GetSpaceQuotaDefinitionResponse getSpaceQuotaDefinitionResponse = GetSpaceQuotaDefinitionResponse.builder().build();
        when(spaceQuotaService.getSpaceQuotaDefinitions(anyString(),anyString())).thenReturn(getSpaceQuotaDefinitionResponse);

        GetSpaceQuotaDefinitionResponse result = spaceQuotaService.getSpaceQuotaDefinitions("spaceQuotaId", "token");
        Assert.assertEquals(getSpaceQuotaDefinitionResponse, result);
    }

    @Test
    public void testCreateSpaceQuotaDefinitions() throws Exception {
        CreateSpaceQuotaDefinitionResponse createSpaceQuotaDefinitionResponse = CreateSpaceQuotaDefinitionResponse.builder().build();
        when(spaceQuotaService.createSpaceQuotaDefinitions(any(),anyString())).thenReturn(createSpaceQuotaDefinitionResponse);

        CreateSpaceQuotaDefinitionResponse result = spaceQuotaService.createSpaceQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0), "token");
        Assert.assertEquals(createSpaceQuotaDefinitionResponse, result);
    }

    @Test
    public void testDeleteSpaceQuotaDefinitions() throws Exception {
        DeleteSpaceQuotaDefinitionResponse deleteSpaceQuotaDefinitionResponse = DeleteSpaceQuotaDefinitionResponse.builder().build();
        when(spaceQuotaService.deleteSpaceQuotaDefinitions(anyString(),anyString())).thenReturn(deleteSpaceQuotaDefinitionResponse);

        DeleteSpaceQuotaDefinitionResponse result = spaceQuotaService.deleteSpaceQuotaDefinitions("guid", "token");
        Assert.assertEquals(deleteSpaceQuotaDefinitionResponse, result);
    }

    @Test
    public void testAssociateSpaceQuotaDefinitions() throws Exception {
        AssociateSpaceQuotaDefinitionResponse associateSpaceQuotaDefinitionResponse = AssociateSpaceQuotaDefinitionResponse.builder().build();
        when(spaceQuotaService.associateSpaceQuotaDefinitions(any(),anyString())).thenReturn(associateSpaceQuotaDefinitionResponse);

        AssociateSpaceQuotaDefinitionResponse result = spaceQuotaService.associateSpaceQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0), "token");
        Assert.assertEquals(associateSpaceQuotaDefinitionResponse, result);
    }

    @Test
    public void testGetListSpaceUsedSpaceQuotaDefinitions() throws Exception {
        ListSpaceQuotaDefinitionSpacesResponse listSpaceQuotaDefinitionSpacesResponse = ListSpaceQuotaDefinitionSpacesResponse.builder().build();
        when(spaceQuotaService.getListSpaceUsedSpaceQuotaDefinitions(anyString(),anyString())).thenReturn(listSpaceQuotaDefinitionSpacesResponse);

        ListSpaceQuotaDefinitionSpacesResponse result = spaceQuotaService.getListSpaceUsedSpaceQuotaDefinitions("guid", "token");
        Assert.assertEquals(listSpaceQuotaDefinitionSpacesResponse, result);
    }

    @Test
    public void testRemoveSpaceQuotaDefinitionsFromSpace() throws Exception {
        when(spaceQuotaService.removeSpaceQuotaDefinitionsFromSpace(any(),anyString())).thenReturn(true);

        boolean result = spaceQuotaService.removeSpaceQuotaDefinitionsFromSpace(new Quota("name", null, true, 0, 0, 0, 0, 0, 0), "token");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testUpdateSpaceQuotaDefinitions() throws Exception {
        UpdateSpaceQuotaDefinitionResponse updateSpaceQuotaDefinitionResponse = UpdateSpaceQuotaDefinitionResponse.builder().build();
        when(spaceQuotaService.updateSpaceQuotaDefinitions(any(),anyString())).thenReturn(updateSpaceQuotaDefinitionResponse);

        UpdateSpaceQuotaDefinitionResponse result = spaceQuotaService.updateSpaceQuotaDefinitions(new Quota("name", null, true, 0, 0, 0, 0, 0, 0), "token");
        Assert.assertEquals(updateSpaceQuotaDefinitionResponse, result);
    }

}
