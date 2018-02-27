package org.openpaas.paasta.portal.api.service;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.AdminMain;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * org.openpaas.paasta.portal.api.service
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration()
//@Transactional("ccTransactionManager")
public class AdminMainServiceTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private AdminMainService adminMainService;


    @Test
    public void test_01_getTotalCountList() throws Exception {
        //adminMainService.getTotalCountList(new AdminMain());
    }


    @Test
    public void test_02_getTotalOrganizationList() throws Exception {
        //adminMainService.getTotalOrganizationList(new AdminMain());
    }


    @Test
    public void test_03_getTotalSpaceList() throws Exception {
        AdminMain testSelectParam = new AdminMain();
        testSelectParam.setOrganizationId(1);

        //adminMainService.getTotalSpaceList(testSelectParam);
    }
}