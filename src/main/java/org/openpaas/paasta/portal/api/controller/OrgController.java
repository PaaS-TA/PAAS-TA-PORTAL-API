package org.openpaas.paasta.portal.api.controller;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.RandomStringUtils;


import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.InviteOrgSpace;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.UserDetail;
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
import java.math.BigDecimal;
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

    /**
     * 조직 요약 정보를 조회한다.
     *
     * @param org     the org
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/org/getOrgSummary"}, method = RequestMethod.POST)
    public Org getOrgSummary(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("getOrgSummary Start : " + org.getOrgName());
        if(org.getOrgName()==null){
            throw new Exception("조직정보를 가져오지 못하였습니다.");
        }
        Org rspOrg = orgService.getOrgSummary(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("getOrgSummary End ");

        return rspOrg;
    }

    /**
     * 조직 이름으로 조회한다.
     *
     * @param org     the org
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/org/getOrgByName"}, method = RequestMethod.POST)
    public CloudOrganization getOrgByName(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("getOrgByName Start : " + org.getOrgName());

        CloudOrganization cloudOrg = new CloudOrganization(null, null);
        /* null로 초기화 할때, 에러시 응답하는 결과값에 content tpye이 세팅되지 않음. */

        cloudOrg = orgService.getOrgByName(org , request.getHeader(AUTHORIZATION_HEADER_KEY));

        LOGGER.info("getOrgByName End ");

        return cloudOrg;
    }

    /**
     * 조직명을 변경한다.
     *
     * @param org     the org
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/org/renameOrg"}, method = RequestMethod.POST)
    public boolean renameOrg(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("Rename Org Start : " + org.getOrgName() + " : " + org.getNewOrgName());

        orgService.renameOrg(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

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
    @RequestMapping(value = {"/org/deleteOrg"}, method = RequestMethod.POST)
    public boolean deleteOrg(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("delete Org Start : " + org.getOrgName());

        orgService.deleteOrg(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

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
    @RequestMapping(value = {"/org/getOrgs"}, method = RequestMethod.POST)
    public List getOrgs(HttpServletRequest request) throws Exception {

        LOGGER.info("Get Orgs Start");

        List<CloudOrganization> orgList  = orgService.getOrgs(request.getHeader(AUTHORIZATION_HEADER_KEY));

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
    @RequestMapping(value = {"/org/createOrg"}, method = RequestMethod.POST)
    public boolean createOrg(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("Create Org Start");

        orgService.createOrg(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

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
    @RequestMapping(value = {"/org/removeOrgFromUser"}, method = RequestMethod.POST)
    public boolean removeUserFromOrg(@RequestBody Org org, HttpServletRequest request) throws Exception {

        LOGGER.info("removeUserFromOrg Start");

        orgService.removeUserFromOrg(org, request.getHeader(AUTHORIZATION_HEADER_KEY));

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
    @RequestMapping(value = {"/org/setOrgRole"}, method = RequestMethod.POST)
    public boolean setOrgRole(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("setOrgRole Start");

        orgService.setOrgRole(body.get("orgName"), body.get("userName"), body.get("userRole"), token);

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
    @RequestMapping(value = {"/org/unsetOrgRole"}, method = RequestMethod.POST)
    public boolean unsetOrgRole(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("unsetOrgRole Start");

        orgService.unsetOrgRole(body.get("orgName"), body.get("userGuid"), body.get("userRole"), token);

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
    @RequestMapping(value = {"/org/getAllUsers"}, method = RequestMethod.POST)
    public List<Map<String, Object>> getAllUsers(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, String> body) throws Exception {

        List<Map<String, Object>> orgUserList= new ArrayList<>();
        try{

            LOGGER.info("getAllUsers Start");
            String orgName =body.getOrDefault("orgName","");
            String userId =body.getOrDefault("userId","");
            if("".equals(orgName) || orgName ==null){
                throw  new Exception(messageSource.getMessage("Organization_not_found", null, Locale.KOREA));
            }
            orgUserList = orgService.getAllUsers(body.get("orgName"), token);
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
    @RequestMapping(value = {"/org/getUsersForOrgRole"}, method = RequestMethod.POST)
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
        List<Map<String, Object>> orgUserList = orgService.getUsersForOrgRole(body.get("orgName").toString(), userList, token);
        orgUserList.addAll(inviteOrgUserList);
        LOGGER.info("getUsersForOrgRole End");

        return orgUserList;
    }

    /**
     * 관리자권한으로 조직 목록을 조회한다.
     *
     * @return the orgs for admin
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/org/getOrgsForAdmin"}, method = RequestMethod.POST)
    public Map<String, Object> getOrgsForAdmin() throws Exception {
        LOGGER.info("getOrgsForAdmin ::");
        List<Object> orgList = orgService.getOrgsForAdmin();

        return new HashMap<String, Object>(){{put("orgList", orgList);}};
    }

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
    @RequestMapping(value = {"/invite/inviteEmailSend"}, method = RequestMethod.POST)
    public Map<String, Object> inviteEmailSend(@RequestBody Map<String, Object> body) throws Exception {
        LOGGER.info("inviteUser ::"+body.toString());
        String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase() + RandomStringUtils.randomAlphanumeric(2).toUpperCase();
        List dataList = (List) body.getOrDefault("dataList",new ArrayList());

        String inviteUserId = (String) body.getOrDefault("inviteUserId", "inviteUserId");
        String userId = (String) body.getOrDefault("userId", "userId");
        body.put("token",token);
        int orgId =-1;


        /** 초대 관련 데이터 입력**/
        List inviteData = new ArrayList();


        //orgId때문 org 정보를 가져온다.
        for (int i=0; i<dataList.size(); i++){
            List inviteList = (List) dataList.get(i);
            if("org".equals((inviteList.get(0)))){
                Map tmpData = new HashMap();
                String gubun = "0";
                String inviteName = (String) inviteList.get(1);
                int inviteId = orgService.getOrgId(inviteName);
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
                int inviteId = 0;
                String gubun = "1";
                String inviteName = (String) inviteList.get(1);
                inviteId = spaceService.getSpacesInfo(inviteName,orgId).get(0).getSpaceId();
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
        orgService.inviteMemberEmail(body);
        return map;
    }

    /**
     * 조직에 사용자를  이메일 인증을 통해 초대한다.
     * body.put("space", testSpace);
     *
     * @param request the request
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/invitations/accept"})
    public Map<String, Object> inviteAccept(@RequestBody HashMap request) throws Exception {

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
            orgService.setOrgSpaceUserList(list);
            result.put("success",messageSource.getMessage("invite.info.success", null, Locale.KOREA));
        }else{
            result.put("error",messageSource.getMessage("invite.info.noCnt", null, Locale.KOREA));
        }
        return  result;
    }

    /**
     * 조직에 초대된 사용자의 가입여부를 조회한다.
     *
     * @param request the request
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/invitations/userInfo"})
    public Map<String, Object> inviteUserInfo(@RequestBody HashMap request) throws Exception {

        String code = (null == request.get("code")) ? "" : request.get("code").toString();
        LOGGER.info("code : "+ code+" : request : " +request.toString());
        Map<String, Object> result = new HashedMap();

        List<Map> list = orgService.selectInviteInfo(code);

        if(list.size() > 0) {
            String inviteId = (String) list.get(0).getOrDefault("inviteUserId", "");
            UserDetail userDetail = userService.getUser(inviteId);
            result.put("userId", inviteId);
            result.put("userDetail", userDetail);
        }
        result.put("listSize", list.size());

        return  result;
    }


    /**
     * 조직에 초대된 사용자의 미가입시 가입시킨다.
     *
     * @param request the request
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/invitations/authUser"})
    public Map<String, Object> inviteUserAdd(@RequestBody HashMap request) throws Exception {

        String code = (null == request.get("code")) ? "" : request.get("code").toString();
        LOGGER.info("code : "+ code+" : request : ");
        Map<String, Object> result = new HashedMap();

        List<Map> list = orgService.selectInviteInfo(code);
        Map userDetail = new HashMap();
        String inviteId = (String) list.get(0).getOrDefault("inviteuserid","");
        userDetail.put("userId", inviteId);
        userService.createUserAdd(userDetail);
        result.put("inserUserIdCnt" ,userService.createUserAdd(userDetail));
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
    @RequestMapping(value = {"/invite/inviteEmailReSend"}, method = RequestMethod.POST)
    public Map<String, Object> inviteEmailReSend(@RequestBody Map<String, Object> body) throws Exception {
        LOGGER.info("inviteUser ::"+body.toString());
        Map map = new HashMap();
        map.put("bSend", orgService.inviteMemberEmailResend(body));
        return map;
    }

    /**
     * 조직에 사용자를 이메일을 재전송한다.
     *
     * @param body the body
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/invite/cancelInvite"}, method = RequestMethod.POST)
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
    @RequestMapping(value = {"/invite/inviteEmailSendCnt"}, method = RequestMethod.POST)
    public Map<String, Object> inviteEmailSendCnt(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,@RequestBody Map<String, Object> body) throws Exception {

        Map map = new HashMap();
        map.put("bSend", orgService.getOrgUser(body, token));
        return map;
    }

    /**
     * 사용자의 조직권한을 삭제한다.
     *
     * @param token the token
     * @param body  the body
     * @return the map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/org/deleteUserOrg"}, method = RequestMethod.POST)
    public Map<String, Object> deleteUserOrg(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,@RequestBody Map<String, Object> body) throws Exception {

        Map map = new HashMap();
        map.put("bSend", orgService.unsetUserOrg(body, token));
        return map;
    }

}
