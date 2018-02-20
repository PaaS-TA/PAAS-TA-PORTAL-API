package org.openpaas.paasta.portal.api.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * org.openpaas.paasta.portal.api.service
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration()
@Transactional("portalTransactionManager")
public class MenuServiceTest extends CommonTest {

    private static String testUserId = "";
    private static Menu testInitParam = null;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MenuService menuService;


    @Before
    public void setUp() throws Exception {
        menuService.insertMenu(testInitParam);
    }


    @BeforeClass
    public static void init() throws Exception {
        testUserId = getPropertyValue("test.clientUserName");

        testInitParam = new Menu();
        testInitParam.setParentNo(0);
        testInitParam.setSortNo(1);
        testInitParam.setMenuName("test_menu_name");
        testInitParam.setMenuPath("test_menu_path");
        testInitParam.setImagePath("test_image_path");
        testInitParam.setOpenWindowYn(Constants.USE_YN_Y);
        testInitParam.setLoginYn(Constants.USE_YN_Y);
        testInitParam.setUseYn(Constants.USE_YN_Y);
        testInitParam.setDescription("test_menu_description");
        testInitParam.setUserId(testUserId);
    }


    @Test
    public void test_01_getMenuMaxNoList() throws Exception {
        menuService.getMenuMaxNoList(new Menu());
    }


    @Test
    public void test_02_01_getMenuList() throws Exception {
        menuService.getMenuList(new Menu());
    }


    @Test
    public void test_02_02_getMenuList() throws Exception {
        menuService.getMenuList(null);
    }


    @Test
    public void test_03_getMenuDetail() throws Exception {
        menuService.getMenuDetail(new Menu() {{
            setNo(1);
        }});
    }


    @Test
    public void test_04_insertMenu() throws Exception {
        Menu testParam = new Menu();

        testParam.setParentNo(1);
        testParam.setSortNo(1);
        testParam.setMenuName("test_insert_menu_name");
        testParam.setMenuPath("test_insert_menu_path");
        testParam.setImagePath("test_insert_image_path");
        testParam.setOpenWindowYn(Constants.USE_YN_Y);
        testParam.setLoginYn(Constants.USE_YN_Y);
        testParam.setUseYn(Constants.USE_YN_Y);
        testParam.setDescription("test_insert_menu_description");
        testParam.setUserId(testUserId);

        menuService.insertMenu(testParam);
    }


    @Test
    public void test_05_updateMenu() throws Exception {
        Menu testParam = new Menu();

        testParam.setParentNo(1);
        testParam.setSortNo(2);
        testParam.setMenuName("test_update_menu_name");
        testParam.setMenuPath("test_update_menu_path");
        testParam.setImagePath("test_update_image_path");
        testParam.setOpenWindowYn(Constants.USE_YN_N);
        testParam.setLoginYn(Constants.USE_YN_N);
        testParam.setUseYn(Constants.USE_YN_N);
        testParam.setDescription("test_update_menu_description");
        testParam.setUserId(testUserId);

        menuService.updateMenu(testParam);
    }


    @Test
    public void test_06_deleteMenu() throws Exception {
        menuService.deleteMenu(new Menu() {{
            setNo(1);
        }});
    }


    @Test
    public void test_07_getUserMenuList() throws Exception {
        menuService.getUserMenuList();
    }

}