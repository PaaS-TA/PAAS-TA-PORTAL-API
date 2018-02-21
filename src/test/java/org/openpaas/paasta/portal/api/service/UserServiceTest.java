package org.openpaas.paasta.portal.api.service;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

/**
 * Created by ijlee on 2016-08-31.
 */
//@Ignore

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(defaultRollback = false)
@Transactional("portalTransactionManager")

public class UserServiceTest extends CommonTest {

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    GlusterfsServiceImpl glusterfsService;

    private MockMvc mvc;

    // token
    private static String adminTestToken = "";

    // cf user
    private static String adminTestId = getPropertyValue("test.admin.id");
    private static String adminTestPassword = getPropertyValue("test.admin.password");
    private static CloudCredentials credentials = new CloudCredentials(adminTestId, adminTestPassword);

    @BeforeClass
    public static void a_init() throws Exception {
        adminTestToken = new CloudFoundryClient(credentials, getTargetURL(apiTarget), true).login().getValue();
    }

    @Test
//  @Ignore
    public void a_createUser() throws Exception {

        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(getPropertyValue("test.insetTestId"));
        userDetail.setPassword(getPropertyValue("test.password"));
        userDetail.setStatus("0");
        userService.createUser(userDetail);
    }

    @Test
//  @Ignore
    public void a_create() throws Exception {

        HashMap userDetail = new HashMap();
        userDetail.put("userId", getPropertyValue("test.insetTestId"));
        userDetail.put("password", getPropertyValue("test.password"));
        userService.create(userDetail);

    }

    @Test
    public void b_updateUser() throws Exception {
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(getPropertyValue("test.insetTestId"));
        userDetail.setPassword(getPropertyValue("test.newPassword"));
        userDetail.setAdminYn("N");
        userDetail.setStatus("1");
        userService.updateUser(userDetail.getUserId(), userDetail);
    }

    @Test
    public void updateUserPassword() throws Exception {

        CloudCredentials re_passwordCredentials = new CloudCredentials(getPropertyValue("test.insetTestId"), getPropertyValue("test.newPassword"));
        userService.updateUserPassword(new CustomCloudFoundryClient(re_passwordCredentials, getTargetURL(apiTarget), true), re_passwordCredentials, getPropertyValue("test.password"));

    }

    @Test
    public void updateUserId() throws Exception {
        userService.updateUserId(getPropertyValue("test.insetTestId"), getPropertyValue("test.updateUserId"));
        userService.updateUserId(getPropertyValue("test.updateUserId"), getPropertyValue("test.insetTestId"));
    }


    @Test
    public void getUser() throws Exception {
        userService.getUser(getPropertyValue("test.insetTestId"));
    }

    @Test
    public void getUserCount() throws Exception {
        userService.getUserCount();
    }

    @Test
    public void isExist() throws Exception {
        userService.isExist(getPropertyValue("test.insetTestId"));
    }


    @Test
    public void getListForTheUser() throws Exception {
        userService.getListForTheUser("managed_organizations", adminTestToken);
    }

    @Test
    public void sendEmail() throws Exception {
        HashMap map = new HashMap();
        map.put("resetPassword", getPropertyValue("test.userId"));
        map.put("sFile", getPropertyValue("test.resetPassword.html"));
        map.put("contextUrl", "user/authPassword");
        userService.sendEmail(map);
    }

    @Test
    @Ignore
    public void createRequestUser() throws Exception {
        HashMap userDetail = new HashMap();
        userDetail.put("userId", getPropertyValue("test.emailId"));
        userService.createRequestUser(userDetail);
        userService.deleteUser(getPropertyValue("test.emailId"));

    }

    @Test
    public void getUserDetailInfo() throws Exception {
        HashMap userDetail = new HashMap();
        userDetail.put("userId", getPropertyValue("test.insetTestId"));
        userService.getUserDetailInfo(userDetail);
    }


    @Test
    public void authAddUser() throws Exception {
        HashMap userDetail = new HashMap();
        userDetail.put("userId", getPropertyValue("test.insetTestId"));
        userDetail.put("searchUserId", getPropertyValue("test.insetTestId"));
        userService.authAddUser(userDetail);
    }

    @Test
    public void authAddAccessCnt() throws Exception {
        HashMap userDetail = new HashMap();
        userDetail.put("userId", getPropertyValue("test.insetTestId"));
        userService.authAddAccessCnt(userDetail);
    }

    @Test
    public void updateAuthUserPassword() throws Exception {
        HashMap userDetail = new HashMap();
        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        CustomCloudFoundryClient adminCcfc = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);
        userDetail.put("userId", getPropertyValue("test.insetTestId"));
        userDetail.put("newPassword", getPropertyValue("test.newPassword"));
        userService.updateAuthUserPassword(adminCcfc, userDetail);
    }

    @Test
    public void resetPassword() throws Exception {
        HashMap map = new HashMap();
        map.put("userId", getPropertyValue("test.insetTestId"));
        map.put("searchUserId", getPropertyValue("test.insetTestId"));

        userService.resetPassword(map);
    }

    @Test
    public void uploadFile() throws Exception {
        File file = new File("./src/test/java/resources/images/test.jpg");

        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        String path = null;

        Map<String, Object> map = userService.uploadFile(multipartFile);
        path = (String) map.getOrDefault("path", "");
        System.out.println("path:" + path);
        glusterfsService.delete(path);
    }

    @Test
    public void getUserInfo_OK() throws Exception {
        String userName = getPropertyValue("test.testUser");
        String userPassword = getPropertyValue("test.testUserPassword");

        HashMap map = new HashMap();
        map.put("userId", userName);
        map.put("password", userPassword);

        List<Map<String, String>> userInfo = userService.getUserInfo();

        Predicate<Map> predicate = user -> user.get("username").equals(userName);
        assertTrue(userInfo.stream().anyMatch(predicate));
    }

    @Test
    public void z_deleteUser1() throws Exception {
        HashMap map = new HashMap();
        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        CustomCloudFoundryClient adminCcfc = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);
        CloudCredentials userCredentials = new CloudCredentials(getPropertyValue("test.insetTestId"), getPropertyValue("test.password"));
        CustomCloudFoundryClient userCcfc = new CustomCloudFoundryClient(userCredentials, getTargetURL(apiTarget), true);
        map.put("userId", getPropertyValue("test.insetTestId"));
        userService.deleteUser(adminCcfc, userCcfc, getPropertyValue("test.insetTestId"));
    }

    @Test
    public void z_deleteUser2() throws Exception {
        userService.deleteUser(getPropertyValue("test.insetTestId"));
    }

    @Test
    @Ignore
    public void createRequestUserCnt() throws Exception {
        Map userDetail = new HashMap();
        userDetail.put("userId", getPropertyValue("test.emailId"));
        userService.createUserAdd(userDetail);
        userService.deleteUser(getPropertyValue("test.emailId"));

    }
}