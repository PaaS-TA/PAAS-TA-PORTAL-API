package org.openpaas.paasta.portal.api.service;

import org.apache.commons.collections.map.HashedMap;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.OrderDirection;
import org.cloudfoundry.client.v2.featureflags.GetFeatureFlagRequest;
import org.cloudfoundry.client.v2.featureflags.GetFeatureFlagResponse;
import org.cloudfoundry.client.v2.jobs.ErrorDetails;
import org.cloudfoundry.client.v2.jobs.JobEntity;
import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionRequest;
import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.organizations.*;
import org.cloudfoundry.client.v2.spaces.*;
import org.cloudfoundry.client.v2.users.UserResource;
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.organizations.AssignOrganizationDefaultIsolationSegmentRequest;
import org.cloudfoundry.client.v3.organizations.AssignOrganizationDefaultIsolationSegmentResponse;
import org.cloudfoundry.operations.organizations.OrganizationDetail;
import org.cloudfoundry.operations.organizations.OrganizationInfoRequest;
import org.cloudfoundry.operations.useradmin.OrganizationUsers;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.uaa.users.User;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.UserRole;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

@EnableAsync
@Service
public class OrgServiceV2 extends Common {
    private final Logger LOGGER = getLogger(this.getClass());

    @Autowired
    private UserServiceV2 userServiceV2;

    @Autowired
    private SpaceServiceV2 spaceServiceV2;

    @Autowired
    private CommonService commonService;

    @Autowired
    private OrgQuotaServiceV2 orgQuotaServiceV2;

    private BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<>(2);

    protected OrgServiceV2() {
        blockingQueue.add(new Object());
    }

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    /**
     * 이름과 할당량 GUID를 받아 조직을 생성한다. (Org Create)
     *
     * @param org
     * @param token
     * @return CreateOrganizationResponse
     * @version 2.0
     * @since 2018.5.2
     */
    public Map createOrg(final Org org, final String token) {
        try {
            final CreateOrganizationResponse response = cloudFoundryClient(tokenProvider(token)).organizations().create(CreateOrganizationRequest.builder().name(org.getOrgName()).quotaDefinitionId(org.getQuotaGuid()).build()).block();
            return new HashMap() {{
                put("RESULT", "SUCCESS");
            }};
        } catch (Exception e) {
            return new HashMap() {{
                put("RESULT", "FAIL");
                put("MSG", e.getMessage());
            }};
        }
    }

