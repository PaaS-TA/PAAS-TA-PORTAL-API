package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudUser;
import org.cloudfoundry.client.v2.spacequotadefinitions.GetSpaceQuotaDefinitionRequest;
import org.cloudfoundry.client.v2.spacequotadefinitions.GetSpaceQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.spaces.*;
import org.cloudfoundry.operations.organizations.OrganizationInfoRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
//import org.openpaas.paasta.portal.api.mapper.cc.OrgMapper;
//import org.openpaas.paasta.portal.api.mapper.cc.SpaceMapper;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;

/**
 * 공간 서비스 - 공간 목록 , 공간 이름 변경 , 공간 생성 및 삭제 등을 제공한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Service
public class SpaceService extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceService.class);
    
    @Autowired
    private AsyncUtilService asyncUtilService;
    
    @Autowired 
    @Lazy // To resolve circular reference
    private OrgService orgService;
    
//    @Autowired
//    private SpaceMapper spaceMapper;
//    @Autowired
//    private OrgMapper orgMapper;

    /**
     * 공간(스페이스) 목록 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param org   the org
     * @param token the token
     * @return ListSpacesResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public ListSpacesResponse getSpaces(String token) {
        ListSpacesResponse response = Common
            .cloudFoundryClient( connectionContext(), tokenProvider( token ) ).spaces()
            .list( ListSpacesRequest.builder().build() ).block();
        
        return response;
    }
    
    /**
     * 공간(스페이스) 목록 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param org   the org
     * @param token the token
     * @return ListSpacesResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public ListSpacesResponse getSpaces(Org org, String token) {
        String orgId = null;
        if (org.getGuid() != null) {
            orgId = org.getGuid().toString();
        } else if (org.getName() != null) {
            orgId = orgService.getOrgId( org.getName(), token );
        } else {
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "To get spaces in org, you must be require org name or org id." );
        }
        
        Objects.requireNonNull( orgId, "Org id must not be null." );
        ListSpacesResponse response = Common
            .cloudFoundryClient( connectionContext(), tokenProvider( token ) ).spaces()
            .list( ListSpacesRequest.builder().organizationId( orgId ).build() ).block();
        
        return response;
    }

    /**
     * 공간을 생성한다. (Space : Create)
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 2.0
     * @since 2018.5.3
     */
    public CreateSpaceResponse createSpace(Space space, String token) throws Exception{
        // TODO
        /*
        String orgName = space.getOrgName();
        String newSpaceName = space.getNewSpaceName();

        if(!stringNullCheck(orgName,newSpaceName)){
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        //해당 조직에 동일한 이름의 공간이 존재하는지 확인
        // java8 stream api 사용
        boolean isSpaceExist = client.getSpaces().stream()
                .anyMatch(existingSpace -> existingSpace.getOrganization().getName().equals(orgName)
                        && existingSpace.getName().equals(newSpaceName));

        if (isSpaceExist) {
            throw new CloudFoundryException(HttpStatus.CONFLICT,"Conflict","Space name already exists");
        }

        client.createSpace(orgName, newSpaceName);
        String userName = client.getCloudInfo().getUser();

        client.setOrgRole(orgName, userName, "users");
        client.setSpaceRole(orgName, newSpaceName, userName, "managers");
        client.setSpaceRole(orgName, newSpaceName, userName, "developers");
        */
        
        Objects.requireNonNull( space.getSpaceName(), "Space name must not be null. Required request body is space name(spaceName) and org GUID (orgGuid)." );
        Objects.requireNonNull( space.getOrgGuid(), "Space name must not be null. Required request body is space name(spaceName) and org GUID (orgGuid)." );
        
        return Common.cloudFoundryClient( connectionContext(), tokenProvider( token ) )
            .spaces().create( CreateSpaceRequest.builder().name( space.getName() )
                .organizationId( space.getOrgGuid() ).build() )
            .block();
    }
    
    /**
     * 공간의 정보를 가져온다. (Space : Read)
     *  
     * @param spaceId
     * @param token
     * @return GetSpaceResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public GetSpaceResponse getSpace(String spaceId, String token) {
        return Common.cloudFoundryClient( connectionContext(), tokenProvider( token ) )
            .spaces().get( GetSpaceRequest.builder().spaceId( spaceId ).build() ).block();
    }

    /**
     * 공간명을 변경한다. (Space : Update)
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public UpdateSpaceResponse renameSpace(Space space, String token) throws Exception{
        String spaceGuid = space.getGuid().toString();
        String newSpaceName = space.getNewSpaceName();
        Objects.requireNonNull( spaceGuid, "Space GUID(guid) must be not null. Request body is made space GUID(guid) and new space name(newSpaceName)." );
        Objects.requireNonNull( newSpaceName, "New space name must be not null. Request body is made space GUID(guid) and new space name(newSpaceName)." );
        if(!stringNullCheck(spaceGuid,newSpaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content(guid or newSpaceName) is missing.");
        }

        return Common.cloudFoundryClient( connectionContext(), tokenProvider( token ) )
            .spaces().update( UpdateSpaceRequest.builder().spaceId( spaceGuid )
                .name( newSpaceName ).build() )
            .block();
    }

    /**
     * 공간을 삭제한다. (Space : Delete)
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public DeleteSpaceResponse deleteSpace(Space space, String token) throws Exception{
        Objects.requireNonNull( space.getGuid(), "Space GUID must not be null. Require parameters; spaceGuid[, recursive]" );

        String spaceGuid = space.getGuid().toString();
        boolean recursive = space.isRecursive();
        if ( !stringNullCheck( spaceGuid ) ) {
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing" );
        }

        /*
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        client.deleteSpace(orgName, spaceName);
        */
        return Common.cloudFoundryClient( connectionContext(), tokenProvider( token ) ).spaces()
            .delete( DeleteSpaceRequest.builder().spaceId( spaceGuid )
                .recursive( recursive ).async( true ).build() ).block();
    }

    public Space getSpaceSummary(Space space, String spaceName) throws Exception{

        String orgName = space.getOrgName();
        if(!stringNullCheck(orgName,spaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
        }

        CustomCloudFoundryClient admin = getCustomCloudFoundryClient(adminUserName, adminPassword);

        String spaceString = admin.getSpaceSummary(orgName, spaceName);
        Space respSpace = new ObjectMapper().readValue(spaceString, Space.class);

        //LOGGER.info(spaceString);
        int memTotal = 0;
        int memUsageTotal = 0;

        for (App app : respSpace.getApps()) {

            memTotal += app.getMemory() * app.getInstances();

            if (app.getState().equals("STARTED")) {
                // space.setAppCountStarted(space.getAppCountStarted() + 1);

                memUsageTotal += app.getMemory() * app.getInstances();

            } else if (app.getState().equals("STOPPED")) {
                //space.setAppCountStopped(space.getAppCountStopped() + 1);
            } else {
                //space.setAppCountCrashed(space.getAppCountCrashed() + 1);
            }
        }

        respSpace.setMemoryLimit(memTotal);
        respSpace.setMemoryUsage(memUsageTotal);

        return respSpace;
    }



    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param spaceId the spaceId
     * @param token the token
     * @return space summary
     * @throws Exception the exception
     */
    public Map<String,Object> getSpaceSummary(String spaceId, String token) throws Exception{

//
//        if(!stringNullCheck(orgName,spaceName)) {
//            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
//        }
//
//        CustomCloudFoundryClient admin = getCustomCloudFoundryClient(adminUserName, adminPassword);
//
//        String spaceString = admin.getSpaceSummary(orgName, spaceName);
//        Space respSpace = new ObjectMapper().readValue(spaceString, Space.class);
//
//        //LOGGER.info(spaceString);
//        int memTotal = 0;
//        int memUsageTotal = 0;
//
//        for (App app : respSpace.getApps()) {
//
//            memTotal += app.getMemory() * app.getInstances();
//
//            if (app.getState().equals("STARTED")) {
//               // space.setAppCountStarted(space.getAppCountStarted() + 1);
//
//                memUsageTotal += app.getMemory() * app.getInstances();
//
//            } else if (app.getState().equals("STOPPED")) {
//                //space.setAppCountStopped(space.getAppCountStopped() + 1);
//            } else {
//                //space.setAppCountCrashed(space.getAppCountCrashed() + 1);
//            }
//        }
//
//        respSpace.setMemoryLimit(memTotal);
//        respSpace.setMemoryUsage(memUsageTotal);
//
//        return respSpace;
        GetSpaceSummaryResponse getSpaceSummaryResponse =
                Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                        .spaces().getSummary(GetSpaceSummaryRequest.builder().spaceId(spaceId).build()).block();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(getSpaceSummaryResponse, Map.class);
    }

    /**
     * 공간 role을 부여한다.
     *
     * @param orgName   the org name
     * @param spaceName the space name
     * @param userName  the user name
     * @param userRole  the user role
     * @param token     the token
     * @return Map space role
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.8.18 최초작성
     */
    public boolean setSpaceRole(String orgName, String spaceName, String userName, String userRole, String token) throws Exception{
        if (!stringNullCheck(orgName, spaceName, userName, userRole)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        String spaceRole = toStringRole(userRole);

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);
        client.setSpaceRole(orgName, spaceName, userName, spaceRole);

        return true;
    }


    /**
     * 공간 role을 제거한다.
     *
     * @param orgName   the org name
     * @param spaceName the space name
     * @param userGuid  the user guid
     * @param userRole  the user role
     * @param token     the token
     * @return Map boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.8.18 최초작성
     */
    public boolean unsetSpaceRole(String orgName, String spaceName, String userGuid, String userRole, String token) throws Exception{
        if (!stringNullCheck(orgName, spaceName, userGuid, userRole)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        String spaceRole = toStringRole(userRole);

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);
        client.unsetSpaceRole(orgName, spaceName, userGuid, spaceRole);

        return true;
    }


    /**
     * role을 문자열로 변환한다.
     *
     * @param userRole
     * @return Map boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.8.18 최초작성
     */
    private String toStringRole(String userRole) {
        String roleStr;

        switch (userRole){
            case "SpaceManager": roleStr = "managers"; break;
            case "SpaceDeveloper": roleStr = "developers"; break;
            case "SpaceAuditor": roleStr = "auditors"; break;
            default: throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Invalid userRole.");
        }

        return roleStr;
    }


    /**
     * 요청된 유저들에 대한 해당 스페이스의 역할목록을 가져온다
     *
     * @param orgName   the org name
     * @param spaceName the space name
     * @param userList  the user list
     * @param token     the token
     * @return users for space role
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.9.05 최초작성
     */
    public List<Map<String, Object>> getUsersForSpaceRole(String orgName, String spaceName, List<Map<String,Object>> userList, String token) throws Exception {

        if (!stringNullCheck(orgName, spaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);
        UUID orgGuid = client.getOrgByName(orgName, true).getMeta().getGuid();

        Future<Map<String, CloudUser>> managers = asyncUtilService.getUsersForSpaceRole_managers(orgGuid, spaceName, client);
        Future<Map<String, CloudUser>> developers = asyncUtilService.getUsersForSpaceRole_developers(orgGuid,spaceName, client);
        Future<Map<String, CloudUser>> auditors = asyncUtilService.getUsersForSpaceRole_auditors(orgGuid,spaceName,client);

        while (!(managers.isDone() && developers.isDone() && auditors.isDone())) {
            Thread.sleep(10);
        }

        //org 유저목록에 스페이스
        List<Map<String, Object>> spaceUserList = putUserList(orgName, spaceName, userList, managers.get() ,developers.get() ,auditors.get());

        return spaceUserList;
    }


    /**
     * 권한별로 수집된 유저정보를 취합하여 하나의 객체로 통합해 리턴한다.
     * @param orgName
     * @param spaceName
     * @param userList
     * @param managers
     * @param developers
     * @param auditors
     * @return spaceUserList
     * @throws Exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.9.05 최초작성
     */
    private List<Map<String, Object>> putUserList(String orgName, String spaceName,
                                                  List<Map<String, Object>> userList,
                                                  Map<String, CloudUser> managers,
                                                  Map<String, CloudUser> developers,
                                                  Map<String, CloudUser> auditors) throws Exception
    {
        List<Map<String, Object>> spaceUserList = new ArrayList<>();

        for(Map<String, Object> userMap : userList) {
            List<String> userRoles = new ArrayList<>();
            if(managers.get(userMap.get("userName")) != null){
                userRoles.add("SpaceManager");
            }
            if(developers.get(userMap.get("userName")) != null){
                userRoles.add("SpaceDeveloper");
            }
            if(auditors.get(userMap.get("userName")) != null){
                userRoles.add("SpaceAuditor");
            }

            userMap.put("orgName", orgName);
            userMap.put("spaceName", spaceName);
            userMap.put("userRoles", userRoles);
            spaceUserList.add(userMap);
        }
        return spaceUserList;
    }

    /**
     * 공간 정보를 조회한다.
     *
     * @param spaceName the space name
     * @param orgId     the org id
     * @return the spaces info
     * @throws Exception the exception
     */
    public List<Space> getSpacesInfo(String spaceName, String orgId) throws Exception{
        Map map = new HashMap();
        map.put("spaceName" , spaceName);
        map.put("orgId" , orgId);
//        List selectSpace = spaceMapper.getSpacesInfo(map);
        List selectSpace = null;
        return selectSpace;
    }

    /**
     * 공간ID로 공간정보를 조회한다.
     *
     * @param spaceId the space id
     * @return the spaces info by id
     * @throws Exception the exception
     */
    public List<Space> getSpacesInfoById(int spaceId) throws Exception{
        Map map = new HashMap();
        map.put("spaceId" , spaceId);
//        List selectSpace = spaceMapper.getSpacesInfoById(map);
        List selectSpace = null;
        return selectSpace;
    }


    /**
     * 공간 쿼터를 조회한다.
     *
     * @param spacequotaid the spaceQuotaId
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.7.11 최초작성
     */
    public Map<String, Object> getSpaceQuota(String spacequotaid, String token) throws Exception {
        GetSpaceQuotaDefinitionResponse getSpaceQuotaDefinitionResponse =
                Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                        .spaceQuotaDefinitions().get(GetSpaceQuotaDefinitionRequest.builder().spaceQuotaDefinitionId(spacequotaid).build()).block();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(getSpaceQuotaDefinitionResponse, Map.class);
    }


    public Space getSpaceSummery(CloudFoundryClient cloudFoundryClient, String spaceId) throws IOException {
        LOGGER.info("Get Space Summary: spaceId={}", spaceId);

        GetSpaceSummaryResponse spaceSummaryResponse = cloudFoundryClient.spaces().getSummary(GetSpaceSummaryRequest.builder().spaceId(spaceId).build()).block();

        Gson gson = new Gson();

        String jsonSummary = gson.toJson(spaceSummaryResponse);
        Space space = new ObjectMapper().readValue(jsonSummary, Space.class);

        int memTotal = 0;
        int memUsageTotal = 0;

        for (App app : space.getApps()) {

            memTotal += app.getMemory() * app.getInstances();

            if (app.getState().equals("STARTED")) {
                space.setAppCountStarted(space.getAppCountStarted() + 1);

                memUsageTotal += app.getMemory() * app.getInstances();

            } else if (app.getState().equals("STOPPED")) {
                space.setAppCountStopped(space.getAppCountStopped() + 1);
            } else {
                space.setAppCountCrashed(space.getAppCountCrashed() + 1);
            }
        }

        space.setMemoryLimit(memTotal);
        space.setMemoryUsage(memUsageTotal);

        return space;
    }

    public String getSpaceId(CloudFoundryClient cloudFoundryClient, String organizationId, String spaceName) {
        LOGGER.info("Get Space Id: organizationId={}, spaceName={}", organizationId, spaceName);

        List<SpaceResource> spaceList = cloudFoundryClient.spaces().list(ListSpacesRequest.builder().organizationId(organizationId).name(spaceName).build()).block().getResources();
        LOGGER.info("Get Space Id: Result size={}", spaceList.size());

        return spaceList.get(0).getMetadata().getId();
    }


}
