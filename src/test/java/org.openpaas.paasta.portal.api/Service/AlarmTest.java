package org.openpaas.paasta.portal.api.Service;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.openpaas.paasta.portal.api.service.AlarmService;
import org.openpaas.paasta.portal.api.service.MonitoringRestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.UUID;

/**
 * Created by swmoon on 2018-06-25
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = {"spring.cloud.config.enable:false"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AlarmTest {

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

    }

}
