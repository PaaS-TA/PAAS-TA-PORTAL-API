//package org.openpaas.paasta.portal.api.service;
//
//import org.cloudfoundry.client.lib.CloudCredentials;
//import org.cloudfoundry.client.lib.CloudFoundryClient;
//import org.cloudfoundry.client.v2.servicebindings.GetServiceBindingResponse;
//import org.cloudfoundry.client.v2.servicebrokers.CreateServiceBrokerResponse;
//import org.cloudfoundry.client.v2.servicebrokers.GetServiceBrokerResponse;
//import org.cloudfoundry.client.v2.servicebrokers.ListServiceBrokersResponse;
//import org.cloudfoundry.client.v2.servicebrokers.UpdateServiceBrokerResponse;
//import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
//import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
//import org.cloudfoundry.identity.uaa.api.common.UaaConnection;
//import org.cloudfoundry.identity.uaa.api.group.UaaGroupOperations;
//import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
//import org.cloudfoundry.reactor.DefaultConnectionContext;
//import org.cloudfoundry.reactor.TokenProvider;
//import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
//import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
//import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
//import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
//import org.openpaas.paasta.portal.api.model.Service;
//import org.openpaas.paasta.portal.api.model.ServiceBroker;
//import org.slf4j.Logger;
//import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.*;
//
//public class ServiceServiceTest {
//    @Mock
//    Logger LOGGER;
//    @Mock
//    LoginService loginService;
//    //Field connectionContext of type DefaultConnectionContext - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    //Field tokenProvider of type PasswordGrantTokenProvider - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    @Mock
//    ObjectMapper objectMapper;
//    @Mock
//    ThreadLocal<DefaultConnectionContext> connectionContextThreadLocal;
//    @InjectMocks
//    ServiceService serviceService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testRenameInstance() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        Map result = serviceService.renameInstance(new Service(), "guid");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testDeleteInstance() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        Map result = serviceService.deleteInstance("guid");
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testGetUserProvided() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        GetUserProvidedServiceInstanceResponse result = serviceService.getUserProvided("token", "userProvidedServiceInstanceId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCreateUserProvided() throws Exception {
//        when(loginService.stringNullCheck(anyVararg())).thenReturn(true);
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = serviceService.createUserProvided("token", new Service());
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testUpdateUserProvided() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        Map result = serviceService.updateUserProvided("guid", "token", new Service());
//        Assert.assertEquals(new HashMap() {{
//            put("String", "String");
//        }}, result);
//    }
//
//    @Test
//    public void testGetServiceBrokers() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider()).thenReturn(null);
//
//        ListServiceBrokersResponse result = serviceService.getServiceBrokers("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetServiceBroker() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider()).thenReturn(null);
//
//        GetServiceBrokerResponse result = serviceService.getServiceBroker(new ServiceBroker(), "token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCreateServiceBroker() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider()).thenReturn(null);
//
//        CreateServiceBrokerResponse result = serviceService.createServiceBroker(new ServiceBroker(), "token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUpdateServiceBroker() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider()).thenReturn(null);
//
//        UpdateServiceBrokerResponse result = serviceService.updateServiceBroker(new ServiceBroker(), "token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testDeleteServiceBroker() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider()).thenReturn(null);
//
//        boolean result = serviceService.deleteServiceBroker("guid", "token");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testGetServicesInstances() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider()).thenReturn(null);
//
//        ListServiceInstancesResponse result = serviceService.getServicesInstances("guid", "token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetServiceBinding() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        GetServiceBindingResponse result = serviceService.getServiceBinding("token", "serviceid");
//        Assert.assertEquals(null, result);
//    }
//
//
//}
