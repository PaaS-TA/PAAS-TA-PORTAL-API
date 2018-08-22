package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.uaa.clients.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ClientServiceTest {

    @Mock
    ClientService clientService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetClientList() throws Exception {
        ListClientsResponse listClientsResponse = ListClientsResponse.builder().itemsPerPage(1).startIndex(1).totalResults(1).build();
        when(clientService.getClientList()).thenReturn(listClientsResponse);

        ListClientsResponse result = clientService.getClientList();
        Assert.assertEquals(listClientsResponse, result);
    }

    @Test
    public void testGetClient() throws Exception {
        GetClientResponse getClientResponse = GetClientResponse.builder().clientId("clientId").build();
        when(clientService.getClient(anyString())).thenReturn(getClientResponse);

        GetClientResponse result = clientService.getClient("clientId");
        Assert.assertEquals(getClientResponse, result);
    }

    @Test
    public void testRegisterClient() throws Exception {
        CreateClientResponse createClientResponse = CreateClientResponse.builder().clientId("clientId").build();
        when(clientService.registerClient(anyMap())).thenReturn(createClientResponse);

        CreateClientResponse result = clientService.registerClient(new HashMap<String, Object>() {{
            put("String", "String");
        }});
        Assert.assertEquals(createClientResponse, result);
    }

    @Test
    public void testUpdateClient() throws Exception {
        UpdateClientResponse clientResponse = UpdateClientResponse.builder().clientId("clientId").build();
        when(clientService.updateClient(anyMap())).thenReturn(clientResponse);

        UpdateClientResponse result = clientService.updateClient(new HashMap<String, Object>() {{
            put("String", "String");
        }});
        Assert.assertEquals(clientResponse, result);
    }

    @Test
    public void testDeleteClient() throws Exception {
        DeleteClientResponse deleteClientResponse = DeleteClientResponse.builder().clientId("clientId").build();
        when(clientService.deleteClient(anyString())).thenReturn(deleteClientResponse);

        DeleteClientResponse result = clientService.deleteClient("clientId");
        Assert.assertEquals(deleteClientResponse, result);
    }


}

