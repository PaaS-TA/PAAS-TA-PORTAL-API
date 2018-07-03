package org.openpaas.paasta.portal.api.config;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

//@ActiveProfiles("local")
//@TestPropertySource(properties = {"eureka.client.enabled=false"}) //Local용
@ActiveProfiles("dev")
@TestPropertySource(properties = {"spring.config.location = classpath:/application.yml","eureka.client.enabled=false"}) // Push 용
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestConfig.class);
}


