package org.openpaas.paasta.portal.api.service;

import org.apache.commons.lang.RandomStringUtils;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.identity.uaa.api.user.UaaUserOperations;
import org.cloudfoundry.identity.uaa.scim.ScimUser;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
//import org.openpaas.paasta.portal.api.mapper.portal.UserDetailMapper;
//import org.openpaas.paasta.portal.api.mapper.uaa.UserMapper;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 유저 서비스 - 마이페이지의 유저의 조회 수정을 처리한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.5.23 최초작성
 */
@Service
@Transactional
public class UserService extends Common {

//    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    //    @Autowired
//    Container container;
//
    private final Logger LOGGER = getLogger(this.getClass());

    /**
     * Portal DB의 UserDetail table을 사용하는 Mapper
     */
//    @Autowired
//    private UserDetailMapper userDetailMapper;
    /**
     * CC DB의 User table을 사용하는 Mapper
     */
//    @Autowired
//    private UserMapper userMapper;

//    @Autowired
//    private GlusterfsServiceImpl glusterfsService;

    /**
     * 사용자 자동생성
     *
     * @param userDetail the user detail
     * @return int int
     */
    public int createUser(UserDetail userDetail) {

        //return userDetailMapper.insert(userDetail);
        return 0;
    }

    /**
     * 사용자 상세화면에서
     * 사용자 정보 수정
     *
     * @param userId     the user id
     * @param userDetail the user detail
     * @return Int updateCount
     */
    public int updateUser(String userId, UserDetail userDetail) {
//        return userDetailMapper.update(userId, userDetail);
        return 0;
    }

    /**
     * 아이디 수정
     *
     * @param oldUserId the old user id
     * @param newUserId the new user id
     * @return Int updateCount
     */
    @Transactional
    public int updateUserId(String oldUserId, String newUserId) {
        int cnt = 0;
//        if (isExist(newUserId) || userMapper.isExist(newUserId) > 0) {
//            cnt = -1;
//        } else {
//            HashMap hashMap = new HashMap();
//            HashMap srchMap = new HashMap();
//            hashMap.put("searchUserId", oldUserId);
//            hashMap.put("userId", newUserId);
//            srchMap.put("userId", oldUserId);
//
//            List list = userDetailMapper.getUserDetailInfo(srchMap);
//            if (list.size() > 0) {
//                cnt += userDetailMapper.upadteUserParam(hashMap);
//                if (list.size() > 0) userMapper.updateUserNameAndEmail(oldUserId, newUserId);
//            }
//        }

        return cnt;
    }

    /**
     * Update user password.
     *
     * @param ccfc        the ccfc
     * @param credentials the credentials
     * @param newPassword the new password
     */
    public void updateUserPassword(CustomCloudFoundryClient ccfc, CloudCredentials credentials, String newPassword) {
        ccfc.updatePassword(credentials, newPassword);
    }

    /**
     * 사용자 상세 정보
     *
     * @param userId the user id
     * @return UserDetail user
     */
    public UserDetail getUser(String userId) {
//        return userDetailMapper.selectOne(userId);
        return null;
    }

    /**
     * portal db에 등록된 UserDetail 수
     *
     * @return int user count
     */
    public int getUserCount() {
//        return userDetailMapper.getUserDetailCount();
        return 0;
    }

    /**
     * 사용자가 존재하는지 여부
     *
     * @param userId the user id
     * @return Boolean boolean
     */
    public boolean isExist(String userId) {
        Boolean flag = false;
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        List listUser = getUserDetailInfo(hashMap);
        if (listUser.size() > 0) {
            flag = true;
        }
//        flag = true;  // JPA 연동이전 테스트용

        return flag;
    }

    /**
     * CloudFoundry와 DB에서 사용자 삭제
     *
     * @param adminCcfc the admin ccfc
     * @param ccfc      the ccfc
     * @param userId    the user id
     * @return 삭제 정보
     */
    public int deleteUser(CustomCloudFoundryClient adminCcfc, CustomCloudFoundryClient ccfc, String userId) {

        adminCcfc.login();
        adminCcfc.deleteUser(ccfc.getUserGuid());

        return deleteUser(userId);
    }

    /**
     * DB에서 사용자 삭제
     *
     * @param userId the user id
     * @return 삭제 정보
     */
    public int deleteUser(String userId) {

//        return userDetailMapper.delete(userId);
        return 0;
    }


