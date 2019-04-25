package org.openpaas.paasta.portal.api.controller;



import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.spaces.*;
import org.cloudfoundry.client.v3.spaces.*;
import org.cloudfoundry.client.v3.spaces.GetSpaceResponse;
import org.cloudfoundry.client.v3.spaces.ListSpacesResponse;
import org.openpaas.paasta.portal.api.model.Space;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.UserRole;
import org.openpaas.paasta.portal.api.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cheolhan on 2019-04-24.
 */
@RestController
public class SpaceControllerV3 extends Common {
    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////


    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceControllerV3.class);

    /**
     * The Space service.
     */
    @Autowired
    SpaceServiceV3 spaceServiceV3;
    /**
     * The Org service.
     */
    @Autowired
    OrgServiceV3 orgServiceV3;

    @Autowired
    UserServiceV3 userServiceV3;

    @Autowired
    AppServiceV3 appServiceV3;

    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param spaceid the spaceId
     * @param token   the token
     * @return Space respSpace
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/spaces/{spaceid}/summary")
    public GetSpaceSummaryResponse getSpaceSummary(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        GetSpaceSummaryResponse respSapceSummary = spaceServiceV3.getSpaceSummary(spaceid, cloudFoundryClient(connectionContext(), tokenProvider(token)));
        return respSapceSummary;
    }

    /**
     * 공간 요약 정보를 조회한다. (관리자)
     *
     * @param spaceid the spaceId
     * @param token   the token
     * @return Space respSpace
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/spaces/{spaceid}/summary-admin")
    public GetSpaceSummaryResponse getSpaceSummaryAdmin(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        GetSpaceSummaryResponse respSapceSummary = spaceServiceV3.getSpaceSummary(spaceid, cloudFoundryClient());
        return respSapceSummary;
    }


    /**
     * 공간 요약 정보 리스트를 조회한다.[dashboard]
     *
     * @param spaceid the spaceId
     * @param token   the token
     * @return Space respSpace
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/spaces/{spaceid}/summarylist")
    public Map getSpaceSummary2(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("Get SpaceSummary Start : " + spaceid);
        GetSpaceSummaryResponse respSapceSummary = spaceServiceV3.getSpaceSummary(spaceid, cloudFoundryClient(tokenProvider(token)));

        Map<String, Object> resultMap = new HashMap<>();
        List<SpaceApplicationSummary> appsArray = new ArrayList<>();
        List<Map<String, Object>> appArray = new ArrayList<>();

        //TODO
        resultMap.put("apps", respSapceSummary.getApplications());
        resultMap.put("guid", respSapceSummary.getId());
        resultMap.put("name", respSapceSummary.getName());
        resultMap.put("services", respSapceSummary.getServices());

        for (SpaceApplicationSummary sapceApplicationSummary : respSapceSummary.getApplications()) {
            Map<String, Object> resultMap2 = new HashMap<>();

            try {
                if (sapceApplicationSummary.getState().equals("STARTED")) {
                    ApplicationStatisticsResponse applicationStatisticsResponse = appServiceV3.getAppStats(sapceApplicationSummary.getId(), token);

                    Double cpu = 0.0;
                    Double mem = 0.0;
                    Double disk = 0.0;
                    int cnt = 0;
                    for (int i = 0; i < applicationStatisticsResponse.getInstances().size(); i++) {
                        if (applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getState().equals("RUNNING")) {
                            Double instanceCpu = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getCpu();
                            Long instanceMem = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getMemory();
                            Long instanceMemQuota = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getMemoryQuota();
                            Long instanceDisk = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getDisk();
                            Long instanceDiskQuota = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getDiskQuota();

                            if (instanceCpu != null) cpu = cpu + instanceCpu * 100;
                            if (instanceMem != null) mem = mem + (double) instanceMem / (double) instanceMemQuota * 100;
                            if (instanceDisk != null)
                                disk = disk + (double) instanceDisk / (double) instanceDiskQuota * 100;

                            cnt++;
                        }
                    }

                    cpu = cpu / cnt;
                    mem = mem / cnt;
                    disk = disk / cnt;

                    resultMap2.put("guid", sapceApplicationSummary.getId());
                    resultMap2.put("cpuPer", Double.parseDouble(String.format("%.2f%n", cpu)));
                    resultMap2.put("memPer", Math.round(mem));
                    resultMap2.put("diskPer", Math.round(disk));
                } else {
                    resultMap2.put("guid", sapceApplicationSummary.getId());
                    resultMap2.put("cpuPer", 0);
                    resultMap2.put("memPer", 0);
                    resultMap2.put("diskPer", 0);
                }

                appsArray.add(sapceApplicationSummary);
                appArray.add(resultMap2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resultMap.put("apps", appsArray);
        resultMap.put("appsPer", appArray);

        LOGGER.info("Get SpaceSummary End ");

        return resultMap;
    }

    /**
     * 공간명을 변경한다.
     *
     * @param space the space
     * @param token the token
     * @return UpdateSpaceResponse
     * @throws Exception the exception
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @PutMapping(Constants.V3_URL + "/spaces")
    public Map renameSpace(@RequestBody Space space, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("renameSpace Start ");
        Map resultMap = spaceServiceV3.renameSpace(space, token);

        LOGGER.info("renameSpace End ");
        return resultMap;
    }


    /**
     * 공간을 삭제한다.
     *
     * @param guid  the space
     * @param token the request
     * @return ModelAndView model
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @DeleteMapping(Constants.V3_URL +  "/spaces/{guid}")
    public Map deleteSpace(@PathVariable String guid, @RequestParam boolean recursive, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("deleteSpace Start ");

        Map resultMap = spaceServiceV3.deleteSpace(guid, recursive, token);

        LOGGER.info("deleteSpace End ");
        return resultMap;
    }

    @RequestMapping(value = {Constants.V3_URL + "/spaces/{guid}/services"}, method = RequestMethod.GET)
    public ListSpaceServicesResponse getSpaceServices(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getSpaceServices Start : " + guid);

        ListSpaceServicesResponse respSpaceServices = spaceServiceV3.getSpaceServices(guid);

        LOGGER.info("getSpaceServices End ");

        return respSpaceServices;
    }

    /**
     * 공간에 속한 유저들의 역할(Role)을 전부 조회한다. 단, 조직에 속해있지만 공간에 속하지 않은 유저는 빈 배열로 채운다.
     *
     * @param spaceId
     * @param token
     * @return Users with roles that belong in the organization
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @GetMapping(Constants.V3_URL + "/spaces/{spaceId}/user-roles")
    public ListSpaceUserRolesResponse getSpaceUserRoles(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return spaceServiceV3.getSpaceUserRoles(spaceId, token);

    }

    @PutMapping(Constants.V3_URL + "/spaces/{spaceId}/user-roles")
    public boolean associateSpaceUserRoles(@PathVariable String spaceId, @RequestBody List<UserRole> Roles, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return spaceServiceV3.associateSpaceUserRoles(spaceId, Roles, token);
    }

    @DeleteMapping(Constants.V3_URL + "/spaces/{spaceId}/user-roles")
    public void removeSpaceUserRoles(@PathVariable String spaceId, @RequestParam String userId, @RequestParam String role, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        spaceServiceV3.removeSpaceUserRole(spaceId, userId, role);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////                            * CLOUD FOUNDRY CLIENT API VERSION 3                                      //////
    //////   Document : https://v3-apidocs.cloudfoundry.org/version/3.69.0/index.html#spaces                    //////
    //////                                     Not-implemented                                                  //////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Space 정보를 가져온다.
     *
     * @param spaceId    the space guid
     * @param  token    user token
     * @return GetSpaceResponse
     * 권한 : 사용자권한
     */
    @GetMapping(Constants.V3_URL+"/spaces/{spaceId:.+}")
    public org.cloudfoundry.client.v3.spaces.GetSpaceResponse getSpace(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return spaceServiceV3.getSpaceV3(spaceId, token);
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
    public org.cloudfoundry.client.v3.spaces.ListSpacesResponse listSpace(@PathVariable String organizationId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
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
    public org.cloudfoundry.client.v3.spaces.ListSpacesResponse listSpaceAdmin(@PathVariable String organizationId){
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
     * @param  space    the space
     * @param token    user token
     * @return CreateOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL+"/spaces")
    public org.cloudfoundry.client.v3.spaces.CreateSpaceResponse createSpace(@RequestBody Space space, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return spaceServiceV3.createSpace(space.getSpaceName(), space.getOrgGuid(), token);
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
