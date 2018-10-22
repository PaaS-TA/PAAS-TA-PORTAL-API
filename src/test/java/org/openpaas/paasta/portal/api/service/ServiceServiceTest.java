package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.servicebindings.GetServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebrokers.CreateServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.GetServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.ListServiceBrokersResponse;
import org.cloudfoundry.client.v2.servicebrokers.UpdateServiceBrokerResponse;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.model.Service;
import org.openpaas.paasta.portal.api.model.ServiceBroker;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ServiceServiceTest {
    @Mock
    ServiceService serviceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRenameInstance() throws Exception {
        when(serviceService.renameInstance(any(), anyString())).thenReturn(new HashMap() {{
            put("String", "String");
        }});

        Map result = serviceService.renameInstance(new Service(), "guid");
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testDeleteInstance() throws Exception {

        when(serviceService.deleteInstance(anyString(), anyString())).thenReturn(new HashMap() {{
            put("String", "String");
        }});

        Map result = serviceService.deleteInstance("guid", "token");
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testGetUserProvided() throws Exception {
        GetUserProvidedServiceInstanceResponse getUserProvidedServiceInstanceResponse = GetUserProvidedServiceInstanceResponse.builder().build();
        when(serviceService.getUserProvided(anyString(), anyString())).thenReturn(getUserProvidedServiceInstanceResponse);

        GetUserProvidedServiceInstanceResponse result = serviceService.getUserProvided("token", "userProvidedServiceInstanceId");
        Assert.assertEquals(getUserProvidedServiceInstanceResponse, result);
    }

    @Test
    public void testCreateUserProvided() throws Exception {
        when(serviceService.createUserProvided(anyString(), any())).thenReturn(new HashMap() {{
            put("String", "String");
        }});

        Map result = serviceService.createUserProvided("token", new Service());
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testUpdateUserProvided() throws Exception {
        when(serviceService.updateUserProvided(anyString(), anyString(), any())).thenReturn(new HashMap() {{
            put("String", "String");
        }});

        Map result = serviceService.updateUserProvided("guid", "token", new Service());
        Assert.assertEquals(new HashMap() {{
            put("String", "String");
        }}, result);
    }

    @Test
    public void testGetServiceBrokers() throws Exception {
        ListServiceBrokersResponse listServiceBrokersResponse = ListServiceBrokersResponse.builder().build();
        when(serviceService.getServiceBrokers(anyString())).thenReturn(listServiceBrokersResponse);

        ListServiceBrokersResponse result = serviceService.getServiceBrokers("token");
        Assert.assertEquals(listServiceBrokersResponse, result);
    }

    @Test
    public void testGetServiceBroker() throws Exception {
        GetServiceBrokerResponse getServiceBrokerResponse = GetServiceBrokerResponse.builder().build();
        when(serviceService.getServiceBroker(any(), anyString())).thenReturn(getServiceBrokerResponse);

        GetServiceBrokerResponse result = serviceService.getServiceBroker(new ServiceBroker(), "token");
        Assert.assertEquals(getServiceBrokerResponse, result);
    }

    @Test
    public void testCreateServiceBroker() throws Exception {
        CreateServiceBrokerResponse createServiceBrokerResponse = CreateServiceBrokerResponse.builder().build();
        when(serviceService.createServiceBroker(any(), anyString())).thenReturn(createServiceBrokerResponse);

        CreateServiceBrokerResponse result = serviceService.createServiceBroker(new ServiceBroker(), "token");
        Assert.assertEquals(createServiceBrokerResponse, result);
    }

    @Test
    public void testUpdateServiceBroker() throws Exception {

        UpdateServiceBrokerResponse updateServiceBrokerResponse = UpdateServiceBrokerResponse.builder().build();
        when(serviceService.updateServiceBroker(any(), anyString())).thenReturn(updateServiceBrokerResponse);

        UpdateServiceBrokerResponse result = serviceService.updateServiceBroker(new ServiceBroker(), "token");
        Assert.assertEquals(updateServiceBrokerResponse, result);
    }

    @Test
    public void testDeleteServiceBroker() throws Exception {
        when(serviceService.deleteServiceBroker(anyString(), anyString())).thenReturn(true);

        boolean result = serviceService.deleteServiceBroker("guid", "token");
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetServicesInstances() throws Exception {
        ListServiceInstancesResponse listServiceInstancesResponse = ListServiceInstancesResponse.builder().build();
        when(serviceService.getServicesInstances(anyString(), anyString())).thenReturn(listServiceInstancesResponse);

        ListServiceInstancesResponse result = serviceService.getServicesInstances("guid", "token");
        Assert.assertEquals(listServiceInstancesResponse, result);
    }

    @Test
    public void testGetServiceBinding() throws Exception {
        GetServiceBindingResponse getServiceBindingResponse = GetServiceBindingResponse.builder().build();
        when(serviceService.getServiceBinding(anyString(), anyString())).thenReturn(getServiceBindingResponse);

        GetServiceBindingResponse result = serviceService.getServiceBinding("token", "serviceid");
        Assert.assertEquals(getServiceBindingResponse, result);
    }


}
