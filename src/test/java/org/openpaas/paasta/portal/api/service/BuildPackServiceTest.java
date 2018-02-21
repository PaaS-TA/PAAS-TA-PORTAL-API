package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildPackServiceTest extends CommonTest {

    @Autowired
    private BuildPackService buildPackService;
    private static CustomCloudFoundryClient clientAdminCustom;

    public static String apiTarget;
    public static String buildPackGuid;

    private static MockMvc mvc;
    BuildPack buildPack = new BuildPack();


    @BeforeClass
    public static void init() throws Exception {

        apiTarget = getPropertyValue("test.apiTarget");

        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        clientAdminCustom = new CustomCloudFoundryClient(adminCredentials, getTargetURL(apiTarget), true);
        clientAdminCustom.login();

    }


    @Test
    public void getValue() throws Exception {

        Map<String, Object> list = buildPackService.getBuildPacks(buildPack, clientAdminCustom);
        List list2 = (List) list.get("resources");
        Map<String, Object> list3 = (Map<String, Object>) list2.get(0);
        Map<String, Object> list4 = (Map<String, Object>) list3.get("metadata");
        buildPackGuid = list4.get("guid").toString();

        assertTrue(list != null);
    }


    @Test
    public void updateValue() throws Exception {
        Gson gson = new Gson();

        buildPack.setGuid(UUID.fromString(buildPackGuid));
        buildPack.setEnable(true);

        buildPackService.updateBuildPack(buildPack, clientAdminCustom);
    }


}