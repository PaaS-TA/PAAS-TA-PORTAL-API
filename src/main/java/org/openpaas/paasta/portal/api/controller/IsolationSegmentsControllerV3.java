package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.isolationsegments.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.IsolationSegmentsServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class IsolationSegmentsControllerV3 extends Common {

    ///////////////////////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 3                                    //////
    //////   Document : http://v3-apidocs.cloudfoundry.org/version/3.69.0/index.html //////
    ///////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    IsolationSegmentsServiceV3 isolationSegmentsServiceV3;

    /**
     * Segments 할당 리스트를 조회한다.
     *
     * @return GetSecurityGroupResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/isolationSegments")
    public ListIsolationSegmentsResponse getIsolationSegments() throws Exception {
        return isolationSegmentsServiceV3.getIsolationSegments();
    }

    /**
     * Segments 할당 리스트를 조회한다. (organizationsId)
     *
     * @param organizationsId the organizations id
     * @return ListIsolationSegmentsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/isolationSegments/organizations/{organizationsId:.+}")
    public ListIsolationSegmentsResponse getIsolationSegmentsByOrgGuid(@PathVariable String organizationsId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsServiceV3.getIsolationSegmentsByOrgGuid(organizationsId);
    }

    /**
     * isolationSegmentId 로 org 리스트를 조회한다.
     *
     * @param isolationSegmentId the isolation segement id
     * @return ListIsolationSegmentEntitledOrganizationsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/isolationSegments/{isolationSegmentId:.+}/organizations")
    public ListIsolationSegmentEntitledOrganizationsResponse getIsolationSegmentsOrgs(@PathVariable String isolationSegmentId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsServiceV3.getIsolationSegmentsOrgs(isolationSegmentId);
    }

    /**
     * isolationSegmentId 로 org 리스트를 조회한다. (relationships)
     *
     * @param isolationSegmentId the isolation segement id
     * @return ListIsolationSegmentOrganizationsRelationshipResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/isolationSegments/{isolationSegmentId:.+}/relationships/organizations")
    public ListIsolationSegmentOrganizationsRelationshipResponse getIsolationSegmentsOrgsRelationships(@PathVariable String isolationSegmentId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsServiceV3.getIsolationSegmentsOrgsRelationships(isolationSegmentId);
    }

    /**
     * TODO 미사용
     * isolationSegmentId 로 space 리스트를 조회한다.
     *
     * @param isolationSegmentId the isolation segement id
     * @return ListIsolationSegmentSpacesRelationshipResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/isolationSegments/{isolationSegmentId:.+}/spaces")
    public ListIsolationSegmentSpacesRelationshipResponse getIsolationSegmentsSpaces(@PathVariable String isolationSegmentId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsServiceV3.getIsolationSegmentsSpaces(isolationSegmentId);
    }

    /**
     * Isolation Segments를 생성한다.
     *
     * @param segmentName the segment name
     * @return CreateIsolationSegmentResponse
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL + "/isolationSegments/{segmentName:.+}")
    public CreateIsolationSegmentResponse createIsolationSegments(@PathVariable String segmentName, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsServiceV3.createIsolationSegments(segmentName);
    }

    /**
     * Isolation Segments를 삭제한다.
     *
     * @param isolationSegmentId the isolation segement id
     * @return Map
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL + "/isolationSegments/{isolationSegmentId:.+}")
    public Map deleteIsolationSegments(@PathVariable String isolationSegmentId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsServiceV3.deleteIsolationSegments(isolationSegmentId);
    }

    /**
     * Isolation Segments에 조직 권한을 부여한다.
     *
     * @param isolationSegmentId the isolation segement id
     * @param organizationsId    the organizations id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL + "/isolationSegments/{isolationSegmentId:.+}/organizations/{organizationsId:.+}")
    public AddIsolationSegmentOrganizationEntitlementResponse enableIsolationSegments(@PathVariable String isolationSegmentId, @PathVariable String organizationsId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsServiceV3.enableIsolationSegments(isolationSegmentId, organizationsId);
    }

    /**
     * Isolation Segments에 조직 권한을 해제한다.
     *
     * @param isolationSegmentId the isolation segement id
     * @param organizationsId    the organizations id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL + "/isolationSegments/{isolationSegmentId:.+}/organizations/{organizationsId:.+}")
    public Map disableIsolationSegments(@PathVariable String isolationSegmentId, @PathVariable String organizationsId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return isolationSegmentsServiceV3.disableIsolationSegments(isolationSegmentId, organizationsId);
    }
}
