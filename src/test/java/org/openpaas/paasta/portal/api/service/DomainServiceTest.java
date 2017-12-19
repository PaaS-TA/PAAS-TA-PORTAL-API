package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openpaas.paasta.portal.api.common.CloudFoundryExceptionMatcher;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Dojun on 2016-07-25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {ApiApplication.class})
public class DomainServiceTest extends CommonTest {

    private static CustomCloudFoundryClient admin;
    private static CustomCloudFoundryClient client;
    private static String token = "";
    private static URL targetUrl;
    private static String testOrg = "junit-domain-test-org";
    private static String testSpace = "junit-domain-test-space";
    private static String testDomainName = "test.domain.xip.io";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Autowired
    private DomainService domainService;

    @BeforeClass
    public static void init() throws Exception {

        targetUrl = new URL(getPropertyValue("test.apiTarget"));
        String username = getPropertyValue("test.clientUserName");

        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        admin = new CustomCloudFoundryClient(adminCredentials, targetUrl, true);

        CloudCredentials credentials = new CloudCredentials(username, "1234");
        token = new CustomCloudFoundryClient(credentials, targetUrl, true).login().getValue();
        client = new CustomCloudFoundryClient(credentials, targetUrl, true);

        client.createOrg(testOrg);
        client.createSpace(testOrg, testSpace);

    }

    @AfterClass
    public static void testFinalize() throws Exception {
        client.deleteSpace(testOrg, testSpace);
        admin.deleteOrg(testOrg);

    }

    @Test
    public void getDomains_All_TEST() throws Exception {
        List<CloudDomain> result = domainService.getDomains(token, "all");
        assertTrue(result.size() > 0);
    }

    @Test
    public void getDomains_Private_TEST() throws Exception {
        domainService.addDomain(token, testOrg, testSpace, testDomainName);
        List<CloudDomain> result;
        try {
            result = domainService.getDomains(token, "private");
        } finally {
            domainService.deleteDomain(token, testOrg, testSpace, testDomainName);
        }

        assertTrue(result.size() > 0);
    }

    @Test
    public void getDomains_Shared_TEST() throws Exception {
        List<CloudDomain> result = domainService.getDomains(token, "shared");
        assertTrue(result.size() > 0);
    }

    @Test
    public void getDomains_InvalidStatus_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Invalid status"));

        domainService.getDomains(token, "invalidStatus");
    }

    @Test
    public void getDomains_EmptyStatus_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Invalid status"));

        domainService.getDomains(token, "");
    }

    @Test
    public void getDomains_InvalidToken_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.UNAUTHORIZED, "Invalid Auth Token"));

        domainService.getDomains("invalidToken", "all");

    }

    @Test
    public void addDomain_TEST() throws Exception {

        boolean result;

        try {
            result = domainService.addDomain(token, testOrg, testSpace, testDomainName);
        } finally {
            domainService.deleteDomain(token, testOrg, testSpace, testDomainName);
        }
        assertTrue(result);
    }

    @Test
    public void addDomain_DuplicateDomain_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "The domain is invalid: name can contain multiple subdomains, each having only alphanumeric characters and hyphens of up to 63 characters, see RFC 1035."));

        domainService.addDomain(token, testOrg, testSpace, "invalid-domain");
    }

    @Test
    public void addDomain_InvalidDomain_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "The domain is invalid: name can contain multiple subdomains, each having only alphanumeric characters and hyphens of up to 63 characters, see RFC 1035."));

        domainService.addDomain(token, testOrg, testSpace, "invalid-domain");
    }

    @Test
    public void addDomain_EmptyParam_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        domainService.addDomain(token, "", "", testDomainName);
    }

    @Test
    public void addDomain_NullParam_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        domainService.addDomain(token, null, null, testDomainName);
    }


    @Test
    public void deleteDomain_TEST() throws Exception {
        domainService.addDomain(token, testOrg, testSpace, testDomainName);
        boolean result = domainService.deleteDomain(token, testOrg, testSpace, testDomainName);

        assertTrue(result);
    }

    @Test
    public void deleteDomain_NonexistentDomain_TEST() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Domain 'nonexistent-domain' not found.");

        domainService.deleteDomain(token, testOrg, testSpace, "nonexistent-domain");
    }

    @Test
    public void deleteDomain_EmptyParam_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        domainService.deleteDomain(token, "", "", "");
    }

    @Test
    public void deleteDomain_NullParam_TEST() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        domainService.deleteDomain(token, null, null, null);
    }

}
