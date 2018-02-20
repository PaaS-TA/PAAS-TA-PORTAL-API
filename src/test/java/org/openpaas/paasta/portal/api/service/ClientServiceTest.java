package org.openpaas.paasta.portal.api.service;

import org.apache.commons.collections.map.HashedMap;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by YJKim on 2016-07-28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional("portalTransactionManager")
public class ClientServiceTest extends Common {

    @Autowired
    ClientService clientService;

    @Test
    public void test01_getClientList() throws Exception {

        Map<String, Object> param = new HashedMap();
        param.put("page_offset", 0);

        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        CustomCloudFoundryClient adminCcfc = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);

        clientService.getClientList(adminCcfc, param);

    }

    @Test
    public void test02_getClient() throws Exception {

        Map<String, Object> param = new HashedMap();
        param.put("client_id", "admin");

        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        CustomCloudFoundryClient adminCcfc = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);

        clientService.getClient(adminCcfc, param);

    }

    @Test
    public void test03_registerUpdateDeleteClient() throws Exception {

        Map<String, Object> param = new HashedMap();
        param.put("client_id", "testRegisterUpdateDelete");
        param.put("client_secret", "testRegisterUpdateDelete");
        param.put("scope", "uaa.none");
        param.put("authorized_grant_types", "client_credentials");

        // Test other options? ///////////////////////////
        // name authorities access_token_validiity refresh_token_validity
        // redirect_uri autoapprove signup_redirect_url
        // clone del_attrs
        ////////////////////////////////////////////////


        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        CustomCloudFoundryClient adminCcfc = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);

        clientService.registerClient(adminCcfc, param);

        clientService.updateClient(adminCcfc, param);

        clientService.deleteClient(adminCcfc, param);

    }

//    @Test (expected = Exception.class)
//    public void test04_exceptionTest() throws Exception {
//
//        Map<String, Object> param = new HashedMap();
//        param.put("page_offset", 0);
//
//        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
//        CustomCloudFoundryClient adminCcfc =  new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);
//
//        clientService.getClientList(adminCcfc, param);
//
//    }


}
