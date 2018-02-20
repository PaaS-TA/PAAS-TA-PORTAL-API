package org.openpaas.paasta.portal.api.service;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.ConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigInfoServiceTest extends CommonTest {

    @Autowired
    private ConfigInfoService configInfoService;

    ConfigInfo configInfo = new ConfigInfo();

    @Test
    public void getValue() throws Exception {

        List<ConfigInfo> list = configInfoService.getValue(configInfo);

        assertTrue(list != null);
    }


    @Test
    public void updateValue() throws Exception {
        configInfo.setName("email_auth_test_yn");
        configInfo.setValue("Y");

        configInfoService.updateValue(configInfo);
    }


}