package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.organizations.AbstractOrganizationResource;
import org.cloudfoundry.client.v2.organizations.GetOrganizationResponse;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v3.organizations.AssignOrganizationDefaultIsolationSegmentResponse;
import org.cloudfoundry.operations.useradmin.OrganizationUsers;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.UserRole;
import org.openpaas.paasta.portal.api.service.OrgServiceV2;
import org.openpaas.paasta.portal.api.service.SpaceServiceV2;
import org.openpaas.paasta.portal.api.service.UserServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by cheolhan on 2018-03-26.
 */
@RestController
public class OrgControllerV2 extends Common {


    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////


    private static final Logger LOGGER = LoggerFactory.getLogger(OrgControllerV2.class);

    /**
     * The User controller.
     */
    @Autowired
    public MessageSource messageSource;
    /**
     * The Org service.
     */
    @Autowired
    OrgServiceV2 orgServiceV2;

    /**
     * The Space Service
     */
    @Autowired
    SpaceServiceV2 spaceServiceV2;

    /**
     * The Space Service
     */
    @Autowired
    UserServiceV2 userServiceV2;



    /**
     * 조직 정보를 조회한다.
     *
     * @param orgId
     * @param token
     * @return information of the organization
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgId}")
    public GetOrganizationResponse getOrg(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("get org start : " + orgId);
        return orgServiceV2.getOrg(orgId, cloudFoundryClient(connectionContext(), tokenProvider(token)));
    }

    /**
     * 조직 요약 정보를 조회한다.
     *
     * @param orgId the org id
     * @param token
     * @return summary of the organization
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgId}/summary")
    public Map getOrgSummary(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("org summary : " + orgId);
        ReactorCloudFoundryClient reactorClients = cloudFoundryClient(connectionContext(), tokenProvider(token));
        return orgServiceV2.getOrgSummaryMap(orgId, reactorClients);
    }

    /**
     * 조직 요약 정보를 조회한다.(관리자)
     *
     * @param orgId the org id
     * @return summary of the organization
     * @throws IllegalArgumentException
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgId}/summary-admin")
    public Map getOrgSummaryAdmin(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        ReactorCloudFoundryClient reactorClients = cloudFoundryClient();
        return orgServiceV2.getOrgSummaryMap(orgId, reactorClients);
    }


    /**
     * 관리자/사용자 권한으로 조직 목록을 조회한다.
     *
     * @param token
     * @return ListOrganizationsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL + "/orgs")
    public ListOrganizationsResponse getOrgsForUser(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.debug("Org list by user");
        return orgServiceV2.getAllOrgsForUser(token);
    }

    /**
     * 관리자 권한으로 조직 목록을 조회한다.
     *
     * @return ListOrganizationsResponse
     */
    @GetMapping(Constants.V2_URL + "/orgs-admin")
    public ListOrganizationsResponse getOrgsForAdmin() {
        LOGGER.debug("Org list for admin");
        return orgServiceV2.getOrgsForAdmin();
    }

    /**
     * 관리자 권한으로 조직 목록을 조회한다.
     *
     * @return ListOrganizationsResponse
     */
    @GetMapping(Constants.V2_URL + "/orgs-admin/{number}")
    public ListOrganizationsResponse getOrgsForAdmin2(@PathVariable int number) {
        LOGGER.debug("Org list for admin");
        return orgServiceV2.getOrgsForAdminAll(number);
    }


    /**
     * 공간 목록을 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param orgId the org
     * @param token the request
     * @return List<CloudSpace>     orgList
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.04.17 (modified)
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgId}/spaces")
    public Map<?, ?> getSpaces(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.debug("Get Spaces " + orgId);
        final Map<String, Object> result = new HashMap<>();
        result.put("spaceList", orgServiceV2.getOrgSpaces(orgId, cloudFoundryClient(connectionContext(), tokenProvider(token))));

        return result;
    }

    /**
     * 공간 목록을 조회한다.(관리자)
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param orgId the org
     * @return List<CloudSpace>     orgList
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.04.17 (modified)
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgId}/spaces-admin")
    public Map<?, ?> getSpacesAdmin(@PathVariable String orgId) {
        LOGGER.debug("Get Spaces " + orgId);
        final Map<String, Object> result = new HashMap<>();
        result.put("spaceList", orgServiceV2.getOrgSpaces(orgId, cloudFoundryClient()));

        return result;
    }


    /**
     * 조직명 중복검사를 실행한다.
     *
     * @param orgName the org
     * @return boolean
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgName}/exist")
    public boolean isExistOrgName(@PathVariable String orgName) {
        return orgServiceV2.isExistOrgName(orgName);
    }

    /**
     * 조직을 생성한다.
     *
     * @param org
     * @param token
     * @return Map
     */
    @PostMapping(Constants.V2_URL + "/orgs")
    public Map createOrg(@RequestBody Org org, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return orgServiceV2.createOrg(org, token);
    }

