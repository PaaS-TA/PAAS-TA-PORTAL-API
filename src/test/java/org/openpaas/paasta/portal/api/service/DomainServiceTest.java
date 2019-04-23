package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.PaginatedResponse;
import org.cloudfoundry.client.v2.domains.ListDomainsResponse;
import org.cloudfoundry.client.v2.privatedomains.CreatePrivateDomainResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class DomainServiceTest {

    @Mock
    DomainService domainService;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDomains() throws Exception {

        when(domainService.getDomains(anyString())).thenReturn(null);

        PaginatedResponse result = domainService.getDomains("status");
        Assert.assertEquals(null, result);
    }

    @Test
    public void testGetOrgPrivateDomain() throws Exception {
        ListDomainsResponse listDomainsResponse = ListDomainsResponse.builder().build();
        when(domainService.getOrgPrivateDomain(anyString())).thenReturn(listDomainsResponse);

        ListDomainsResponse result = domainService.getOrgPrivateDomain("orguid");
        Assert.assertEquals(listDomainsResponse, result);
    }

    @Test
    public void testAddDomain() throws Exception {

        when(domainService.addDomain(anyString(), anyString(), anyString())).thenReturn(thenReturn);

        Map result = domainService.addDomain("token", "domainName", "orgId");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testAddDomain2() throws Exception {
        when(domainService.addDomain(anyString(), anyString(), anyString(), anyBoolean())).thenReturn(thenReturn);

        Map result = domainService.addDomain("token", "domainName", "orgId", true);
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testAddPrivateDomain() throws Exception {
        CreatePrivateDomainResponse createPrivateDomainResponse = CreatePrivateDomainResponse.builder().build();
        when(domainService.addPrivateDomain(anyString(), anyString())).thenReturn(createPrivateDomainResponse);

        CreatePrivateDomainResponse result = domainService.addPrivateDomain("domainName", "orgId");
        Assert.assertEquals(createPrivateDomainResponse, result);
    }

    @Test
    public void testDeleteDomain() throws Exception {
        when(domainService.deleteDomain(anyString(), anyString())).thenReturn(thenReturn);

        Map result = domainService.deleteDomain("orgId", "domainName");
        Assert.assertEquals(thenReturn, result);
    }


}