    /**
     * role에 따른 조직 및 영역 조회
     *
     * @param keyOfRole the key of role
     * @param token     the token
     * @return Map  <p> keyOfRole값을 파라미터로 보내 유저가 해당 role을 가지고 있는 모든 org 또는 space 정보를 가져온다. ex: 'managed_organizations' 을 입력하여 해당 유저가 Org Manager role을 가지고 있는 모든 org를 확인할 수 있다. <p> 조직 role           keyOfRole 값 ORG MANAGER:        managed_organizations BILLING MANAGER:    billing_managed_organizations ORG AUDITOR:        audited_organizations ORG USER:           organizations <p> 영역 role SPACE MANAGER:      managed_spaces SPACE DEVELOPER:    spaces SPACE AUDITOR:      audited_spaces
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.6.9 최초작성
     */
    public List getListForTheUser(String keyOfRole, String token) throws Exception {

        List<Map> listOrgOrSpace = new ArrayList<>();

        switch (keyOfRole) {
            case "managed_organizations":
                break;
            case "billing_managed_organizations":
                break;
            case "audited_organizations":
                break;
            case "organizations":
                break;
            case "managed_spaces":
                break;
            case "spaces":
                break;
            case "audited_spaces":
                break;
            default:
                throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Requested parameter is invalid");
        }

        CustomCloudFoundryClient admin = getCustomCloudFoundryClient(adminUserName, adminPassword);
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token);

        String userGuid = client.getUserGuid();

        Map<String, Object> allOrgOrSpace = admin.listAllOrgOrSpaceForTheUser(userGuid, keyOfRole);

        //to return
        List<Map> resources = (List) allOrgOrSpace.get("resources");

        if (keyOfRole.contains("organizations")) {
            for (Map<String, Map> resource : resources) {
                Map entityMap = new HashMap();
                entityMap.put("orgName", resource.get("entity").get("name"));
                entityMap.put("created", resource.get("metadata").get("created_at"));
                entityMap.put("updated", resource.get("metadata").get("updated_at"));
                listOrgOrSpace.add(entityMap);
            }
        } else if (keyOfRole.contains("spaces")) {
            for (Map<String, Map> resource : resources) {
                Map entityMap = new HashMap();
                entityMap.put("spaceName", resource.get("entity").get("name"));
                entityMap.put("created", resource.get("metadata").get("created_at"));
                entityMap.put("updated", resource.get("metadata").get("updated_at"));
                listOrgOrSpace.add(entityMap);
            }
        }
        LOGGER.info(listOrgOrSpace.toString());

