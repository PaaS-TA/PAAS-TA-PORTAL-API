package org.openpaas.paasta.portal.api.controller;



import org.cloudfoundry.client.v3.spaces.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.SpaceServiceV3;
import org.openpaas.paasta.portal.api.service.UserServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by cheolhan on 2019-04-24.
 */
@RestController
public class SpaceControllerV3 extends Common {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////                            * CLOUD FOUNDRY CLIENT API VERSION 3                                      //////
    //////   Document : https://v3-apidocs.cloudfoundry.org/version/3.69.0/index.html#spaces                    //////
    //////                                     Not-implemented                                                  //////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceControllerV3.class);

    @Autowired
    SpaceServiceV3 spaceServiceV3;

    /**
     * Space 정보를 가져온다.
     *
     * @param spaceId    the space guid
     * @param  token    user token
     * @return GetSpaceResponse
     * 권한 : 사용자권한
     */
    @GetMapping(Constants.V3_URL+"/spaces/{spaceId:.+}")
    public GetSpaceResponse getSpace(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return spaceServiceV3.getSpace(spaceId, token);
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
    @GetMapping(Constants.V3_URL+"/spaces/{organizationId:.+}")
    public ListSpacesResponse listSpace(@PathVariable String organizationId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return spaceServiceV3.listSpace(token,organizationId);
    }

    /**
     * Space 리스트 정보를 가져온다.
     *
     * @param  organizationId    the organization guid
     * @return ListSpacesResponse
     * 권한 : 관리자 권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/spaces-admin/{organizationId:.+}")
    public ListSpacesResponse listSpaceAdmin(@PathVariable String organizationId){
        return spaceServiceV3.listSpaceAdmin(organizationId);
    }

    /**
     * Space명 중복검사를 실행한다.
     * @param  organizationId    the organization guid
     * @param  spaceName                this space name
     * @return boolean
     * 권한 : 사용자
     */
    @GetMapping(Constants.V3_URL + "/spaces/{organizationId:.+}/{spaceName:.+}/exist")
    public boolean isExistSpaceName(@PathVariable String organizationId, @PathVariable String spaceName) {
        return spaceServiceV3.isExistSpaceName(organizationId,spaceName);
    }

    /**
     * Space 을 생성한다.
     *
     * @param  guid    the organization guid
     * @param  name    the space name
     * @param token    user token
     * @return CreateOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL+"/spaces")
    public CreateSpaceResponse createSpace(@RequestBody String name, @RequestBody String guid, @PathVariable String organizationId,  @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return spaceServiceV3.createSpace(name,guid, token);
    }

    /**
     * Space Isolation 에 Isolation Segments 를 설정한다.
     *
     * @param spaceId            the space id
     * @param isolationSegmentId the isolation segement id
     * @return AssignSpaceIsolationSegmentResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V3_URL + "/spaces/{spaceId:.+}/isolationSegments/{isolationSegmentId:.+}")
    public AssignSpaceIsolationSegmentResponse setSpaceDefaultIsolationSegments(@PathVariable String spaceId, @PathVariable String isolationSegmentId) throws Exception {
        return spaceServiceV3.setSpaceDefaultIsolationSegments(spaceId, isolationSegmentId);
    }

    /**
     * Space Isolation 에 Isolation Segments 를 해제한다.
     *
     * @param spaceId the space id
     * @return AssignSpaceIsolationSegmentResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V3_URL + "/spaces/{spaceId:.+}/isolationSegments/reset")
    public AssignSpaceIsolationSegmentResponse resetSpaceDefaultIsolationSegments(@PathVariable String spaceId) throws Exception {
        return spaceServiceV3.resetSpaceDefaultIsolationSegments(spaceId);
    }
}
