package org.openpaas.paasta.portal.api.service;

import org.apache.commons.collections.map.HashedMap;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.org.codehaus.jackson.JsonParseException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CloudFoundryExceptionMatcher;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.Service;
import org.openpaas.paasta.portal.api.model.ServiceBroker;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceServiceTest extends CommonTest {

    private static CloudFoundryClient clientAdmin;
    private static CustomCloudFoundryClient clientAdminCustom;
    private static CustomCloudFoundryClient customClient;
    private static CloudFoundryClient client;
    private static String customClientToken;
    private static UUID userProvidedInstanceGuid;
    private static String apiTarget = "";
    private static String serviceTestOrg = "";
    private static String serviceTestSpace = "";
    private static String userProvidedInstanceName = "";
    private static String createTestUP = "";
    private static String serviceBrokerName = "";
    private static String serviceBrokerUsername = "";
    private static String serviceBrokerPassword = "";
    private static String serviceBrokerUrl = "";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Autowired
    private ServiceService serviceService;

    @BeforeClass
    public static void init() throws Exception {

        Properties properties = new Properties();
        InputStream source = OrgServiceTest.class.getClassLoader().getResourceAsStream("config.properties");
        properties.load(source);

        apiTarget = getPropertyValue("test.apiTarget");
        serviceTestOrg = getPropertyValue("test.serviceTestOrg");
        serviceTestSpace = getPropertyValue("test.serviceTestSpace");
        userProvidedInstanceName = getPropertyValue("test.userProvidedInstanceName");
        createTestUP = getPropertyValue("test.createTestUP");

        serviceBrokerName = getPropertyValue("test.serviceBrokerName");
        serviceBrokerUsername = getPropertyValue("test.serviceBrokerUsername");
        serviceBrokerPassword = getPropertyValue("test.serviceBrokerPassword");
        serviceBrokerUrl = getPropertyValue("test.serviceBrokerUrl");

        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        clientAdminCustom = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), "OCP", "dev", true);
        clientAdminCustom.login();

        clientAdmin = new CloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);
        clientAdmin.login();

        CloudCredentials clientCredentials = new CloudCredentials(getPropertyValue("test.clientUserName"), getPropertyValue("test.clientUserPassword"));
        customClientToken = new CustomCloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true).login().getValue();
        customClient = new CustomCloudFoundryClient(clientCredentials, getTargetURL(apiTarget), true);
        customClient.createOrg(serviceTestOrg);
        customClient.createSpace(serviceTestOrg, serviceTestSpace);
        customClient.setSpaceRole(serviceTestOrg, serviceTestSpace, "junit-test-user", "developers");

        customClient = new CustomCloudFoundryClient(clientCredentials, getTargetURL(apiTarget), serviceTestOrg, serviceTestSpace, true);

        CloudService service = new CloudService();
        service.setName(userProvidedInstanceName);
        Map<String, Object> credentials = new HashedMap();
        credentials.put("testCredentials", "testValue");

        customClient.createUserProvidedService(service, credentials);
        userProvidedInstanceGuid = customClient.getUserProvidedServiceInstanceGuid(serviceTestOrg, serviceTestSpace, userProvidedInstanceName);

    }

    @AfterClass
    public static void testFinalize() throws Exception {
        customClient.deleteInstanceService(userProvidedInstanceGuid);
        clientAdminCustom.deleteSpace(serviceTestOrg, serviceTestSpace);
        clientAdminCustom.deleteOrg(serviceTestOrg);
    }

    @Test
    public void getInstanceService() throws Exception {

        Service service = new Service();
        service.setGuid(userProvidedInstanceGuid);

        serviceService.getServiceInstance(service, clientAdmin);
    }

    @Test
    public void renameInstanceService() throws Exception {

        Service service = new Service();
        service.setGuid(userProvidedInstanceGuid);
        service.setNewName(userProvidedInstanceName);

        serviceService.renameInstanceService(service, clientAdminCustom);
    }

    @Test
    public void renameInstanceUserProvidedService() throws Exception {

        Service service = new Service();
        service.setGuid(userProvidedInstanceGuid);
        service.setNewName("test-ups");

        boolean result;
        try {
            result = serviceService.renameInstanceService(service, clientAdminCustom);
        } finally {
            service.setNewName(userProvidedInstanceName);
            serviceService.renameInstanceService(service, clientAdminCustom);
        }
        assertTrue(result);
    }


    @Test
    public void deleteInstanceService() throws Exception {

        Service service = new Service();
        service.setGuid(null);

        serviceService.deleteInstanceService(service, clientAdminCustom);

    }

    @Test
    public void createUserProvided_Ok() throws Exception {
        String credentials = "{\"testCredentials\":\"testValue\"}";
        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", createTestUP);
        body.put("credentials", credentials);

        boolean result;
        try {
            result = serviceService.createUserProvided(customClientToken, body);
        } finally {
            UUID serviceInstanceGuid = customClient.getUserProvidedServiceInstanceGuid(serviceTestOrg, serviceTestSpace, createTestUP);
            Service service = new Service();
            service.setGuid(serviceInstanceGuid);
            serviceService.deleteInstanceService(service, customClient);
        }

        assertTrue(result);
    }

    @Test
    public void createUserProvided_Ok_With_SyslogDrain() throws Exception {
        String credentials = "{\"testCredentials\":\"testValue\"}";
        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", createTestUP);
        body.put("credentials", credentials);
        body.put("syslogDrainUrl", "syslogDrainUrl.com");

        boolean result;
        try {
            result = serviceService.createUserProvided(customClientToken, body);
        } finally {
            UUID serviceInstanceGuid = customClient.getUserProvidedServiceInstanceGuid(serviceTestOrg, serviceTestSpace, createTestUP);
            Service service = new Service();
            service.setGuid(serviceInstanceGuid);
            serviceService.deleteInstanceService(service, customClient);
        }

        assertTrue(result);
    }

    @Test
    public void createUserProvided_EmptyParam() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Map<String, String> body = new HashMap<String, String>();
        serviceService.createUserProvided(customClientToken, body);
    }

    @Test
    public void createUserProvided_Param_Contains_Spaces() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("No matching organization and space found for org: invalid org name space: " + serviceTestSpace);

        Map<String, String> body = new HashMap<String, String>();
        String credentials = "{\"testCredentials\":\"testValue\"}";

        body.put("orgName", "invalid org name");
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", createTestUP);
        body.put("credentials", credentials);
        body.put("syslogDrainUrl", "syslogDrainUrl.com");

        serviceService.createUserProvided(customClientToken, body);
    }


    @Test
    public void createUserProvided_Invalid_Credentials() throws Exception {
        expectedException.expect(JsonParseException.class);

        String credentials = "{create : invalid credentials}";
        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", createTestUP);
        body.put("credentials", credentials);
        body.put("syslogDrainUrl", "createtest.com");

        serviceService.createUserProvided(customClientToken, body);
    }


    @Test
    public void getUserProvided_Ok() throws Exception {

        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", userProvidedInstanceName);

        Map<String, Object> result = serviceService.getUserProvided(customClientToken, body);

        assertTrue(result.size() >= 1);
    }

    @Test
    public void getUserProvided_EmptyPram() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Map<String, String> body = new HashMap<String, String>();

        serviceService.getUserProvided(customClientToken, body);
    }

    @Test
    public void getUserProvided_Nonexistent_ServiceInstanceName() throws Exception {
        expectedException.expect(NoSuchElementException.class);
        expectedException.expectMessage("No value present");

        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", "nonexistent-instance-name");

        serviceService.getUserProvided(customClientToken, body);
    }

    @Test
    public void getUserProvided_Nonexistent_OrgName() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("No matching organization and space found for org: nonexistent-org space: " + serviceTestSpace);

        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", "nonexistent-org");
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", "invalid-instance-name");

        serviceService.getUserProvided(customClientToken, body);
    }

    @Test
    public void getUserProvided_Param_Contains_Space() throws Exception {
        expectedException.expect(NoSuchElementException.class);
        expectedException.expectMessage("No value present");

        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", "invalid instance name");

        serviceService.getUserProvided(customClientToken, body);
    }

    @Test
    public void updateUserProvided_Ok() throws Exception {
        String credentials = "{\"testCredentials\":\"testValue\"}";

        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", userProvidedInstanceName);
        body.put("newServiceInstanceName", userProvidedInstanceName);
        body.put("credentials", credentials);
        body.put("syslogDrainUrl", "updatetest.com");

        boolean result = serviceService.updateUserProvided(customClientToken, body);

        assertTrue(result);
    }

    @Test
    public void updateUserProvided_EmptyParam() throws Exception {
        expectedException.expect(CloudFoundryException.class);
        expectedException.expect(new CloudFoundryExceptionMatcher(
                HttpStatus.BAD_REQUEST, "Required request body content is missing"));

        Map<String, String> body = new HashMap<String, String>();

        serviceService.updateUserProvided(customClientToken, body);
    }

    @Test
    public void updateUserProvided_Nonexistent_ServiceInstanceName() throws Exception {
        expectedException.expect(NoSuchElementException.class);
        expectedException.expectMessage("No value present");

        String credentials = "{\"testCredentials\":\"testValue\"}";
        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", "nonexistent-instance-name");
        body.put("newServiceInstanceName", userProvidedInstanceName);
        body.put("credentials", credentials);
        body.put("syslogDrainUrl", "updatetest.com");

        serviceService.updateUserProvided(customClientToken, body);
    }

    @Test
    public void updateUserProvided_Param_Contains_Space() throws Exception {
        expectedException.expect(NoSuchElementException.class);
        expectedException.expectMessage("No value present");

        String credentials = "{\"testCredentials\":\"testValue\"}";
        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", "invalid instance name");
        body.put("newServiceInstanceName", userProvidedInstanceName);
        body.put("credentials", credentials);
        body.put("syslogDrainUrl", "updatetest.com");

        serviceService.updateUserProvided(customClientToken, body);
    }

    @Test
    public void updateUserProvided_Invalid_Credentials() throws Exception {
        expectedException.expect(JsonParseException.class);

        String credentials = "{update : invalid credentials}";
        Map<String, String> body = new HashMap<String, String>();
        body.put("orgName", serviceTestOrg);
        body.put("spaceName", serviceTestSpace);
        body.put("serviceInstanceName", userProvidedInstanceName);
        body.put("newServiceInstanceName", userProvidedInstanceName);
        body.put("credentials", credentials);
        body.put("syslogDrainUrl", "updatetest.com");

        serviceService.updateUserProvided(customClientToken, body);
    }


    @Test
    public void getServiceBrokers() throws Exception {

        ServiceBroker serviceBroker = new ServiceBroker();
        serviceBroker.setName(serviceBrokerName);

        serviceService.getServiceBrokers(serviceBroker, clientAdmin);
    }

    @Test
    public void getServiceBroker() throws Exception {

        ServiceBroker serviceBroker = new ServiceBroker();
        serviceBroker.setName(serviceBrokerName);

        serviceService.getServiceBroker(serviceBroker, clientAdmin);
    }

    @Test
    public void renameServiceBroker() throws Exception {

        ServiceBroker serviceBroker = new ServiceBroker();
        serviceBroker.setName(serviceBrokerName);
        serviceBroker.setNewName(serviceBrokerName);

        serviceService.renameServiceBroker(serviceBroker, clientAdminCustom);
    }

    @Test
    public void updateServiceBroker() throws Exception {

        ServiceBroker serviceBroker = new ServiceBroker();
        serviceBroker.setName(serviceBrokerName);
        serviceBroker.setUsername(serviceBrokerUsername);
        serviceBroker.setPassword(serviceBrokerPassword);
        serviceBroker.setUrl(serviceBrokerUrl);

        serviceService.updateServiceBroker(serviceBroker, clientAdmin);
    }


//    @Test
//    public void createServiceBroker() throws Exception {
//
//        ServiceBroker serviceBroker = new ServiceBroker();
//        serviceBroker.setName(serviceBrokerName);
//        serviceBroker.setUsername(serviceBrokerUsername);
//        serviceBroker.setPassword(serviceBrokerPassword);
//        serviceBroker.setUrl(serviceBrokerUrl);
//
//        serviceService.createServiceBroker(serviceBroker, clientAdmin);
//    }
//
//


//
//    @Test
//    public void z_deleteServiceBroker() throws Exception {
//
//        ServiceBroker serviceBroker = new ServiceBroker();
//        serviceBroker.setName(serviceBrokerName);
//
//        serviceService.deleteServiceBroker(serviceBroker, clientAdmin);
//    }

}
