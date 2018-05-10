package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CloudUser;
import org.cloudfoundry.client.v2.jobs.ErrorDetails;
import org.cloudfoundry.client.v2.jobs.JobEntity;
import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionRequest;
import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.organizations.*;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.operations.organizations.OrganizationDetail;
import org.cloudfoundry.operations.organizations.OrganizationInfoRequest;
import org.cloudfoundry.uaa.users.UserInfoRequest;
import org.cloudfoundry.uaa.users.UserInfoResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.model.InviteOrgSpace;
import org.openpaas.paasta.portal.api.model.Org;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 조직 서비스 - 조직 목록 , 조직 이름 변경 , 조직 생성 및 삭제 등을 제공한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@EnableAsync
@Service
public class OrgService extends Common {

    private final Logger LOGGER = getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private SpaceService spaceService;
    @Autowired
    private AsyncUtilService asyncUtilService;


    /*
     * role 문자를 변경한다.
     *
     * @param userRole
     * @return
     */
    private String toStringRole(String userRole) {
        String roleStr;

        switch (userRole) {
            case "users":
                roleStr = "users";
                break;
            case "OrgManager":
                roleStr = "managers";
                break;
            case "BillingManager":
                roleStr = "billing_managers";
                break;
            case "OrgAuditor":
                roleStr = "auditors";
                break;
            default:
                throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Invalid userRole.");
        }

        return roleStr;
    }

    /**
     * 권한별로 수집된 유저정보를 취합하여 하나의 객체로 만들어 리턴한다.
     *
     * @param userList
     * @param managers
     * @param billingManagers
     * @param auditors
     * @return orgUserList
     * @throws Exception
     * @author 김도준
     * @version 1.0
     * @since 2016.9.05 최초작성
     */
    private List<Map<String, Object>> putUserList(String orgName, List<Map<String, Object>> userList, Map<String, CloudUser> managers, Map<String, CloudUser> billingManagers, Map<String, CloudUser> auditors) throws Exception {
        List<Map<String, Object>> orgUserList = new ArrayList<>();

        for (Map<String, Object> userMap : userList) {
            List<String> userRoles = new ArrayList<>();
            if (managers.get(userMap.get("userName")) != null) {
                userRoles.add("OrgManager");
            }
            if (billingManagers.get(userMap.get("userName")) != null) {
                userRoles.add("BillingManager");
            }
            if (auditors.get(userMap.get("userName")) != null) {
                userRoles.add("OrgAuditor");
            }

            userMap.put("orgName", orgName);
            userMap.put("userRoles", userRoles);
            userMap.put("inviteYn", "N");
            userMap.put("token", "");
            orgUserList.add(userMap);
        }
        return orgUserList;
    }

    /**
     * 조직 role를 조회한다.
     *
     * @param org   the org
     * @param token the token
     * @return the org role
     * @throws Exception the exception
     */
    public List<CloudSpace> getOrgRole(Org org, String token) throws Exception {
        /*List<CloudSpace> listSpace = spaceService.getSpaces(org, token);
        return listSpace;*/
        return null;
    }

    /**
     * 조직과 공간의 초대 정보를 등록한다.
     *
     * @param map the map
     * @return the int
     * @throws Exception the exception
     */
    public int insertOrgSpaceUser(HashMap map) throws Exception {
        // TODO
        // int cnt = inviteOrgSpaceMapper.insertInviteOrgSpace(map);
        int cnt = 0;
        return cnt;
    }

    /**
     * 조직과 공간의 초대 정보를 수한다.정
     *
     * @param map the map
     * @return the int
     * @throws Exception the exception
     */
    public int updateOrgSpaceUser(HashMap map) throws Exception {
        // TODO
        // int cnt = inviteOrgSpaceMapper.updateOrgSpaceUser(map);
        int cnt = 0;
        return cnt;
    }

