package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.servicebindings.GetServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebrokers.CreateServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.GetServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.ListServiceBrokersResponse;
import org.cloudfoundry.client.v2.servicebrokers.UpdateServiceBrokerResponse;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.model.Service;
import org.openpaas.paasta.portal.api.model.ServiceBroker;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ServiceServiceV2Test {
    @Mock
    ServiceServiceV2 serviceServiceV2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRenameInstance() throws Exception {
        when(serviceServiceV2.renameInstance(any(), anyString())).thenReturn(new HashMap() {{
            put("String", "String");
        }});

        Map result = serviceServiceV2.renameInstance(new Service(), "guid");
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testDeleteInstance() throws Exception {

        when(serviceServiceV2.deleteInstance(anyString(), anyString())).thenReturn(new HashMap() {{
            put("String", "String");
        }});

        Map result = serviceServiceV2.deleteInstance("guid", "token");
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testGetUserProvided() throws Exception {
        GetUserProvidedServiceInstanceResponse getUserProvidedServiceInstanceResponse = GetUserProvidedServiceInstanceResponse.builder().build();
        when(serviceServiceV2.getUserProvided(anyString(), anyString())).thenReturn(getUserProvidedServiceInstanceResponse);

        GetUserProvidedServiceInstanceResponse result = serviceServiceV2.getUserProvided("token", "userProvidedServiceInstanceId");
        Assert.assertEquals(getUserProvidedServiceInstanceResponse, result);
    }

    @Test
    public void testCreateUserProvided() throws Exception {
        when(serviceServiceV2.createUserProvided(anyString(), any())).thenReturn(new HashMap() {{
            put("String", "String");
        }});

        Map result = serviceServiceV2.createUserProvided("token", new Service());
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testUpdateUserProvided() throws Exception {
        when(serviceServiceV2.updateUserProvided(anyString(), anyString(), any())).thenReturn(new HashMap() {{
            put("String", "String");
        }});

        Map result = serviceServiceV2.updateUserProvided("guid", "token", new Service());
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testGetServiceBrokers() throws Exception {
        ListServiceBrokersResponse listServiceBrokersResponse = ListServiceBrokersResponse.builder().build();
        when(serviceServiceV2.getServiceBrokers(anyString())).thenReturn(listServiceBrokersResponse);

        ListServiceBrokersResponse result = serviceServiceV2.getServiceBrokers("token");
        Assert.assertEquals(listServiceBrokersResponse, result);
    }

    @Test
    public void testGetServiceBroker() throws Exception {
        GetServiceBrokerResponse getServiceBrokerResponse = GetServiceBrokerResponse.builder().build();
        when(serviceServiceV2.getServiceBroker(any(), anyString())).thenReturn(getServiceBrokerResponse);

        GetServiceBrokerResponse result = serviceServiceV2.getServiceBroker(new ServiceBroker(), "token");
        Assert.assertEquals(getServiceBrokerResponse, result);
    }

    @Test
    public void testCreateServiceBroker() throws Exception {
        CreateServiceBrokerResponse createServiceBrokerResponse = CreateServiceBrokerResponse.builder().build();
        when(serviceServiceV2.createServiceBroker(any(), anyString())).thenReturn(createServiceBrokerResponse);

        CreateServiceBrokerResponse result = serviceServiceV2.createServiceBroker(new ServiceBroker(), "token");
        Assert.assertEquals(createServiceBrokerResponse, result);
    }

    @Test
    public void testUpdateServiceBroker() throws Exception {

        UpdateServiceBrokerResponse updateServiceBrokerResponse = UpdateServiceBrokerResponse.builder().build();
        when(serviceServiceV2.updateServiceBroker(any(), anyString())).thenReturn(updateServiceBrokerResponse);

        UpdateServiceBrokerResponse result = serviceServiceV2.updateServiceBroker(new ServiceBroker(), "token");
        Assert.assertEquals(updateServiceBrokerResponse, result);
    }

    @Test
    public void testDeleteServiceBroker() throws Exception {
        when(serviceServiceV2.deleteServiceBroker(anyString(), anyString())).thenReturn(true);

        boolean result = serviceServiceV2.deleteServiceBroker("guid", "token");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetServicesInstances() throws Exception {
        ListServiceInstancesResponse listServiceInstancesResponse = ListServiceInstancesResponse.builder().build();
        when(serviceServiceV2.getServicesInstances(anyString())).thenReturn(listServiceInstancesResponse);

        ListServiceInstancesResponse result = serviceServiceV2.getServicesInstances("guid");
        Assert.assertEquals(listServiceInstancesResponse, result);
    }

    @Test
    public void testGetServiceBinding() throws Exception {
        GetServiceBindingResponse getServiceBindingResponse = GetServiceBindingResponse.builder().build();
        when(serviceServiceV2.getServiceBinding(anyString(), anyString())).thenReturn(getServiceBindingResponse);

        GetServiceBindingResponse result = serviceServiceV2.getServiceBinding("token", "serviceid");
        Assert.assertEquals(getServiceBindingResponse, result);
    }


}
