package org.openpaas.paasta.portal.api.controller;

import org.apache.commons.collections.map.HashedMap;
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
    //////   * CLOUD FOUNDRY CLIENT API VERSION 1                   //////
    //////   Document : (None)                                      //////
    //////////////////////////////////////////////////////////////////////

    /**
     * 조직명을 변경한다.
     *
     * @param org     the org
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/org/renameOrg"}, method = RequestMethod.POST)
    public boolean renameOrg(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("Rename Org Start : " + org.getOrgName() + " : " + org.getNewOrgName());

//        orgService.renameOrgV1(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("Rename Org End ");


        return true;
    }

    /**
     * 조직을 삭제한다.
     *
     * @param org     the org
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/org/deleteOrg"}, method = RequestMethod.POST)
    public boolean deleteOrgV1(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("delete Org Start : " + org.getOrgName());

//        orgService.deleteOrgV1(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("delete Org End ");


        return true;
    }

    /**
     * 조직 목록을 조회한다.
     *
     * @param request the request
     * @return List<CloudOrganization>    orgList
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.5.13 최초작성
     */
    @Deprecated
    @RequestMapping(value = {V1_URL + "/org/getOrgs"}, method = RequestMethod.POST)
    public List<OrganizationResource> getOrgs(HttpServletRequest request) {

        LOGGER.info("Get Orgs Start");

        List<OrganizationResource> orgList  = orgService.getOrgs(request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("Get Orgs End");


        return orgList;
    }

    /**
     * 조직을 생성한다.
     *
     * @param org     the org
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.5.16 최초작성
     */
    @RequestMapping(value = {V1_URL + "/org/createOrg"}, method = RequestMethod.POST)
    public boolean createOrg(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("Create Org Start");

//        orgService.createOrg(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("Create Org End. Created org: " + org.getNewOrgName());

        return true;
    }

    /**
     * 조직을 탈퇴한다.
     *
     * @param org     the org
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.1 최초작성
     */
    @RequestMapping(value = {V1_URL + "/org/removeOrgFromUser"}, method = RequestMethod.POST)
    public boolean removeUserFromOrg(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("removeUserFromOrg Start");

//        orgService.removeUserFromOrg(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("removeUserFromOrg End");

        return true;
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
    @RequestMapping(value = {V1_URL + "/org/setOrgRole"}, method = RequestMethod.POST)
    public boolean setOrgRole(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("setOrgRole Start");

//        orgService.setOrgRole(body.get("orgName"), body.get("userName"), body.get("userRole"), token);

        LOGGER.info("setOrgRole End");

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
    @RequestMapping(value = {V1_URL + "/org/unsetOrgRole"}, method = RequestMethod.POST)
    public boolean unsetOrgRole(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("unsetOrgRole Start");

//        orgService.unsetOrgRole(body.get("orgName"), body.get("userGuid"), body.get("userRole"), token);

        LOGGER.info("unsetOrgRole End");

        return true;
    }

    /**
     * 전체 사용자를 조회한다.
     *
     * @param token the token
     * @param body  the body
     * @return the all users
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/org/getAllUsers"}, method = RequestMethod.POST)
    public List<Map<String, Object>> getAllUsers(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, String> body) throws Exception {

        List<Map<String, Object>> orgUserList= new ArrayList<>();
        try{

            LOGGER.info("getAllUsers Start");
            String orgName =body.getOrDefault("orgName","");
            String userId =body.getOrDefault("userId","");
            if("".equals(orgName) || orgName ==null){
                throw  new Exception(messageSource.getMessage("Organization_not_found", null, Locale.KOREA));
            }
//            orgUserList = orgService.getAllUsers(body.get("orgName"), token);
            String gubun ="0";
            List<Map<String, Object>> inviteOrgUserList = orgService.getUsersByInvite(body.get("orgName"), userId, gubun);
            orgUserList.addAll(inviteOrgUserList);
            LOGGER.info("getAllUsers End");
        }catch (Exception e){
            e.printStackTrace();
        }
        return orgUserList;
    }

    /**
     * 조직 사용자 권한별 리스트를 조회한다.
     *
     * @param token the token
     * @param body  the body
     * @return users for org role
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/org/getUsersForOrgRole"}, method = RequestMethod.POST)
    public List<Map<String, Object>> getUsersForOrgRole(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, Object> body) throws Exception {

        LOGGER.info("getUsersForOrgRole Start");
        String userId = (String) body.getOrDefault("userId","");
        String gubun ="0";
        List<Map<String, Object>> inviteOrgUserList = orgService.getUsersByInvite(body.get("orgName").toString(), userId, gubun);
        List userList = (List<Map<String, Object>>)body.get("userList");
        for (int i=0;i<userList.size();i++){
            Map map = (Map) userList.get(i);
            String sInviteYn = (String)map.getOrDefault("inviteYn","N");
            if("Y".equals(sInviteYn)){
                userList.remove(i);
            }
        }
//        List<Map<String, Object>> orgUserList = orgService.getUsersForOrgRole(body.get("orgName").toString(), (List<Map<String, Object>>)body.get("userList"), token);
//        List<Map<String, Object>> orgUserList = orgService.getUsersForOrgRole(body.get("orgName").toString(), userList, token);
//        orgUserList.addAll(inviteOrgUserList);
        LOGGER.info("getUsersForOrgRole End");

        return null;
    }

    /**
     * 사용자의 조직권한을 삭제한다.
     *
     * @param token the token
     * @param body  the body
     * @return the map
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/org/deleteUserOrg"}, method = RequestMethod.POST)
    public Map<String, Object> deleteUserOrg(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,@RequestBody Map<String, Object> body) throws Exception {

        Map map = new HashMap();
//        map.put("bSend", orgService.unsetUserOrg(body, token));
        return map;
    }


    // related to invition users and sending email

    /**
     * 조직에 사용자를  이메일 인증을 통해 초대한다.
     * body.put("userId", userId);
     * body.put("inviteUserId", inviteId);
     * body.put("org", orgTestOrg);
     * body.put("space", testSpace);
     *
     * @param body the body
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/invite/inviteEmailSend"}, method = RequestMethod.POST)
    public Map<String, Object> inviteEmailSend(@RequestBody Map<String, Object> body) throws Exception {
        /*
        LOGGER.info("inviteUser ::"+body.toString());
        String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase() + RandomStringUtils.randomAlphanumeric(2).toUpperCase();
        List dataList = (List) body.getOrDefault("dataList",new ArrayList());

        String inviteUserId = (String) body.getOrDefault("inviteUserId", "inviteUserId");
        String userId = (String) body.getOrDefault("userId", "userId");
        body.put("token",token);
        String orgId = null;


        // 초대 관련 데이터 입력
        List inviteData = new ArrayList();


        //orgId때문 org 정보를 가져온다.
        for (int i=0; i<dataList.size(); i++){
            List inviteList = (List) dataList.get(i);
            if("org".equals((inviteList.get(0)))){
                Map tmpData = new HashMap();
                String gubun = "0";
                String inviteName = (String) inviteList.get(1);
                String inviteId = orgService.getOrgId(inviteName, null);
                orgId = inviteId;
                tmpData.put("token",token);
                tmpData.put("gubun",gubun);
                tmpData.put("inviteId",inviteId);
                tmpData.put("inviteName",inviteName);
                tmpData.put("roleName", inviteList.get(2));
                tmpData.put("inviteUserId",inviteUserId);
                tmpData.put("userId",userId);
                tmpData.put("createTime",new Date());
                inviteData.add(tmpData);
            }
        }

        for (int i=0; i<dataList.size(); i++){
            List inviteList = (List) dataList.get(i);
            if("space".equals((inviteList.get(0)))){
                Map tmpData = new HashMap();
                String inviteId = null;
                String gubun = "1";
                String inviteName = (String) inviteList.get(1);
                inviteId = spaceService.getSpacesInfo(inviteName, orgId).get(0).getSpaceGuid();
                tmpData.put("token",token);
                tmpData.put("gubun",gubun);
                tmpData.put("inviteId",inviteId);
                tmpData.put("inviteName",inviteName);
                tmpData.put("roleName", inviteList.get(2));
                tmpData.put("inviteUserId",inviteUserId);
                tmpData.put("userId",userId);
                tmpData.put("createTime",new Date());
                inviteData.add(tmpData);
            }
        }
        int iRtn = 0;
        for (int i =0; i <  inviteData.size(); i++){
            HashMap inviteMap =(HashMap) inviteData.get(i);
            InviteOrgSpace inviteOrgSpace = new InviteOrgSpace();
            inviteOrgSpace.setGubun((String) inviteMap.getOrDefault("gubun",""));
            inviteOrgSpace.setInviteId((int) inviteMap.getOrDefault("inviteId",-1));
            inviteOrgSpace.setInviteName((String) inviteMap.getOrDefault("inviteName",""));
            inviteOrgSpace.setRoleName((String) inviteMap.getOrDefault("roleName",""));
            inviteOrgSpace.setUserId((String) inviteMap.getOrDefault("userId",""));
            List<InviteOrgSpace> selectOrgSpaceUserList = orgService.selectOrgSpaceUser(inviteOrgSpace);
            int rtnCnt = 0;
//            if (selectOrgSpaceUserList.size() > 0){
//                int id = selectOrgSpaceUserList.get(0).getId();
//                inviteMap.put("id", id);
//                rtnCnt = orgService.updateOrgSpaceUser(inviteMap);
//            }else {
                rtnCnt = orgService.insertOrgSpaceUser(inviteMap);
//            }
            iRtn = rtnCnt >0 ? iRtn+1 : iRtn;
        }
        Map map = new HashMap();
        map.put("rtnCnt", iRtn);
//        orgService.inviteMemberEmail(body);
        return map;
        */

        // TODO invite users into org
        return new HashMap<String, Object>();
    }

    /**
     * 조직에 사용자를  이메일 인증을 통해 초대한다.
     * body.put("space", testSpace);
     *
     * @param request the request
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/invitations/accept"})
    public Map<String, Object> inviteAccept(@RequestBody HashMap request) throws Exception {

        /*
        String code = (null == request.get("code")) ? "" : request.get("code").toString();
        LOGGER.info("code : "+ code+" : request : ");
        Map<String, Object> result = new HashedMap();

        List<Map> list = orgService.selectInviteInfo(code);
        BigDecimal bigDecimal= (BigDecimal) list.get(0).getOrDefault("accessCnt",0);
        int        accessCnt = bigDecimal.intValue();
        String inviteuserid = (String) list.get(0).getOrDefault("inviteUserId",0);
        result.put("inviteuserid",inviteuserid);
        int cnt = orgService.updateInviteY(code);
        orgService.updateAccessCnt(code, accessCnt+1);
        if(cnt>0) {
//            orgService.setOrgSpaceUserList(list);
            result.put("success",messageSource.getMessage("invite.info.success", null, Locale.KOREA));
        }else{
            result.put("error",messageSource.getMessage("invite.info.noCnt", null, Locale.KOREA));
        }
        return  result;
        */
        // TODO
        return new HashMap<String, Object>();
    }

    /**
     * 조직에 초대된 사용자의 가입여부를 조회한다.
     *
     * @param request the request
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/invitations/userInfo"})
    public Map<String, Object> inviteUserInfo(@RequestBody HashMap request) throws Exception {
        /*
        String code = (null == request.get("code")) ? "" : request.get("code").toString();
        LOGGER.info("code : "+ code+" : request : " +request.toString());
        Map<String, Object> result = new HashedMap();

        List<Map> list = orgService.selectInviteInfo(code);

        if(list.size() > 0) {
            String inviteId = (String) list.get(0).getOrDefault("inviteUserId", "");
//            UserDetail userDetail = userService.getUser(inviteId);
//            result.put("userId", inviteId);
//            result.put("userDetail", userDetail);
        }
        result.put("listSize", list.size());

        return  result;
        */
        // TODO
        return new HashMap<String, Object>();
    }


    /**
     * 조직에 초대된 사용자의 미가입시 가입시킨다.
     *
     * @param request the request
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/invitations/authUser"})
    public Map<String, Object> inviteUserAdd(@RequestBody HashMap request) throws Exception {

        String code = (null == request.get("code")) ? "" : request.get("code").toString();
        LOGGER.info("code : "+ code+" : request : ");
        Map<String, Object> result = new HashedMap();

        List<Map> list = orgService.selectInviteInfo(code);
        Map userDetail = new HashMap();
        String inviteId = (String) list.get(0).getOrDefault("inviteuserid","");
        userDetail.put("userId", inviteId);
//        userService.createUserAdd(userDetail);
//        result.put("inserUserIdCnt" ,userService.createUserAdd(userDetail));
        result.put("userId" ,inviteId);
        result.put("userDetail" ,userDetail);
        return  result;
    }

    /**
     * 조직에 사용자를  이메일을 재전송한다.
     *
     * @param body the body
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/invite/inviteEmailReSend"}, method = RequestMethod.POST)
    public Map<String, Object> inviteEmailReSend(@RequestBody Map<String, Object> body) throws Exception {
        LOGGER.info("inviteUser ::"+body.toString());
        Map map = new HashMap();
//        map.put("bSend", orgService.inviteMemberEmailResend(body));
        return map;
    }

    /**
     * 조직에 사용자를 이메일을 재전송한다.
     *
     * @param body the body
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/invite/cancelInvite"}, method = RequestMethod.POST)
    public Map<String, Object> cancelInvite(@RequestBody Map<String, Object> body) throws Exception {
        LOGGER.info("inviteUser ::"+body.toString());
        Map map = new HashMap();
        map.put("cancelInvite", orgService.cancelInvite(body));
        return map;
    }

    /**
     * 조직에 사용자를  이메일 인증을 통해 초대하기전 초대정보가 존재하는지 확인한다.
     * body.put("inviteUserId", inviteId);
     *
     * @param token the token
     * @param body  the body
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {V1_URL + "/invite/inviteEmailSendCnt"}, method = RequestMethod.POST)
    public Map<String, Object> inviteEmailSendCnt(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,@RequestBody Map<String, Object> body) throws Exception {

        Map map = new HashMap();
//        map.put("bSend", orgService.getOrgUser(body, token));
        return map;
    }

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private String getCFAuthorization(final HttpServletRequest request) {
    	return request.getHeader(AUTHORIZATION_HEADER_KEY);
    }

    /**
     * JSON to Object mapping method
     * @param obj a object
     * @param clazz mapping class(type)
     * @return Mapping object
     */
    private <T> T convertValue(Object obj, Class<T> clazz) {
    	return new ObjectMapper().convertValue(obj, clazz);
    }

    /**
     * 조직 정보를 조회한다.
     *
     * @param orgId
     * @param token
     * @return information of the organization
     */
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
    public SummaryOrganizationResponse getOrgSummary(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("org summary : " + orgId);
		if (orgId == null) {
			throw new IllegalArgumentException("조직정보를 가져오지 못하였습니다.");
		}
		return orgService.getOrgSummary(orgId, token);
    }

    /**
     * 관리자/사용자 권한으로 조직 목록을 조회한다.
     *
     * @return the orgs for admin
     * @throws Exception the exception
     */
    @GetMapping(V2_URL + "/orgs")
    public ListOrganizationsResponse getOrgsForUser(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.debug("Org list by user");
        return orgService.getOrgsForUser(token);
    }

    /**
     * 관리자 권한으로 조직 목록을 조회한다.
     * @return
     */
    @GetMapping(V2_URL + "/orgs-admin")
    public ListOrganizationsResponse getOrgsForAdmin() {
    	LOGGER.debug("Org list for admin");
    	return orgService.getOrgsForAdmin();
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
    @GetMapping(V2_URL + "/orgs/{orgId}/spaces")
    public Map<?, ?> getSpaces(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
    	LOGGER.debug("Get Spaces " + orgId);
    	final Map<String, Object> result = new HashMap<>();
		result.put("spaceList", orgService.getOrgSpaces(orgId, token));

        return result;
    }
    
    /**
     * 사용자의 조직의 이름을 변경한다.
     * @param org
     * @param token
     * @param token
     * @return
     */
    @PutMapping( V2_URL + "/orgs" )
    public UpdateOrganizationResponse renameOrg( 
        @RequestBody Org org, @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        return orgService.renameOrg( org, token );
    }
    
    /**
     * 사용자의 조직을 삭제한다.
     * @param guid the organization id (guid)
     * @param token the token
     * @return boolean
     * @throws Exception the exception
     */
    @DeleteMapping(V2_URL+"/orgs")
    public DeleteOrganizationResponse deleteOrg(
        @RequestParam String guid, @RequestParam boolean recursive, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token )
        throws Exception {
        return orgService.deleteOrg(guid, recursive, token);
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
    @GetMapping(V2_URL + "/orgs/{orgId}/quota")
    public GetOrganizationQuotaDefinitionResponse getOrgQuota(@PathVariable String orgId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        LOGGER.info("Get quota of org {}" + orgId);
        return orgService.getOrgQuota(orgId, token);
    }
    
    @PutMapping(V2_URL + "/orgs/{orgId}/quota")
    public UpdateOrganizationResponse updateOrgQuota(@PathVariable String orgId, @RequestBody Org org, @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        LOGGER.info("Update quota of org {} / quota {}", org.getGuid(), org.getQuotaGuid());
        return orgService.updateOrgQuota( orgId, org, token );
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
    @GetMapping(V2_URL + "/orgs/{orgName:.+}/user-roles/{userName:.+}")
    public UserRole getOrgUserRoleByUsername ( @PathVariable String orgName,
                                               @PathVariable String userName,
                                               @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        final String userId = userService.getUserIdByUsername( userName );
        Objects.requireNonNull( userId, "Username cannot found" );

        LOGGER.info( "getOrgUserRoleByUsername : Org name : {} / User name : {} / User id : {}",
            orgName, userName, userId );
        OrganizationUsers users = orgService.getOrgUserRolesByOrgName( orgName, token );
        final boolean isManager = users.getManagers().parallelStream().anyMatch( userName::equals );
        final boolean isBillingManager = users.getBillingManagers().parallelStream().anyMatch( userName::equals );
        final boolean isAuditor = users.getAuditors().parallelStream().anyMatch( userName::equals );


        return UserRole.builder()
            .userEmail( userName )
            .userId( userId )
            .addRole( isManager?         "OrgManager"       : null )
            .addRole( isBillingManager?  "BillingManager"   : null )
            .addRole( isAuditor?         "OrgAuditor"       : null )
            .build();
    }

    @GetMapping(V2_URL + "/orgs/{orgName:.+}/user-roles/{userName:.+}/is-manager")
    public boolean isOrgManager(@PathVariable String orgName,
                                @PathVariable String userName,
                                @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token) {
        LOGGER.info( "isOrgManager : Org name : {} / User name : {}", orgName,
            userName);
        return orgService.getOrgUserRolesByOrgName( orgName, token )
            .getManagers().parallelStream().anyMatch(userName::equals );
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
    @DeleteMapping(V2_URL + "/orgs/{orgId}/user-roles")
    public void removeOrgUserRoles ( @PathVariable String orgId,
                                     @RequestParam String userId, @RequestParam String role,
                                     @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token ) {
        Objects.requireNonNull( userId, "User ID(userId) is required" );
        Objects.requireNonNull( role, "Org Role(role) is required" );
        LOGGER.info("Remove organization role of user (Delete) : {} / {}", userId, role);
        orgService.removeOrgUserRole( orgId, userId, role, token );
    }

    // TODO invite user
    public void inviteUser() { }

    // TODO cancel invite user
    public void cancelInvitionUser() { }

    // TODO cancel member
    public void cancelOrganizationMember() { }


    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 3                   //////
    //////   Document : http://v3-apidocs.cloudfoundry.org          //////
    //////   Not yet implemented                                    //////
    //////////////////////////////////////////////////////////////////////

    // Not-implemented
}
