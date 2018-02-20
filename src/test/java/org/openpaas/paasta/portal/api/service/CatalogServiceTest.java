package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudServiceInstance;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.controller.CatalogController;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.model.Catalog;
import org.openpaas.paasta.portal.api.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpUpgradeHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * org.openpaas.paasta.portal.api.service
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.07.19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration()
@Transactional("portalTransactionManager")
public class CatalogServiceTest extends CommonTest {

    @Autowired
    private WebApplicationContext wac;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    CatalogService catalogService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    ServiceService serviceService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    AppService appService;

    private MockMvc mvc;
    private static Gson gson = new Gson();
    private static CustomCloudFoundryClient TEST_CUSTOM_ADMIN_CLIENT;
    private static CloudFoundryClient TEST_ADMIN_CLIENT;
    private static final String TEST_URL = "/catalog";
    private static String TEST_ADMIN_TOKEN = "";
    private static String testUserId = "";
    private static String cfAuthorization = "";
    private static String testFilePath = "";
    private static String testOrg = "";
    private static String testSpace = "";
    private static String testJavaBuildPack = "";
    private static String testDomainUrl = "";
    private static String testRubyBuildPack = "";
    private static String testEgovBuildPack = "";

    private static final String TEST_COUNT_NAME = "test_count_name";

    private static final String TEST_INSERT_NAME = "test_insert_name";
    private static final String TEST_INSERT_CLASSIFICATION = "test_insert_classification";
    private static final String TEST_INSERT_SUMMARY = "test_insert_summary";
    private static final String TEST_INSERT_DESCRIPTION = "test_insert_description";
    private static final String TEST_INSERT_THUMB_IMG_NAME = "test_insert_thumb_img_name";
    private static final String TEST_INSERT_THUMB_IMG_PATH = "test_insert_thumb_img_path";

    private static final String TEST_UPDATE_NAME = "test_update_name";
    private static final String TEST_UPDATE_CLASSIFICATION = "test_update_classification";
    private static final String TEST_UPDATE_SUMMARY = "test_update_summary";
    private static final String TEST_UPDATE_DESCRIPTION = "test_update_description";
    private static final String TEST_UPDATE_THUMB_IMG_NAME = "test_update_thumb_img_name";
    private static final String TEST_UPDATE_THUMB_IMG_PATH = "test_update_thumb_img_path";

    private static final String TEST_SERVICE_INSTANCE_NAME = "catalog-service-instance-test";

    private static final String TEST_APP_NAME = "catalog-test-app";

    private static Catalog testInitCatalog = null;
    private static Catalog testSelectCatalog = null;
    private static Catalog testInsertCatalog = null;
    private static Catalog testUpdateCatalog = null;

    private static List<Integer> servicePackCategoryNoList = new ArrayList<>();


    @Before
    public void setUp() throws Exception {
        catalogService.insertBuildPackCatalog(testInitCatalog);
        catalogService.insertServicePackCatalog(testInitCatalog);
        catalogService.insertStarterCatalog(testInitCatalog);

        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        this.commonProcessBeforeTest();
    }

    @SuppressWarnings("ConstantConditions")
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String cfApiTarget = getPropertyValue("test.apiTarget");

        testUserId = getPropertyValue("test.clientUserName");
        cfAuthorization = getPropertyValue("test.cf.authorization");
        testFilePath = getPropertyValue("test.file.path");
        testOrg = getPropertyValue("test.org");
        testSpace = getPropertyValue("test.space");
        testJavaBuildPack = getPropertyValue("test.java.build.pack");
        testDomainUrl = getPropertyValue("test.domain.url");
        testRubyBuildPack = getPropertyValue("test.ruby.build.pack");
        testEgovBuildPack = getPropertyValue("test.egov.build.pack");

        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        TEST_ADMIN_TOKEN = new CloudFoundryClient(adminCredentials, getTargetURL(cfApiTarget), true).login().getValue();
        TEST_ADMIN_CLIENT = new CloudFoundryClient(adminCredentials, getTargetURL(cfApiTarget), true);
        TEST_CUSTOM_ADMIN_CLIENT = new CustomCloudFoundryClient(adminCredentials, getTargetURL(cfApiTarget), true);

        testInitCatalog = new Catalog();
        testInitCatalog.setName("test_name");
        testInitCatalog.setClassification("test_classification");
        testInitCatalog.setSummary("test_summary");
        testInitCatalog.setDescription("test_description");
        testInitCatalog.setBuildPackName("test_build_pack_name");
        testInitCatalog.setServicePackName("test_service_pack_name");
        testInitCatalog.setThumbImgName("test_thumb_img_name");
        testInitCatalog.setThumbImgPath("test_thumb_img_path");
        testInitCatalog.setUseYn(Constants.USE_YN_Y);
        testInitCatalog.setBuildPackCategoryNo(1);

        servicePackCategoryNoList.add(1);
        servicePackCategoryNoList.add(2);
        testInitCatalog.setServicePackCategoryNoList(servicePackCategoryNoList);

