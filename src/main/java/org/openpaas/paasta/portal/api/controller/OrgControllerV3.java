package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.organizations.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.UserRole;
import org.openpaas.paasta.portal.api.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by cheolhan on 2019-04-24.
 */
@RestController
public class OrgControllerV3 extends Common {


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////                            * CLOUD FOUNDRY CLIENT API VERSION 3                                      //////
    //////   Document : https://v3-apidocs.cloudfoundry.org/version/3.69.0/index.html#organizations             //////
    //////                                     Not-implemented                                                  //////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static final Logger LOGGER = LoggerFactory.getLogger(OrgControllerV3.class);

    @Autowired
    OrgServiceV3 orgServiceV3;

    /**
     * Organizations 정보를 가져온다.
     *
     * @param organizationsId the organization id
     * @param token           user token
     * @return GetOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/orgs/{organizationsId:.+}")
    public GetOrganizationResponse getOrg(@PathVariable String organizationsId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return orgServiceV3.getOrg(organizationsId, token);
    }

    /**
     * Organizations 리스트 정보를 가져온다.
     *
     * @param token user token
     * @return ListOrganizationsResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/orgs")
    public ListOrganizationsResponse listOrg(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return orgServiceV3.listOrg(token);
    }

    /**
     * Organizations 리스트 정보를 가져온다.
     *
     * @return ListOrganizationsResponse
     * 권한 : 관리자 권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/orgs-admin")
    public ListOrganizationsResponse listOrgAdmin() {
        return orgServiceV3.listOrgAdmin();
    }

    /**
     * 조직명 중복검사를 실행한다.
     *
     * @return boolean
     * 권한 : 사용자
     */
    @GetMapping(Constants.V3_URL + "/orgs/{orgName:.+}/exist")
    public boolean isExistOrgName(@PathVariable String orgName) {
        return orgServiceV3.isExistOrgName(orgName);
    }


    /**
     * Organizations 을 생성한다.
     *
     * @param name  the organization name
     * @param token user token
     * @return CreateOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL + "/orgs")
    public CreateOrganizationResponse createOrg(@RequestBody String name, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return orgServiceV3.createOrg(name, token);
    }


    /**
     * Organizations 에 Isolation Segments default 를 설정한다.
     *
     * @param organizationsId    the organizations id
     * @param isolationSegmentId the isolation segement id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V3_URL + "/orgs/{organizationsId:.+}/isolationSegments/{isolationSegmentId:.+}")
    public AssignOrganizationDefaultIsolationSegmentResponse setOrgDefaultIsolationSegments(@PathVariable String organizationsId, @PathVariable String isolationSegmentId) throws Exception {
        return orgServiceV3.setOrgDefaultIsolationSegments(organizationsId, isolationSegmentId);
    }

    /**
     * Organizations 에 Isolation Segments default 를 재설정한다.
     *
     * @param organizationsId the organizations id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V3_URL + "/orgs/{organizationsId:.+}/isolationSegments/reset")
    public AssignOrganizationDefaultIsolationSegmentResponse resetOrgDefaultIsolationSegments(@PathVariable String organizationsId) throws Exception {
        return orgServiceV3.resetOrgDefaultIsolationSegments(organizationsId);
    }
}