    public boolean isExistOrgName(final String orgName) {
        try {
            return orgName.equals(getOrgUsingName(orgName).getName());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExistOrg(final String orgId) {
        try {
            return orgId.equals(getOrg(orgId).getMetadata().getId());
        } catch (Exception e) {
            return false;
        }
    }

    public GetOrganizationResponse getOrg(final String orgId) {
        return getOrg(orgId, null);
    }

    /**
     * 조직 Id를 이용해 조직 정보를 조회한다. (Org Read, fully info.)
     *
     * @param orgId                     the org id
     * @param reactorCloudFoundryClient the ReactorCloudFoundryClient
     * @return GetOrganizationResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public GetOrganizationResponse getOrg(final String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        return reactorCloudFoundryClient.organizations().get(GetOrganizationRequest.builder().organizationId(orgId).build()).block();
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
    public SummaryOrganizationResponse getOrgSummary(final String orgId, final String token) {
        return cloudFoundryClient(tokenProvider(token)).organizations().summary(SummaryOrganizationRequest.builder().organizationId(orgId).build()).block();
    }

    //@HystrixCommand(commandKey = "getOrgSummaryMap")
    public Map getOrgSummaryMap(final String orgId, final ReactorCloudFoundryClient reactorClients) {
        LOGGER.info(DateTime.now().toString());
        LOGGER.info("===========================");
        Map map = new HashedMap();
        try {
            /*
             * OrgSummary 정보 추출
             *
             */
            SummaryOrganizationResponse summaryOrganizationResponse = reactorClients.organizations().summary(SummaryOrganizationRequest.builder().organizationId(orgId).build()).block();
            List<OrganizationSpaceSummary> organizationSpaceSummaries = summaryOrganizationResponse.getSpaces();


            int memDevTotal = 0;
            int memProTotal = 0;
            int appTotal = 0;
            int serviceTotal = 0;
            for (OrganizationSpaceSummary organizationSpaceSummary : organizationSpaceSummaries) {

                memDevTotal += organizationSpaceSummary.getMemoryDevelopmentTotal();
                memProTotal += organizationSpaceSummary.getMemoryProductionTotal();
                appTotal += organizationSpaceSummary.getApplicationCount();
                serviceTotal += organizationSpaceSummary.getServiceCount();
            }

            map.put("all_memoryDevelopmentTotal", memDevTotal);
            map.put("all_memoryProductionTotal", memProTotal);
            map.put("all_applicationTotal", appTotal);
            map.put("all_serviceTotal", serviceTotal);


            List<Map> summaryOrganization = objectMapper.convertValue(organizationSpaceSummaries, List.class);
            map.put("resource", summaryOrganization);
            /*
             * Org quota 정보 추출
             */
            GetOrganizationResponse getOrganizationResponse = getOrg(orgId, reactorClients);
            LOGGER.info("Org name :: " + getOrganizationResponse.getEntity().getName());
            LOGGER.info("Org Quotaid :: " + getOrganizationResponse.getEntity().getQuotaDefinitionId());
            String quotaDefinitionId = getOrganizationResponse.getEntity().getQuotaDefinitionId();

            //GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = orgQuotaService.getOrgQuotaDefinitions(quotaDefinitionId, token);
            GetOrganizationQuotaDefinitionResponse getOrganizationQuotaDefinitionResponse = reactorClients.organizationQuotaDefinitions().get(GetOrganizationQuotaDefinitionRequest.builder().organizationQuotaDefinitionId(quotaDefinitionId).build()).log().block();
            Map quota = objectMapper.convertValue(getOrganizationQuotaDefinitionResponse, Map.class);
            quota.remove("metadata");
            map.put("quota", quota.get("entity"));

        } catch (Exception e) {
            e.printStackTrace();
            map.clear();
        }
        LOGGER.info("===========================");
        LOGGER.info(DateTime.now().toString());
        return map;
    }


    /**
     * 조직 목록을 조회한다. 단, 내부의 resources만 추출해서 반환한다.
     *
     * @param token the token
     * @return List<OrganizationResource> organization list
     * @version 2.0
     * @since 2018.4.22
     */
    @Deprecated
    public List<OrganizationResource> getOrgs(String token) {
        return cloudFoundryClient(tokenProvider(token)).organizations().list(ListOrganizationsRequest.builder().build()).block().getResources();
    }

    /**
     * 사용자 포털에서 조직목록을 요청했을때, 한 페이지당 10개의 조직목록을 응답한다. (Org Read for all)
     *
     * @param reactorCloudFoundryClient the ReactorCloudFoundryClient
     * @return ListOrganizationsResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public ListOrganizationsResponse getOrgsForUser(final ReactorCloudFoundryClient reactorCloudFoundryClient, int page) {
        return reactorCloudFoundryClient.organizations().list(ListOrganizationsRequest.builder().page(page).resultsPerPage(10).build()).block();
    }

    /**
     * 운영자 포털에서 조직목록을 요청했을때, 모든 조직목록을 응답한다. (Org Read for all)
     *
     * @param token the token
     * @return ListOrganizationsResponse
     * @author cheolhan
     * @version 2.0
     * @since 2018.8.17
     */
    public ListOrganizationsResponse getAllOrgsForUser(String token) {
        return cloudFoundryClient(tokenProvider(token)).organizations().list(ListOrganizationsRequest.builder().build()).block();
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
        return cloudFoundryClient().organizations().list(ListOrganizationsRequest.builder().build()).block();
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
        return getOrgUsingName(orgName, token).getId();
    }

    public OrganizationDetail getOrgUsingName(final String name) {
        return getOrgUsingName(name, null);
    }