    /**
     * 사용자의 조직의 이름을 변경한다.
     *
     * @param org
     * @param token
     * @param token
     * @return Map
     */
    @PutMapping(Constants.V2_URL + "/orgs")
    public Map renameOrg(@RequestBody Org org, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("renameOrg Start ");
        Map resultMap = orgServiceV2.renameOrg(org, token);

        LOGGER.info("renameOrg End ");
        return resultMap;
    }

    /**
     * 사용자의 조직을 삭제한다.
     *
     * @param guid      the organization id (guid)
     * @param recursive is recursive deleting org?
     * @param token     the token
     * @return boolean
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V2_URL + "/orgs/{guid}")
    public Map deleteOrg(@PathVariable String guid, @RequestParam boolean recursive, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("deleteOrg Start ");
        Map resultMap = orgServiceV2.deleteOrg(guid, recursive, token);

        LOGGER.info("deleteOrg End ");
        return resultMap;
    }

    // quota read, update

    /**
     * 조직의 자원 할당량을 조회한다.
     *
     * @param orgId the org id
     * @param token the request
     * @return GetOrganizationQuotaDefinitionResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgId}/quota")
    public GetOrganizationQuotaDefinitionResponse getOrgQuota(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("Get quota of org {}" + orgId);
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(token));
        return orgServiceV2.getOrgQuota(orgId, reactorCloudFoundryClient);
    }

    /**
     * 조직의 자원 할당량을 조회한다.(관리자)
     *
     * @param orgId the org id
     * @return GetOrganizationQuotaDefinitionResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgId}/quota-admin")
    public GetOrganizationQuotaDefinitionResponse getOrgQuotaAdmin(@PathVariable String orgId) {
        LOGGER.info("Get quota of org {}" + orgId);
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword));
        return orgServiceV2.getOrgQuota(orgId, reactorCloudFoundryClient);
    }

    /**
     * 조직의 자원 할당량을 수정한다.
     *
     * @param orgId
     * @param org
     * @param token
     * @return Map
     * @throws Exception the exception
     */
    @PutMapping(Constants.V2_URL + "/orgs/{orgId}/quota")
    public Map changeQuota(@PathVariable String orgId, @RequestBody Org org, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("changeQuota Start ");
        Map resultMap = orgServiceV2.updateOrgQuota(orgId, org, token);

        LOGGER.info("changeQuota End ");
        return resultMap;
    }

    /**
     * 조직에 속한 유저들의 역할(Role)을 전부 조회한다.
     *
     * @param orgId
     * @param token
     * @return Users with roles that belong in the organization
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgId}/user-roles")
    public Map<String, Collection<UserRole>> getOrgUserRoles(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        Objects.requireNonNull(orgId, "Org Id");
        Objects.requireNonNull(token, "token");
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(token));
        return orgServiceV2.getOrgUserRoles(orgId, reactorCloudFoundryClient);

    }

    /**
     * 조직 이름과 유저 이름으로 해당 조직에서 유저가 가진 역할(Role)을 조회한다.
     *
     * @param orgName  (org name)
     * @param userName (user email)
     * @param token
     * @return UserRole
     */
    @GetMapping(Constants.V2_URL + "/orgs/{orgName:.+}/user-roles/{userName:.+}")
    public UserRole getOrgUserRoleByUsername(@PathVariable String orgName, @PathVariable String userName, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        final String userId = userServiceV2.getUserIdByUsername(userName);
        Objects.requireNonNull(userId, "Username cannot found");

        LOGGER.info("getOrgUserRoleByUsername : Org name : {} / User name : {} / User id : {}", orgName, userName, userId);
        OrganizationUsers users = orgServiceV2.getOrgUserRolesByOrgName(orgName, token);
        final boolean isManager = users.getManagers().stream().anyMatch(userName::equals);
        final boolean isBillingManager = users.getBillingManagers().stream().anyMatch(userName::equals);
        final boolean isAuditor = users.getAuditors().stream().anyMatch(userName::equals);


        return UserRole.builder().userEmail(userName).userId(userId).addRole(isManager ? "OrgManager" : null).addRole(isBillingManager ? "BillingManager" : null).addRole(isAuditor ? "OrgAuditor" : null).build();
    }

    @GetMapping(Constants.V2_URL + "/orgs/{orgName:.+}/user-roles/{userName:.+}/is-manager")
    public boolean isOrgManager(@PathVariable String orgName, @PathVariable String userName, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("isOrgManager : Org name : {} / User name : {}", orgName, userName);
        return orgServiceV2.getOrgUserRolesByOrgName(orgName, token).getManagers().stream().anyMatch(userName::equals);
    }

