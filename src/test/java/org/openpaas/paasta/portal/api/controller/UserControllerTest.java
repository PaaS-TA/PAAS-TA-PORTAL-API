package org.openpaas.paasta.portal.api.controller;

import com.google.gson.Gson;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {ApiApplication.class})
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
@Transactional("portalTransactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest extends CommonTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;

    private static Gson gson = new Gson();

    private static final String AUTHORIZATION_HEADER_KEY="cf-Authorization";

    // token
    private static String adminTestToken = "";
    private static String testToken      = "";

    // userDetail
    private static UserDetail testUser          = null;
    private static UserDetail insertTestUser   = null;
    private static UserDetail notFoundTestUser = null;

    // cf user
    private static String testId               = "junit-test-user";
    private static String testPassword        = "1234";
    private static String adminTestId = getPropertyValue("test.admin.id");
    private static String adminTestPassword = getPropertyValue("test.admin.password");

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        mvc.perform(put("/user/insertUser")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                .content(gson.toJson(testUser)));
    }

    @BeforeClass
    public static void a_init() throws Exception{
//        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        //Get adminTestToken for test
        CloudCredentials credentials = new CloudCredentials(adminTestId, adminTestPassword);
        adminTestToken = new CloudFoundryClient(credentials, getTargetURL(apiTarget), true).login().getValue();

        CloudCredentials clientCredentials = new CloudCredentials(testId, testPassword);
        testToken = new CloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true).login().getValue();

        //init userDetail for test
        testUser = new UserDetail();
        testUser.setUserId(testId);
        testUser.setStatus("not ok");
        testUser.setAddress("test_Addr");
        testUser.setAddressDetail("test_Addr_Detail");
        testUser.setTellPhone("11111111111");
        testUser.setZipCode("111111");

        insertTestUser = new UserDetail();
        insertTestUser.setUserId("Insert_Test_ID");
        //insertTestUser.setUserId("test_id");
        insertTestUser.setStatus("not ok");
        insertTestUser.setAddress("test_Addr");
        insertTestUser.setAddressDetail("test_Addr_Detail");
        insertTestUser.setTellPhone("11111111111");
        insertTestUser.setZipCode("111111");

        notFoundTestUser = new UserDetail();
        notFoundTestUser.setUserId("NotFound_Test_ID");

    }

    @Test
    public void insertUser_200OK() throws Exception {
        String url  = "/user/insertUser";

        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                .content(gson.toJson(insertTestUser)))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                //.andExpect(jsonPath())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());

    }

    @Test
    public void insertUser_409Conflict() throws Exception {
        String url  = "/user/insertUser";

        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                .content(gson.toJson(testUser)))
                .andExpect(status().isConflict())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print());

    }

    @Test
    public void getUser_200OK() throws Exception {
        String user_id  = testUser.getUserId();
        String url      = "/user/getUser/"+user_id;

        mvc.perform(get(url)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                //.andExpect(jsonPath())
                .andExpect(jsonPath("$.User", notNullValue()))
                .andDo(print());

    }

    @Test
    public void updateUser_200OK() throws Exception {
        String user_id  = testUser.getUserId();
        String url      = "/user/updateUser/"+user_id;

        UserDetail userDetail = new UserDetail();

        userDetail.setUserId(user_id);
        userDetail.setStatus("not ok");
        userDetail.setAddress("test_Addr");
        userDetail.setAddressDetail("test_Addr_Detail");
        userDetail.setTellPhone("22222222222");
        userDetail.setZipCode("222222");
        userDetail.setAdminYn("N");

        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                .content(gson.toJson(userDetail)))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                //.andExpect(jsonPath())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());

    }

    @Test
    public void updateUser_400BadRequest() throws Exception {
        String user_id  = notFoundTestUser.getUserId();
        String url      = "/user/updateUser/"+user_id;

        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                .content(gson.toJson(notFoundTestUser)))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print());

    }

    @Test
    public void getUserAndOrgs_200OK() throws Exception {
        String user_id  = testUser.getUserId();
        String url      = "/user/getUserAndOrgs/"+user_id;

        mvc.perform(get(url)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.Orgs", notNullValue()))
                .andExpect(jsonPath("$.User", notNullValue()))
                .andDo(print());
    }

    @Test
    public void getUserAndOrgs_400BadRequest() throws Exception {
        String user_id  = notFoundTestUser.getUserId();
        String url      = "/user/getUserAndOrgs/"+user_id;

        mvc.perform(get(url)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken))
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print());
    }

    @Test
    public void getUserCount() throws  Exception {
        String url  = "/user/getUserCount";

        mvc.perform(get(url)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateUserEmail_200OK() throws Exception {

        String userId = testUser.getUserId();
        String url = "/user/updateUserEmail/"+userId;

        Map<String, String> body = new HashMap<>();
        body.put("userId","new"+userId);

        mvc.perform(put(url)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(body)))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().isOk())
                .andDo(print());

        //convert
        userId = testUser.getUserId();
        url = "/user/updateUserEmail/"+"new"+userId;

        body = new HashMap<>();
        body.put("userId", userId);

        mvc.perform(put(url)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(body)))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateUserEmail_400BadRequest() throws Exception {
        String userId = notFoundTestUser.getUserId();
        String url = "/user/updateUserEmail/"+userId;

        Map<String, String> body = new HashMap<>();
        body.put("userId","new"+userId);

        mvc.perform(put(url)
                .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(body)))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void updateUserEmail_403Conflict() throws Exception {
        String userId = testUser.getUserId();
        String url = "/user/updateUserEmail/"+userId;

        Map<String, String> body = new HashMap<>();
        body.put("userId", userId);

        mvc.perform(put(url)
                    .header(AUTHORIZATION_HEADER_KEY, adminTestToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(body)))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    public void updateUserPassword_200OK() throws Exception {
        String url = "/user/updateUserPassword/"+testId;
        String oldPassword = testPassword;
        String newPassword = testPassword+"1234";

        Map<String, String> body = new HashMap<>();
        body.put("oldPassword", oldPassword);
        body.put("password", newPassword);

        mvc.perform(put(url)
                .header(AUTHORIZATION_HEADER_KEY, testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(body)))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().isOk())
                .andDo(print());

        mvc.perform(put(url)
                .header(AUTHORIZATION_HEADER_KEY, testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(body)))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        CloudCredentials clientCredentials = new CloudCredentials(testId, newPassword);
        testToken = new CloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true).login().getValue();

        // revert
        body = new HashMap<>();
        body.put("id", testId);
        body.put("oldPassword", newPassword);
        body.put("password", oldPassword);
        mvc.perform(put(url)
                .header(AUTHORIZATION_HEADER_KEY, testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(body)))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().isOk())
                .andDo(print());


    }

    // 테스트 후에 삭제한 유저를 다시 돌려놔야하는데 아직 register 기능이 구현되지 않았음. 차후에 적용.
    @Test
    @Ignore
    public void deleteUser_200OK() throws Exception {
        String url = "/user/deleteUser/"+"lij";
        String password = "lij";

        Map<String, String> body = new HashMap<>();
        body.put("password", password);

        mvc.perform(put(url)
                .header(AUTHORIZATION_HEADER_KEY, testToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(body)))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(status().isOk())
                .andDo(print());


//        CloudCredentials clientCredentials = new CloudCredentials(new DefaultOAuth2AccessToken(adminTestToken));
//        CloudFoundryClient adminCfc = new CloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true);

//        adminCfc.register(testId, testPassword);

    }

    @Test
    public void getListForTheUser_200_ManagedOrganizations() throws Exception {

        String keyOfRole = "managed_organizations";
        String url = "/user/getListForTheUser/"+keyOfRole;

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, testToken))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getListForTheUser_200_BillingManagedOrganizations() throws Exception {

        String keyOfRole = "billing_managed_organizations";
        String url = "/user/getListForTheUser/"+keyOfRole;

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, testToken))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getListForTheUser_200_AuditedOrganizations() throws Exception {

        String keyOfRole = "audited_organizations";
        String url = "/user/getListForTheUser/"+keyOfRole;

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, testToken))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getListForTheUser_200_Organizations() throws Exception {

        String keyOfRole = "organizations";
        String url = "/user/getListForTheUser/"+keyOfRole;

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, testToken))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getListForTheUser_200_ManagedSpaces() throws Exception {

        String keyOfRole = "managed_spaces";
        String url = "/user/getListForTheUser/"+keyOfRole;

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, testToken))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getListForTheUser_200_Spaces() throws Exception {

        String keyOfRole = "spaces";
        String url = "/user/getListForTheUser/"+keyOfRole;

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, testToken))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void getListForTheUser_200_AuditedSpaces() throws Exception {

        String keyOfRole = "audited_spaces";
        String url = "/user/getListForTheUser/"+keyOfRole;

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, testToken))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getListForTheUser_400_InvalidParam() throws Exception {

        String keyOfRole = "invalidParam";
        String url = "/user/getListForTheUser/"+keyOfRole;

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, testToken))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Ignore
    public void updateAuthUserPassword() throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, Object> body = new HashMap<>();

            body.put("username", "test");
            body.put("userId", "test");
            body.put("newPassword", "test1");

            mvc.perform(post("/user/authResetPassword")
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(body)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Ignore
    public void regiterUser() throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, Object> body = new HashMap<>();

            body.put("userId", "test");
            body.put("password", "test");
            body.put("username" , "test");
            mvc.perform(post("/user/authAddUser")
                    .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(body)))
                    .andExpect(status().isOk())
                    .andDo(print());

        }catch(Exception e){
            e.printStackTrace();
        }
    }


}