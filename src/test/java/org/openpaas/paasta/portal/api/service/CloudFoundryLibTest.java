package org.openpaas.paasta.portal.api.service;

import com.amazonaws.util.json.JSONArray;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.openpaas.paasta.portal.api.common.CommonTest.getPropertyValue;

/**
 * Created by mg on 2016-05-02.
 */
@Ignore
public class CloudFoundryLibTest {

    private CloudFoundryClient cfc;
    private OAuth2AccessToken token;
    private URL targetUrl;

    @Before
    public void init() throws IOException {

        targetUrl = new URL(getPropertyValue("test.apiTarget"));

        CloudCredentials credentials = new CloudCredentials(getPropertyValue("test.clientUserName"), getPropertyValue("test.clientUserPassword"));

        token = new CloudFoundryClient(credentials, targetUrl, true).login();
        cfc = new CloudFoundryClient(new CloudCredentials(new DefaultOAuth2AccessToken(token)), targetUrl, true);

    }

    @Test
    public void cfTarget() {
        System.out.println("");
    }

    @Test
    public void getSpaces() {

        System.out.println("\ncfc.getSpaces():\n");

        List<CloudSpace> csList = cfc.getSpaces();

        JSONArray ja = new JSONArray(csList);

        System.out.println("jsonArray: \n" + ja);
/*

        for (int i = 0; i < csList.size(); i++ ) {
            System.out.println("List< CloudSpace > index (" + i + ") "+ csList.get(i).getName());
        }
*/

    }

    @Test
    public void createService() {
        CloudService cs = new CloudService();
        cs.setLabel("delivery-pipeline");
        cs.setPlan("utf8");
        cs.setName("client-test-pipeline");
        cfc.createService(cs);
    }

    @Test
    public void getServices() {

        List<CloudService> csList = cfc.getServices();

        System.out.println("Service List : \n");

        for (CloudService cs : csList) {
            System.out.println("name : " + cs.getName());
        }


    }

    @Test
    public void renameOrg() {

    }

    @Test
    public void updatePassword() throws Exception {
        String username = "password-change-test";
        String password = "12345";

        CustomCloudFoundryClient cfc1 = new CustomCloudFoundryClient(new CloudCredentials(username, password), targetUrl, true);
        cfc1.login();
        password = "1234";
        cfc1.updatePassword(password);
        CustomCloudFoundryClient cfc2 = new CustomCloudFoundryClient(new CloudCredentials(username, password), targetUrl, true);
        cfc2.login();
        password = "12345";
        cfc2.updatePassword(password);
    }

    @Test
    public void deleteUser() throws Exception {
    }

}