    /**
     * 조직과 공간의 초대 사용자를 조회한다.
     *
     * @param inviteOrgSpace the invite org space
     * @return the list
     * @throws Exception the exception
     */
    public List<InviteOrgSpace> selectOrgSpaceUser(InviteOrgSpace inviteOrgSpace) throws Exception {
        // TODO
//        List<InviteOrgSpace> list = inviteOrgSpaceMapper.selectOrgSpaceUser(inviteOrgSpace);
//        return list;
        return null;
    }


    /**
     * 공간에 대한 초대를 완료하고 초대상태를 Y로 만들어준다.
     *
     * @param code 초대 토큰
     * @return 상태를 Y 로 수정한 개수
     */
    public int updateInviteY(String code) {
        Map map = new HashMap();
        map.put("token", code);
//        int cnt = inviteOrgSpaceMapper.updateInviteY(map);
        int cnt = 0;
        return cnt;
    }

    /**
     * 공간 초대 이메일을 통해 접근된 회수를 수정한다.
     *
     * @param code      the code
     * @param accessCnt //접근회수
     * @return 수정된 개수
     */
    public int updateAccessCnt(String code, int accessCnt) {
        Map map = new HashMap();
        map.put("token", code);
        map.put("accessCnt", accessCnt);
//        int cnt = inviteOrgSpaceMapper.updateAccessCnt(map);
        int cnt = 0;
        return cnt;
    }

    /**
     * 공간에 초대한 이메일의 token을 가진 초대 정보를 가져온다.
     *
     * @param code the code
     * @return List 초대 정보
     */
    public List selectInviteInfo(String code) {
        Map map = new HashMap();
        map.put("token", code);
//        List list = inviteOrgSpaceMapper.selectInviteInfo(map);
        List list = null;
        return list;
    }


    /**
     * 사용자 탭의 getAllUsers 서비스에 데이터 추가할 데이터를 가져온다.
     *
     * @param orgName :공간 이름
     * @param userId  : 로그인한 사용자 아이디
     * @param gubun   the gubun
     * @return 예 )[{userName: "lij", userGuid: "db040322-c831-4d51-b391-4f9ff8102dc9", inviteYn: "Y",…}]
     */
    public List<Map<String, Object>> getUsersByInvite(String orgName, String userId, String gubun) {
        InviteOrgSpace inviteOrgSpace = new InviteOrgSpace();

        inviteOrgSpace.setGubun(gubun);
        inviteOrgSpace.setUserId(userId);
        inviteOrgSpace.setInviteName(orgName);
        //List<InviteOrgSpace> orgInviteList = inviteOrgSpaceMapper.getUsersByInvite(inviteOrgSpace);
        List<InviteOrgSpace> orgInviteList = null;
        List<Map<String, Object>> orgUserList = new ArrayList<>();
        String voUserId = "";
        for (int i = 0; i < orgInviteList.size(); i++) {
            InviteOrgSpace vo = orgInviteList.get(i);
            if (!vo.getInviteUserId().equals(voUserId)) {
                Map voMap = new HashMap<>();
                voUserId = vo.getInviteUserId();
                //String voUserGuid = userMapper.getUserGuid(voUserId) == null ? "" : userMapper.getUserGuid(voUserId);
                String voUserGuid = "";
                voMap.put("userName", voUserId);
                voMap.put("orgName", vo.getInviteName());
                voMap.put("userGuid", voUserGuid);
                voMap.put("token", vo.getToken());
                voMap.put("inviteYn", "Y");
                List list = new ArrayList();
                String rolename = "";
                for (int j = 0; j < orgInviteList.size(); j++) {
                    InviteOrgSpace vo1 = orgInviteList.get(i);
                    if (vo1.getInviteUserId().equals(voUserId)) {
                        if (!vo.getRoleName().equals(rolename)) {
                            rolename = vo.getRoleName();
                            list.add(rolename);
                        }
                    }
                }
                voMap.put("userRoles", list);
                orgUserList.add(voMap);
            }
        }
        return orgUserList;
    }


