package org.openpaas.paasta.portal.api.service;

import org.apache.commons.lang.RandomStringUtils;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CloudUser;
import org.cloudfoundry.client.lib.util.JsonUtil;
import org.cloudfoundry.client.v2.Metadata;
import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionRequest;
import org.cloudfoundry.client.v2.organizationquotadefinitions.GetOrganizationQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.organizations.*;


import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.spaces.SpaceEntity;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.users.GetUserRequest;
import org.cloudfoundry.client.v2.users.GetUserResponse;
import org.cloudfoundry.client.v2.users.UserResource;
import org.cloudfoundry.uaa.users.UserInfoRequest;
import org.cloudfoundry.uaa.users.UserInfoResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.model.InviteOrgSpace;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import sun.rmi.runtime.Log;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Future;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * 조직 서비스 - 조직 목록 , 조직 이름 변경 , 조직 생성 및 삭제 등을 제공한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@EnableAsync
@Service
public class OrgService extends Common {

    private final Logger LOGGER = getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private SpaceService spaceService;
    @Autowired
    private AsyncUtilService asyncUtilService;
//    @Autowired
//    private OrgMapper orgMapper;
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private InviteOrgSpaceMapper inviteOrgSpaceMapper;



    /**
     * 조직명을 변경한다.
     *
     * @param org   the org
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public boolean renameOrgV1(@RequestBody Org org, String token) throws Exception {

        //token setting
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        client.renameOrg(org.getOrgName(),org.getNewOrgName());
        return true;

    }

    /**
     * 조직을 삭제한다.
     *
     * @param org   the org
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public boolean deleteOrgV1(@RequestBody Org org, String token) throws Exception {

        CloudFoundryClient client = getCloudFoundryClient(token);
        CloudInfo cloudInfo = client.getCloudInfo();

        List<Map> list = userService.getListForTheUser("managed_organizations", token);

        Boolean auth = false;

        for (Map orgMap : list) {
            if (org.getOrgName().equals(orgMap.get("orgName"))) {
                auth = true;
            }
        }
        //OrgManager 권한이 있는지 체크
        if (auth || (adminUserName).equals(cloudInfo.getUser())) {
            CustomCloudFoundryClient admin = getCustomCloudFoundryClient(adminUserName, adminPassword);
            admin.deleteOrg(org.getOrgName());

        } else {
            throw new CloudFoundryException(HttpStatus.FORBIDDEN,"Forbidden","You are not authorized to perform the requested action.");
        }

        return true;

    }

    /**
     * 조직을 생성한다.
     *
     * @param org   the org
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.5.16 최초작성
     */
    public boolean createOrg(@RequestBody Org org, String token) throws Exception {

        String newOrgName = org.getNewOrgName();

        if (!stringNullCheck(newOrgName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        //token setting
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        client.createOrg(newOrgName);

        if (client.getCloudInfo().getUser().equals(adminUserName)) {
            /**  Cloud Foundry ORG Roles
             *   managers : ORG MANAGER
             *   billing_managers : BILLING MANAGER
             *   auditors : ORG AUDITOR
             */
            client.setOrgRole(newOrgName, client.getCloudInfo().getUser(), "users");
            client.setOrgRole(newOrgName, client.getCloudInfo().getUser(), "managers");
        }

        return true;
    }

    /**
     * 조직을 탈퇴한다.
     *
     * @param org   the org
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.1 최초작성
     */
    public boolean removeUserFromOrg(@RequestBody Org org, String token) throws Exception {

        String orgName = org.getOrgName();

        if (!stringNullCheck(orgName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        //token setting
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        List<CloudSpace> spaceList = client.getSpaces();

        String userGuid = client.getUserGuid();

        //해당 유저가 해당 조직의 모든 공간들에 대해 가진 role을 모두 제거
        CustomCloudFoundryClient admin = getCustomCloudFoundryClient(adminUserName, adminPassword);

        if (spaceList != null) {

            for (CloudSpace space : spaceList) {
                if (space.getOrganization().getName().equals(orgName)) {
                    //unset space role
                    String spaceName = space.getName();
                    admin.unsetSpaceRole(orgName, spaceName, userGuid, "auditors");
                    admin.unsetSpaceRole(orgName, spaceName, userGuid, "developers");
                    admin.unsetSpaceRole(orgName, spaceName, userGuid, "managers");
                    admin.removeSpaceFromUser(userGuid, orgName, spaceName);
                }
            }
        }

        //해당 유저가 해당 조직에 대해 가지고 있는 role을 제거
        admin.unsetOrgRole(orgName, userGuid, "auditors");
        admin.unsetOrgRole(orgName, userGuid, "managers");
        admin.removeOrgFromUser(userGuid, orgName);

        return true;
    }


    /**
     * 조직 role을 부여한다.
     * users role을 부여하지 않고 다른 role을 부여할 경우에도 그대로 role이 부여되므로 문제가
     *
     * @param orgName  the org name
     * @param userName the user name
     * @param userRole the user role
     * @param token    the token
     * @return Map org role
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.8.10 최초작성
     */
    public boolean setOrgRole(String orgName, String userName, String userRole, String token) throws Exception{
        if (!stringNullCheck(orgName,userName, userRole)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        String orgRole = toStringRole(userRole);

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        client.setOrgRole(orgName, userName, orgRole);

        return true;
    }


    /**
     * 조직 role을 제거한다.
     *
     * @param orgName  the org name
     * @param userGuid the user guid
     * @param userRole the user role
     * @param token    the token
     * @return Map boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.8.10 최초작성
     */
    public boolean unsetOrgRole(String orgName, String userGuid, String userRole, String token) throws Exception{
        if (!stringNullCheck(orgName,userGuid, userRole)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        String orgRole = toStringRole(userRole);

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);
        client.unsetOrgRole(orgName, userGuid, orgRole);

        return true;
    }

    /*
     * role 문자를 변경한다.
     *
     * @param userRole
     * @return
     */
    private String toStringRole(String userRole) {
        String roleStr;

        switch (userRole){
            case "users": roleStr = "users"; break;
            case "OrgManager": roleStr = "managers"; break;
            case "BillingManager": roleStr = "billing_managers"; break;
            case "OrgAuditor": roleStr = "auditors"; break;
            default: throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Invalid userRole.");
        }

        return roleStr;
    }

    /**
     * 해당 조직의 유저목록과 각 유저의 역할을 가져온다.
     *
     * @param orgName the org name
     * @param token   the token
     * @return all users
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.8.31 최초작성
     */
    public List<Map<String, Object>> getAllUsers(String orgName, String token) throws Exception {

        if (!stringNullCheck(orgName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }
        List<Map<String, Object>> orgUserList = new ArrayList();
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);
        UUID orgGuid = null;
        orgGuid = client.getOrgByName(orgName, true).getMeta().getGuid();
        LOGGER.debug("orgGuid : " +orgGuid);
        if(orgGuid!=null) {
            Future<List<Map<String, Object>>> users = asyncUtilService.getUsers(orgGuid, client);
            Future<Map<String, CloudUser>> managers = asyncUtilService.getUsersForOrgRole_managers(orgGuid, client);
            Future<Map<String, CloudUser>> billingManagers = asyncUtilService.getUsersForOrgRole_billingManagers(orgGuid, client);
            Future<Map<String, CloudUser>> auditors = asyncUtilService.getUsersForOrgRole_auditors(orgGuid, client);

            while (!(users.isDone() && managers.isDone() && billingManagers.isDone() && auditors.isDone())) {
                Thread.sleep(10); //10-millisecond pause between each check
            }
            orgUserList = putUserList(orgName, users.get(), managers.get() ,billingManagers.get() ,auditors.get());
        }

        LOGGER.debug("orgUserList : " +orgUserList.size());


        return orgUserList;
    }

    /**
     * 요청된 유저가 해당 조직에서 가지고 있는 역할을 가져온다.
     *
     * @param orgName  the org name
     * @param userList the user list
     * @param token    the token
     * @return users for org role
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.9.05 최초작성
     */
    public List<Map<String, Object>> getUsersForOrgRole(String orgName, List<Map<String,Object>> userList, String token) throws Exception {

        if (!stringNullCheck(orgName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);
        UUID orgGuid = client.getOrgByName(orgName, true).getMeta().getGuid();

        Future<Map<String, CloudUser>> managers = asyncUtilService.getUsersForOrgRole_managers(orgGuid,client);
        Future<Map<String, CloudUser>> billingManagers = asyncUtilService.getUsersForOrgRole_billingManagers(orgGuid, client);
        Future<Map<String, CloudUser>> auditors = asyncUtilService.getUsersForOrgRole_auditors(orgGuid,client);

        while (!(managers.isDone() && billingManagers.isDone() && auditors.isDone())) {
            Thread.sleep(10);
        }

        List<Map<String, Object>> orgUserList = putUserList(orgName, userList, managers.get() ,billingManagers.get() ,auditors.get());
        for (int i=0 ; i<orgUserList.size();i++){
            Map orgMap = orgUserList.get(i);
            LOGGER.debug("orgMap" + i+" : " + JsonUtil.convertToJson(orgMap));
        }
        return orgUserList;
    }

    /**
     * 권한별로 수집된 유저정보를 취합하여 하나의 객체로 만들어 리턴한다.
     * @param userList
     * @param managers
     * @param billingManagers
     * @param auditors
     * @return orgUserList
     * @throws Exception
     * @author 김도준
     * @version 1.0
     * @since 2016.9.05 최초작성
     */
    private List<Map<String, Object>> putUserList( String orgName,
                                                   List<Map<String, Object>> userList,
                                                   Map<String, CloudUser> managers,
                                                   Map<String, CloudUser> billingManagers,
                                                   Map<String, CloudUser> auditors) throws Exception
    {
        List<Map<String, Object>> orgUserList = new ArrayList<>();

        for(Map<String, Object> userMap : userList) {
            List<String> userRoles = new ArrayList<>();
            if(managers.get(userMap.get("userName")) != null){
                userRoles.add("OrgManager");
            }
            if(billingManagers.get(userMap.get("userName")) != null){
                userRoles.add("BillingManager");
            }
            if(auditors.get(userMap.get("userName")) != null){
                userRoles.add("OrgAuditor");
            }

            userMap.put("orgName", orgName);
            userMap.put("userRoles", userRoles);
            userMap.put("inviteYn", "N");
            userMap.put("token", "");
            orgUserList.add(userMap);
        }
        return orgUserList;
    }

    /**
     * 조직 role를 조회한다.
     *
     * @param org   the org
     * @param token the token
     * @return the org role
     * @throws Exception the exception
     */
    public List<CloudSpace> getOrgRole(Org org, String token) throws Exception {

        List<CloudSpace> listSpace = spaceService.getSpaces(org, token);
        return listSpace;

    }

    /**
     * 조직과 공간의 초대 정보를 등록한다.
     *
     * @param map the map
     * @return the int
     * @throws Exception the exception
     */
    public int insertOrgSpaceUser(HashMap map) throws Exception{
        // TODO
        // int cnt = inviteOrgSpaceMapper.insertInviteOrgSpace(map);
        int cnt = 0;
        return cnt;
    }

    /**
     * 조직과 공간의 초대 정보를 수한다.정
     *
     * @param map the map
     * @return the int
     * @throws Exception the exception
     */
    public int updateOrgSpaceUser(HashMap map) throws Exception{
        // TODO
        // int cnt = inviteOrgSpaceMapper.updateOrgSpaceUser(map);
        int cnt = 0;
        return cnt;
    }

    /**
     * 조직과 공간의 초대 사용자를 조회한다.
     *
     * @param inviteOrgSpace the invite org space
     * @return the list
     * @throws Exception the exception
     */
    public List<InviteOrgSpace> selectOrgSpaceUser (InviteOrgSpace inviteOrgSpace) throws Exception{
        // TODO
//        List<InviteOrgSpace> list = inviteOrgSpaceMapper.selectOrgSpaceUser(inviteOrgSpace);
//        return list;
        return null;
    }

    /**
     * 조직과 공간의 사용자를 저장한다.
     *
     * @param map the map
     * @throws Exception the exception
     */
    public void setOrgSpaceUser(Map map) throws Exception{

        List listOrg = (List)map.getOrDefault("listOrg", new ArrayList());
        List listSpace = (List)map.getOrDefault("listSpace", new ArrayList());
        String userId = (String)map.getOrDefault("userId","");
        LOGGER.debug("map : "+map);
        CloudFoundryClient admin = getCloudFoundryClient(adminUserName, adminPassword);

        for (int i=0; i<listOrg.size(); i++){

            List listOrgInfo = (List)listOrg.get(i);
            String orgName = listOrgInfo.get(0).toString();
//            CloudEntityResourceMapper resourceMapper = new CloudEntityResourceMapper();
            if ((Boolean)listOrgInfo.get(1)){
                setOrgRole(orgName, userId, "OrgManager", admin.login().getValue());
            }
            if ((Boolean)listOrgInfo.get(2)){
                setOrgRole(orgName, userId, "BillingManager", admin.login().getValue());
            }
            if ((Boolean)listOrgInfo.get(3)){
                setOrgRole(orgName, userId, "OrgAuditor", admin.login().getValue());
            }
            for (int j=0; j<listSpace.size(); j++){
                    List list = (List) listSpace.get(j);
                    String spaceName = list.get(0).toString();
                    if ((Boolean)list.get(1)){
                        spaceService.setSpaceRole(orgName, spaceName,userId,"SpaceManager",admin.login().getValue());
                    }
                    if ((Boolean)list.get(2)){
                    spaceService.setSpaceRole(orgName, spaceName,userId,"SpaceDeveloper",admin.login().getValue());
                    }
                    if ((Boolean)list.get(3)){
                    spaceService.setSpaceRole(orgName, spaceName,userId,"SpaceAuditor",admin.login().getValue());
                    }
            }
        }
    }

    /**
     * 조직과 공간의 사용자를 삭제한다.
     *
     * @param map the map
     * @throws Exception the exception
     */
    public void unsetOrgSpaceUser(Map map) throws Exception{

        List listOrg = (List)map.getOrDefault("listOrg", new ArrayList());
        List listSpace = (List)map.getOrDefault("listSpace", new ArrayList());
        String userId = (String)map.getOrDefault("userId","");
        LOGGER.debug("map : "+map);
        CloudFoundryClient admin = getCloudFoundryClient(adminUserName, adminPassword);

        for (int i=0; i<listOrg.size(); i++){

            List listOrgInfo = (List)listOrg.get(i);
            String orgName = listOrgInfo.get(0).toString();
            if ((Boolean)listOrgInfo.get(1)){
                unsetOrgRole(orgName, userId, "OrgManager", admin.login().getValue());
            }
            if ((Boolean)listOrgInfo.get(2)){
                unsetOrgRole(orgName, userId, "BillingManager", admin.login().getValue());
            }
            if ((Boolean)listOrgInfo.get(3)){
                unsetOrgRole(orgName, userId, "OrgAuditor", admin.login().getValue());
            }
            for (int j=0; j<listSpace.size(); j++){
                List list = (List) listSpace.get(j);
                String spaceName = list.get(0).toString();
                if ((Boolean)list.get(1)){
                    spaceService.unsetSpaceRole(orgName, spaceName,userId,"SpaceManager",admin.login().getValue());
                }
                if ((Boolean)list.get(2)){
                    spaceService.unsetSpaceRole(orgName, spaceName,userId,"SpaceDeveloper",admin.login().getValue());
                }
                if ((Boolean)list.get(3)){
                    spaceService.unsetSpaceRole(orgName, spaceName,userId,"SpaceAuditor",admin.login().getValue());
                }
            }
        }
    }

    /**
     * 사용자 초대 이메일을 발송한다.
     *
     * @param map {userId : 초대하는 사용자 아이디,,inviteId 사용자 아이디, }
     * @return
     * @throws Exception the exception
     */
    @Async
    public void inviteMemberEmail(Map map) throws Exception{

        List dataList = (List) map.getOrDefault("dataList",new ArrayList());
        String token = (String) map.getOrDefault("token","token");
        String sOrg ="";
        String sSpace ="";
        String tmpSpace ="";
        for (int i=0; i<dataList.size(); i++){
            List inviteList = (List) dataList.get(i);
            if("space".equals(inviteList.get(0))){
                if("".equals(tmpSpace)){
                    sSpace = (String) inviteList.get(1);
                    tmpSpace = (String) inviteList.get(1);
                }else{
                    if(!tmpSpace.equals(dataList.get(1))){
                        tmpSpace = (String) inviteList.get(1);
                        sSpace = sSpace + ", "+ inviteList.get(1);
                    }
                }
            }
            if("org".equals(inviteList.get(0))){
                if("".equals(sOrg)){
                    sOrg = (String) inviteList.get(1);
                }
            }
        }
        map.put("code", token);
        map.put("org", sOrg);
        map.put("space", sSpace);
        map.put("sFile", "accept.html");
        map.put("contextUrl", "/invitations/accept");
        sendInviteEmail(map);
    }

    /**
     * body의 내용을 url의 parameter로 보내준다.
     * 이미지를 넣을 경우
     *
     * @param body {userId , refreshToken, ....}
     * @return boolean boolean
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    public boolean sendInviteEmail(Map body) throws IOException, MessagingException {
        Boolean bRtn = false;
        try {
            String inviteId = (String) body.getOrDefault("inviteUserId", "inviteUserId");
            String userId = (String) body.getOrDefault("userId", "userId");
            String code = (String) body.getOrDefault("code", "code");
            String org = (String) body.getOrDefault("org", "");
            String space = (String) body.getOrDefault("space", "");

            String sSpan = " " + org + " 조직 ";
            if(!"".equals(space)){
                sSpan = sSpan +", " + space + " 공간 ";
            }
            // 메일 관련 정보
            Properties properties = emailConfig.properties();


//         메일 내용
            String username = (String) body.getOrDefault("username", properties.get("mail.smtp.username"));
            String userEmail = (String) body.getOrDefault("userEmail", properties.get("mail.smtp.userEmail"));
            ClassLoader classLoader = getClass().getClassLoader();
            String sFile = (String) body.getOrDefault("sFile", properties.get("mail.smtp.sFile"));
            String authUrl = (String) body.getOrDefault("authUrl", properties.get("mail.smtp.authUrl"));
            String contextUrl = (String) body.getOrDefault("contextUrl","");

            String subject = (String) body.getOrDefault("subject", properties.get("mail.smtp.subject"));
            // 인증
            Authenticator auth = emailConfig.auth();
            subject = sSpan + subject;

            // 메일 세션
            Session session = Session.getInstance(properties, auth);

            File file = new File(classLoader.getResource(sFile).getFile());
            System.out.println(file.getAbsolutePath());
            Document doc = Jsoup.parse(file, "UTF-8");
            Elements elementAhref = doc.select("a[href]");
            Elements elementSpan = doc.select("span");

            if (elementAhref.size() != 0) {
                elementAhref.get(0).attr("href", authUrl +contextUrl+ "?code=" + code);
            }
            if (elementSpan.size() > 0) {
                elementSpan.get(0).childNode(0).attr("text", userId);
                elementSpan.get(1).childNode(0).attr("text", sSpan);
            }

            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setDataHandler(new DataHandler(new ByteArrayDataSource(doc.outerHtml(), "text/html")));
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            msg.setContent(doc.outerHtml(), "text/html;charset=" + "EUC-KR");
            msg.setFrom(new InternetAddress(userEmail, username));
            msg.setReplyTo(InternetAddress.parse(userEmail, false));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(inviteId, false));
            Transport.send(msg);
            bRtn = true;
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
        return bRtn;

    }

    /**
     * 조직 이름을 가지고 조직 아이디를 조회한다.
     *
     * @param orgName //조직 이름
     * @return 조직 아이디
     * @throws Exception the exception
     */
    public int getOrgId(String orgName) throws Exception{

//        Map<String, Object> selectedOrg = orgMapper.selectOrg(orgName);
        Map<String, Object> selectedOrg = null;
        int orgId = (int)selectedOrg.get("orgId");

        return orgId;
    }

    /**
     * 공간에 대한 초대를 완료하고 초대상태를 Y로 만들어준다.
     *
     * @param code 초대 토큰
     * @return 상태를 Y 로 수정한 개수
     */
    public int updateInviteY(String code){
        Map map = new HashMap();
        map.put("token",code);
//        int cnt = inviteOrgSpaceMapper.updateInviteY(map);
        int cnt = 0;
        return cnt;
    }

    /**
     * 공간 초대 이메일을 통해 접근된 회수를 수정한다.
     *
     * @param code      the code
     * @param accessCnt //접근회수
     * @return 수정된 개수
     */
    public int updateAccessCnt(String code, int accessCnt){
        Map map = new HashMap();
        map.put("token",code);
        map.put("accessCnt",accessCnt);
//        int cnt = inviteOrgSpaceMapper.updateAccessCnt(map);
        int cnt =0;
        return cnt;
    }

    /**
     * 공간에 초대한 이메일의 token을 가진 초대 정보를 가져온다.
     *
     * @param code the code
     * @return List 초대 정보
     */
    public List selectInviteInfo(String code){
        Map map = new HashMap();
        map.put("token",code);
//        List list = inviteOrgSpaceMapper.selectInviteInfo(map);
        List list = null;
        return list;
    }

    /**
     * 초대정보 list 에 대한 공간의 CloudFoundry에 적용한다.
     *
     * @param list the list
     * @throws Exception the exception
     */
    public void setOrgSpaceUserList(List<Map> list) throws Exception {
        for (int i=0;i<list.size();i++){
            Map map = list.get(i);
            setOrgSpaceMember(map);
        }
    }

    /**
     * 초대정보 list 에 대한 공간의 CloudFoundry에 적용한다.
     *
     * @param map the map
     * @throws Exception the exception
     */
    public void setOrgSpaceMember(Map map) throws Exception{
        String gubun = (String) map.getOrDefault("gubun", "");
        String role = (String) map.getOrDefault("roleName", "");
        String inviteName = (String) map.getOrDefault("inviteName", "");
        String userId = (String) map.getOrDefault("inviteUserId", "");
        int inviteId = Integer.parseInt(map.getOrDefault("inviteId", -1).toString());
        CloudFoundryClient admin = getCloudFoundryClient(adminUserName, adminPassword);

        if("0".equals(gubun)){
            setOrgRole(inviteName, userId, "users", admin.login().getValue());
            setOrgRole(inviteName, userId, role, admin.login().getValue());
        }

        if("1".equals(gubun)){
            List<Space> spaceInfo = spaceService.getSpacesInfoById(inviteId);
            spaceService.setSpaceRole(spaceInfo.get(0).getOrgName(), inviteName,userId,role,admin.login().getValue());
        }
    }

    /**
     * 사용자 탭의 getAllUsers 서비스에 데이터 추가할 데이터를 가져온다.
     *
     * @param orgName :공간 이름
     * @param userId  : 로그인한 사용자 아이디
     * @param gubun   the gubun
     * @return 예 )[{userName: "lij", userGuid: "db040322-c831-4d51-b391-4f9ff8102dc9", inviteYn: "Y",…}]
     */
    public List<Map<String, Object>> getUsersByInvite(String orgName, String userId, String gubun) {
        InviteOrgSpace inviteOrgSpace = new InviteOrgSpace();

        inviteOrgSpace.setGubun(gubun);
        inviteOrgSpace.setUserId(userId);
        inviteOrgSpace.setInviteName(orgName);
        //List<InviteOrgSpace> orgInviteList = inviteOrgSpaceMapper.getUsersByInvite(inviteOrgSpace);
        List<InviteOrgSpace> orgInviteList = null;
        List<Map<String, Object>>orgUserList = new ArrayList<>();
        String voUserId = "";
        for (int i = 0; i < orgInviteList.size(); i++){
            InviteOrgSpace vo = orgInviteList.get(i);
            if (!vo.getInviteUserId().equals(voUserId)) {
                Map voMap = new HashMap<>();
                voUserId = vo.getInviteUserId();
                //String voUserGuid = userMapper.getUserGuid(voUserId) == null ? "" : userMapper.getUserGuid(voUserId);
                String voUserGuid = "";
                voMap.put("userName", voUserId);
                voMap.put("orgName", vo.getInviteName());
                voMap.put("userGuid", voUserGuid);
                voMap.put("token", vo.getToken());
                voMap.put("inviteYn", "Y");
                List list = new ArrayList();
                String rolename ="";
                for (int j = 0; j < orgInviteList.size(); j++){
                    InviteOrgSpace vo1 = orgInviteList.get(i);
                    if (vo1.getInviteUserId().equals(voUserId)) {
                        if(!vo.getRoleName().equals(rolename)) {
                            rolename = vo.getRoleName();
                            list.add(rolename);
                        }
                    }
                }
                voMap.put("userRoles", list);
                orgUserList.add(voMap);
            }
        }
        return orgUserList;
    }

    /**
     * 사용자 초대 이메일을 재전송한다.
     *
     * @param map {userId : 초대하는 사용자 아이디,,inviteId 사용자 아이디, }
     * @return map map
     * @throws Exception the exception
     */
    public Map inviteMemberEmailResend(Map map) throws Exception{
        InviteOrgSpace inviteOrgSpace = new InviteOrgSpace();
        inviteOrgSpace.setToken((String) map.getOrDefault("token", ""));
        inviteOrgSpace.setInviteId(-1);
        List<InviteOrgSpace> dataList = (List) selectOrgSpaceUser(inviteOrgSpace);
        String inviteId = "";
        String userId = "";
        //재전송코드
        String reToken = RandomStringUtils.randomAlphanumeric(6).toUpperCase() + RandomStringUtils.randomAlphanumeric(2).toUpperCase();
        String sOrg ="";
        String sSpace ="";
        String tmpSpace ="";
        for (int i=0; i<dataList.size(); i++){
            InviteOrgSpace vo = dataList.get(i);
            if("1".equals(vo.getGubun())){
                if("".equals(tmpSpace)){
                    sSpace =  vo.getInviteName();
                    tmpSpace = vo.getInviteName();
                }else{
                    if(!tmpSpace.equals(dataList.get(1))){
                        tmpSpace = vo.getInviteName();
                        sSpace = sSpace + ", "+ vo.getInviteName();
                    }
                }
            }
            if("0".equals(vo.getGubun())){
                if("".equals(sOrg)){
                    sOrg = vo.getInviteName();
                }
            }
            inviteId = vo.getInviteUserId();
            userId = vo.getUserId();
        }
        map.put("code", reToken);
        map.put("org", sOrg);
        map.put("space", sSpace);
        map.put("sFile", "accept.html");
        map.put("inviteUserId",inviteId);
        map.put("userId",userId);
        map.put("contextUrl", "/invitations/accept");
        HashMap updateTokenMap = new HashMap<>();
        updateTokenMap.put("createTime",new Date());
        updateTokenMap.put("retoken",reToken);
        updateTokenMap.put("token",inviteOrgSpace.getToken());
//        inviteOrgSpaceMapper.updateOrgSpaceUserToken(updateTokenMap);
        Boolean bRtn = sendInviteEmail(map);
        return updateTokenMap;
    }

    /**
     * 초대한 token 정보를 가지고 초대취소를 수행한다.
     *
     * @param map the map
     * @return int int
     * @throws Exception the exception
     */
    public int cancelInvite(Map map) throws Exception {
//        return inviteOrgSpaceMapper.deleteOrgSpaceUserToken(map);
        return 0;
    }

    /**
     * 해당 조직의 사용자를 조회한다.
     *
     * @param body  the body
     * @param token the token
     * @return List Map
     * @throws Exception the exception
     * @author injeong
     * @version 1.0
     * @since 2016.8.31 최초작성
     */
    public List<Map<String, Object>> getOrgUser(Map body, String token) throws Exception {

        String orgName = (String) body.getOrDefault("orgName" , "");
        if (!stringNullCheck(orgName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);
        UUID orgGuid = null;
        orgGuid = client.getOrgByName(orgName, true).getMeta().getGuid();
        LOGGER.debug("orgGuid : " +orgGuid);
        List<Map<String, Object>> users = client.getUsers(orgGuid);

        LOGGER.debug("getOrgUser : " +users.toString());

        return users;
    }

    /**
     * 사용자의 조직 권한을 삭제한다.
     *
     * @param body  the body
     * @param token the token
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean unsetUserOrg(Map body, String token) throws Exception {

        String orgName = (String) body.getOrDefault("orgName","");
        String userGuid= (String) body.getOrDefault("userGuid","");

        if (!stringNullCheck(orgName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }
        List<Map> listOrgOrSpace = new ArrayList<>();

//        List keyOfRole = new ArrayList();
//        keyOfRole.add("managed_organizations");
//        keyOfRole.add("billing_managed_organizations");
//        keyOfRole.add("organizations");
//        keyOfRole.add("managed_spaces");
//        keyOfRole.add("spaces");
//        keyOfRole.add("audited_spaces");
//        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);
        //해당 유저가 해당 조직의 모든 공간들에 대해 가진 role을 모두 제거

        CustomCloudFoundryClient admin = getCustomCloudFoundryClient(adminUserName, adminPassword);
        Map<String, Object> allOrgOrSpace = admin.listAllOrgOrSpaceForTheUser(userGuid, "spaces");
        List<Map> resources = (List) allOrgOrSpace.get("resources");
        for (Map<String, Map> resource : resources) {
            String unSetSpaceName = (String) resource.get("entity").get("name");
            admin.unsetSpaceRole(orgName, unSetSpaceName, userGuid, "auditors");
            admin.unsetSpaceRole(orgName, unSetSpaceName, userGuid, "developers");
            admin.unsetSpaceRole(orgName, unSetSpaceName, userGuid, "managers");
            admin.removeSpaceFromUser(userGuid, orgName, unSetSpaceName);
        }
        //해당 유저가 해당 조직에 대해 가지고 있는 role을 제거
        admin.unsetOrgRole(orgName, userGuid, "auditors");
        admin.unsetOrgRole(orgName, userGuid, "managers");
        admin.unsetOrgRole(orgName, userGuid, "billing_managers");
        admin.removeOrgFromUser(userGuid, orgName);
        return true;
    }


    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private final Duration blockDuration = Duration.ofSeconds(30);

    /**
     * 조직 Id를 이용해 조직 정보를 조회한다.
     *
     * @param orgId the org id
     * @param token the token
     * @return GetOrganizationResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public GetOrganizationResponse getOrg(String orgId, String token) {
    	GetOrganizationResponse response =
    		Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
    		.organizations()
    		.get(GetOrganizationRequest.builder().organizationId(orgId).build())
    		.block(blockDuration);

    	return response;
    }

    /**
     * 조직 Id를 이용해 조직 요약 정보를 조회한다.
     *
     * @param orgId the org id
     * @param token the token
     * @return SummaryOrganizationResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public SummaryOrganizationResponse getOrgSummary(String orgId, String token) {
		SummaryOrganizationResponse response =
			Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
		    .organizations()
		    .summary(SummaryOrganizationRequest.builder().organizationId(orgId).build())
		    .block(blockDuration);

		return response;
	}

    /**
     * 사용자/운영자 포털에서 조직목록을 요청했을때, 모든 조직목록을 응답한다.
     *
     * @param token the token
     * @return ListOrganizationsResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
	public ListOrganizationsResponse getOrgsForUser(final String token) {
		ListOrganizationsResponse response =
			Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
		    .organizations()
		    .list(ListOrganizationsRequest.builder().build())
		    .block(blockDuration);

		return response;
	}

	/**
     * 조직 목록을 조회한다. 단, 내부의 resources만 추출해서 반환한다.
     *
     * @param token the token
     * @return List<OrganizationResource> organization list
     * @author 김도준(1.x), hgcho(2.x)
     * @version 2.0
     * @since 2018.4.22
     */
    public List<OrganizationResource> getOrgs(String token) {
    	ListOrganizationsResponse response =
			Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
		    .organizations()
		    .list(ListOrganizationsRequest.builder().build())
		    .block(blockDuration);

    	return response.getResources();
    }

    /**
     * 운영자 포털에서 조직목록을 요청했을때, 모든 조직목록을 응답한다.
     *
     * @return ListOrganizationsResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
	public ListOrganizationsResponse getOrgsForAdmin() {
		ListOrganizationsResponse response =
			Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword))
		    .organizations()
		    .list(ListOrganizationsRequest.builder().build())
		    .block(blockDuration);

		return response;
	}

	/**
     * 운영자/사용자 포털에서 스페이스 목록을 요청했을때, 해당 조직의 모든 스페이스 목록을 응답한다.
     *
     * @param orgId the org id
     * @param token the token
     * @return Map&lt;String, Object&gt;
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public ListSpacesResponse getOrgSpaces(String orgId, String token) {
		ListSpacesResponse listSpacesResponse =
			Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
			.spaces()
		    .list(ListSpacesRequest.builder().organizationId(orgId).build())
		    .block(blockDuration);

		return listSpacesResponse;
    }

    /**
     * 조직의 Quota를 조회한다.
     *
     *
     * @param orgId the org id
     * @param token the token
     * @return Map&lt;String, Object&gt;
     * @author hgcho
     * @version 2.0
     * @since 2018.4.22
     */
    public GetOrganizationQuotaDefinitionResponse getOrgQuota(String orgId, String token) {
    	//final Map<String, Object> listOrgs = (List<Map<String, Object>>get).get("resources")

    	GetOrganizationResponse org = getOrg(orgId, token);
    	String quotaId = org.getEntity().getQuotaDefinitionId();

        GetOrganizationQuotaDefinitionResponse response =
	        Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
	        .organizationQuotaDefinitions()
	        .get(GetOrganizationQuotaDefinitionRequest.builder()
	        	.organizationQuotaDefinitionId(quotaId).build())
	        .block(blockDuration);

        return response;
    }
    public boolean deleteOrg(String orgid, String token) throws Exception {
        boolean result = false;

        UserInfoResponse userInfoResponse=
                Common.uaaClient(connectionContext(), tokenProvider(token))
                .users().userInfo(UserInfoRequest.builder().build()).block();
        ListOrganizationManagersResponse listOrganizationManagersResponse =
                Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .organizations().listManagers(ListOrganizationManagersRequest.builder().organizationId(orgid).build()).block();
        for (UserResource resource: listOrganizationManagersResponse.getResources()) {
            if(resource.getEntity().getUsername().equals(userInfoResponse.getUserName()))
            {
                LOGGER.info("삭제성공" + resource.getEntity().getUsername() + "::" + userInfoResponse.getUserName());
                Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                .organizations().delete(DeleteOrganizationRequest.builder().organizationId(orgid).build()).block();
                result = true;
            }
        }

        return result;
    }

    public UpdateOrganizationResponse renameOrg(String orgid, String orgrename, String token)
    {
        UpdateOrganizationResponse updateOrganizationResponse =
                Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .organizations().update(UpdateOrganizationRequest.builder().organizationId(orgid).name(orgrename).build()).block();
        return updateOrganizationResponse;
    }
}
