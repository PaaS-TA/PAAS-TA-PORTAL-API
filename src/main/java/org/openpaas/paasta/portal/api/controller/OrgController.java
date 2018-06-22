package org.openpaas.paasta.portal.api.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.collections.map.HashedMap;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.organizations.*;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.operations.useradmin.OrganizationUsers;
import org.codehaus.jackson.map.ObjectMapper;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.UserRole;
import org.openpaas.paasta.portal.api.service.OrgService;
import org.openpaas.paasta.portal.api.service.SpaceService;
import org.openpaas.paasta.portal.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 조직 컨트롤러 - 조직 목록 , 조직 이름 변경 , 조직 생성 및 삭제 등을 제공한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
@Transactional
public class OrgController extends Common {

    /**
     * V1 URL HEAD = (empty string)*/
    private static final String V1_URL = Constants.V1_URL;

    /**
     * V2 URL HEAD = "/v2"
     */
    private static final String V2_URL = Constants.V2_URL;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrgController.class);

    /**
     * The User controller.
     */
    @Autowired
    public MessageSource messageSource;
    /**
     * The Org service.
     */
    @Autowired
    OrgService orgService;

    /**
     * The Space Service
     */
    @Autowired
    SpaceService spaceService;

    /**
     * The Space Service
     */
    @Autowired
    UserService userService;

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    /**
     * 조직 정보를 조회한다.
     *
     * @param orgId
     * @param token
     * @return information of the organization
     */
    @HystrixCommand(fallbackMethod = "getOrg")
    @GetMapping(V2_URL + "/orgs/{orgId}")
    public GetOrganizationResponse getOrg(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
    	LOGGER.info("get org start : " + orgId);
    	if (orgId == null)
    		throw new IllegalArgumentException("Org id is empty.");

    	return orgService.getOrg(orgId, token);
    }

    /**
     * 조직 요약 정보를 조회한다.
     *
     * @param orgId     the org id
     * @param token the request
     * @return summary of the organization
     */

    @GetMapping(V2_URL + "/orgs/{orgId}/summary")
    public Map getOrgSummary(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("org summary : " + orgId);
		if (orgId == null) {
			throw new IllegalArgumentException("조직정보를 가져오지 못하였습니다.");
		}
		return orgService.getOrgSummaryMap(orgId, token);
    }

    /**
     * 관리자/사용자 권한으로 조직 목록을 조회한다.
     *
     * @return the orgs for admin
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getOrgsForUser")
    @GetMapping(V2_URL + "/orgs")
    public ListOrganizationsResponse getOrgsForUser(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.debug("Org list by user");
        return orgService.getOrgsForUser(token);
    }

    /**
     * 관리자 권한으로 조직 목록을 조회한다.
     * @return
     */
    @HystrixCommand(fallbackMethod = "getOrgsForAdmin")
    @GetMapping(V2_URL + "/orgs-admin")
    public ListOrganizationsResponse getOrgsForAdmin() {
    	LOGGER.debug("Org list for admin");
    	return orgService.getOrgsForAdmin();
    }
    /**
     * 관리자 권한으로 조직 목록을 조회한다.
     * @return
     */
    @HystrixCommand(fallbackMethod = "getOrgsForAdmin2")
    @GetMapping(V2_URL + "/orgs-admin/{number}")
    public ListOrganizationsResponse getOrgsForAdmin2(@PathVariable int number) {
        LOGGER.debug("Org list for admin");
        return orgService.getOrgsForAdminAll(number);
    }


    /**
     * 공간 목록을 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param orgId     the org
     * @param token the request
     * @return List<CloudSpace>     orgList
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.04.17 (modified)
     */
    @HystrixCommand(fallbackMethod = "getSpaces")
    @GetMapping(V2_URL + "/orgs/{orgId}/spaces")
    public Map<?, ?> getSpaces(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
    	LOGGER.debug("Get Spaces " + orgId);
    	final Map<String, Object> result = new HashMap<>();
		result.put("spaceList", orgService.getOrgSpaces(orgId, token));

        return result;
    }

    @HystrixCommand(fallbackMethod = "isExistOrgName")
    @GetMapping(V2_URL + "/orgs/{orgName}/exist")
    public boolean isExistOrgName(@PathVariable String orgName) {
        return orgService.isExistOrgName( orgName );
    }

    /**
     * 이름을 가지고 조직을 생성한다.
     */
    @HystrixCommand(fallbackMethod = "createOrg")
    @PostMapping( V2_URL + "/orgs" )
    public CreateOrganizationResponse createOrg ( @RequestBody Org org, @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        return orgService.createOrg( org, token );
    }

    /**
     * 사용자의 조직의 이름을 변경한다.
     * @param org
     * @param token
     * @param token
     * @return
     */
    @HystrixCommand(fallbackMethod = "renameOrg")
    @PutMapping( V2_URL + "/orgs" )
    public Map renameOrg(@RequestBody Org org, @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token) {
        LOGGER.info("renameOrg Start ");

        Map resultMap = orgService.renameOrg(org, token);

        LOGGER.info("renameOrg End ");
        return resultMap;
    }
    
    /**
     * 사용자의 조직을 삭제한다.
     * @param guid the organization id (guid)
     * @param recursive is recursive deleting org?
     * @param token the token
     * @return boolean
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "deleteOrg")
    @DeleteMapping(V2_URL+"/orgs/{guid}")
    public Map deleteOrg(@PathVariable String guid, @RequestParam boolean recursive, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token ) throws Exception {
        LOGGER.info("deleteOrg Start ");

        Map resultMap = orgService.deleteOrg(guid, recursive, token);

        LOGGER.info("deleteOrg End ");
        return resultMap;
    }
    
    // space read-only
    public ListSpacesResponse getOrgSpaces(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return orgService.getOrgSpaces( orgId, token );
    }
    
    // quota read, update
    /**
     * 조직의 자원 할당량을 조회한다.
     *
     * @param orgId     the org id
     * @param token the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getOrgQuota")
    @GetMapping(V2_URL + "/orgs/{orgId}/quota")
    public GetOrganizationQuotaDefinitionResponse getOrgQuota(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("Get quota of org {}" + orgId);
        return orgService.getOrgQuota(orgId, token);
    }

    @HystrixCommand(fallbackMethod = "changeQuota")
    @PutMapping(V2_URL + "/orgs/{orgId}/quota")
    public Map changeQuota(@PathVariable String orgId, @RequestBody Org org, @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        LOGGER.info("changeQuota Start ");

        Map resultMap = orgService.updateOrgQuota( orgId, org, token );

        LOGGER.info("changeQuota End ");
        return resultMap;
    }

    /**
     * 조직에 속한 유저들의 역할(Role)을 전부 조회한다.
     * @param orgId
     * @param token
     * @return Users with roles that belong in the organization
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @HystrixCommand(fallbackMethod = "getOrgUserRoles")
    @GetMapping(V2_URL + "/orgs/{orgId}/user-roles")
    public Map<String, Collection<UserRole>> getOrgUserRoles ( @PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY ) String token ) {
        Objects.requireNonNull( orgId, "Org Id" );
        Objects.requireNonNull( token, "token" );
        if (orgService.isExistOrg( orgId ))
            return orgService.getOrgUserRoles( orgId, token );
        else {
            return Collections.<String, Collection<UserRole>>emptyMap();
        }
    }

    /**
     * 조직 이름과 유저 이름으로 해당 조직에서 유저가 가진 역할(Role)을 조회한다.
     * @param orgName (org name)
     * @param userName (user email)
     * @param token
     * @return UserRole
     */
    @HystrixCommand(fallbackMethod = "getOrgUserRoleByUsername")
    @GetMapping(V2_URL + "/orgs/{orgName:.+}/user-roles/{userName:.+}")
    public UserRole getOrgUserRoleByUsername ( @PathVariable String orgName,
                                               @PathVariable String userName,
                                               @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        final String userId = userService.getUserIdByUsername( userName );
        Objects.requireNonNull( userId, "Username cannot found" );

        LOGGER.info( "getOrgUserRoleByUsername : Org name : {} / User name : {} / User id : {}",
            orgName, userName, userId );
        OrganizationUsers users = orgService.getOrgUserRolesByOrgName( orgName, token );
        final boolean isManager = users.getManagers().stream().anyMatch( userName::equals );
        final boolean isBillingManager = users.getBillingManagers().stream().anyMatch( userName::equals );
        final boolean isAuditor = users.getAuditors().stream().anyMatch( userName::equals );


        return UserRole.builder()
            .userEmail( userName )
            .userId( userId )
            .addRole( isManager?         "OrgManager"       : null )
            .addRole( isBillingManager?  "BillingManager"   : null )
            .addRole( isAuditor?         "OrgAuditor"       : null )
            .build();
    }

    @HystrixCommand(fallbackMethod = "isOrgManager")
    @GetMapping(V2_URL + "/orgs/{orgName:.+}/user-roles/{userName:.+}/is-manager")
    public boolean isOrgManager(@PathVariable String orgName,
                                @PathVariable String userName,
                                @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token) {
        LOGGER.info( "isOrgManager : Org name : {} / User name : {}", orgName,
            userName);
        return orgService.getOrgUserRolesByOrgName( orgName, token )
            .getManagers().stream().anyMatch( userName::equals );
    }

    /**
     * 조직에 속한 유저에게 역할을 할당한다.
     * @param orgId
     * @param body
     * @param token
     * @return User with role that belongs in the organization
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @HystrixCommand(fallbackMethod = "associateOrgUserRoles")
    @PutMapping(V2_URL + "/orgs/{orgId}/user-roles")
    public AbstractOrganizationResource associateOrgUserRoles ( @PathVariable String orgId,
                                        @RequestBody UserRole.RequestBody body,
                                        @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        Objects.requireNonNull( body.getUserId(), "User ID(userId) is required" );
        Objects.requireNonNull( body.getRole(), "Org Role(role) is required" );
        LOGGER.info("Associate organization role of user (Update) : {} / {}", body.getUserId(), body.getRole());
        return orgService.associateOrgUserRole( orgId, body.getUserId(), body.getRole(), token );
    }

    /**
     * 조직에 속한 유저의 역할을 제거한다.
     * @param orgId
     * @param userId
     * @param role
     * @param token
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @HystrixCommand(fallbackMethod = "removeOrgUserRoles")
    @DeleteMapping(V2_URL + "/orgs/{orgId}/user-roles")
    public boolean removeOrgUserRoles ( @PathVariable String orgId,
                                     @RequestParam String userId, @RequestParam String role,
                                     @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        Objects.requireNonNull( userId, "User ID(userId) is required" );
        Objects.requireNonNull( role, "Org Role(role) is required" );
        LOGGER.info("Remove organization role of user (Delete) : {} / {}", userId, role);
        orgService.removeOrgUserRole( orgId, userId, role, token );
        return true;
    }

    // TODO invite user
    public void inviteUser() {

    }

    // TODO cancel invite user
    public void cancelInvitionUser() { }

    // TODO cancel member
    @HystrixCommand(fallbackMethod = "cancelOrganizationMember")
    @DeleteMapping(V2_URL + "/orgs/{orgId}/member")
    public boolean cancelOrganizationMember( @PathVariable String orgId,
                                          @RequestParam String userId,
                                          @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        Objects.requireNonNull( orgId, "Organization ID is required" );
        Objects.requireNonNull( userId, "User ID is required" );

        boolean isSuccessed = orgService.cancelOrganizationMember( orgId, userId, token );
        if (isSuccessed) {
            LOGGER.info( "Success to cancel organization member : org ID {} / user ID {}", orgId, userId );
            return true;
        } else {
            LOGGER.error( "Fail to cancel organization member : org ID {} / user ID {}", orgId, userId );
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "Fail to cancel organization member" );
        }
    }

    @HystrixCommand(fallbackMethod = "associateOrgUserRoles2")
    @PutMapping(V2_URL + "/orgs/user-roles")
    public Boolean associateOrgUserRoles2(@RequestBody Map body) {
        LOGGER.info("associateOrgUserRoles Start");
        return orgService.associateOrgUserRole2(body);
    }

    @HystrixCommand(fallbackMethod = "orgList")
    @GetMapping(V2_URL + "/orgList")
    public Map orgList(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.debug("orgList Start");

        Map resultMap = new HashMap();
        List<Map> orgList = new ArrayList<Map>();

        ListOrganizationsResponse listOrganizationsResponse = orgService.getOrgsForUser(token);

        listOrganizationsResponse.getResources().forEach(orgs -> {
            Map orgMap = new HashMap();

            try {
                orgMap.put("org", orgs);

                ListSpacesResponse listSpacesResponse = orgService.getOrgSpaces(orgs.getMetadata().getId(), token);
                orgMap.put("space", listSpacesResponse);

                Map<String, Collection<UserRole>> userRoles = orgService.getOrgUserRoles(orgs.getMetadata().getId(), token);
                orgMap.put("userRoles", userRoles);

                GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = orgService.getOrgQuota(orgs.getMetadata().getId(), token);
                orgMap.put("quota", getOrganizationQuotaDefinitionResponse);

                orgList.add(orgMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        resultMap.put("result", orgList);

        LOGGER.debug("orgList End");
        return resultMap;
    }


    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 3                   //////
    //////   Document : http://v3-apidocs.cloudfoundry.org          //////
    //////   Not yet implemented                                    //////
    //////////////////////////////////////////////////////////////////////

    // Not-implemented
}