    /**
     * 초대한 token 정보를 가지고 초대취소를 수행한다.
     *
     * @param map the map
     * @return int int
     * @throws Exception the exception
     */
    public int cancelInvite(Map map) throws Exception {
//        return inviteOrgSpaceMapper.deleteOrgSpaceUserToken(map);
        return 0;
    }


    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    LoginService loginService;

    public TokenGrantTokenProvider tokenProviderWithRefresh(String token) {
        try {
            OAuth2AccessToken refreshToken = loginService.refresh(token);
            if (refreshToken != null) token = refreshToken.getValue();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        if (token.indexOf("bearer") < 0) {
            token = "bearer " + token;
        }
        return new TokenGrantTokenProvider(token);
    }

    /**
     * 이름을 받아 조직을 생성한다. (Org Create)
     *
     * @param token
     * @param orgName
     * @return CreateOrganizationResponse
     * @version 2.0
     * @since 2018.5.2
     */
    public CreateOrganizationResponse createOrg(final String token, final String orgName) {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().create(CreateOrganizationRequest.builder().name(orgName).build()).block();
    }

    /**
     * 조직 Id를 이용해 조직 정보를 조회한다. (Org Read, fully info.)
     *
     * @param orgId the org id
     * @param token the token
     * @return GetOrganizationResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public GetOrganizationResponse getOrg(final String orgId, final String token) {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().get(GetOrganizationRequest.builder().organizationId(orgId).build()).block();
    }

    /**
     * 조직 Id를 이용해 조직 요약 정보를 조회한다. (Org Read, summary)
     *
     * @param orgId the org id
     * @param token the token
     * @return SummaryOrganizationResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    // Org Read
    public SummaryOrganizationResponse getOrgSummary(final String orgId, final String token) {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().summary(SummaryOrganizationRequest.builder().organizationId(orgId).build()).block();
    }

    /**
     * 조직 목록을 조회한다. 단, 내부의 resources만 추출해서 반환한다.
     *
     * @param token the token
     * @return List<OrganizationResource> organization list
     * @author 김도준(1.x), hgcho(2.x)
     * @version 2.0
     * @since 2018.4.22
     */
    @Deprecated
    public List<OrganizationResource> getOrgs(String token) {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().list(ListOrganizationsRequest.builder().build()).block().getResources();
    }

    /**
     * 사용자/운영자 포털에서 조직목록을 요청했을때, 모든 조직목록을 응답한다. (Org Read for all)
     *
     * @param token the token
     * @return ListOrganizationsResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public ListOrganizationsResponse getOrgsForUser(final String token) {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().list(ListOrganizationsRequest.builder().build()).block();
    }

    /**
     * 운영자 포털에서 조직목록을 요청했을때, 모든 조직목록을 응답한다. (Org Read for all)
     *
     * @return ListOrganizationsResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public ListOrganizationsResponse getOrgsForAdmin() {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword)).organizations().list(ListOrganizationsRequest.builder().build()).block();
    }


    /**
     * 조직 이름을 가지고 조직 아이디를 조회한다.
     *
     * @param orgName //조직 이름
     * @return 조직 아이디
     * @throws Exception the exception
     * @version 2.0
     * @author hgcho
     * @since 2018.5.4
     */
    public String getOrgId(String orgName, String token) {
        return getOrg(orgName, token).getMetadata().getId();
    }

    public OrganizationDetail getOrgUsingName(final String name, final String token) {
        return Common.cloudFoundryOperations(connectionContext(), tokenProvider(token)).organizations().get(OrganizationInfoRequest.builder().name(name).build()).block();
    }

    /**
     * 사용자 포털에서 조직의 이름을 수정한다. (Org Update : name)
     *
     * @param org
     * @param token
     * @return UpdateOrganizationResponse
     * @author hgcho, ParkCholhan
     * @version 2.0
     * @since 2018.5.2
     */
    public UpdateOrganizationResponse renameOrg(Org org, String token) {
        Objects.requireNonNull(org.getGuid(), "Org GUID(guid) must not be null.");
        Objects.requireNonNull(org.getNewOrgName(), "New org name(newOrgName) must not be null.");
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().update(UpdateOrganizationRequest.builder().organizationId(org.getGuid().toString()).name(org.getNewOrgName()).build()).block();
    }

    /**
     * 사용자 포털에서 조직을 삭제한다. (Org Delete)<br>
     * 만약 false가 넘어올 경우, 권한이 없거나 혹은
     *
     * @param org
     * @param token
     * @return
     * @throws Exception
     * @author hgcho, ParkCheolhan
     * @version 2.1
     * @since 2018.5.2
     */
    public DeleteOrganizationResponse deleteOrg(Org org, String token) throws Exception {
        boolean result = false;

        /*
        // 기존 구현 내용 (created by ParkCheolhan)
        // 자신의 사용자 정보를 가져온다.
        UserInfoResponse userInfoResponse=
                Common.uaaClient(connectionContext(), tokenProvider(token))
                .users().userInfo(UserInfoRequest.builder().build()).block();
        // OrgManager 권한이 있는 유저 정보를 모두 가져온다.
        ListOrganizationManagersResponse listOrganizationManagersResponse =
                Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .organizations().listManagers(ListOrganizationManagersRequest.builder().organizationId(orgid).build()).block();
        // 현재 유저가 
        for (UserResource resource: listOrganizationManagersResponse.getResources()) {
            if(resource.getEntity().getUsername().equals(userInfoResponse.getUserName()))
            {
                LOGGER.info("삭제성공" + resource.getEntity().getUsername() + "::" + userInfoResponse.getUserName());
                Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                .organizations().delete(DeleteOrganizationRequest.builder().organizationId(orgid).build()).block();
                result = true;
            }
        }
        */

        Objects.requireNonNull(org.getGuid(), "Org GUID(guid) must not be null.");
        final String orgId = org.getGuid().toString();
        final boolean recursive = org.isRecursive();
        final SummaryOrganizationResponse orgSummary = getOrgSummary(orgId, token);

        //// Check Admin user
        // 현재 token의 유저 정보를 가져온다.
        // TODO 특정 유저의 정보를 가져오는 메소드가 구현되어 있으면, 해당 메소드로 교체할 것. (hgcho)
        final UserInfoResponse userInfoResponse = Common.uaaClient(connectionContext(), tokenProvider(token)).users().userInfo(UserInfoRequest.builder().build()).block();

        // Admin 계정인지 확인
        final boolean isAdmin = userInfoResponse.getUserName().equals(adminUserName);

        if (isAdmin) {
            // Admin 계정인 경우 강제적으로 Org 밑의 모든 리소스(spaces, buildpack, app...)를 recursive하게 제거한다.
            LOGGER.warn("Org({}) exists user(s) included OrgManager role... but it deletes forced.", orgSummary.getName());
            return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().delete(DeleteOrganizationRequest.builder().organizationId(orgId).recursive(true).async(true).build()).block();
        }

        ///// Real user
        // 지우려는 조직의 OrgManager Role이 주어진 유저를 찾는다.
        // TODO 특정 Role에 해당하는 유저를 찾는 메소드가 구현되면, 해당 메소드로 교체할 것. (hgcho)
        final ListOrganizationManagersResponse managerResponse = Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().listManagers(ListOrganizationManagersRequest.builder().organizationId(orgId).build()).block();

        // OrgManager role을 가진 유저 수 파악
        final int countManagerUsers = managerResponse.getTotalResults();

        // 자신에게'만' OrgManager Role이 주어진게 맞는지 탐색
        final boolean existManagerRoleExactly = managerResponse.getResources().stream().filter(ur -> ur.getMetadata().getId().equals(userInfoResponse.getUserId())).count() == 1L;

        LOGGER.debug("existManagerRoleExactly : {} / countManagerUsers : {}", existManagerRoleExactly, countManagerUsers);
        if (existManagerRoleExactly) {
            // OrgManager role을 가진 User가 2명 이상일 경우 무조건 작업 Cancel    
            if (countManagerUsers == 1) {
                // 정확히 일치할 때
                LOGGER.debug("Though user isn't admin, user can delete organization if user's role is OrgManager.");
                LOGGER.debug("User : {}, To delete org : {}(GUID : {})", userInfoResponse.getUserId(), orgSummary.getName(), org.getGuid().toString());
                return Common
                        //.cloudFoundryClient( connectionContext(), tokenProvider( token ) )
                        .cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword)).organizations().delete(DeleteOrganizationRequest.builder().organizationId(orgId).recursive(recursive).async(true).build()).block();
            } else if (countManagerUsers > 1) {
                // 해당 유저 이외의 다른 유저가 OrgManager Role이 있는 경우 (409 : Conflict)
                return DeleteOrganizationResponse.builder().entity(JobEntity.builder().error("OrgManager users is greater than 1. To delete org, you have to unset OrgManager role of other user(s).").errorDetails(ErrorDetails.builder().code(409).build()).build()).build();
            } else {
                // 해당 유저를 포함하여 누구도 OrgManager Role이 없는 경우 (403 : Forbidden)
                return DeleteOrganizationResponse.builder().entity(JobEntity.builder().error("OrgManager users is zero. To delete org, you have to get OrgManager role.").errorDetails(ErrorDetails.builder().code(403).build()).build()).build();
            }
        } else {
            // 해당 유저에게 OrgManager Role이 없는 경우 (403 : Forbidden)
            return DeleteOrganizationResponse.builder().entity(JobEntity.builder().error("You don't have a OrgManager role. To delete org, you have to get OrgManager role.").errorDetails(ErrorDetails.builder().code(403).build()).build()).build();
        }
    }

    //// Org's space : Read only (Space list)
    //// Space ==> SpaceService (CRUD)

    /**
     * 운영자/사용자 포털에서 스페이스 목록을 요청했을때, 해당 조직의 모든 스페이스 목록을 응답한다.
     *
     * @param orgId the org id
     * @param token the token
     * @return Map&lt;String, Object&gt;
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public ListSpacesResponse getOrgSpaces(String orgId, String token) {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token)).spaces().list(ListSpacesRequest.builder().organizationId(orgId).build()).block();
    }

    //// Org's Quota setting : Read, Update
    //// Globally quota detail info. ==> OrgQuotaService (CRUD)

    /**
     * 조직의 Quota를 조회한다. (Org Read : quota(s))
     *
     * @param orgId the org id
     * @param token the token
     * @return Map&lt;String, Object&gt;
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public GetOrganizationQuotaDefinitionResponse getOrgQuota(String orgId, String token) {
        GetOrganizationResponse org = getOrg(orgId, token);
        String quotaId = org.getEntity().getQuotaDefinitionId();

        GetOrganizationQuotaDefinitionResponse response = Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword)).organizationQuotaDefinitions().get(GetOrganizationQuotaDefinitionRequest.builder().organizationQuotaDefinitionId(quotaId).build()).block();

        return response;
    }

    /**
     * 조직에 할당한 Quota를 변경한다. <br>
     * (기존의 Quota GUID와 동일한 경우에도 변경하는 동작을 수행한다) (Org Update : set quota)
     *
     * @param orgId
     * @param org
     * @param token
     * @return UpdateOrganizationResponse
     * @version 2.0
     * @since 2018.5.2
     */
    // Org 
    public UpdateOrganizationResponse updateOrgQuota(String orgId, Org org, String token) {
        Objects.requireNonNull(org.getGuid(), "Org GUID must not be null. Require parameters; guid and quotaGuid.");
        Objects.requireNonNull(org.getQuotaGuid(), "Org GUID must not be null. Require parameters; guid and quotaGuid.");
        String orgGuid = org.getGuid().toString();
        String quotaGuid = org.getQuotaGuid();

        if (!orgId.equals(orgGuid))
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Org GUID in the path doesn't match org GUID in request body.");

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword)).organizations().update(UpdateOrganizationRequest.builder().organizationId(orgGuid).quotaDefinitionId(quotaGuid).build()).block();
    }
}
