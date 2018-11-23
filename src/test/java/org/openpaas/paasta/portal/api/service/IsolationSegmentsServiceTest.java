package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v3.isolationsegments.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*", "javax.crypto.*", "org.openpaas.paasta.portal.api.common.*", "org.openpaas.paasta.portal.api.config.*"})
@TestPropertySource(properties = {"spring.config.location = classpath:/application.yml","eureka.client.enabled=false"}) // Push 용
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IsolationSegmentsServiceTest {

    @Mock
    IsolationSegmentsService isolationSegmentsService;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetIsolationSegments() throws Exception {
        ListIsolationSegmentsResponse listIsolationSegmentsResponse = ListIsolationSegmentsResponse.builder().build();
        when(isolationSegmentsService.getIsolationSegments()).thenReturn(listIsolationSegmentsResponse);

        ListIsolationSegmentsResponse result = isolationSegmentsService.getIsolationSegments();
        Assert.assertEquals(listIsolationSegmentsResponse, result);
    }

    @Test
    public void testGetIsolationSegmentsByOrgGuid() throws Exception {
        ListIsolationSegmentsResponse listIsolationSegmentsResponse = ListIsolationSegmentsResponse.builder().build();
        when(isolationSegmentsService.getIsolationSegmentsByOrgGuid(anyString())).thenReturn(listIsolationSegmentsResponse);

        ListIsolationSegmentsResponse result = isolationSegmentsService.getIsolationSegmentsByOrgGuid(anyString());
        Assert.assertEquals(listIsolationSegmentsResponse, result);
    }

    @Test
    public void testGetIsolationSegmentsOrgs() throws Exception {
        ListIsolationSegmentEntitledOrganizationsResponse listIsolationSegmentEntitledOrganizationsResponse = ListIsolationSegmentEntitledOrganizationsResponse.builder().build();
        when(isolationSegmentsService.getIsolationSegmentsOrgs(anyString())).thenReturn(listIsolationSegmentEntitledOrganizationsResponse);

        ListIsolationSegmentEntitledOrganizationsResponse result = isolationSegmentsService.getIsolationSegmentsOrgs(anyString());
        Assert.assertEquals(listIsolationSegmentEntitledOrganizationsResponse, result);
    }

    @Test
    public void testGetIsolationSegmentsOrgsRelationships() throws Exception {
        ListIsolationSegmentOrganizationsRelationshipResponse listIsolationSegmentOrganizationsRelationshipResponse = ListIsolationSegmentOrganizationsRelationshipResponse.builder().build();
        when(isolationSegmentsService.getIsolationSegmentsOrgsRelationships(anyString())).thenReturn(listIsolationSegmentOrganizationsRelationshipResponse);

        ListIsolationSegmentOrganizationsRelationshipResponse result = isolationSegmentsService.getIsolationSegmentsOrgsRelationships(anyString());
        Assert.assertEquals(listIsolationSegmentOrganizationsRelationshipResponse, result);
    }

    @Test
    public void testGetIsolationSegmentsSpaces() throws Exception {
        ListIsolationSegmentSpacesRelationshipResponse listIsolationSegmentSpacesRelationshipResponse = ListIsolationSegmentSpacesRelationshipResponse.builder().build();
        when(isolationSegmentsService.getIsolationSegmentsSpaces(anyString())).thenReturn(listIsolationSegmentSpacesRelationshipResponse);

        ListIsolationSegmentSpacesRelationshipResponse result = isolationSegmentsService.getIsolationSegmentsSpaces(anyString());
        Assert.assertEquals(listIsolationSegmentSpacesRelationshipResponse, result);
    }

    @Test
    public void testCreateIsolationSegments() throws Exception {
        CreateIsolationSegmentResponse createIsolationSegmentResponse = CreateIsolationSegmentResponse.builder().name("name").createdAt("2018-11-23 23:23:23").id("id").build();
        when(isolationSegmentsService.createIsolationSegments(anyString())).thenReturn(createIsolationSegmentResponse);

        CreateIsolationSegmentResponse result = isolationSegmentsService.createIsolationSegments(anyString());
        Assert.assertEquals(createIsolationSegmentResponse, result);
    }

    @Test
    public void testDeleteIsolationSegments() throws Exception {
        when(isolationSegmentsService.deleteIsolationSegments(anyString())).thenReturn(thenReturn);

        Map result = isolationSegmentsService.deleteIsolationSegments(anyString());
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testEnableIsolationSegments() throws Exception {
        AddIsolationSegmentOrganizationEntitlementResponse addIsolationSegmentOrganizationEntitlementResponse = AddIsolationSegmentOrganizationEntitlementResponse.builder().build();
        when(isolationSegmentsService.enableIsolationSegments(anyString(), anyString())).thenReturn(addIsolationSegmentOrganizationEntitlementResponse);

        AddIsolationSegmentOrganizationEntitlementResponse result = isolationSegmentsService.enableIsolationSegments(anyString(), anyString());
        Assert.assertEquals(addIsolationSegmentOrganizationEntitlementResponse, result);
    }

    @Test
    public void testDisableIsolationSegments() throws Exception {
        when(isolationSegmentsService.disableIsolationSegments(anyString(), anyString())).thenReturn(thenReturn);

        Map result = isolationSegmentsService.disableIsolationSegments(anyString(), anyString());
        Assert.assertEquals(thenReturn, result);
    }
}
