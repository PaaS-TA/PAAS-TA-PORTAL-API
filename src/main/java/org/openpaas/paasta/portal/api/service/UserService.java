package org.openpaas.paasta.portal.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.users.GetUserRequest;
import org.cloudfoundry.client.v2.users.GetUserResponse;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.tokens.GetTokenByClientCredentialsRequest;
import org.cloudfoundry.uaa.tokens.GetTokenByClientCredentialsResponse;
import org.cloudfoundry.uaa.users.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.openpaas.paasta.portal.api.model.UserManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;


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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    @Autowired
    RestTemplate restTemplate;

    /**
     * 사용자 생성
     *
     * @param userDetail the user detail
     * @return int int
     */
    @HystrixCommand(commandKey = "createUser")
    public Map createUser(UserDetail userDetail) {
        LOGGER.info("createUser ::: " + userDetail.getUserId());
        Map result = new HashMap();
        try {
            ReactorUaaClient reactorUaaClient = Common.uaaClient(connectionContext(apiTarget, true), tokenProvider(this.getToken()));
            reactorUaaClient.users().create(CreateUserRequest.builder().userName(userDetail.getUserId()).password(userDetail.getPassword()).active(userDetail.getActive()).email(Email.builder().value(userDetail.getUserId()).primary(false).build()).build()).block();

            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }


        return result;
    }


    /**
     * 사용자 정보 수정
     *
     * @param userDetail the user detail
     * @return int int
     */
    @HystrixCommand(commandKey = "updateUser")
    public int updateUser(UserDetail userDetail, String token) {
        LOGGER.info("updateUser ::: " + userDetail.getUserId());
        Map result = new HashMap();
        try {
            ReactorUaaClient reactorUaaClient = Common.uaaClient(connectionContext(apiTarget, true), tokenProvider(this.getToken()));
            reactorUaaClient.users().update(UpdateUserRequest.builder().userName(userDetail.getUserId()).phoneNumber(PhoneNumber.builder().value(userDetail.getTellPhone()).build()).email(Email.builder().value(userDetail.getUserName()).build()).build()).block();

            //TODO : ORG 권한 부여
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }

        return 0;
    }


    /**
     * 사용자 패스워드를 변경한다.
     *
     * @param userId      the user id
     * @param oldPassword the user id
     * @param newPassword the user id
     * @return UserDetail user
     */
    @HystrixCommand(commandKey = "updateUserPassword")
    public Map updateUserPassword(String userId, String oldPassword, String newPassword, String token) {

        LOGGER.info("updateUserPassword ::: " + userId);
        LOGGER.info("updateUserPassword ::: " + oldPassword);
        LOGGER.info("updateUserPassword ::: " + newPassword);

        Map result = new HashMap();
        try {

            ReactorUaaClient reactorUaaClient = Common.uaaClient(connectionContext(), tokenProvider(userId, oldPassword));
            UserInfoResponse userInfoResponse = reactorUaaClient.users().userInfo(UserInfoRequest.builder().build()).block();
            ChangeUserPasswordResponse changeUserPasswordResponse = reactorUaaClient.users().changePassword(ChangeUserPasswordRequest.builder().userId(userInfoResponse.getUserId()).oldPassword(oldPassword).password(newPassword).build()).block();
            LOGGER.info("updateUserPassword status :: " + changeUserPasswordResponse.getStatus());

            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }

        return result;
    }

    /**
     * 사용자 패스워드를 초기화 한다.
     * 패스워드를 입력안할경우 임의 값으로 패스워드를 변경한다.
     */
    @HystrixCommand(commandKey = "resetPassword")
    public Map resetPassword(String userId, String password) {
        LOGGER.info("resetPassword ::: " + userId);
        LOGGER.info("resetPassword ::: " + password);

        Map result = new HashMap();
        try {
            ReactorUaaClient reactorUaaClient = Common.uaaClient(connectionContext(apiTarget, true), tokenProvider(this.getToken()));


            GetTokenByClientCredentialsResponse getTokenByClientCredentialsResponse = reactorUaaClient.tokens().getByClientCredentials(GetTokenByClientCredentialsRequest.builder().clientId(uaaLoginClientId).clientSecret(uaaLoginClientSecret).build()).block();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", getTokenByClientCredentialsResponse.getTokenType() + " " + getTokenByClientCredentialsResponse.getAccessToken());
            HttpEntity<Map> resetEntity = new HttpEntity(userId, headers);
            ResponseEntity<Map> responseEntity = restTemplate.exchange(uaaTarget + "/password_resets?client_id=" + uaaLoginClientId, HttpMethod.POST, resetEntity, Map.class);
            LOGGER.debug(responseEntity.getBody().toString());

            String code = responseEntity.getBody().get("code").toString();
            String userGuid = responseEntity.getBody().get("user_id").toString();
            LOGGER.debug("CODE ::: " + code);
            LOGGER.debug("userGuid ::: " + userGuid);

            headers.add("Content-Type", "application/json");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("code", code);
            param.put("new_password", password);
            HttpEntity<Map> changeEntity = new HttpEntity<Map>(param, headers);
            ResponseEntity<Map> response = restTemplate.exchange(uaaTarget + "/password_change", HttpMethod.POST, changeEntity, Map.class);
            LOGGER.debug(response.getBody().toString());

            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }

        return result;
    }

    /**
     * 사용자 패스워드를 만료시킨다.
     * 패스워드를 사용자가 변경하게 수정한다.
     */
    @HystrixCommand(commandKey = "expiredPassword")
    public Map expiredPassword(String userGuid) {
        LOGGER.info("resetPassword ::: " + userGuid);

        Map result = new HashMap();
        try {
            ReactorUaaClient reactorUaaClient = Common.uaaClient(connectionContext(apiTarget, true), tokenProvider(this.getToken()));
            reactorUaaClient.users().expirePassword(ExpirePasswordRequest.builder().passwordChangeRequired(true).userId(userGuid).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }

        return result;
    }


    /**
     * CloudFoundry 사용자 삭제
     *
     * @param userId the user id
     * @return 삭제 정보
     */
    @HystrixCommand(commandKey = "deleteUser")
    public Map deleteUser(String userId) {
        LOGGER.info("deleteUser ::: " + userId);

        Map result = new HashMap();
        try {
            ReactorUaaClient reactorUaaClient = Common.uaaClient(connectionContext(apiTarget, true), tokenProvider(this.getToken()));
            DeleteUserResponse deleteUserResponse = reactorUaaClient.users().delete(DeleteUserRequest.builder().userId(userId).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }

        return result;
    }

    @HystrixCommand(commandKey = "getUsernameFromToken")
    public String getUsernameFromToken(String token) {
        return Common.uaaClient(connectionContext(apiTarget, true), tokenProvider(token)).getUsername().block();
    }

    @HystrixCommand(commandKey = "getUser")
    public User getUser(String token) {
        final String userName = getUsernameFromToken(token);
        return this.getUserSummaryByUsername(userName);
    }

    /**
     * CloudFoundry 이용하여 사용자 정보 출력
     *
     * @return 삭제 정보
     */
    @HystrixCommand(commandKey = "getUser")
    public GetUserResponse getUser(String userGuid, String token) throws MalformedURLException, URISyntaxException {

        LOGGER.info("getUser ::: ");
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(this.getToken()));
        GetUserResponse getUserResponse = cloudFoundryClient.users().get(GetUserRequest.builder().userId(userGuid).build()).block();
        return getUserResponse;
    }


    /**
     * CloudFoundry 이용하여 사용자 전체 정보 출력
     *
     * @return 삭제 정보
     */
    @HystrixCommand(commandKey = "allUsers")
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        try {
            LOGGER.info("allUsers ::: ");
            ReactorUaaClient reactorUaaClient = Common.uaaClient(connectionContext(apiTarget, true), tokenProvider(this.getToken()));
            ListUsersResponse listUsersResponse = reactorUaaClient.users().list(ListUsersRequest.builder().build().builder().build()).block();
            users = listUsersResponse.getResources();
            return users;
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 메일인증된 CloundFoundry 회원을 생성한다.
     *
     * @param map the map
     * @return boolean
     * @throws Exception the exception
     */
    @HystrixCommand(commandKey = "create")
    public boolean create(HashMap map) throws Exception {
        Boolean bRtn = false;
        return bRtn;
    }


    private enum UaaUserLookupFilterType {Username, Id, Origin}

    @HystrixCommand(commandKey = "createUserLookupFilter")
    private String createUserLookupFilter(UaaUserLookupFilterType filterType, String filterValue) {
        Objects.requireNonNull(filterType, "User lookup FilterType");
        Objects.requireNonNull(filterValue, "User lookup FilterValue");

        StringBuilder builder = new StringBuilder();
        builder.append(filterType.name()).append(" eq \"").append(filterValue).append("\"");
        return builder.toString();
    }

    /**
     * 유저 이름(user name)으로 유저의 GUID(user id)를 가져온다.
     *
     * @param username
     * @return User ID
     */
    @HystrixCommand(commandKey = "getUserIdByUsername")
    public String getUserIdByUsername(String username) {
        final List<User> userList = Common.uaaClient(connectionContext(), tokenProvider(this.getToken())).users().list(ListUsersRequest.builder().filter(createUserLookupFilter(UaaUserLookupFilterType.Username, username)).build()).block().getResources();
        if (userList.size() <= 0) {
            //throw new CloudFoundryException( HttpStatus.NOT_FOUND, "User name cannot find" );
            return null;
        }

        return userList.get(0).getId();
    }

    /**
     * 유저 GUID(user id)로 유저의 이름(user name)을 가져온다.
     *
     * @param userId
     * @return User name
     */
    @HystrixCommand(commandKey = "getUsernameByUserId")
    public String getUsernameByUserId(String userId) {
        final List<User> userList = Common.uaaClient(connectionContext(), tokenProvider(this.getToken())).users().list(ListUsersRequest.builder().filter(createUserLookupFilter(UaaUserLookupFilterType.Id, userId)).build()).block().getResources();
        if (userList.size() <= 0) {
            //throw new CloudFoundryException( HttpStatus.NOT_FOUND, "User ID cannot find" );
            return null;
        }

        return userList.get(0).getId();
    }

    private User getUserSummaryWithFilter(UaaUserLookupFilterType filterType, String filterValue) {
        final ListUsersResponse response = Common.uaaClient(connectionContext(), tokenProvider(this.getToken())).users().list(org.cloudfoundry.uaa.users.ListUsersRequest.builder().filter(createUserLookupFilter(filterType, filterValue)).build()).block();
        if (response.getResources().size() <= 0)
            throw new CloudFoundryException(HttpStatus.NOT_FOUND, (filterType.name() + " of user cannot find"));

        return response.getResources().get(0);
    }

    public User getUserSummary(String userId) {
        return getUserSummaryWithFilter(UaaUserLookupFilterType.Id, userId);
    }

    public User getUserSummaryByUsername(String userName) {
        return getUserSummaryWithFilter(UaaUserLookupFilterType.Username, userName);
    }

    public UpdateUserResponse UpdateUserActive(String userGuid) {
         ReactorUaaClient uaaClient = Common.uaaClient(connectionContext(apiTarget, true), tokenProvider(this.getToken()));
         User user = getUserSummaryWithFilter(UaaUserLookupFilterType.Username, userGuid);
         Name name = Name.builder().familyName((user.getName().getFamilyName()==null)||(user.getName().getFamilyName().equals(""))?"familyName":user.getName().getFamilyName()).givenName((user.getName().getGivenName()==null)||(user.getName().getFamilyName().equals(""))?"givenName":user.getName().getGivenName()).build();
         final boolean active = !user.getActive();
         UpdateUserResponse updateUserResponse = uaaClient.users().update(UpdateUserRequest.builder().name(name).userName(user.getUserName()).version(user.getMeta().getVersion().toString()).email(user.getEmail().get(0)).id(user.getId()).active(active).build()).block();
         return updateUserResponse;
    }

}