        return listOrgOrSpace;
    }


    /**
     * 사용자 정보 인증
     * potalDB에 사용자 정보를 등록한후 이메일을 보낸다.
     *
     * @param body the body
     * @return boolean
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    @Transactional
    public boolean createRequestUser(HashMap body) throws IOException, MessagingException {
        LOGGER.info("SERVICE createTmpUser map : ", body.toString());
        HashMap map = new HashMap();
        Boolean bRtn = false;
        map.put("userId", body.get("userId"));
        List<UserDetail> listUser = getUserDetailInfo(map);
        String randomId = RandomStringUtils.randomAlphanumeric(17).toUpperCase() + RandomStringUtils.randomAlphanumeric(2).toUpperCase();
        map.put("refreshToken", randomId);
        map.put("authAccessTime", new Date());
        if (listUser.size() < 1) {
//            userDetailMapper.createRequestUser(map);
        } else {
            map.put("searchUserId", body.get("userId"));
            map.put("authAccessCnt", 0);
//            userDetailMapper.upadteUserParam(map);
        }

        Boolean resultSendEmail = sendEmail(map);
        if (resultSendEmail) {
            bRtn = true;
        }
        return bRtn;

    }


    /**
     * 사용자의 상세정보를 조회한다.
     *
     * @param map the map
     * @return List<UserDetail> user detail info
     */
    public List<UserDetail> getUserDetailInfo(HashMap map) {
        LOGGER.info(this.getClass().getName() + ":" + "getUserDetailInfo  :: map :: " + map.isEmpty());
//        List<UserDetail> listRtn = userDetailMapper.getUserDetailInfo(map);
//        return listRtn;
        return null;
    }


    /**
     * 비밀번호 인증 을 한다.
     *z`
     * @param map (userId, refreshToken)
     * @return 성공, 실패 여부
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    public boolean resetPassword(HashMap map) throws IOException, MessagingException {

        Boolean bRtn = false;
        String randomId = RandomStringUtils.randomAlphanumeric(17).toUpperCase() + RandomStringUtils.randomAlphanumeric(2).toUpperCase();
        map.put("refreshToken", randomId);
        map.put("authAccessCnt", 0);
        map.put("authAccessTime", new Date());

//        int resultCreateUser = userDetailMapper.upadteUserParam(map);
        int resultCreateUser = 0;
        if (resultCreateUser >= 1) {
            map.put("resetPassword", resultCreateUser);
            map.put("sFile", "resetPassword.html");
            map.put("contextUrl", "user/authPassword");
            bRtn = sendEmail(map);
        }
        return bRtn;
    }

    /**
     * 이메일 인증된 사용자의 추가 정보를 저장한다.
     *
     * @param map (이름, 비밀번호)
     * @return boolean
     */
    public boolean authAddUser(HashMap map) {
        Boolean bRtn = false;
//        int rtn = userDetailMapper.upadteUserParam(map);
        int rtn = 0;
        if (rtn < 1) {

            bRtn = true;
            LOGGER.debug("rtn : "+rtn);
        }
        return bRtn;
    }


    /**
     * 이메일 인증된 사용자의 접근 횟수를 더하여 저장한다.
     *
     * @param map the map
     * @return boolean
     */
    public boolean authAddAccessCnt (HashMap map) {
        Boolean bRtn = false;
        int accessCnt = (Integer)map.getOrDefault("authAccessCnt",0) +1;
        String userId = (String)map.getOrDefault("userId","");
        HashMap requestBody = new HashMap();
        requestBody.put("userId",userId);
        requestBody.put("searchUserId",userId);
        requestBody.put("authAccessCnt",accessCnt);

//        int rtn = userDetailMapper.upadteUserParam(requestBody);
        int rtn = 0;
        if (rtn < 1) {

            bRtn = true;
            LOGGER.debug("rtn : "+rtn);
        }
        return bRtn;
    }


    /**
     * 이메일 인증후 비밀번호를 등록한다.
     * <p>
     * id, password 방식으로 CloudFoundry 인증 토큰을 OAuth2AccessToken 형태로 반환한다.
     *
     * @param customCloudFoundryClient the custom cloud foundry client
     * @param map                      the map
     * @return OAuth2AccessToken boolean
     * @throws Exception the exception
     */
    public boolean updateAuthUserPassword(CustomCloudFoundryClient customCloudFoundryClient, HashMap map) throws Exception {

        Boolean bRtn = false;
        String userId = (String)map.getOrDefault("userId","");
        String newPassword = (String)map.getOrDefault("newPassword","");

        Map rtnMap = customCloudFoundryClient.resetPassword(userId, newPassword, uaaLoginClientId, uaaLoginClientSecret, uaaTarget);
        if(userId.equals(rtnMap.getOrDefault("username",""))){
            bRtn = true;
        }
        return bRtn;
    }

    /**
     * 메일인증된 CloundFoundry 회원을 생성한다.
     *
     * @param map the map
     * @return boolean
     * @throws Exception the exception
     */
    public boolean create(HashMap map) throws Exception {
        Boolean bRtn = false;
        UaaUserOperations operations = getUaaUserOperations(uaaClientId);
        String userId = (String) map.getOrDefault("userId", "");
        String password = (String) map.getOrDefault("password", "");
        String userEmail = (String) map.getOrDefault("userId", "");

        ScimUser scimUser = new ScimUser(userId, userId, "givenName", "familyName");
        ScimUser.Email email = new ScimUser.Email();
        email.setValue(userEmail);
        email.setPrimary(true);
        List<ScimUser.Email> emails1 = new ArrayList<>();
        emails1.add(email);
        scimUser.setEmails(emails1);
        scimUser.setVerified(true);
        scimUser.setPassword(password);

        ScimUser results = operations.createUser(scimUser);
        String resultUserId = results.getUserName();

        if(userId.equals(resultUserId)) bRtn = true;
        return bRtn ;
    }

    /**
     * 파일 업로드
     *
     * @param multipartFile the multipart file
     * @return map map
     * @throws Exception the exception
     */
    public Map<String, Object> uploadFile(MultipartFile multipartFile) throws Exception {
        return new HashMap<String, Object>() {{
            //put("path", glusterfsService.upload(multipartFile));
            put("path", "");
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }

    /**
     * 전체 UAA 유저의 userName과 userGuid를 가져온다.
     *
     * @return userInfo list
     */
    public List<Map<String,String>> getUserInfo(){
//        List<Map<String,String>> userInfo = userMapper.getUserInfo();
//        return userInfo;
        return null;
    }

    /**
     * Create user add int.
     *
     * @param createMap the create map
     * @return the int
     */
    public int createUserAdd(Map createMap) {
//        return userDetailMapper.createRequestUser(createMap);
        return 0;
    }
//    Map<String, Object> allOrgOrSpace = admin.listAllOrgOrSpaceForTheUser(userGuid, keyOfRole);
}
