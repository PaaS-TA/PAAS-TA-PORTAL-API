package org.openpaas.paasta.portal.api.service;



import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.organizations.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@EnableAsync
@Service
public class OrgServiceV3 extends Common {
    private final Logger LOGGER = getLogger(this.getClass());

    /**
     * Organizations 정보를 가져온다.
     *
     * @param guid    the organization id
     * @param token    user token
     * @return GetOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public GetOrganizationResponse getOrg(String guid, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.organizationsV3().get(GetOrganizationRequest.builder().organizationId(guid).build()).block();
    }

    /**
     * Organizations 리스트 정보를 가져온다.
     * @param  token    user token
     * @return ListOrganizationsResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public  ListOrganizationsResponse listOrg(String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        ListOrganizationsResponse listOrganizationsResponse = reactorCloudFoundryClient.organizationsV3().list(ListOrganizationsRequest.builder().build()).block();
        int i;
        for(i = 1 ; listOrganizationsResponse.getPagination().getTotalPages().intValue() > i ; i++){
            listOrganizationsResponse.getResources().addAll(reactorCloudFoundryClient.organizationsV3().list(ListOrganizationsRequest.builder().page(i+1).build()).block().getResources());
        }
        return listOrganizationsResponse;
    }

    /**
     * Organizations 을 생성한다.
     *
     * @param name    the organization name
     * @param token    user token
     * @return CreateOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public CreateOrganizationResponse createOrg(String name, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.organizationsV3().create(CreateOrganizationRequest.builder().name(name).build()).block();
    }

    /**
     * Organizations 에 Isolation Segments default 를 설정한다.
     *
     * @param organizationsId    the organizations id
     * @param isolationSegmentId the isolation segement id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * 권한 : 관리자권한
     * @throws Exception the exception
     */
    public AssignOrganizationDefaultIsolationSegmentResponse setOrgDefaultIsolationSegments(String organizationsId, String isolationSegmentId) throws Exception {
        return cloudFoundryClient().organizationsV3().assignDefaultIsolationSegment(AssignOrganizationDefaultIsolationSegmentRequest.builder().organizationId(organizationsId).data(Relationship.builder().id(isolationSegmentId).build()).build()).block();
    }

    /**
     * Organizations 에 Isolation Segments default 를 재설정한다.
     *
     * @param organizationsId the organizations id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * 권한 : 관리자권한
     * @throws Exception the exception
     */
    public AssignOrganizationDefaultIsolationSegmentResponse resetOrgDefaultIsolationSegments(String organizationsId) throws Exception {
        return cloudFoundryClient().organizationsV3().assignDefaultIsolationSegment(AssignOrganizationDefaultIsolationSegmentRequest.builder().organizationId(organizationsId).build()).block();
    }

}
