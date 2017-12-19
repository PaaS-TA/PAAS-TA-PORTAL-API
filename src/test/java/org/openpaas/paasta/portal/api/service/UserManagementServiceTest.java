package org.openpaas.paasta.portal.api.service;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.UserDetail;
import org.openpaas.paasta.portal.api.model.UserManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * org.openpaas.paasta.portal.api.service
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.08.31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ApiApplication.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration()
@Transactional("portalTransactionManager")
public class UserManagementServiceTest extends CommonTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private UserManagementService userManagementService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private UserService userService;

    private static String testUserId = "";


    @Before
    public void setUp() throws Exception {
        this.createUserAccount(testUserId);
    }


    @BeforeClass
    public static void init() throws Exception {
        testUserId = getPropertyValue("test.user.id.email.account");
    }


    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }


    @After
    public void tearDown() throws Exception {
        this.deleteUserAccount(testUserId);
    }


    @Test
    public void test_01_01_getUserInfoList() throws Exception {
        userManagementService.getUserInfoList(new UserManagement() {{
            setPageNo(Constants.PAGE_NO);
            setPageSize(Constants.PAGE_SIZE);
        }});
    }


    @Test
    public void test_01_02_checkPageNumber_getUserInfoList() throws Exception {
        userManagementService.getUserInfoList(new UserManagement() {{
            setPageNo(0);
            setPageSize(0);
        }});
    }


    @Test
    public void test_02_setResetPassword() throws Exception {
        userManagementService.setResetPassword(new UserManagement() {{
            setUserId(testUserId);
        }});
    }


    @Test
    public void test_03_01_updateOperatingAuthority() throws Exception {
        userManagementService.updateOperatingAuthority(new UserManagement() {{
            setUserId(testUserId);
        }});
    }


    @Test
    public void test_03_02_updateOperatingAuthority() throws Exception {
        userManagementService.updateOperatingAuthority(new UserManagement() {{
            setUserId(getPropertyValue("test.adminUserName"));
        }});
    }


    @Test
    public void test_04_deleteUserAccount() throws Exception {
        userManagementService.deleteUserAccount(new UserManagement() {{
            setUserId(testUserId);
        }});
    }


    public void createUserAccount(String reqUserId) throws Exception {
        userService.createUser(new UserDetail() {{
            setUserId(reqUserId);
            setStatus("1");
            setAdminYn(Constants.USE_YN_Y);
        }});
    }


    public void deleteUserAccount(String reqUserId) throws Exception {
        userManagementService.deleteUserAccount(new UserManagement() {{
            setUserId(reqUserId);
        }});
    }
}