package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.spaces.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Space;
import org.openpaas.paasta.portal.api.model.UserRole;
import org.openpaas.paasta.portal.api.service.AppService;
import org.openpaas.paasta.portal.api.service.OrgService;
import org.openpaas.paasta.portal.api.service.SpaceService;
import org.openpaas.paasta.portal.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 공간 컨트롤러 - 공간 목록 , 공간 이름 변경 , 공간 생성 및 삭제 등을 제공한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
@Transactional
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceController extends Common {

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
    AppService appService;

    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param spaceid  the spaceId
     * @param request the request
     * @return Space respSpace
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL+"/spaces/{spaceid}/summary"}, method = RequestMethod.GET)
    public GetSpaceSummaryResponse getSpaceSummary(@PathVariable String spaceid, HttpServletRequest request) throws Exception {
        LOGGER.info("Get SpaceSummary Start : " + spaceid);

        GetSpaceSummaryResponse respSapceSummary = spaceService.getSpaceSummary(spaceid, this.getToken());
//        return spaceService.getSpaceSummary(spaceid,request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("Get SpaceSummary End ");

        return respSapceSummary;
    }

    /**
     * 공간 요약 정보 리스트를 조회한다.[dashboard]
     *
     * @param spaceid  the spaceId
     * @param request the request
     * @return Space respSpace
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V2_URL+"/spaces/{spaceid}/summarylist"}, method = RequestMethod.GET)
    public Map getSpaceSummary2(@PathVariable String spaceid, HttpServletRequest request) throws Exception {
        LOGGER.info("Get SpaceSummary Start : " + spaceid);

        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> appArray = new ArrayList<>();
//        appArray = respSapceSummary.getApplications();

            GetSpaceSummaryResponse respSapceSummary = spaceService.getSpaceSummary(spaceid, this.getToken());
            resultMap.put("apps", respSapceSummary.getApplications());
            resultMap.put("guid", respSapceSummary.getId());
            resultMap.put("name", respSapceSummary.getName());
            resultMap.put("services", respSapceSummary.getServices());

            for (SpaceApplicationSummary sapceApplicationSummary : respSapceSummary.getApplications()) {
                Map<String, Object> resultMap2 = new HashMap<>();
                try{
                    if (sapceApplicationSummary.getState().equals("STARTED")) {
                        ApplicationStatisticsResponse applicationStatisticsResponse = appService.getAppStats(sapceApplicationSummary.getId(), this.getToken());

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

                    appArray.add(resultMap2);
                }
                catch (Exception e){

                }
            }
        resultMap.put("appsPer", appArray);

        LOGGER.info("Get SpaceSummary End ");

        return resultMap;
    }

    @GetMapping(V2_URL + "/spaces/{spaceid}")
    public GetSpaceResponse getSpace(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String authHeader) {
        return spaceService.getSpace( spaceId, authHeader );
    }

    /**
     * 공간명을 변경한다.
     *
     * @param space   the space
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     * @return UpdateSpaceResponse
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @PutMapping(V2_URL + "/spaces")
    public Map renameSpace(@RequestBody Space space, HttpServletRequest request) throws Exception {
        LOGGER.info("renameSpace Start ");

        Map resultMap = spaceService.renameSpace(space, request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("renameSpace End ");
        return resultMap;
    }


    /**
     * 공간을 삭제한다.
     *
     * @param guid   the space
     * @param request the request
     * @return ModelAndView model
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @DeleteMapping(V2_URL + "/spaces/{guid}")
    public Map deleteSpace(@PathVariable String guid, @RequestParam boolean recursive, HttpServletRequest request) throws Exception {
        LOGGER.info("deleteSpace Start ");

        Map resultMap = spaceService.deleteSpace(guid, recursive, request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("deleteSpace End ");
        return resultMap;
    }

     /**
     * 공간을 생성한다.
     *
     * @param space   the space
     * @param authHeader a cloud foundry access token
     * @return boolean boolean
     * @throws Exception the exception
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @PostMapping(V2_URL + "/spaces")
    public Map createSpace(@RequestBody Space space, @RequestHeader( AUTHORIZATION_HEADER_KEY ) String authHeader) throws Exception {
        LOGGER.info("createSpace Start ");

        Map resultMap = spaceService.createSpace(space, authHeader);

        LOGGER.info("createSpace End ");
        return resultMap;
    }

    @RequestMapping(value = {Constants.V2_URL+"/spaces/{guid}/services"}, method = RequestMethod.GET)
    public ListSpaceServicesResponse getSpaceServices(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getSpaceServices Start : " + guid);

        ListSpaceServicesResponse respSpaceServices = spaceService.getSpaceServices(guid, this.getToken());

        LOGGER.info("getSpaceServices End ");

        return respSpaceServices;
    }

    /**
     * 공간에 속한 유저들의 역할(Role)을 전부 조회한다. 단, 조직에 속해있지만 공간에 속하지 않은 유저는 빈 배열로 채운다.
     * @param spaceId
     * @param token
     * @return Users with roles that belong in the organization
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @GetMapping(V2_URL + "/spaces/{spaceId}/user-roles")
    public Map<String, Collection<UserRole>> getSpaceUserRoles ( @PathVariable String spaceId, @RequestHeader
        (AUTHORIZATION_HEADER_KEY ) String token ) {
        Objects.requireNonNull( spaceId, "Space Id" );
        // token can fill "NULL" value.
        //Objects.requireNonNull( token, "token" );

        if (spaceService.isExistSpace( spaceId )) {
            return spaceService.getSpaceUserRoles( spaceId, token );
        } else {
            return Collections.<String, Collection<UserRole>>emptyMap();
        }
    }

    @PutMapping(V2_URL + "/spaces/{spaceId}/user-roles")
    public AbstractSpaceResource associateSpaceUserRoles( @PathVariable String spaceId,
                                                          @RequestBody UserRole.RequestBody body,
                                                          @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        Objects.requireNonNull( body.getUserId(), "User ID(userId) is required" );
        Objects.requireNonNull( body.getRole(), "Org Role(role) is required" );
        LOGGER.info( "Associate organization role of user (Update) : {} / {}", body.getUserId(), body.getRole() );
        return spaceService.associateSpaceUserRole( spaceId, body.getUserId(), body.getRole() );
    }

    @DeleteMapping(V2_URL + "/spaces/{spaceId}/user-roles")
    public void removeSpaceUserRoles( @PathVariable String spaceId,
                                                       @RequestParam String userId, @RequestParam String role,
                                                       @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        Objects.requireNonNull( userId, "User ID(userId) is required" );
        Objects.requireNonNull( role, "Org Role(role) is required" );
        LOGGER.info("Remove organization role of user (Delete) : {} / {}", userId, role);
        spaceService.removeSpaceUserRole( spaceId, userId, role );
    }
}
