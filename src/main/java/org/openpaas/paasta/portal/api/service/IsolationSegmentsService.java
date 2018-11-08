package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.applications.UpdateApplicationRequest;
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.isolationsegments.*;
import org.cloudfoundry.client.v3.organizations.AssignOrganizationDefaultIsolationSegmentRequest;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@EnableAsync
@Service
public class IsolationSegmentsService extends Common {

    /**
     * Segments 할당 리스트를 조회한다.
     *
     * @return GetSecurityGroupResponse
     * @throws Exception the exception
     */
    public ListIsolationSegmentsResponse getIsolationSegments() throws Exception {
        return cloudFoundryClient(connectionContext()).isolationSegments()
                .list(ListIsolationSegmentsRequest.builder()
                        .build()).block();
    }

    /**
     * Segments 할당 리스트를 조회한다. (organizationsId)
     *
     * @param organizationsId  the organizations id
     * @return ListIsolationSegmentsResponse
     * @throws Exception the exception
     */
    public ListIsolationSegmentsResponse getIsolationSegmentsByOrgGuid(String organizationsId) throws Exception {
        return cloudFoundryClient(connectionContext()).isolationSegments()
                .list(ListIsolationSegmentsRequest.builder()
                        .organizationId(organizationsId)
                        .build()).block();
    }

    /**
     * isolationSegmentId 로 org 리스트를 조회한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @return ListIsolationSegmentEntitledOrganizationsResponse
     * @throws Exception the exception
     */
    public ListIsolationSegmentEntitledOrganizationsResponse getIsolationSegmentsOrgs(String isolationSegmentId) throws Exception {
        return cloudFoundryClient(connectionContext()).isolationSegments()
                .listEntitledOrganizations(ListIsolationSegmentEntitledOrganizationsRequest.builder()
                        .isolationSegmentId(isolationSegmentId)
                        .build()).block();
    }

    /**
     * isolationSegmentId 로 org 리스트를 조회한다. (relationships)
     *
     * @param isolationSegmentId  the isolation segement id
     * @return ListIsolationSegmentOrganizationsRelationshipResponse
     * @throws Exception the exception
     */
    public ListIsolationSegmentOrganizationsRelationshipResponse getIsolationSegmentsOrgsRelationships(String isolationSegmentId) throws Exception {
        return cloudFoundryClient(connectionContext()).isolationSegments()
                .listOrganizationsRelationship(ListIsolationSegmentOrganizationsRelationshipRequest.builder()
                        .isolationSegmentId(isolationSegmentId)
                        .build()).block();
    }

    /**
     * isolationSegmentId 로 space 리스트를 조회한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @return ListIsolationSegmentSpacesRelationshipResponse
     * @throws Exception the exception
     */
    public ListIsolationSegmentSpacesRelationshipResponse getIsolationSegmentsSpaces(String isolationSegmentId) throws Exception {
        return cloudFoundryClient(connectionContext()).isolationSegments()
                .listSpacesRelationship(ListIsolationSegmentSpacesRelationshipRequest.builder()
                        .isolationSegmentId(isolationSegmentId)
                        .build()).block();
    }

    /**
     * Isolation Segments를 생성한다.
     *
     * @param segmentName  the segment name
     * @return CreateIsolationSegmentResponse
     * @throws Exception the exception
     */
    public CreateIsolationSegmentResponse createIsolationSegments(String segmentName) throws Exception {
        return cloudFoundryClient(connectionContext()).isolationSegments()
                .create(CreateIsolationSegmentRequest.builder()
                        .name(segmentName)
                        .build()).block();
    }

    /**
     * Isolation Segments를 삭제한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @return DeleteIsolationSegmentResponse
     * @throws Exception the exception
     */
    public Map deleteIsolationSegments(String isolationSegmentId) throws Exception {
        Map resultMap = new HashMap();

        try {
            cloudFoundryClient(connectionContext()).isolationSegments()
                    .delete(DeleteIsolationSegmentRequest.builder()
                            .isolationSegmentId(isolationSegmentId)
                            .build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e.getMessage());
        }

        return resultMap;
    }

    /**
     * Isolation Segments에 조직 권한을 부여한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @param organizationsId  the organizations id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * @throws Exception the exception
     */
    public AddIsolationSegmentOrganizationEntitlementResponse eanbleIsolationSegments(String isolationSegmentId, String organizationsId) throws Exception {
        return cloudFoundryClient(connectionContext()).isolationSegments()
                .addOrganizationEntitlement(AddIsolationSegmentOrganizationEntitlementRequest.builder()
                        .isolationSegmentId(isolationSegmentId)
                        .data(Relationship.builder()
                                .id(organizationsId)
                                .build())
                        .build()).block();
    }

    /**
     * Isolation Segments에 조직 권한을 해제한다.
     *
     * @param isolationSegmentId  the isolation segement id
     * @param organizationsId  the organizations id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * @throws Exception the exception
     */
    public Map disableIsolationSegments(String isolationSegmentId, String organizationsId) throws Exception {
        Map resultMap = new HashMap();

        try {
            cloudFoundryClient(connectionContext()).isolationSegments()
                    .removeOrganizationEntitlement(RemoveIsolationSegmentOrganizationEntitlementRequest.builder()
                            .isolationSegmentId(isolationSegmentId)
                            .organizationId(organizationsId)
                            .build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e.getMessage());
        }

        return resultMap;
    }

}
