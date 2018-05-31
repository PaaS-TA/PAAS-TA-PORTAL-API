package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.spaces.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Space;
import org.openpaas.paasta.portal.api.service.AppService;
import org.openpaas.paasta.portal.api.service.OrgService;
import org.openpaas.paasta.portal.api.service.SpaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        GetSpaceSummaryResponse respSapceSummary = spaceService.getSpaceSummary(spaceid, this.getToken());

        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> appArray = new ArrayList<>();
//        appArray = respSapceSummary.getApplications();

        resultMap.put("apps", respSapceSummary.getApplications());
        resultMap.put("guid", respSapceSummary.getId());
        resultMap.put("name", respSapceSummary.getName());
        resultMap.put("services", respSapceSummary.getServices());

        for(SpaceApplicationSummary sapceApplicationSummary : respSapceSummary.getApplications()) {
            Map<String, Object> resultMap2 = new HashMap<>();
            if(sapceApplicationSummary.getState().equals("STARTED")) {
                ApplicationStatisticsResponse applicationStatisticsResponse = appService.getAppStats(sapceApplicationSummary.getId(), this.getToken());

                Double cpu = 0.0;
                Double mem = 0.0;
                Double disk = 0.0;
                int cnt = 0;
                for(int i = 0; i < applicationStatisticsResponse.getInstances().size(); i++) {
                    if(applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getState().equals("RUNNING")) {
                        Double instanceCpu = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getCpu();
                        Long instanceMem = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getMemory();
                        Long instanceMemQuota = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getMemoryQuota();
                        Long instanceDisk = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getDisk();
                        Long instanceDiskQuota = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getDiskQuota();

                        if(instanceCpu != null) cpu = cpu + instanceCpu * 100;
                        if(instanceMem != null) mem = mem + (double)instanceMem / (double)instanceMemQuota * 100;
                        if(instanceDisk != null) disk = disk + (double)instanceDisk / (double)instanceDiskQuota * 100;

                        cnt++;
                    }
                }

                cpu = cpu / cnt;
                mem = mem / cnt;
                disk = disk / cnt;

                System.out.println("cpu : "+Double.parseDouble(String.format("%.2f%n", cpu)));
                System.out.println("mem : "+Math.round(mem));
                System.out.println("disk : "+Math.round(disk));
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
    public UpdateSpaceResponse renameSpace(@RequestBody Space space, HttpServletRequest request) throws Exception {
        LOGGER.info("Rename Space : {}", space.getNewSpaceName());
        return spaceService.renameSpace(space, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }


    /**
     * 공간을 삭제한다.
     *
     * @param guid   the space
     * @param recursive is to delete recursive?
     * @param authHeader a cloud foundry access token
     * @return ModelAndView model
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @DeleteMapping(V2_URL + "/spaces")
    public DeleteSpaceResponse deleteSpace(@RequestParam String guid, @RequestParam boolean recursive, @RequestHeader(
        AUTHORIZATION_HEADER_KEY )
        String authHeader) {
        // Delete method cannot bind body. So space's information receives parameters.
        final Space space = new Space();
        space.setSpaceGuid(guid);
        space.setRecursive(recursive);
        LOGGER.info("Delete Space : {} / recursive deleting : {}", space.getGuid(), space.isRecursive());
        return spaceService.deleteSpace(space, authHeader);
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
    public CreateSpaceResponse createSpace(@RequestBody Space space, @RequestHeader( AUTHORIZATION_HEADER_KEY ) String authHeader) throws Exception {
        LOGGER.info("Create Space : {}, {}", space.getSpaceName(), space.getOrgGuid());
        return spaceService.createSpace(space, authHeader);
    }


    /**
     * 조직 role을 부여한다.
     *
     * @param token the token
     * @param body  the body
     * @return Map org role
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.8.10 최초작성
     */
    @RequestMapping(value = {"/space/setSpaceRole"}, method = RequestMethod.POST)
    // TODO
    public boolean setOrgRole(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("setSpaceRole Start");

//        spaceService.setSpaceRole(body.get("orgName"),body.get("spaceName"), body.get("userName"), body.get("userRole"), token);

        LOGGER.info("setSpaceRole End");

        return true;
    }

    /**
     * 조직 role을 제거한다.
     *
     * @param token the token
     * @param body  the body
     * @return Map boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.8.10 최초작성
     */
    @RequestMapping(value = {"/space/unsetSpaceRole"}, method = RequestMethod.POST)
    // TODO
    public boolean unsetOrgRole(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("unsetSpaceRole Start");

//        spaceService.unsetSpaceRole(body.get("orgName"),body.get("spaceName"), body.get("userGuid"), body.get("userRole"), token);

        LOGGER.info("unsetSpaceRole End");

        return true;
    }

    /**
     * 조직 role을 제거한다.
     *
     * @param token the token
     * @param body  the body
     * @return Map boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.9.1 최초작성
     */
    @RequestMapping(value = {"/space/getUsersForSpaceRole"}, method = RequestMethod.POST)
    // TODO
    public List<Map<String, Object>> getUsersForSpaceRole(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, Object> body) throws Exception {

        LOGGER.info("getUsersForSpaceRole Start");
        String gubun ="1";
        String userId = (String) body.getOrDefault("userId","");
        List userList = (List<Map<String, Object>>)body.get("userList");
        for (int i=0;i<userList.size();i++){
            Map map = (Map) userList.get(i);
            String sInviteYn = (String)map.getOrDefault("inviteYn","N");
            if("Y".equals(sInviteYn)){
                userList.remove(i);
            }
        }
        List<Map<String, Object>> inviteOrgUserList = orgService.getUsersByInvite(body.get("spaceName").toString(), userId, gubun);
//        List<Map<String, Object>> spaceUserList = spaceService.getUsersForSpaceRole(body.get("orgName").toString(), body.get("spaceName").toString(), userList, token);
//        spaceUserList.addAll(inviteOrgUserList);
        LOGGER.info("getUsersForSpaceRole End");

        return null;
    }

    /**
     * 관리자 권한의 공간을 조회한다.
     *
     * @param body the body
     * @return Map boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.9.1 최초작성
     */
    @RequestMapping(value = {"/space/getSpacesForAdmin"}, method = RequestMethod.POST)
    // TODO
    public Map<String, Object> getSpacesForAdmin(@RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("getSpacesForAdmin ::");

        //List<Object> spaceList = spaceService.getSpacesForAdmin(body.get("orgName"));

        //return new HashMap<String, Object>(){{put("spaceList", spaceList );}};
        return null;
    }

    @RequestMapping(value = {Constants.V2_URL+"/spaces/{guid}/services"}, method = RequestMethod.GET)
    public ListSpaceServicesResponse getSpaceServices(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getSpaceServices Start : " + guid);

        ListSpaceServicesResponse respSpaceServices = spaceService.getSpaceServices(guid, this.getToken());

        LOGGER.info("getSpaceServices End ");

        return respSpaceServices;
    }

}