    public OrganizationDetail getOrgUsingName(final String name, final String token) {
        final TokenProvider internalTokenProvider;
        if (null != token && !"".equals(token)) internalTokenProvider = tokenProvider(token);
        else internalTokenProvider = tokenProvider();

        return cloudFoundryOperations(connectionContext(), internalTokenProvider).organizations().get(OrganizationInfoRequest.builder().name(name).build()).block();
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
    public Map renameOrg(Org org, String token) {
        Map resultMap = new HashMap();

        try {
            cloudFoundryClient(tokenProvider(token)).organizations().update(UpdateOrganizationRequest.builder().organizationId(org.getGuid().toString()).name(org.getNewOrgName()).build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 사용자 포털에서 조직을 삭제한다. (Org Delete)<br>
     * 만약 false가 넘어올 경우, 권한이 없거나 혹은
     *
     * @param orgId the organization guid
     * @param token the client's token
     * @return DeleteOrganizationResponse
     * @throws Exception
     * @author hgcho, ParkCheolhan
     * @version 2.1
     * @since 2018.5.2
     */
    public Map deleteOrg(String orgId, boolean recursive, String token) throws Exception {
        Map resultMap = new HashMap();
        try {
            final SummaryOrganizationResponse orgSummary = getOrgSummary(orgId, token);

            //// Check Admin user
            // 현재 token의 유저 정보를 가져온다.
            final User user = userServiceV2.getUser(token);

            // Admin 계정인지 확인
            final boolean isAdmin = user.getUserName().equals(adminUserName);

            if (isAdmin) {
                // Admin 계정인 경우 강제적으로 Org 밑의 모든 리소스(spaces, buildpack, app...)를 recursive하게 제거한다.
                LOGGER.warn("Org({}) exists user(s) included OrgManager role... but it deletes forced.", orgSummary.getName());
                DeleteOrganizationResponse eleteOrganizationResponse = cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword)).organizations().delete(DeleteOrganizationRequest.builder().organizationId(orgId).recursive(true).async(true).build()).block();

                resultMap.put("result", true);
                return resultMap;
            }

            ///// Real user
            // 지우려는 조직의 OrgManager Role이 주어진 유저를 찾는다.
            // TODO 특정 Role에 해당하는 유저를 찾는 메소드가 구현되면, 해당 메소드로 교체할 것. (hgcho)
            final ListOrganizationManagersResponse managerResponse = cloudFoundryClient(connectionContext(), tokenProvider(token)).organizations().listManagers(ListOrganizationManagersRequest.builder().organizationId(orgId).build()).block();

            // OrgManager role을 가진 유저 수 파악
            final int countManagerUsers = managerResponse.getTotalResults();

            // 자신에게'만' OrgManager Role이 주어진게 맞는지 탐색
            final boolean existManagerRoleExactly = managerResponse.getResources().stream().filter(ur -> ur.getMetadata().getId().equals(user.getId())).count() == 1L;

            LOGGER.debug("existManagerRoleExactly : {} / countManagerUsers : {}", existManagerRoleExactly, countManagerUsers);
            if (countManagerUsers >= 1 && existManagerRoleExactly) {
                // OrgManager 유저 수가 1명 이상이면서 본인이 OrgManager 일 때
                LOGGER.debug("Though user isn't admin, user can delete organization if user's role is OrgManager.");
                LOGGER.debug("User : {}, To delete org : {} (GUID : {})", user.getId(), orgSummary.getName(), orgId);
                DeleteOrganizationResponse eleteOrganizationResponse = cloudFoundryClient(connectionContext(), tokenProvider()).organizations().delete(DeleteOrganizationRequest.builder().organizationId(orgId).recursive(recursive).async(true).build()).block();

                resultMap.put("result", true);
                return resultMap;

            /*
            // 해당 유저 이외의 다른 유저가 OrgManager Role이 있는 경우 (409 : Conflict)
            return DeleteOrganizationResponse.builder().entity( JobEntity.builder().error( "OrgManager users is greater than 1. To delete org, you have to unset OrgManager role of other user(s)." ).errorDetails( ErrorDetails.builder().code( 409 ).build() ).build() ).build();
            */
            } else {
                // 해당 유저에게 OrgManager Role이 없는 경우 (403 : Forbidden)
                DeleteOrganizationResponse eleteOrganizationResponse = DeleteOrganizationResponse.builder().entity(JobEntity.builder().error("You don't have a OrgManager role. To delete org, you have to get OrgManager role.").errorDetails(ErrorDetails.builder().code(403).build()).id("httpstatus-403").build()).build();

                resultMap.put("result", false);
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }


    /**
     * 운영자/사용자 포털에서 스페이스 목록을 요청했을때, 해당 조직의 모든 스페이스 목록을 응답한다.
     *
     * @param orgId                     the org id
     * @param reactorCloudFoundryClient the ReactorCloudFoundryClient
     * @return Map&lt;String, Object&gt;
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public ListSpacesResponse getOrgSpaces(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        return spaceServiceV2.getSpaces(orgId, reactorCloudFoundryClient);
    }

    //// Org's Quota setting : Read, Update
    //// Globally quota detail info. ==> OrgQuotaService (CRUD)

    /**
     * 조직의 Quota를 조회한다. (Org Read : quota(s))
     *
     * @param orgId                     the org id
     * @param reactorCloudFoundryClient the ReactorCloudFoundryClient
     * @return Map&lt;String, Object&gt;
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public GetOrganizationQuotaDefinitionResponse getOrgQuota(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        GetOrganizationResponse org = getOrg(orgId, reactorCloudFoundryClient);
        String quotaId = org.getEntity().getQuotaDefinitionId();

        return reactorCloudFoundryClient.organizationQuotaDefinitions().get(GetOrganizationQuotaDefinitionRequest.builder().organizationQuotaDefinitionId(quotaId).build()).block();
    }

    /**
     * 조직에 할당한 Quota를 변경한다. <br>
     * (기존의 Quota GUID와 동일한 경우에도 변경하는 동작을 수행한다) (Org Update : set quota)
     *
     * @param orgId
     * @param org
     * @param token
     * @return UpdateOrganizationResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.2
     */
    public Map updateOrgQuota(String orgId, Org org, String token) {
        Map resultMap = new HashMap();

        try {
            cloudFoundryClient().organizations().update(UpdateOrganizationRequest.builder().organizationId(orgId).quotaDefinitionId(org.getQuotaGuid()).build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }


    //// Org-Role
    private enum OrgRole {
        OrgManager, BillingManager, OrgAuditor, ORGMANAGER, BILLINGMANAGER, ORGAUDITOR,
    }

    protected List<UserResource> listAllOrgUsers(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        final ListOrganizationUsersResponse response = reactorCloudFoundryClient.organizations().listUsers(ListOrganizationUsersRequest.builder().organizationId(orgId).orderDirection(OrderDirection.ASCENDING).build()).block();
        return response.getResources();
    }

    private List<UserResource> listOrgManagerUsers(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        final ListOrganizationManagersResponse response = reactorCloudFoundryClient.organizations().listManagers(ListOrganizationManagersRequest.builder().organizationId(orgId).orderDirection(OrderDirection.ASCENDING).build()).block();

        return response.getResources();
    }

    private List<UserResource> listBillingManagerUsers(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        final ListOrganizationBillingManagersResponse response = reactorCloudFoundryClient.organizations().listBillingManagers(ListOrganizationBillingManagersRequest.builder().organizationId(orgId).orderDirection(OrderDirection.ASCENDING).build()).block();

        return response.getResources();
    }

    private List<UserResource> listOrgAuditorUsers(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        final ListOrganizationAuditorsResponse response = reactorCloudFoundryClient.organizations().listAuditors(ListOrganizationAuditorsRequest.builder().organizationId(orgId).orderDirection(OrderDirection.ASCENDING).build()).block();

        return response.getResources();
    }

    public Map<String, Collection<UserRole>> getOrgUserRoles(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        Map<String, UserRole> userRoles = new HashMap<>();
        listAllOrgUsers(orgId, reactorCloudFoundryClient).stream().map(resource -> UserRole.builder().userId(resource.getMetadata().getId()).userEmail(resource.getEntity().getUsername()).modifiableRoles(true).build()).filter(ur -> null != ur).forEach(ur -> userRoles.put(ur.getUserId(), ur));

        listOrgManagerUsers(orgId, reactorCloudFoundryClient).stream().map(ur -> userRoles.get(ur.getMetadata().getId())).filter(ur -> null != ur).forEach(ur -> ur.addRole("OrgManager"));

        listBillingManagerUsers(orgId, reactorCloudFoundryClient).stream().map(ur -> userRoles.get(ur.getMetadata().getId())).filter(ur -> null != ur).forEach(ur -> ur.addRole("BillingManager"));

        listOrgAuditorUsers(orgId, reactorCloudFoundryClient).stream().map(ur -> userRoles.get(ur.getMetadata().getId())).filter(ur -> null != ur).forEach(ur -> ur.addRole("OrgAuditor"));
        //roles.put( "all_users",  );
        final Map<String, Collection<UserRole>> result = new HashMap<>();
        result.put("user_roles", userRoles.values());
        return result;
    }

    //@HystrixCommand(commandKey = "getOrgUserRolesByOrgName")
    public OrganizationUsers getOrgUserRolesByOrgName(String orgName, String token) {
        return cloudFoundryOperations(tokenProvider(token)).userAdmin().listOrganizationUsers(org.cloudfoundry.operations.useradmin.ListOrganizationUsersRequest.builder().organizationName(orgName).build()).block();
    }

    //@HystrixCommand(commandKey = "isOrgManagerUsingOrgName")
    public boolean isOrgManagerUsingOrgName(String orgName, String token) {
        final String orgId = getOrgId(orgName, token);
        final String userId = userServiceV2.getUser(token).getId();
        return isOrgManager(orgId, userId);
    }

    //@HystrixCommand(commandKey = "isOrgManagerUsingToken")
    public boolean isOrgManagerUsingToken(String orgId, String token) {
        final String userId = userServiceV2.getUser(token).getId();
        return isOrgManager(orgId, userId);
    }

    //@HystrixCommand(commandKey = "isOrgManager")
    public boolean isOrgManager(String orgId, String userId) {
        return hasOrgRole(orgId, userId, OrgRole.OrgManager.name());
    }

    //@HystrixCommand(commandKey = "isBillingManager")
    public boolean isBillingManager(String orgId, String userId) {
        return hasOrgRole(orgId, userId, OrgRole.BillingManager.name());
    }

    //@HystrixCommand(commandKey = "isOrgAuditor")
    public boolean isOrgAuditor(String orgId, String userId) {
        return hasOrgRole(orgId, userId, OrgRole.OrgAuditor.name());
    }


    private enum SpaceRole {
        SpaceManager, SpaceDeveloper, SpaceAuditor
    }

    private boolean hasOrgRole(String orgId, String userId, String role) {

        Objects.requireNonNull(role, "role");

        final String roleKey = "user_roles";
        Stream<UserRole> userRoles = getOrgUserRoles(orgId, cloudFoundryClient()).get(roleKey).stream().filter(ur -> ur.getRoles().contains(role));
        boolean matches = userRoles.anyMatch(ur -> ur.getUserId().equals(userId));

        return matches;
    }

    private AssociateOrganizationManagerResponse associateOrgManager(String orgId, String userId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        spaceServiceV2.associateAllSpaceUserRolesByOrgId(orgId, userId, targetSpaceRole(OrgRole.OrgManager), reactorCloudFoundryClient);

        return reactorCloudFoundryClient.organizations().associateManager(AssociateOrganizationManagerRequest.builder().organizationId(orgId).managerId(userId).build()).block();
    }

    private AssociateOrganizationBillingManagerResponse associateBillingManager(String orgId, String userId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        // CHECK : Is needed to bill Org's Billing manager?
        spaceServiceV2.associateAllSpaceUserRolesByOrgId(orgId, userId, targetSpaceRole(OrgRole.BillingManager), reactorCloudFoundryClient);

        return reactorCloudFoundryClient.organizations().associateBillingManager(AssociateOrganizationBillingManagerRequest.builder().organizationId(orgId).billingManagerId(userId).build()).block();
    }

    private AssociateOrganizationAuditorResponse associateOrgAuditor(String orgId, String userId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        spaceServiceV2.associateAllSpaceUserRolesByOrgId(orgId, userId, targetSpaceRole(OrgRole.OrgAuditor), reactorCloudFoundryClient);

        return reactorCloudFoundryClient.organizations().associateAuditor(AssociateOrganizationAuditorRequest.builder().organizationId(orgId).auditorId(userId).build()).block();
    }

    /**
     * 조직에 속한 유저에 대한 역할(Role)을 할당한다.
     *
     * @param orgId
     * @param userId
     * @param role
     * @param token
     * @return
     */
    public AbstractOrganizationResource associateOrgUserRole(String orgId, String userId, String role, String token) {
        try {
            final Object lock = blockingQueue.take();
            token = adminToken(token);
            ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            Objects.requireNonNull(orgId, "Org Id");
            Objects.requireNonNull(userId, "User Id");
            Objects.requireNonNull(role, "role");

            final OrgRole roleEnum;
            try {
                roleEnum = OrgRole.valueOf(role);
            } catch (IllegalArgumentException e) {
                LOGGER.error("This role is invalid : {}", role);
                throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Request role is invalid : " + role);
            }

            AbstractOrganizationResource response;
            switch (roleEnum) {
                case OrgManager:
                case ORGMANAGER:
                    response = associateOrgManager(orgId, userId, reactorCloudFoundryClient);
                    break;
                case BillingManager:
                case BILLINGMANAGER:
                    response = associateBillingManager(orgId, userId, reactorCloudFoundryClient);
                    break;
                case OrgAuditor:
                case ORGAUDITOR:
                    response = associateOrgAuditor(orgId, userId, reactorCloudFoundryClient);
                    break;
                default:
                    throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Request role is invalid : " + role);
            }

            blockingQueue.put(lock);

            return response;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> targetSpaceRole(OrgRole orgRole) {
        final Set<String> targetSpaceRole = new HashSet<>();
        switch (orgRole) {
            case OrgManager:
            case ORGMANAGER:
                targetSpaceRole.add(SpaceRole.SpaceManager.name());
            case OrgAuditor:
            case ORGAUDITOR:
                targetSpaceRole.add(SpaceRole.SpaceDeveloper.name());
            case BillingManager:
            case BILLINGMANAGER:
                targetSpaceRole.add(SpaceRole.SpaceAuditor.name());
                break;
            default:
                throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Request role is invalid : " + orgRole);
        }

        return targetSpaceRole;
    }

    private void removeOrgManager(String orgId, String userId) {
        LOGGER.debug("---->> Remove OrgManager role of member({}) in org({}).", userId, orgId);
        cloudFoundryClient().organizations().removeManager(RemoveOrganizationManagerRequest.builder().organizationId(orgId).managerId(userId).build()).block();
    }


    private void removeBillingManager(String orgId, String userId) {
        LOGGER.debug("---->> Remove BillingManager role of member({}) in org({}).", userId, orgId);
        cloudFoundryClient().organizations().removeBillingManager(RemoveOrganizationBillingManagerRequest.builder().organizationId(orgId).billingManagerId(userId).build()).block();
    }

    private void removeOrgAuditor(String orgId, String userId) {
        LOGGER.debug("---->> Remove OrgAuditor role of member({}) in org({}).", userId, orgId);
        cloudFoundryClient().organizations().removeAuditor(RemoveOrganizationAuditorRequest.builder().organizationId(orgId).auditorId(userId).build()).block();
    }


    private void removeAllRoles(String orgId, String userId) {
        try {
            final Object lock = blockingQueue.take();

            LOGGER.debug("--> Remove all member({})'s roles in org({}).", userId, orgId);
            spaceServiceV2.removeAllSpaceUserRolesByOrgId(orgId, userId, targetSpaceRole(OrgRole.OrgManager));
            removeOrgManager(orgId, userId);
            removeBillingManager(orgId, userId);
            removeOrgAuditor(orgId, userId);
            LOGGER.debug("--> Done to remove all member({})'s roles in org({}).", userId, orgId);

            blockingQueue.put(lock);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 조직에 속한 유저에 대한 역할(Role)을 제거한다.
     *
     * @param orgId
     * @param userId
     * @param role
     * @param token  (but ignore a token because of removing manager forced)
     */
    //@HystrixCommand(commandKey = "removeOrgUserRole")
    public void removeOrgUserRole(String orgId, String userId, String role, String token) {
        try {
            Objects.requireNonNull(orgId, "Org Id");
            Objects.requireNonNull(userId, "User Id");
            Objects.requireNonNull(role, "role");

            final OrgRole roleEnum;
            try {
                roleEnum = OrgRole.valueOf(role);
            } catch (IllegalArgumentException e) {
                LOGGER.error("This role is invalid : {}", role);
                return;
            }
            switch (roleEnum) {
                case OrgManager:
                case ORGMANAGER:
                    removeOrgManager(orgId, userId);
                    break;
                case BillingManager:
                case BILLINGMANAGER:
                    removeBillingManager(orgId, userId);
                    break;
                case OrgAuditor:
                case ORGAUDITOR:
                    removeOrgAuditor(orgId, userId);
                    break;
                default:
                    throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Request role is invalid : " + role);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO invite user
    public void inviteUser(String orgId, String userId, String token) {

    }

    // TODO cancel invite user
    public void cancelInvitionUser() {

    }

    // TODO cancel member
    //@HystrixCommand(commandKey = "cancelOrganizationMember")
    public boolean cancelOrganizationMember(String orgId, String userId, String token) {
        final boolean isManager = isOrgManager(orgId, userId);
        LOGGER.info("isOrgManager : {} / Org Guid : {} / User Guid : {}", isManager, orgId, userId);

        try {
            removeAllRoles(orgId, userId);
            cloudFoundryClient(connectionContext(), tokenProvider()).organizations().removeUser(RemoveOrganizationUserRequest.builder().organizationId(orgId).userId(userId).build()).block();

            return true;
        } catch (Exception ex) {
            LOGGER.error("Fail to cancel organization member : org ID {} / user ID {}", orgId, userId);
            LOGGER.error("Occured a exception because of canceling organization member...", ex);
            return false;
        }
    }

    //@HystrixCommand(commandKey = "associateOrgUserRole2")
    public boolean associateOrgUserRole2(Map body) {
        try {
            Map<String, Object> inviteAcceptMap = commonService.procCommonApiRestTemplate("/v2/email/inviteAccept", HttpMethod.POST, body, null);
            LOGGER.info(inviteAcceptMap.toString());

            if (inviteAcceptMap.get("result").toString().equals("false")) {
                return false;
            }

            String id = inviteAcceptMap.get("id").toString();
            String orgGuid = inviteAcceptMap.get("orgGuid").toString();
            String userEmail = inviteAcceptMap.get("userId").toString();
            String userId = userServiceV2.getUserIdByUsername(userEmail);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = null;

            try {
                jsonObj = (JSONObject) jsonParser.parse(inviteAcceptMap.get("role").toString());
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }

            JSONArray orgArray = (JSONArray) jsonObj.get("org");

            for (int i = 0; i < orgArray.size(); i++) {
                JSONObject orgObj = (JSONObject) orgArray.get(i);

                cloudFoundryClient().organizations().associateUser(AssociateOrganizationUserRequest.builder().organizationId(orgGuid).userId(userId).build()).block();

                if (orgObj.get("om").toString().equals("true")) {
                    LOGGER.info("om");
                    AssociateOrganizationManagerResponse associateOrganizationManagerResponse = cloudFoundryClient(connectionContext(), tokenProvider()).organizations().associateManager(AssociateOrganizationManagerRequest.builder().organizationId(orgGuid).managerId(userId).build()).block();
                }
                if (orgObj.get("bm").toString().equals("true")) {
                    LOGGER.info("bm");
                    AssociateOrganizationBillingManagerResponse associateOrganizationBillingManagerResponse = cloudFoundryClient(connectionContext(), tokenProvider()).organizations().associateBillingManager(AssociateOrganizationBillingManagerRequest.builder().organizationId(orgGuid).billingManagerId(userId).build()).block();
                }
                if (orgObj.get("oa").toString().equals("true")) {
                    LOGGER.info("oa");
                    AssociateOrganizationAuditorResponse associateOrganizationAuditorResponse = cloudFoundryClient(connectionContext(), tokenProvider()).organizations().associateAuditor(AssociateOrganizationAuditorRequest.builder().organizationId(orgGuid).auditorId(userId).build()).block();
                }
            }

            JSONArray spaceArray = (JSONArray) jsonObj.get("space");

            for (int j = 0; j < spaceArray.size(); j++) {
                JSONObject spaceObj = (JSONObject) spaceArray.get(j);
                Set key = spaceObj.keySet();
                Iterator<String> iter = key.iterator();

                while (iter.hasNext()) {
                    String keyname = iter.next();
                    JSONArray spaceArray2 = (JSONArray) spaceObj.get(keyname);

                    for (int k = 0; k < spaceArray2.size(); k++) {
                        JSONObject spaceObj2 = (JSONObject) spaceArray2.get(k);

                        if (spaceObj2.get("sm").toString().equals("true")) {
                            LOGGER.info("sm");
                            AssociateSpaceManagerResponse associateSpaceManagerResponse = cloudFoundryClient(connectionContext(), tokenProvider()).spaces().associateManager(AssociateSpaceManagerRequest.builder().spaceId(keyname).managerId(userId).build()).block();
                        }
                        if (spaceObj2.get("sd").toString().equals("true")) {
                            LOGGER.info("sd");
                            AssociateSpaceDeveloperResponse associateSpaceDeveloperResponse = cloudFoundryClient(connectionContext(), tokenProvider()).spaces().associateDeveloper(AssociateSpaceDeveloperRequest.builder().spaceId(keyname).developerId(userId).build()).block();
                        }
                        if (spaceObj2.get("sa").toString().equals("true")) {
                            LOGGER.info("sa");
                            AssociateSpaceAuditorResponse associateSpaceAuditorResponse = cloudFoundryClient(connectionContext(), tokenProvider()).spaces().associateAuditor(AssociateSpaceAuditorRequest.builder().spaceId(keyname).auditorId(userId).build()).block();
                        }
                    }
                }
            }

            inviteAcceptMap.put("gubun", "success");

            Map<String, Object> inviteAcceptUpdateMap = commonService.procCommonApiRestTemplate("/v2/email/inviteAcceptUpdate", HttpMethod.POST, inviteAcceptMap, null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 운영자 포털에서 조직목록을 요청했을때, 페이지별 조직목록을 응답한다.
     *
     * @return ListOrganizationsResponse
     * @version 2.0
     * @since 2018.4.22
     */
    //@HystrixCommand(commandKey = "getOrgsForAdminAll")
    public ListOrganizationsResponse getOrgsForAdminAll(int number) {
        return cloudFoundryClient().organizations().list(ListOrganizationsRequest.builder().page(number).build()).block();
    }

    /**
     * 플래그 사용여부를 가져온다.
     *
     * @return Map
     * @version 2.0
     * @since 2018.7.30
     */
    public Map orgFlag(String flagname, String token) throws Exception {
        try {
            GetFeatureFlagResponse getFeatureFlagResponse = cloudFoundryClient(tokenProvider(token)).featureFlags().get(GetFeatureFlagRequest.builder().name(flagname).build()).block();
            return new HashMap() {{
                put("RESULT", getFeatureFlagResponse.getEnabled());
            }};
        } catch (Exception e) {
            return new HashMap() {{
                put("RESULT", "FAIL");
                put("MSG", e.getMessage());
            }};
        }
    }

    /**
     * Organizations 에 Isolation Segments default 를 설정한다.
     *
     * @param organizationsId    the organizations id
     * @param isolationSegmentId the isolation segement id
     * @return AddIsolationSegmentOrganizationEntitlementResponse
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
     * @throws Exception the exception
     */
    public AssignOrganizationDefaultIsolationSegmentResponse resetOrgDefaultIsolationSegments(String organizationsId) throws Exception {
        return cloudFoundryClient().organizationsV3().assignDefaultIsolationSegment(AssignOrganizationDefaultIsolationSegmentRequest.builder().organizationId(organizationsId).build()).block();
    }


}
