package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.applications.ListApplicationsResponse;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingResponse;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansResponse;
import org.cloudfoundry.client.v2.services.ListServicesResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Catalog;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@PrepareForTest(Common.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CatalogServiceV2Test {

    @Mock
    CatalogServiceV2 catalogServiceV2;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetCatalogServicePlanList() throws Exception {
        ListServicePlansResponse listServicePlansResponse = ListServicePlansResponse.builder().build();
        when(catalogServiceV2.getCatalogServicePlanList(anyString(), anyString())).thenReturn(listServicePlansResponse);

        ListServicePlansResponse result = catalogServiceV2.getCatalogServicePlanList("servicename", "token");
        Assert.assertEquals(listServicePlansResponse, result);
    }

    @Test
    public void testGetCatalogAppList() throws Exception {
        ListApplicationsResponse listApplicationsResponse = ListApplicationsResponse.builder().build();
        when(catalogServiceV2.getCatalogAppList(anyString(), anyString(), anyString())).thenReturn(listApplicationsResponse);

        ListApplicationsResponse result = catalogServiceV2.getCatalogAppList("orgid", "spaceid", "token");
        Assert.assertEquals(listApplicationsResponse, result);
    }

    @Test
    public void testGetCheckCatalogApplicationNameExists() throws Exception {

        when(catalogServiceV2.getCheckCatalogApplicationNameExists(anyString(), anyString(), anyString(), anyString(), any())).thenReturn(thenReturn);

        Map<String, Object> result = catalogServiceV2.getCheckCatalogApplicationNameExists("name", "orgid", "spaceid", "token", null);
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testCreateApp() throws Exception {
        when(catalogServiceV2.createApp(any(), anyString(), anyString(), any())).thenReturn(thenReturn);

        Map<String, Object> result = catalogServiceV2.createApp(new Catalog(), "token", "token2", null);
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testCreateAppTemplate() throws Exception {

        when(catalogServiceV2.createAppTemplate(any(), anyString(), anyString(), any())).thenReturn(thenReturn);

        Map<String, Object> result = catalogServiceV2.createAppTemplate(new Catalog(), "token", "token2", null);
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testProcCatalogCreateServiceInstanceV2() throws Exception {
        when(catalogServiceV2.procCatalogCreateServiceInstanceV2(any(), anyObject())).thenReturn(thenReturn);

        Map result = catalogServiceV2.procCatalogCreateServiceInstanceV2(new Catalog(), null);
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testProcCatalogBindService() throws Exception {
        CreateServiceBindingResponse createServiceBindingResponse = CreateServiceBindingResponse.builder().build();
        when(catalogServiceV2.procCatalogBindService(any(), anyObject())).thenReturn(createServiceBindingResponse);

        CreateServiceBindingResponse result = catalogServiceV2.procCatalogBindService(new Catalog(), null);
        Assert.assertEquals(createServiceBindingResponse, result);
    }

    @Test
    public void testListServiceInstancesResponse() throws Exception {
        ListServiceInstancesResponse listServiceInstancesResponse = ListServiceInstancesResponse.builder().build();
        when(catalogServiceV2.listServiceInstancesResponse(anyString(), anyString(), anyString())).thenReturn(listServiceInstancesResponse);

        ListServiceInstancesResponse result = catalogServiceV2.listServiceInstancesResponse("orgid", "spaceid", "token");
        Assert.assertEquals(listServiceInstancesResponse, result);
    }

    @Test
    public void testGetService() throws Exception {
        ListServicesResponse listServicesResponse = ListServicesResponse.builder().build();
        when(catalogServiceV2.getService()).thenReturn(listServicesResponse);

        ListServicesResponse result = catalogServiceV2.getService();
        Assert.assertEquals(listServicesResponse, result);
    }

}
