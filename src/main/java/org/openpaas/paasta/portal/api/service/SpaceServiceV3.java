package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.spaces.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.stereotype.Service;

@Service
public class SpaceServiceV3 extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceServiceV3.class);

    /**
     * Space 정보를 가져온다.
     *
     * @param spaceId    the space guid
     * @param  token    user token
     * @return GetSpaceResponse
     * 권한 : 사용자권한
     */
    public GetSpaceResponse getSpace(String spaceId, String token){
        return cloudFoundryClient(tokenProvider(token)).spacesV3().get(GetSpaceRequest.builder().spaceId(spaceId).build()).block();
    }

    /**
     * Space 리스트 정보를 가져온다.
     *
     * @param  organizationId    the organization guid
     * @param  token    user token
     * @return ListSpacesResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public ListSpacesResponse listSpace(String organizationId, String token){
        return this.allSpaceList(cloudFoundryClient(tokenProvider(token)), organizationId);
    }

    /**
     * Space 리스트 정보를 가져온다.
     *
     * @param  organizationId    the organization guid
     * @return ListSpacesResponse
     * 권한 : 관리자 권한
     * @throws Exception the exception
     */
    public ListSpacesResponse listSpaceAdmin(String organizationId){
        return this.allSpaceList(cloudFoundryClient(), organizationId);
    }

    private ListSpacesResponse allSpaceList(ReactorCloudFoundryClient reactorCloudFoundryClient, String orgGuid){
        ListSpacesResponse listSpacesResponse = reactorCloudFoundryClient.spacesV3().list(ListSpacesRequest.builder().organizationId(orgGuid).build()).block();
        int i;
        for(i = 1 ; listSpacesResponse.getPagination().getTotalPages().intValue() > i ; i++){
            listSpacesResponse.getResources().addAll(reactorCloudFoundryClient.spacesV3().list(ListSpacesRequest.builder().page(i+1).build()).block().getResources());
        }
        return listSpacesResponse;
    }

    /**
     * Space명 중복검사를 실행한다.
     * @param  organizationId    the organization guid
     * @param  spaceName                this space name
     * @return boolean
     * 권한 : 사용자
     */
    public boolean isExistSpaceName(String organizationId, String spaceName){
        ListSpacesResponse listSpacesResponse = this.listSpaceAdmin(organizationId);
        long number = listSpacesResponse.getResources().stream().filter(space -> space.getName().equals(spaceName)).count();
        if(number > 0){
            return false;
        }
        return true;
    }

    /**
     * Space 을 생성한다.
     *
     * @param  organizationId    the organization guid
     * @param  name    the space name
     * @param token    user token
     * @return CreateOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public CreateSpaceResponse createSpace(String name,String organizationId, String token){
        return cloudFoundryClient(tokenProvider(token)).spacesV3().create(CreateSpaceRequest.builder().name(name)
                .relationships(SpaceRelationships.builder()
                        .organization(ToOneRelationship.builder()
                                .data(Relationship.builder().id(organizationId).build()).build()).build()).build()).block();
    }

    /**
     * Space Isolation 에 Isolation Segments default 를 설정한다.
     *
     * @param spaceId            the space id
     * @param isolationSegmentId the isolation segement id
     * @return AssignSpaceIsolationSegmentResponse
     * @throws Exception the exception
     */
    public AssignSpaceIsolationSegmentResponse setSpaceDefaultIsolationSegments(String spaceId, String isolationSegmentId) throws Exception {
        return cloudFoundryClient().spacesV3().assignIsolationSegment(AssignSpaceIsolationSegmentRequest.builder().spaceId(spaceId).data(Relationship.builder().id(isolationSegmentId).build()).build()).block();
    }

    /**
     * Space Isolation 에 Isolation Segments default 를 해제한다.
     *
     * @param spaceId the space id
     * @return AssignSpaceIsolationSegmentResponse
     * @throws Exception the exception
     */
    public AssignSpaceIsolationSegmentResponse resetSpaceDefaultIsolationSegments(String spaceId) throws Exception {
        return cloudFoundryClient().spacesV3().assignIsolationSegment(AssignSpaceIsolationSegmentRequest.builder().spaceId(spaceId).build()).block();
    }
}
