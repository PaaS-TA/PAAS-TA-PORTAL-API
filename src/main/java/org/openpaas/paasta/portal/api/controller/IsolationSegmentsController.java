package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.isolationsegments.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.IsolationSegmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class IsolationSegmentsController extends Common {

    @Autowired
    IsolationSegmentsService isolationSegmentsService;

    /**
     * Segments 할당 리스트를 조회한다.
     *
     * @return GetSecurityGroupResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/isolationSegments")
    public ListIsolationSegmentsResponse getIsolationSegments() throws Exception {
        return isolationSegmentsService.getIsolationSegments();
    }

    /**
     * Segments 할당 리스트를 조회한다. (organizationsId)
     *
     * @param organizationsId  the organizations id
     * @return ListIsolationSegmentsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/isolationSegments/organizations/{organizationsId:.+}")
    public ListIsolationSegmentsResponse getIsolationSegmentsByOrgGuid(@PathVariable String organizationsId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsService.getIsolationSegmentsByOrgGuid(organizationsId);
    }

    /**
     * isolationSegmentId 로 org 리스트를 조회한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @return ListIsolationSegmentEntitledOrganizationsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/isolationSegments/{isolationSegmentId:.+}/organizations")
    public ListIsolationSegmentEntitledOrganizationsResponse getIsolationSegmentsOrgs(@PathVariable String isolationSegmentId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsService.getIsolationSegmentsOrgs(isolationSegmentId);
    }

    /**
     * isolationSegmentId 로 org 리스트를 조회한다. (relationships)
     *
     * @param isolationSegmentId  the isolation segement id
     * @return ListIsolationSegmentOrganizationsRelationshipResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/isolationSegments/{isolationSegmentId:.+}/relationships/organizations")
    public ListIsolationSegmentOrganizationsRelationshipResponse getIsolationSegmentsOrgsRelationships(@PathVariable String isolationSegmentId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsService.getIsolationSegmentsOrgsRelationships(isolationSegmentId);
    }

    /**
     * TODO 미사용
     * isolationSegmentId 로 space 리스트를 조회한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @return ListIsolationSegmentSpacesRelationshipResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/isolationSegments/{isolationSegmentId:.+}/spaces")
    public ListIsolationSegmentSpacesRelationshipResponse getIsolationSegmentsSpaces(@PathVariable String isolationSegmentId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsService.getIsolationSegmentsSpaces(isolationSegmentId);
    }

    /**
     * Isolation Segments를 생성한다.
     *
     * @param segementName  the segement name
     * @return CreateIsolationSegmentResponse
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL+"/isolationSegments/{segementName:.+}")
    public CreateIsolationSegmentResponse createIsolationSegments(@PathVariable String segementName, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsService.createIsolationSegments(segementName);
    }

    /**
     * Isolation Segments를 삭제한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @return Map
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL+"/isolationSegments/{isolationSegmentId:.+}")
    public Map deleteIsolationSegments(@PathVariable String isolationSegmentId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsService.deleteIsolationSegments(isolationSegmentId);
    }

    /**
     * Isolation Segments에 조직 권한을 부여한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @param organizationsId  the organizations id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL+"/isolationSegments/{isolationSegmentId:.+}/organizations/{organizationsId:.+}")
    public AddIsolationSegmentOrganizationEntitlementResponse eanbleIsolationSegments(@PathVariable String isolationSegmentId, @PathVariable String organizationsId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsService.eanbleIsolationSegments(isolationSegmentId, organizationsId);
    }

    /**
     * Isolation Segments에 조직 권한을 해제한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @param organizationsId  the organizations id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL+"/isolationSegments/{isolationSegmentId:.+}/organizations/{organizationsId:.+}")
    public Map disableIsolationSegments(@PathVariable String isolationSegmentId, @PathVariable String organizationsId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsService.disableIsolationSegments(isolationSegmentId, organizationsId);
    }
}