        testInitCatalog.setUserId(testUserId);
    }


    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }


    @After
    public void tearDown() throws Exception {
        this.commonProcessAfterTest();
    }


    @Test
    public void test_01_getBuildPackList() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getBuildPackList(req);
    }


    @Test
    public void test_02_getServicePackList() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getServicePackList(req);
    }


    @Test
    public void test_03_getBuildPackCatalog() throws Exception {
        catalogService.getBuildPackCatalogList(new Catalog());
    }


    @Test
    public void test_04_getServicePackCatalog() throws Exception {
        catalogService.getServicePackCatalogList(new Catalog());
    }


    @Test
    public void test_05_01_getBuildPackCatalogCount() throws Exception {
        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getBuildPackCatalogCount(new Catalog() {{
            setName(TEST_COUNT_NAME);
        }}, res);
    }


    @Test
    public void test_05_02_checkDuplicated_getBuildPackCatalogCount() throws Exception {
        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getBuildPackCatalogCount(new Catalog() {{
            setName(testInitCatalog.getName());
        }}, res);
    }


    @Test
    public void test_06_01_getServicePackCatalogCount() throws Exception {
        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getServicePackCatalogCount(new Catalog() {{
            setName(TEST_COUNT_NAME);
        }}, res);
    }


    @Test
    public void test_06_02_checkDuplicated_getServicePackCatalogCount() throws Exception {
        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getServicePackCatalogCount(new Catalog() {{
            setName(testInitCatalog.getName());
        }}, res);
    }


    @Test
    public void test_07_insertBuildPackCatalog() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setName(TEST_INSERT_NAME);
        testInsertCatalog.setClassification(TEST_INSERT_CLASSIFICATION);
        testInsertCatalog.setSummary(TEST_INSERT_SUMMARY);
        testInsertCatalog.setDescription(TEST_INSERT_DESCRIPTION);
        testInsertCatalog.setBuildPackName("test_insert_build_pack_name");
        testInsertCatalog.setThumbImgName(TEST_INSERT_THUMB_IMG_NAME);
        testInsertCatalog.setThumbImgPath(TEST_INSERT_THUMB_IMG_PATH);
        testInsertCatalog.setUseYn(Constants.USE_YN_Y);
        testInsertCatalog.setUserId(testUserId);

        catalogService.insertBuildPackCatalog(testInsertCatalog);
    }


    @Test
    public void test_08_insertServicePackCatalog() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setName(TEST_INSERT_NAME);
        testInsertCatalog.setClassification(TEST_INSERT_CLASSIFICATION);
        testInsertCatalog.setSummary(TEST_INSERT_SUMMARY);
        testInsertCatalog.setDescription(TEST_INSERT_DESCRIPTION);
        testInsertCatalog.setServicePackName("test_insert_service_pack_name");
        testInsertCatalog.setThumbImgName(TEST_INSERT_THUMB_IMG_NAME);
        testInsertCatalog.setThumbImgPath(TEST_INSERT_THUMB_IMG_PATH);
        testInsertCatalog.setUseYn(Constants.USE_YN_Y);
        testInsertCatalog.setUserId(testUserId);

        catalogService.insertServicePackCatalog(testInsertCatalog);
    }


    @Test
    public void test_09_updateBuildPackCatalog() throws Exception {
        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setName(TEST_UPDATE_NAME);
        testUpdateCatalog.setClassification(TEST_UPDATE_CLASSIFICATION);
        testUpdateCatalog.setSummary(TEST_UPDATE_SUMMARY);
        testUpdateCatalog.setDescription(TEST_UPDATE_DESCRIPTION);
        testUpdateCatalog.setBuildPackName("test_update_build_pack_name");
        testUpdateCatalog.setThumbImgName(TEST_UPDATE_THUMB_IMG_NAME);
        testUpdateCatalog.setThumbImgPath(TEST_UPDATE_THUMB_IMG_PATH);
        testUpdateCatalog.setUseYn(Constants.USE_YN_N);
        testUpdateCatalog.setUserId(testUserId);

        catalogService.updateBuildPackCatalog(testUpdateCatalog);
    }


    @Test
    public void test_10_updateServicePackCatalog() throws Exception {
        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setName(TEST_UPDATE_NAME);
        testUpdateCatalog.setClassification(TEST_UPDATE_CLASSIFICATION);
        testUpdateCatalog.setSummary(TEST_UPDATE_SUMMARY);
        testUpdateCatalog.setDescription(TEST_UPDATE_DESCRIPTION);
        testUpdateCatalog.setServicePackName("test_update_service_pack_name");
        testUpdateCatalog.setThumbImgName(TEST_UPDATE_THUMB_IMG_NAME);
        testUpdateCatalog.setThumbImgPath(TEST_UPDATE_THUMB_IMG_PATH);
        testUpdateCatalog.setUseYn(Constants.USE_YN_N);
        testUpdateCatalog.setUserId(testUserId);

        catalogService.updateServicePackCatalog(testUpdateCatalog);
    }


    @Test
    public void test_11_deleteBuildPackCatalog() throws Exception {
        catalogService.deleteBuildPackCatalog(new Catalog());
    }


    @Test
    public void test_12_01_deleteServicePackCatalog() throws Exception {
        catalogService.deleteServicePackCatalog(new Catalog());
    }


    @Test
    public void test_12_02_getCheckDelete() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setNo(99999999);

        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getCheckDeleteBuildPackCatalogCount(testSelectCatalog, res);
        catalogService.getCheckDeleteServicePackCatalogCount(testSelectCatalog, res);
    }


    @Test
    public void test_12_03_checkConflict_getCheckDelete() throws Exception {
        testInsertCatalog = new Catalog();
        testSelectCatalog = new Catalog();

        testInsertCatalog.setName(TEST_INSERT_NAME);
        testInsertCatalog.setClassification(TEST_INSERT_CLASSIFICATION);
        testInsertCatalog.setSummary(TEST_INSERT_SUMMARY);
        testInsertCatalog.setDescription(TEST_INSERT_DESCRIPTION);
        testInsertCatalog.setThumbImgName(TEST_INSERT_THUMB_IMG_NAME);
        testInsertCatalog.setThumbImgPath(TEST_INSERT_THUMB_IMG_PATH);
        testInsertCatalog.setUseYn(Constants.USE_YN_Y);
        testInsertCatalog.setUserId(testUserId);
        testInsertCatalog.setBuildPackCategoryNo(99999999);

        List<Integer> testList = new ArrayList<>();

        testList.add(99999999);
        testInsertCatalog.setServicePackCategoryNoList(testList);

        mvc.perform(post(TEST_URL + "/insertStarterCatalog")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCatalog)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testSelectCatalog.setNo(99999999);

        mvc.perform(post(TEST_URL + "/getCheckDeleteBuildPackCatalogCount")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, TEST_ADMIN_TOKEN)
                .content(gson.toJson(testSelectCatalog)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isConflict())
                .andDo(print());


        mvc.perform(post(TEST_URL + "/getCheckDeleteServicePackCatalogCount")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, TEST_ADMIN_TOKEN)
                .content(gson.toJson(testSelectCatalog)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isConflict())
                .andDo(print());
    }


    @Test
    public void test_13_getStarterNamesList() throws Exception {
        catalogService.getStarterNamesList(new Catalog());
    }


    @Test
    public void test_14_getBuildPackNamesList() throws Exception {
        catalogService.getBuildPackNamesList(new Catalog());
    }


    @Test
    public void test_15_getServicePackNamesList() throws Exception {
        catalogService.getServicePackNamesList(new Catalog());
    }


    @Test
    public void test_16_getOneStarterCatalog() throws Exception {
        catalogService.getOneStarterCatalog(new Catalog() {{
            setNo(catalogService.getStarterCatalogMaxNumber());
        }});
    }


    @Test
    public void test_17_insertStarterCatalog() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setName(TEST_INSERT_NAME);
        testInsertCatalog.setClassification(TEST_INSERT_CLASSIFICATION);
        testInsertCatalog.setSummary(TEST_INSERT_SUMMARY);
        testInsertCatalog.setDescription(TEST_INSERT_DESCRIPTION);
        testInsertCatalog.setThumbImgName(TEST_INSERT_THUMB_IMG_NAME);
        testInsertCatalog.setThumbImgPath(TEST_INSERT_THUMB_IMG_PATH);
        testInsertCatalog.setUseYn(Constants.USE_YN_Y);
        testInsertCatalog.setUserId(testUserId);

        testInsertCatalog.setBuildPackCategoryNo(1);

        servicePackCategoryNoList.add(1);
        servicePackCategoryNoList.add(2);
        testInsertCatalog.setServicePackCategoryNoList(servicePackCategoryNoList);

        catalogService.insertStarterCatalog(testInsertCatalog);
    }


    @Test
    public void test_18_updateStarterCatalog() throws Exception {
        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setNo(1);
        testUpdateCatalog.setName(TEST_UPDATE_NAME);
        testUpdateCatalog.setClassification(TEST_UPDATE_CLASSIFICATION);
        testUpdateCatalog.setSummary(TEST_UPDATE_SUMMARY);
        testUpdateCatalog.setDescription(TEST_UPDATE_DESCRIPTION);
        testUpdateCatalog.setThumbImgName(TEST_UPDATE_THUMB_IMG_NAME);
        testUpdateCatalog.setThumbImgPath(TEST_UPDATE_THUMB_IMG_PATH);
        testUpdateCatalog.setUseYn(Constants.USE_YN_N);
        testUpdateCatalog.setUserId(testUserId);

        testUpdateCatalog.setStarterCategoryNo(1);
        testUpdateCatalog.setBuildPackCategoryNo(1);
        servicePackCategoryNoList.add(1);
        servicePackCategoryNoList.add(2);
        testUpdateCatalog.setServicePackCategoryNoList(servicePackCategoryNoList);

        catalogService.updateStarterCatalog(testUpdateCatalog);
    }


    @Test
    public void test_19_deleteStarterCatalog() throws Exception {
        catalogService.deleteStarterCatalog(new Catalog() {{
            setNo(1);
            setStarterCategoryNo(1);
        }});
    }


    @Test
    public void test_20_01_OK_getStarterCatalogCount() throws Exception {
        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getStarterCatalogCount(new Catalog() {{
            setName(TEST_COUNT_NAME);
        }}, res);
    }


    @Test
    public void test_20_02_Conflict_getStarterCatalogCount() throws Exception {
        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getStarterCatalogCount(new Catalog() {{
            setName(testInitCatalog.getName());
        }}, res);
    }


    @Test
    public void test_21_uploadAndDeleteThumbnailByUrl_200OK() throws Exception {
        String url = "/catalog/uploadThumbnailImage/";
        File file = new File(testFilePath);

        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        MediaType mediaType = new MediaType("multipart", "form-data");

        MvcResult result = mvc.perform(fileUpload(url).file(multipartFile)
                .contentType(mediaType))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CatalogController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andReturn();

        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        String path = (String) json.get("path");
        testSelectCatalog = new Catalog();
        testSelectCatalog.setThumbImgPath(path);

        url = "/catalog/deleteThumbnailImage/";

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCatalog)))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CatalogController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print());

    }


    @Test
    public void test_22_uploadAndDeleteThumbnailByName_200OK() throws Exception {
        String url = "/catalog/uploadThumbnailImage/";
        File file = new File(testFilePath);

        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        MediaType mediaType = new MediaType("multipart", "form-data");

        MvcResult result = mvc.perform(fileUpload(url).file(multipartFile)
                .contentType(mediaType))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CatalogController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andReturn();

        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        String path = (String) json.get("path");
        path = path.substring(path.lastIndexOf("/") + 1);
        testSelectCatalog = new Catalog();
        testSelectCatalog.setThumbImgPath(path);

        url = "/catalog/deleteThumbnailImage/";

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCatalog)))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CatalogController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print());

    }


    @Test
    public void test_30_getCatalogLeftMenuList() throws Exception {
        catalogService.getCatalogLeftMenuList();
    }


    @Test
    public void test_31_getCatalogHistoryList() throws Exception {
        catalogService.getCatalogHistoryList(new Catalog());
    }


    @Test
    public void test_32_getCatalogSpaceList() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCatalogSpaceList(new Catalog() {{
            setOrgName(testOrg);
        }}, req);
    }


    @Test
    public void test_33_getCatalogDomainList() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCatalogDomainList(req);
    }


    @Test
    public void test_34_getCatalogServicePlanList() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);
        testSelectCatalog.setServicePackName(this.procBeforeTestGetServicePackInfo().get("SERVICE_NAME").toString());

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCatalogServicePlanList(testSelectCatalog, req);
    }


    @Test
    public void test_35_getCatalogMultiServicePlanList() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);
        testSelectCatalog.setServicePackName(this.procBeforeTestGetServicePackInfo().get("SERVICE_NAME").toString());

        List<Catalog> testMultiSelectCatalogList = new ArrayList<>();

        Catalog testMultiSelectCatalog1 = new Catalog();

        testMultiSelectCatalog1.setName(this.procBeforeTestGetServicePackInfo().get("SERVICE_NAME").toString());
        testMultiSelectCatalog1.setServicePackName(this.procBeforeTestGetServicePackInfo().get("SERVICE_NAME").toString());

        testMultiSelectCatalogList.add(testMultiSelectCatalog1);

        testSelectCatalog.setServicePlanList(testMultiSelectCatalogList);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCatalogMultiServicePlanList(testSelectCatalog, req);
    }


    @Test
    public void test_36_getCatalogAppList() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCatalogAppList(testSelectCatalog, req);
    }


    @Test
    public void test_37_01_getCheckCatalogApplicationNameExists() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCheckCatalogApplicationNameExists(testSelectCatalog, req, res);
    }


    @Test
    public void test_37_02_checkConflict_getCheckCatalogApplicationNameExists() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);
        testSelectCatalog.setName(TEST_APP_NAME);

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCheckCatalogApplicationNameExists(testSelectCatalog, req, res);
    }


    @Test
    public void test_38_01_getCheckCatalogServiceInstanceNameExists() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCheckCatalogServiceInstanceNameExists(testSelectCatalog, req, res);
    }


    @Test
    public void test_38_02_checkConflict_getCheckCatalogServiceInstanceNameExists() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);
        testSelectCatalog.setServiceInstanceName(TEST_SERVICE_INSTANCE_NAME);

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.getCheckCatalogServiceInstanceNameExists(testSelectCatalog, req, res);
    }


    @Test
    public void test_39_01_getCheckCatalogRouteExists() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);
        testSelectCatalog.setDomainName(testDomainUrl);
        testSelectCatalog.setRouteName("test-false." + testDomainUrl);

        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getCheckCatalogRouteExists(testSelectCatalog, res);
    }


    @Test
    public void test_39_02_checkConflict_getCheckCatalogRouteExists() throws Exception {
        testSelectCatalog = new Catalog();
        testSelectCatalog.setOrgName(testOrg);
        testSelectCatalog.setSpaceName(testSpace);
        testSelectCatalog.setDomainName(testDomainUrl);
        testSelectCatalog.setRouteName("portal-registration-dev." + testDomainUrl);

        MockHttpServletResponse res = new MockHttpServletResponse();

        catalogService.getCheckCatalogRouteExists(testSelectCatalog, res);
    }


    @Test
    public void test_40_getCatalogStarterRelationList() throws Exception {
        catalogService.getCatalogStarterRelationList(new Catalog() {{
            setCatalogNo(1);
        }});
    }


    @Test
    public void test_41_01_executeCatalogStarter() throws Exception {
        Catalog testServicePlan = new Catalog();
        List<Catalog> servicePlanList = new ArrayList<>();

        testServicePlan.setServiceInstanceName(TEST_SERVICE_INSTANCE_NAME);
        testServicePlan.setServicePlan(this.procBeforeTestGetServicePackInfo().get("SERVICE_PLAN_GUID").toString());
        testServicePlan.setAppBindYn(Constants.USE_YN_Y);
        testServicePlan.setParameter("{}");
        servicePlanList.add(testServicePlan);

        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setName(TEST_APP_NAME);
        testUpdateCatalog.setOrgName(testOrg);
        testUpdateCatalog.setSpaceName(testSpace);

        testUpdateCatalog.setBuildPackName(testJavaBuildPack);
        testUpdateCatalog.setHostName(TEST_APP_NAME + "." + testDomainUrl);
        testUpdateCatalog.setAppSampleStartYn(Constants.USE_YN_Y);

        testUpdateCatalog.setAppSampleFilePath(this.procBeforeTestGetBuildPackInfo(testJavaBuildPack).get("APP_SAMPLE_FILE_PATH").toString());
        testUpdateCatalog.setAppName(testUpdateCatalog.getName());

        testUpdateCatalog.setServicePlanList(servicePlanList);

        testUpdateCatalog.setDiskSize(Constants.CREATE_APPLICATION_DISK_SIZE);
        testUpdateCatalog.setMemorySize(Constants.CREATE_APPLICATION_MEMORY_SIZE);

        // DELETE APPLICATION BEFORE TEST
        this.deleteApplication(TEST_APP_NAME, testOrg, testSpace, TEST_APP_NAME + "." + testDomainUrl);

        // DELETE SERVICE INSTANCE
        this.deleteServiceInstance(TEST_SERVICE_INSTANCE_NAME);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        Map<String, Object> resultMap = catalogService.executeCatalogStarter(testUpdateCatalog, req);

        // FOR TEST
        App unbindServiceParam = new App();
        unbindServiceParam.setName(testUpdateCatalog.getAppName());
        unbindServiceParam.setServiceName(testServicePlan.getServiceInstanceName());

        appService.unbindService(unbindServiceParam, TEST_ADMIN_TOKEN);

        List<Catalog> resultList = (List<Catalog>) resultMap.get("SERVICE_INSTANCE_GUID_LIST");

        for (Catalog serviceInstanceGuid : resultList) {
            Service deleteInstanceServiceParam = new Service();
            deleteInstanceServiceParam.setGuid(serviceInstanceGuid.getServiceInstanceGuid());

            // DELETE SERVICE INSTANCE
            serviceService.deleteInstanceService(deleteInstanceServiceParam, TEST_CUSTOM_ADMIN_CLIENT);
        }

        // DELETE APPLICATION
        App deleteAppParam = new App();
        deleteAppParam.setName(testUpdateCatalog.getName());
        deleteAppParam.setOrgName(testUpdateCatalog.getOrgName());
        deleteAppParam.setSpaceName(testUpdateCatalog.getSpaceName());

        appService.deleteApp(deleteAppParam, TEST_ADMIN_TOKEN);

        // DELETE ROUTE
        List<String> tempList = new ArrayList<>();
        tempList.add(testUpdateCatalog.getHostName());
        appService.deleteRoute(testUpdateCatalog.getOrgName(), testUpdateCatalog.getSpaceName(), tempList, TEST_ADMIN_TOKEN);
    }


    @Test
    public void test_41_02_stopApplication_bind_executeCatalogStarter() throws Exception {
        Catalog testServicePlan = new Catalog();
        List<Catalog> servicePlanList = new ArrayList<>();

        testServicePlan.setServiceInstanceName(TEST_SERVICE_INSTANCE_NAME);
        testServicePlan.setServicePlan(this.procBeforeTestGetServicePackInfo().get("SERVICE_PLAN_GUID").toString());
        testServicePlan.setAppBindYn(Constants.USE_YN_Y);
        servicePlanList.add(testServicePlan);

        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setName(TEST_APP_NAME);
        testUpdateCatalog.setOrgName(testOrg);
        testUpdateCatalog.setSpaceName(testSpace);
        testUpdateCatalog.setBuildPackName(testJavaBuildPack);
        testUpdateCatalog.setHostName(TEST_APP_NAME + "." + testDomainUrl);
        testUpdateCatalog.setAppSampleStartYn(Constants.USE_YN_N);
        testUpdateCatalog.setAppSampleFilePath(this.procBeforeTestGetBuildPackInfo(testJavaBuildPack).get("APP_SAMPLE_FILE_PATH").toString());
        testUpdateCatalog.setAppName(testUpdateCatalog.getName());

        testUpdateCatalog.setServicePlanList(servicePlanList);

        testUpdateCatalog.setDiskSize(0);
        testUpdateCatalog.setMemorySize(0);

        // DELETE APPLICATION BEFORE TEST
        this.deleteApplication(TEST_APP_NAME, testOrg, testSpace, TEST_APP_NAME + "." + testDomainUrl);

        // DELETE SERVICE INSTANCE
        this.deleteServiceInstance(TEST_SERVICE_INSTANCE_NAME);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        Map<String, Object> resultMap = catalogService.executeCatalogStarter(testUpdateCatalog, req);

        // FOR TEST
        App unbindServiceParam = new App();
        unbindServiceParam.setName(testUpdateCatalog.getAppName());
        unbindServiceParam.setServiceName(testServicePlan.getServiceInstanceName());

        appService.unbindService(unbindServiceParam, TEST_ADMIN_TOKEN);

        List<Catalog> resultList = (List<Catalog>) resultMap.get("SERVICE_INSTANCE_GUID_LIST");

        for (Catalog serviceInstanceGuid : resultList) {
            Service deleteInstanceServiceParam = new Service();
            deleteInstanceServiceParam.setGuid(serviceInstanceGuid.getServiceInstanceGuid());

            // DELETE SERVICE INSTANCE
            serviceService.deleteInstanceService(deleteInstanceServiceParam, TEST_CUSTOM_ADMIN_CLIENT);
        }

        // DELETE APPLICATION
        App deleteAppParam = new App();
        deleteAppParam.setName(testUpdateCatalog.getName());
        deleteAppParam.setOrgName(testUpdateCatalog.getOrgName());
        deleteAppParam.setSpaceName(testUpdateCatalog.getSpaceName());

        appService.deleteApp(deleteAppParam, TEST_ADMIN_TOKEN);

        // DELETE ROUTE
        List<String> tempList = new ArrayList<>();
        tempList.add(testUpdateCatalog.getHostName());
        appService.deleteRoute(testUpdateCatalog.getOrgName(), testUpdateCatalog.getSpaceName(), tempList, TEST_ADMIN_TOKEN);
    }


    @Test
    public void test_41_03_startApplication_bindNone_executeCatalogStarter() throws Exception {
        Catalog testServicePlan = new Catalog();
        List<Catalog> servicePlanList = new ArrayList<>();

        testServicePlan.setServiceInstanceName(TEST_SERVICE_INSTANCE_NAME);
        testServicePlan.setServicePlan(this.procBeforeTestGetServicePackInfo().get("SERVICE_PLAN_GUID").toString());
        servicePlanList.add(testServicePlan);

        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setName(TEST_APP_NAME);
        testUpdateCatalog.setOrgName(testOrg);
        testUpdateCatalog.setSpaceName(testSpace);
        testUpdateCatalog.setBuildPackName(testJavaBuildPack);
        testUpdateCatalog.setHostName(TEST_APP_NAME + "." + testDomainUrl);
        testUpdateCatalog.setAppSampleStartYn(Constants.USE_YN_Y);
        testUpdateCatalog.setAppSampleFilePath(this.procBeforeTestGetBuildPackInfo(testJavaBuildPack).get("APP_SAMPLE_FILE_PATH").toString());
        testUpdateCatalog.setAppName(testUpdateCatalog.getName());

        testUpdateCatalog.setServicePlanList(servicePlanList);

        // DELETE APPLICATION BEFORE TEST
        this.deleteApplication(TEST_APP_NAME, testOrg, testSpace, TEST_APP_NAME + "." + testDomainUrl);

        // DELETE SERVICE INSTANCE
        this.deleteServiceInstance(TEST_SERVICE_INSTANCE_NAME);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        Map<String, Object> resultMap = catalogService.executeCatalogStarter(testUpdateCatalog, req);

        // FOR TEST
        List<Catalog> resultList = (List<Catalog>) resultMap.get("SERVICE_INSTANCE_GUID_LIST");

        for (Catalog serviceInstanceGuid : resultList) {
            Service deleteInstanceServiceParam = new Service();
            deleteInstanceServiceParam.setGuid(serviceInstanceGuid.getServiceInstanceGuid());

            // DELETE SERVICE INSTANCE
            serviceService.deleteInstanceService(deleteInstanceServiceParam, TEST_CUSTOM_ADMIN_CLIENT);
        }

        // DELETE APPLICATION
        App deleteAppParam = new App();
        deleteAppParam.setName(testUpdateCatalog.getName());
        deleteAppParam.setOrgName(testUpdateCatalog.getOrgName());
        deleteAppParam.setSpaceName(testUpdateCatalog.getSpaceName());

        appService.deleteApp(deleteAppParam, TEST_ADMIN_TOKEN);

        // DELETE ROUTE
        List<String> tempList = new ArrayList<>();
        tempList.add(testUpdateCatalog.getHostName());
        appService.deleteRoute(testUpdateCatalog.getOrgName(), testUpdateCatalog.getSpaceName(), tempList, TEST_ADMIN_TOKEN);
    }


    @Test
    public void test_41_04_stopApplication_bindNone_executeCatalogStarter() throws Exception {
        Catalog testServicePlan = new Catalog();
        List<Catalog> servicePlanList = new ArrayList<>();

        testServicePlan.setServiceInstanceName(TEST_SERVICE_INSTANCE_NAME);
        testServicePlan.setServicePlan(this.procBeforeTestGetServicePackInfo().get("SERVICE_PLAN_GUID").toString());
        servicePlanList.add(testServicePlan);

        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setName(TEST_APP_NAME);
        testUpdateCatalog.setOrgName(testOrg);
        testUpdateCatalog.setSpaceName(testSpace);
        testUpdateCatalog.setBuildPackName(testJavaBuildPack);
        testUpdateCatalog.setHostName(TEST_APP_NAME + "." + testDomainUrl);
        testUpdateCatalog.setAppSampleStartYn(Constants.USE_YN_N);
        testUpdateCatalog.setAppSampleFilePath(this.procBeforeTestGetBuildPackInfo(testJavaBuildPack).get("APP_SAMPLE_FILE_PATH").toString());
        testUpdateCatalog.setAppName(testUpdateCatalog.getName());

        testUpdateCatalog.setServicePlanList(servicePlanList);

        // DELETE APPLICATION BEFORE TEST
        this.deleteApplication(TEST_APP_NAME, testOrg, testSpace, TEST_APP_NAME + "." + testDomainUrl);

        // DELETE SERVICE INSTANCE
        this.deleteServiceInstance(TEST_SERVICE_INSTANCE_NAME);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        Map<String, Object> resultMap = catalogService.executeCatalogStarter(testUpdateCatalog, req);

        // FOR TEST
        List<Catalog> resultList = (List<Catalog>) resultMap.get("SERVICE_INSTANCE_GUID_LIST");

        for (Catalog serviceInstanceGuid : resultList) {
            Service deleteInstanceServiceParam = new Service();
            deleteInstanceServiceParam.setGuid(serviceInstanceGuid.getServiceInstanceGuid());

            // DELETE SERVICE INSTANCE
            serviceService.deleteInstanceService(deleteInstanceServiceParam, TEST_CUSTOM_ADMIN_CLIENT);
        }

        // DELETE APPLICATION
        App deleteAppParam = new App();
        deleteAppParam.setName(testUpdateCatalog.getName());
        deleteAppParam.setOrgName(testUpdateCatalog.getOrgName());
        deleteAppParam.setSpaceName(testUpdateCatalog.getSpaceName());

        appService.deleteApp(deleteAppParam, TEST_ADMIN_TOKEN);

        // DELETE ROUTE
        List<String> tempList = new ArrayList<>();
        tempList.add(testUpdateCatalog.getHostName());
        appService.deleteRoute(testUpdateCatalog.getOrgName(), testUpdateCatalog.getSpaceName(), tempList, TEST_ADMIN_TOKEN);
    }


    @Test
    public void test_42_insertCatalogHistoryStarter() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setCatalogNo(1);
        testInsertCatalog.setUserId(testUserId);

        catalogService.insertCatalogHistoryStarter(testInsertCatalog);
    }


    @Test
    public void test_43_01_war_executeCatalogBuildPack() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setName(TEST_APP_NAME);
        testInsertCatalog.setOrgName(testOrg);
        testInsertCatalog.setSpaceName(testSpace);
        testInsertCatalog.setBuildPackName(testJavaBuildPack);
        testInsertCatalog.setHostName(TEST_APP_NAME + "." + testDomainUrl);
        testInsertCatalog.setAppSampleStartYn(Constants.USE_YN_Y);
        testInsertCatalog.setAppSampleFilePath(this.procBeforeTestGetBuildPackInfo(testJavaBuildPack).get("APP_SAMPLE_FILE_PATH").toString());

        // DELETE APPLICATION BEFORE TEST
        this.deleteApplication(TEST_APP_NAME, testOrg, testSpace, TEST_APP_NAME + "." + testDomainUrl);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.executeCatalogBuildPack(testInsertCatalog, req);

        // DELETE APPLICATION
        App deleteAppParam = new App();
        deleteAppParam.setName(testInsertCatalog.getName());
        deleteAppParam.setOrgName(testInsertCatalog.getOrgName());
        deleteAppParam.setSpaceName(testInsertCatalog.getSpaceName());

        appService.deleteApp(deleteAppParam, TEST_ADMIN_TOKEN);

        // DELETE ROUTE
        List<String> tempList = new ArrayList<>();
        tempList.add(testInsertCatalog.getHostName());
        appService.deleteRoute(testInsertCatalog.getOrgName(), testInsertCatalog.getSpaceName(), tempList, TEST_ADMIN_TOKEN);
    }


    @Test
    public void test_43_02_zip_executeCatalogBuildPack() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setName(TEST_APP_NAME);
        testInsertCatalog.setOrgName(testOrg);
        testInsertCatalog.setSpaceName(testSpace);
        testInsertCatalog.setBuildPackName(testRubyBuildPack);
        testInsertCatalog.setHostName(TEST_APP_NAME + "." + testDomainUrl);
        testInsertCatalog.setAppSampleStartYn(Constants.USE_YN_Y);
        testInsertCatalog.setAppSampleFilePath(this.procBeforeTestGetBuildPackInfo(testRubyBuildPack).get("APP_SAMPLE_FILE_PATH").toString());

        // DELETE APPLICATION BEFORE TEST
        this.deleteApplication(TEST_APP_NAME, testOrg, testSpace, TEST_APP_NAME + "." + testDomainUrl);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.executeCatalogBuildPack(testInsertCatalog, req);

        // DELETE APPLICATION
        App app = new App();
        app.setName(testInsertCatalog.getName());
        app.setOrgName(testInsertCatalog.getOrgName());
        app.setSpaceName(testInsertCatalog.getSpaceName());

        mvc.perform(post("/app/deleteApp")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, TEST_ADMIN_TOKEN)
                .content(gson.toJson(app)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());

        // DELETE ROUTE
        List<String> tempList = new ArrayList<>();
        tempList.add(testInsertCatalog.getHostName());
        appService.deleteRoute(testInsertCatalog.getOrgName(), testInsertCatalog.getSpaceName(), tempList, TEST_ADMIN_TOKEN);
    }


    @Test
    public void test_43_03_egov_uploadAppNone_executeCatalogBuildPack() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setName(TEST_APP_NAME);
        testInsertCatalog.setOrgName(testOrg);
        testInsertCatalog.setSpaceName(testSpace);
        testInsertCatalog.setBuildPackName(testEgovBuildPack);
        testInsertCatalog.setHostName(TEST_APP_NAME + "." + testDomainUrl);
        testInsertCatalog.setAppSampleFilePath(Constants.USE_YN_N);
        testInsertCatalog.setAppSampleStartYn(Constants.USE_YN_N);

        // DELETE APPLICATION BEFORE TEST
        this.deleteApplication(TEST_APP_NAME, testOrg, testSpace, TEST_APP_NAME + "." + testDomainUrl);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        catalogService.executeCatalogBuildPack(testInsertCatalog, req);

        // DELETE APPLICATION
        App deleteAppParam = new App();
        deleteAppParam.setName(testInsertCatalog.getName());
        deleteAppParam.setOrgName(testInsertCatalog.getOrgName());
        deleteAppParam.setSpaceName(testInsertCatalog.getSpaceName());

        appService.deleteApp(deleteAppParam, TEST_ADMIN_TOKEN);

        // DELETE ROUTE
        List<String> tempList = new ArrayList<>();
        tempList.add(testInsertCatalog.getHostName());
        appService.deleteRoute(testInsertCatalog.getOrgName(), testInsertCatalog.getSpaceName(), tempList, TEST_ADMIN_TOKEN);
    }


    @Test
    public void test_44_insertCatalogHistoryBuildPack() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setCatalogNo(1);
        testInsertCatalog.setUserId(testUserId);

        catalogService.insertCatalogHistoryBuildPack(testInsertCatalog);
    }


    @Test
    public void test_45_01_executeCatalogServicePack() throws Exception {
        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setServiceInstanceName(TEST_SERVICE_INSTANCE_NAME);
        testUpdateCatalog.setOrgName(testOrg);
        testUpdateCatalog.setSpaceName(testSpace);
        testUpdateCatalog.setServicePlan(this.procBeforeTestGetServicePackInfo().get("SERVICE_PLAN_GUID").toString());

        // DELETE SERVICE INSTANCE
        this.deleteServiceInstance(TEST_SERVICE_INSTANCE_NAME);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        Map<String, Object> resultMap = catalogService.executeCatalogServicePack(testUpdateCatalog, req);

        Service deleteInstanceServiceParam = new Service();
        deleteInstanceServiceParam.setGuid(UUID.fromString(resultMap.get("SERVICE_INSTANCE_GUID").toString()));

        serviceService.deleteInstanceService(deleteInstanceServiceParam, TEST_CUSTOM_ADMIN_CLIENT);
    }


    @Test
    public void test_45_02_bind_executeCatalogServicePack() throws Exception {
        // DELETE SERVICE INSTANCE
        this.deleteServiceInstance(TEST_SERVICE_INSTANCE_NAME);

        testUpdateCatalog = new Catalog();
        testUpdateCatalog.setServiceInstanceName(TEST_SERVICE_INSTANCE_NAME);
        testUpdateCatalog.setOrgName(testOrg);
        testUpdateCatalog.setSpaceName(testSpace);
        testUpdateCatalog.setServicePlan(this.procBeforeTestGetServicePackInfo().get("SERVICE_PLAN_GUID").toString());
        testUpdateCatalog.setAppName(TEST_APP_NAME);
        testUpdateCatalog.setAppBindYn(Constants.USE_YN_Y);
        testUpdateCatalog.setParameter("");

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        Map<String, Object> resultMap = catalogService.executeCatalogServicePack(testUpdateCatalog, req);

        App unbindServiceParam = new App();
        unbindServiceParam.setName(testUpdateCatalog.getAppName());
        unbindServiceParam.setServiceName(testUpdateCatalog.getServiceInstanceName());

        appService.unbindService(unbindServiceParam, TEST_ADMIN_TOKEN);

        Service deleteInstanceServiceParam = new Service();
        deleteInstanceServiceParam.setGuid(UUID.fromString(resultMap.get("SERVICE_INSTANCE_GUID").toString()));

        serviceService.deleteInstanceService(deleteInstanceServiceParam, TEST_CUSTOM_ADMIN_CLIENT);
    }


    @Test
    public void test_46_insertCatalogHistoryServicePack() throws Exception {
        testInsertCatalog = new Catalog();
        testInsertCatalog.setCatalogNo(1);
        testInsertCatalog.setUserId(testUserId);

        catalogService.insertCatalogHistoryServicePack(testInsertCatalog);
    }


    public Map<String, Object> procBeforeTestGetServicePackInfo() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader(cfAuthorization, TEST_ADMIN_TOKEN);

        Map<String, Object> resultServicePackListMap = catalogService.getServicePackList(req);
        List<Map<String, Object>> resultServicePackList = (List<Map<String, Object>>) resultServicePackListMap.get("list");

        Map<String, Object> resultServicePlanListMap = catalogService.getCatalogServicePlanList(new Catalog() {{
            setServicePackName(resultServicePackList.get(0).get("name").toString());
            setOrgName(testOrg);
            setSpaceName(testSpace);
        }}, req);

        List<Map<String, Object>> resultServicePlanList = (List<Map<String, Object>>) resultServicePlanListMap.get("list");

        return new HashMap<String, Object>() {{
            put("SERVICE_NAME", resultServicePackList.get(0).get("name").toString());
            put("SERVICE_PLAN_GUID", resultServicePlanList.get(0).get("guid").toString());
        }};
    }


    public Map<String, Object> procBeforeTestGetBuildPackInfo(String reqBuildPackName) throws Exception {
        Map<String, Object> resultMap = catalogService.getBuildPackCatalogList(new Catalog() {{
            setBuildPackName(reqBuildPackName);
        }});
        List<Catalog> resultList = (List<Catalog>) resultMap.get("list");

        return new HashMap<String, Object>() {{
            put("APP_SAMPLE_FILE_PATH", resultList.get(0).getAppSampleFilePath());
        }};
    }


    public void commonProcessBeforeTest() throws Exception {
        // DELETE APPLICATION, SERVICE INSTANCE, SPACE, ORG
        this.commonProcessAfterTest();

        // CREATE ORG
        TEST_CUSTOM_ADMIN_CLIENT.createOrg(testOrg);

        // CREATE SPACE
        TEST_CUSTOM_ADMIN_CLIENT.createSpace(testOrg, testSpace);

        // CREATE SERVICE INSTANCE
        String servicePlanGuid = this.procBeforeTestGetServicePackInfo().get("SERVICE_PLAN_GUID").toString();
        catalogService.executeCatalogServicePack(new Catalog() {{
            setServiceInstanceName(TEST_SERVICE_INSTANCE_NAME);
            setOrgName(testOrg);
            setSpaceName(testSpace);
            setServicePlan(servicePlanGuid);
        }}, new MockHttpServletRequest() {
            @Override
            public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
                return null;
            }

            {
            addHeader(cfAuthorization, TEST_ADMIN_TOKEN);
        }});

        // CREATE APP
        catalogService.executeCatalogBuildPack(new Catalog() {{
            setName(TEST_APP_NAME);
            setOrgName(testOrg);
            setSpaceName(testSpace);
            setHostName(TEST_APP_NAME + "." + testDomainUrl);
            setAppSampleFilePath(Constants.USE_YN_N);
            setAppSampleStartYn(Constants.USE_YN_N);
        }}, new MockHttpServletRequest() {
            @Override
            public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
                return null;
            }

            {
            addHeader(cfAuthorization, TEST_ADMIN_TOKEN);
        }});
    }


    public void commonProcessAfterTest() throws Exception {
        // DELETE SERVICE INSTANCE
        this.deleteServiceInstance(TEST_SERVICE_INSTANCE_NAME);

        // DELETE APPLICATION
        this.deleteApplication(TEST_APP_NAME, testOrg, testSpace, TEST_APP_NAME + "." + testDomainUrl);

        // DELETE SPACE
        this.deleteSpace(testOrg, testSpace);

        // DELETE ORG
        this.deleteOrganization(testOrg);
    }


    public void deleteApplication(String reqName, String reqOrgName, String reqSpaceName, String reqHostName) throws Exception {
        try {
            TEST_ADMIN_CLIENT.getApplication(reqName);

            // DELETE APPLICATION
            App deleteAppParam = new App();
            deleteAppParam.setName(reqName);
            deleteAppParam.setOrgName(reqOrgName);
            deleteAppParam.setSpaceName(reqSpaceName);

            appService.deleteApp(deleteAppParam, TEST_ADMIN_TOKEN);

            // DELETE ROUTE
            List<String> reqHostNameList = new ArrayList<>();
            reqHostNameList.add(reqHostName);
            appService.deleteRoute(reqOrgName, reqSpaceName, reqHostNameList, TEST_ADMIN_TOKEN);

        } catch (Exception e) {
            try {
                // DELETE ROUTE
                List<String> reqHostNameList = new ArrayList<>();
                reqHostNameList.add(reqHostName);
                appService.deleteRoute(reqOrgName, reqSpaceName, reqHostNameList, TEST_ADMIN_TOKEN);
            } catch (Exception e1) {
                // TO DO
            }
        }
    }


    public void deleteServiceInstance(String reqServiceInstanceName) throws Exception {
        try {
            CloudServiceInstance cloudServiceInstance = TEST_ADMIN_CLIENT.getServiceInstance(reqServiceInstanceName);

            Service deleteInstanceServiceParam = new Service();
            deleteInstanceServiceParam.setGuid(cloudServiceInstance.getMeta().getGuid());

            // DELETE SERVICE INSTANCE
            serviceService.deleteInstanceService(deleteInstanceServiceParam, TEST_CUSTOM_ADMIN_CLIENT);

        } catch (Exception e) {
            // TO DO
        }
    }


    public void deleteSpace(String reqOrgName, String reqSpaceName) throws Exception {
        try {
            TEST_CUSTOM_ADMIN_CLIENT.deleteSpace(reqOrgName, reqSpaceName);

        } catch (Exception e) {
            // TO DO
        }
    }


    public void deleteOrganization(String reqOrgName) throws Exception {
        try {
            TEST_CUSTOM_ADMIN_CLIENT.deleteOrg(reqOrgName);

        } catch (Exception e) {
            // TO DO
        }
    }
}