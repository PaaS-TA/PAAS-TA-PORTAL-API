package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.v2.spaces.CreateSpaceResponse;
import org.cloudfoundry.client.v2.spaces.GetSpaceResponse;
import org.cloudfoundry.client.v2.spaces.UpdateSpaceResponse;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.openpaas.paasta.portal.api.service.OrgService;
import org.openpaas.paasta.portal.api.service.SpaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param spaceid  the spaceId
     * @param request the request
     * @return Space respSpace
     * @throws Exception the exception
     * @version 2.0
     * @author hgcho
     * @since 2018.5.3
     */
    @GetMapping(V2_URL + "/spaces/{spaceid}/summary")
    public Map<String, Object> getSpaceSummary(@PathVariable String spaceId, HttpServletRequest request) throws Exception {
        LOGGER.info("Get SpaceSummary Start : " + spaceId);
        return spaceService.getSpaceSummary(spaceId, request.getHeader(AUTHORIZATION_HEADER_KEY));
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
     */
    @PostMapping(V2_URL + "/spaces")
    public UpdateSpaceResponse renameSpace(@RequestBody Space space, HttpServletRequest request) throws Exception {
        LOGGER.info("Rename Space : {}", space.getNewSpaceName());
        return spaceService.renameSpace(space, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }


    /**
     * 공간을 삭제한다.
     *
     * @param space   the space
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/space/deleteSpace"}, method = RequestMethod.POST)
    // TODO
    public boolean deleteSpace(@RequestBody Space space, HttpServletRequest request) throws Exception {
        LOGGER.info("Delete Space Start ");

        spaceService.deleteSpace(space, request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("Delete Space End ");
        return true;
    }

     /**
     * 공간을 생성한다.
     *
     * @param space   the space
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.5.20 최초작성
     */
    @RequestMapping(value = {"/space/createSpace"}, method = RequestMethod.POST)
    // TODO
    public CreateSpaceResponse createSpace(@RequestBody Space space, @RequestHeader( AUTHORIZATION_HEADER_KEY ) String authHeader) throws Exception {
        LOGGER.info("Create Space : {}", space.getName(), space.getOrgGuid());
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

        spaceService.setSpaceRole(body.get("orgName"),body.get("spaceName"), body.get("userName"), body.get("userRole"), token);

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

        spaceService.unsetSpaceRole(body.get("orgName"),body.get("spaceName"), body.get("userGuid"), body.get("userRole"), token);

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
        List<Map<String, Object>> spaceUserList = spaceService.getUsersForSpaceRole(body.get("orgName").toString(), body.get("spaceName").toString(), userList, token);
        spaceUserList.addAll(inviteOrgUserList);
        LOGGER.info("getUsersForSpaceRole End");

        return spaceUserList;
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


    /**
     * 공간 쿼터를 조회한다.
     *
     * @param spacequotaid  the spaceQuotaId
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @GetMapping(V2_URL + "/spaces/{spacequotaid}/quota")
    public Map<String, Object> getSpaceQuota(@PathVariable String spacequotaid, HttpServletRequest request) throws Exception {
        LOGGER.info("getSpaceQuota Start" + spacequotaid);
        return spaceService.getSpaceQuota(spacequotaid, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

}