    /**
     * 조직에 속한 유저에게 역할을 할당한다.
     *
     * @param orgId
     * @param body
     * @param token
     * @return User with role that belongs in the organization
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @PutMapping(Constants.V2_URL + "/orgs/{orgId}/user-roles")
    public AbstractOrganizationResource associateOrgUserRoles(@PathVariable String orgId, @RequestBody UserRole.RequestBody body, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return orgServiceV2.associateOrgUserRole(orgId, body.getUserId(), body.getRole(), token);
    }

    /**
     * 조직에 속한 유저의 역할을 제거한다.
     *
     * @param orgId
     * @param userId
     * @param role
     * @param token
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @DeleteMapping(Constants.V2_URL + "/orgs/{orgId}/user-roles")
    public boolean removeOrgUserRoles(@PathVariable String orgId, @RequestParam String userId, @RequestParam String role, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        orgServiceV2.removeOrgUserRole(orgId, userId, role, token);
        return true;
    }

    // TODO invite user
    public void inviteUser() {

    }

    // TODO cancel invite user
    public void cancelInvitionUser() {
    }

    // TODO cancel member
    @DeleteMapping(Constants.V2_URL + "/orgs/{orgId}/member")
    public boolean cancelOrganizationMember(@PathVariable String orgId, @RequestParam String userId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        Objects.requireNonNull(orgId, "Organization ID is required");
        Objects.requireNonNull(userId, "User ID is required");

        boolean isSuccessed = orgServiceV2.cancelOrganizationMember(orgId, userId, token);
        if (isSuccessed) {
            return true;
        } else {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Fail to cancel organization member");
        }
    }

    @PutMapping(Constants.V2_URL + "/orgs/user-roles")
    public Boolean associateOrgUserRoles2(@RequestBody Map body) {
        return orgServiceV2.associateOrgUserRole2(body);
    }

    @GetMapping(Constants.V2_URL + "/orgList/{page}")
    public Map orgList(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable int page) throws Exception {
        LOGGER.info("orgList Start");
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(token));
        Map resultMap = new HashMap();
        List<Map> orgList = new ArrayList<Map>();

        ListOrganizationsResponse listOrganizationsResponse = orgServiceV2.getOrgsForUser(reactorCloudFoundryClient, page);
        LOGGER.info("OrgsForUser");
        listOrganizationsResponse.getResources().forEach(orgs -> {
            Map orgMap = new HashMap();

            try {
                orgMap.put("org", orgs);

//                ListSpacesResponse listSpacesResponse = orgService.getOrgSpaces(orgs.getMetadata().getId(), reactorCloudFoundryClient);
//                orgMap.put("space", listSpacesResponse);
//
//                Map<String, Collection<UserRole>> userRoles = orgService.getOrgUserRoles(orgs.getMetadata().getId(), reactorCloudFoundryClient);
//                orgMap.put("userRoles", userRoles);
//
//                GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = orgService.getOrgQuota(orgs.getMetadata().getId(), reactorCloudFoundryClient);
//                orgMap.put("quota", getOrganizationQuotaDefinitionResponse);

                orgList.add(orgMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        LOGGER.info("listOrganizationsResponse");
        resultMap.put("result", orgList);

        LOGGER.debug("orgList End");
        return resultMap;
    }

    @GetMapping(Constants.V2_URL + "/orgDetail/{guid}")
    public Map orgList(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable String guid) throws Exception {
        LOGGER.info("orgDetail Start");
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(token));
        Map resultMap = new HashMap();
        List<Map> orgList = new ArrayList<Map>();

        Map orgMap = new HashMap();

        try {
            ListSpacesResponse listSpacesResponse = orgServiceV2.getOrgSpaces(guid, reactorCloudFoundryClient);
            orgMap.put("space", listSpacesResponse);

            Map<String, Collection<UserRole>> userRoles = orgServiceV2.getOrgUserRoles(guid, reactorCloudFoundryClient);
            orgMap.put("userRoles", userRoles);

            GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = orgServiceV2.getOrgQuota(guid, reactorCloudFoundryClient);
            orgMap.put("quota", getOrganizationQuotaDefinitionResponse);

            orgList.add(orgMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        resultMap.put("result", orgMap);

        LOGGER.debug("orgDetail End");
        return resultMap;
    }

    /**
     * flag 사용여부를 조회한다.
     *
     * @param flagname
     * @param token
     * @author cheolhan
     * @version 2.0
     * @since 2018.10.01
     */
    @GetMapping(Constants.V2_URL + "/{flagname}/orgflag")
    public Map orgFlag(@PathVariable String flagname, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return orgServiceV2.orgFlag(flagname.toLowerCase(), token);
    }

}
