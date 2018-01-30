package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CloudUser;
import org.cloudfoundry.client.v2.spaces.GetSpaceSummaryRequest;
import org.cloudfoundry.client.v2.spaces.GetSpaceSummaryResponse;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.codehaus.jackson.map.ObjectMapper;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.mapper.cc.OrgMapper;
import org.openpaas.paasta.portal.api.mapper.cc.SpaceMapper;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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
    private SpaceMapper spaceMapper;
    @Autowired
    private OrgMapper orgMapper;

    /**
     * 공간(스페이스) 목록 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param org   the org
     * @param token the token
     * @return List<CloudSpace>      orgList
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.7.11 최초작성
     */
    public List<CloudSpace> getSpaces(Org org, String token) throws Exception {

        String orgName = org.getOrgName();

        if (!stringNullCheck(orgName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        //요청된 조직내에 있는 공간을 찾는다.
        /* java8 stream api 사용 */
        List<CloudSpace> spaceList = client.getSpaces().stream()
                .filter(cloudSpace -> cloudSpace.getOrganization().getName().equals(orgName))
                .collect(Collectors.toList());

        if (spaceList.isEmpty()) {
            throw new CloudFoundryException(HttpStatus.NO_CONTENT,"No Content","Space not found");
        }

        return spaceList;
    }

    /**
     * 공간을 생성한다.
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.7.11 최초작성
     */
    public boolean createSpace(Space space, String token) throws Exception{

        String orgName = space.getOrgName();
        String newSpaceName = space.getNewSpaceName();

        if(!stringNullCheck(orgName,newSpaceName)){
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        //해당 조직에 동일한 이름의 공간이 존재하는지 확인
            /* java8 stream api 사용 */
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

        return true;
    }


    /**
     * 공간을 삭제한다.
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.7.11 최초작성
     */
    public boolean deleteSpace(Space space, String token) throws Exception{

        String orgName = space.getOrgName();
        String spaceName = space.getSpaceName();

        if (!stringNullCheck(orgName,spaceName)){
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        client.deleteSpace(orgName, spaceName);

        return true;
    }

    /**
     * 공간명을 변경한다.
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.7.11 최초작성
     */
    public boolean renameSpace(Space space, String token) throws Exception{

        String orgName = space.getOrgName();
        String spaceName = space.getSpaceName();
        String newSpaceName = space.getNewSpaceName();

        if(!stringNullCheck(orgName,spaceName,newSpaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        client.renameSpace(orgName, spaceName, newSpaceName);

        return true;
    }

    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param space the space
     * @param token the token
     * @return space summary
     * @throws Exception the exception
     */
    public Space getSpaceSummary(Space space, String token) throws Exception{

        String orgName = space.getOrgName();
        String spaceName = space.getSpaceName();

        if(!stringNullCheck(orgName,spaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
        }

        CustomCloudFoundryClient admin = getCustomCloudFoundryClient(adminUserName, adminPassword);

        String spaceString = admin.getSpaceSummary(space.getOrgName(), space.getSpaceName());
        Space respSpace = new ObjectMapper().readValue(spaceString, Space.class);

        //LOGGER.info(spaceString);

        int memTotal = 0;
        int memUsageTotal = 0;

        for (App app : respSpace.getApps()) {

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

        respSpace.setMemoryLimit(memTotal);
        respSpace.setMemoryUsage(memUsageTotal);

        return respSpace;
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
     * 운영자 포털에서 스페이스 목록을 요청했을때, 해당 조직의 모든 스페이스 목록을 응답한다.
     *
     * @param orgName the org name
     * @return HashMap<String Object>
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.9.12 최초작성
     */
    public List<Object> getSpacesForAdmin(String orgName) throws Exception{

        Map<String, Object> selectedOrg = orgMapper.selectOrg(orgName);
        int orgId = (int)selectedOrg.get("orgId");

        return spaceMapper.getSpacesForAdmin(orgId);
    }

    /**
     * 공간 정보를 조회한다.
     *
     * @param spaceName the space name
     * @param orgId     the org id
     * @return the spaces info
     * @throws Exception the exception
     */
    public List<Space> getSpacesInfo(String spaceName, int orgId) throws Exception{
        Map map = new HashMap();
        map.put("spaceName" , spaceName);
        map.put("orgId" , orgId);
        List selectSpace = spaceMapper.getSpacesInfo(map);
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
        List selectSpace = spaceMapper.getSpacesInfoById(map);
        return selectSpace;
    }


    /**
     * 공간 쿼터를 조회한다.
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.7.11 최초작성
     */
    public String getSpaceQuota(Space space, String token) throws Exception {

        String orgName = space.getOrgName();
        String spaceName = space.getSpaceName();

        if (!stringNullCheck(orgName, spaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
        }

        CustomCloudFoundryClient admin = getCustomCloudFoundryClient(adminUserName, adminPassword);

        String spaceInfo = admin.getSpace(orgName, spaceName);

        LOGGER.info(spaceInfo);

        Space respSpace = new ObjectMapper().readValue(spaceInfo, Space.class);

        if (respSpace.getEntity().getSpaceQuotaDefinitionGuid() == null) {
            return null;
        } else {
            return admin.getSpaceQuota(respSpace.getEntity().getSpaceQuotaDefinitionGuid());
        }

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
