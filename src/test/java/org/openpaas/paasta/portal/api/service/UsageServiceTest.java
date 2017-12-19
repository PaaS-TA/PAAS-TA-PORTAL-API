package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.Usage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * org.openpaas.paasta.portal.api.service
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ApiApplication.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration()
@Transactional("portalTransactionManager")
public class UsageServiceTest extends CommonTest {

    private static CustomCloudFoundryClient TEST_CUSTOM_ADMIN_CLIENT;
    private static String TEST_ADMIN_TOKEN = "";
    private static String cfAuthorization = "";
    private static String testOrg = "";
    private static String testSpace = "";

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private UsageService usageService;


    @SuppressWarnings("ConstantConditions")
    @BeforeClass
    public static void init() throws Exception {
        String cfApiTarget = getPropertyValue("test.apiTarget");
        cfAuthorization = getPropertyValue("test.cf.authorization");
        testOrg = getPropertyValue("test.org");
        testSpace = getPropertyValue("test.space");

        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        TEST_ADMIN_TOKEN = new CloudFoundryClient(adminCredentials, getTargetURL(cfApiTarget), true).login().getValue();
        TEST_CUSTOM_ADMIN_CLIENT = new CustomCloudFoundryClient(adminCredentials, getTargetURL(cfApiTarget), true);
    }


    @Test
    public void test_01_getUsageOrganizationList() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        usageService.getUsageOrganizationList(req);
    }


    @Test
    public void test_02_getUsageSpaceList() throws Exception {
        // DELETE SPACE
        this.deleteSpace(testOrg, testSpace);

        // DELETE ORG
        this.deleteOrganization(testOrg);

        // CREATE ORG
        TEST_CUSTOM_ADMIN_CLIENT.createOrg(testOrg);

        // CREATE SPACE
        TEST_CUSTOM_ADMIN_CLIENT.createSpace(testOrg, testSpace);

        usageService.getUsageSpaceList(new Usage() {{
            setOrgName(testOrg);
        }}, new MockHttpServletRequest() {{
            addHeader(cfAuthorization, TEST_ADMIN_TOKEN);
        }});

        // DELETE SPACE
        this.deleteSpace(testOrg, testSpace);

        // DELETE ORG
        this.deleteOrganization(testOrg);
    }


    @Test
    public void test_03_getUsageSearchList() throws Exception {
        Usage testParam = new Usage();
        testParam.setOrgGuid(testOrg);
        testParam.setSpaceGuid("all");
        testParam.setFromMonth("201601");
        testParam.setToMonth("201609");

//        usageService.getUsageSearchList(testParam);
    }


    public void deleteSpace(String reqOrgName, String reqSpaceName) throws Exception {
        try {
            TEST_CUSTOM_ADMIN_CLIENT.deleteSpace(reqOrgName, reqSpaceName);

        } catch (Exception e) {
            // TO DO
        }
    }


    public void deleteOrganization(String reqOrgName) throws Exception {
        try {
            TEST_CUSTOM_ADMIN_CLIENT.deleteOrg(reqOrgName);

        } catch (Exception e) {
            // TO DO
        }
    }
}