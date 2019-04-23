package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.spaces.*;
import org.cloudfoundry.client.v3.spaces.AssignSpaceIsolationSegmentResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Space;
import org.openpaas.paasta.portal.api.model.UserRole;
import org.openpaas.paasta.portal.api.service.AppServiceV2;
import org.openpaas.paasta.portal.api.service.OrgService;
import org.openpaas.paasta.portal.api.service.SpaceService;
import org.openpaas.paasta.portal.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
public class SpaceController extends Common {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////


    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceController.class);
    private final String V2_URL = "/v2";
    /**
     * The Space service.
     */
    @Autowired
    SpaceService spaceService;
    /**
     * The Org service.
     */
    @Autowired
    OrgService orgService;

    @Autowired
    UserService userService;

    @Autowired
    AppServiceV2 appServiceV2;

    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param spaceid the spaceId
     * @param token   the token
     * @return Space respSpace
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL + "/spaces/{spaceid}/summary")
    public GetSpaceSummaryResponse getSpaceSummary(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        token = adminToken(token);
        GetSpaceSummaryResponse respSapceSummary = spaceService.getSpaceSummary(spaceid, cloudFoundryClient(connectionContext(), tokenProvider(token)));
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
    @GetMapping(Constants.V2_URL + "/spaces/{spaceid}/summary-admin")
    public GetSpaceSummaryResponse getSpaceSummaryAdmin(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        GetSpaceSummaryResponse respSapceSummary = spaceService.getSpaceSummary(spaceid, cloudFoundryClient());
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
    @GetMapping(Constants.V2_URL + "/spaces/{spaceid}/summarylist")
    public Map getSpaceSummary2(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("Get SpaceSummary Start : " + spaceid);
        token = orgService.adminToken(token);
        GetSpaceSummaryResponse respSapceSummary = spaceService.getSpaceSummary(spaceid, cloudFoundryClient(tokenProvider(token)));

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
                    ApplicationStatisticsResponse applicationStatisticsResponse = appServiceV2.getAppStats(sapceApplicationSummary.getId(), token);

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

    @GetMapping(V2_URL + "/spaces/{spaceid}")
    public GetSpaceResponse getSpace(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String authHeader) {
        authHeader = orgService.adminToken(authHeader);
        return spaceService.getSpace(spaceId, authHeader);
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
    @PutMapping(V2_URL + "/spaces")
    public Map renameSpace(@RequestBody Space space, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("renameSpace Start ");
        token = orgService.adminToken(token);
        Map resultMap = spaceService.renameSpace(space, token);

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
    @DeleteMapping(V2_URL + "/spaces/{guid}")
    public Map deleteSpace(@PathVariable String guid, @RequestParam boolean recursive, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("deleteSpace Start ");
        token = orgService.adminToken(token);
        Map resultMap = spaceService.deleteSpace(guid, recursive, token);

        LOGGER.info("deleteSpace End ");
        return resultMap;
    }

    /**
     * 공간을 생성한다.
     *
     * @param space the space
     * @param token a cloud foundry access token
     * @return boolean boolean
     * @throws Exception the exception
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @PostMapping(V2_URL + "/spaces")
    public Map createSpace(@RequestBody Space space, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("createSpace Start ");
        token = orgService.adminToken(token);
        Map resultMap = spaceService.createSpace(space, token);

        LOGGER.info("createSpace End ");
        return resultMap;
    }

    @RequestMapping(value = {Constants.V2_URL + "/spaces/{guid}/services"}, method = RequestMethod.GET)
    public ListSpaceServicesResponse getSpaceServices(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getSpaceServices Start : " + guid);

        ListSpaceServicesResponse respSpaceServices = spaceService.getSpaceServices(guid);

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
    @GetMapping(V2_URL + "/spaces/{spaceId}/user-roles")
    public ListSpaceUserRolesResponse getSpaceUserRoles(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        token = orgService.adminToken(token);
        return spaceService.getSpaceUserRoles(spaceId, token);

    }

    @PutMapping(V2_URL + "/spaces/{spaceId}/user-roles")
    public boolean associateSpaceUserRoles(@PathVariable String spaceId, @RequestBody List<UserRole> Roles, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        token = orgService.adminToken(token);
        return spaceService.associateSpaceUserRoles(spaceId, Roles, token);
    }

    @DeleteMapping(V2_URL + "/spaces/{spaceId}/user-roles")
    public void removeSpaceUserRoles(@PathVariable String spaceId, @RequestParam String userId, @RequestParam String role, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        token = orgService.adminToken(token);
        spaceService.removeSpaceUserRole(spaceId, userId, role);
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
        return spaceService.setSpaceDefaultIsolationSegments(spaceId, isolationSegmentId);
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
        return spaceService.resetSpaceDefaultIsolationSegments(spaceId);
    }
}
