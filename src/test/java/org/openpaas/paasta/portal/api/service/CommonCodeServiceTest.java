package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.controller.CommonCodeController;
import org.openpaas.paasta.portal.api.model.CommonCode;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
public class CommonCodeServiceTest extends CommonTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;
    private static Gson gson = new Gson();
    private static String TEST_URL = "/commonCode";
    private static String cfAuthorization = "";

    private static CommonCode testInitCommonCode = null;
    private static CommonCode testSelectCommonCode = null;
    private static CommonCode testInsertCommonCode = null;
    private static CommonCode testUpdateCommonCode = null;
    private static CommonCode testDeleteCommonCode = null;


    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        testInitCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInitCommonCode)))
                .andExpect(handler().handlerType(CommonCodeController.class))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isOk())
                .andDo(print());

        testInitCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInitCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @BeforeClass
    public static void init() throws Exception {
        cfAuthorization = getPropertyValue("test.cf.authorization");

        testInitCommonCode = new CommonCode();
        testInitCommonCode.setPageNo(Constants.PAGE_NO);
        testInitCommonCode.setPageSize(Constants.PAGE_SIZE);
        testInitCommonCode.setUserId(getPropertyValue("test.clientUserName"));

        testInitCommonCode.setId("test_id");
        testInitCommonCode.setName("test_name");
        testInitCommonCode.setKey("test_key");
        testInitCommonCode.setValue("test_value");
        testInitCommonCode.setGroupId(testInitCommonCode.getId());
        testInitCommonCode.setUseYn(Constants.USE_YN_Y);
    }


    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void test_01_getCommonCodeById() throws Exception {
        testSelectCommonCode = new CommonCode();

        String codeId = "/" + testInitCommonCode.getId();

        mvc.perform(get(TEST_URL + "/getCommonCode" + codeId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCommonCode)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andDo(print());
    }


    @Test
    public void test_02_getCommonCode() throws Exception {
        testSelectCommonCode = new CommonCode();
        testSelectCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/getCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCommonCode)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());


        testSelectCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/getCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCommonCode)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());
    }


    @Test
    public void test_021_checkEmpty_param_getCommonCode() throws Exception {
        testSelectCommonCode = new CommonCode();
        testSelectCommonCode.setProcType("");

        mvc.perform(post(TEST_URL + "/getCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCommonCode)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());
    }


    @Test
    public void test_022_checkPageNumber_getCommonCode() throws Exception {
        testSelectCommonCode = new CommonCode();
        testSelectCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);
        testSelectCommonCode.setPageNo(0);
        testSelectCommonCode.setPageSize(0);

        mvc.perform(post(TEST_URL + "/getCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCommonCode)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());


        testSelectCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/getCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCommonCode)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());

        testSelectCommonCode.setPageNo(1);
        testSelectCommonCode.setPageSize(1);

        mvc.perform(post(TEST_URL + "/getCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCommonCode)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());


        testSelectCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/getCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testSelectCommonCode)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.result", notNullValue()))
                .andDo(print());
    }


    @Test
    public void test_03_success_insertCommonCode() throws Exception {
        testInsertCommonCode = new CommonCode();
        testInsertCommonCode.setId("test_insert_id");
        testInsertCommonCode.setName("test_insert_name");
        testInsertCommonCode.setKey("test_insert_key");
        testInsertCommonCode.setValue("test_insert_value");
        testInsertCommonCode.setGroupId(testInsertCommonCode.getId());
        testInsertCommonCode.setUseYn(Constants.USE_YN_Y);
        testInsertCommonCode.setUserId(testInitCommonCode.getUserId());
        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isOk())
                .andDo(print());


        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_031_checkConflict_emptyParam_insertCommonCode() throws Exception {
        testInsertCommonCode = new CommonCode();
        testInsertCommonCode.setId("");
        testInsertCommonCode.setKey("");
        testInsertCommonCode.setName("");
        testInsertCommonCode.setValue("");
        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isConflict())
                .andDo(print());


        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isConflict())
                .andDo(print());
    }


    @Test
    public void test_032_checkConflict_duplicatedParam_insertCommonCode() throws Exception {
        testInsertCommonCode = new CommonCode();
        testInsertCommonCode.setId(testInitCommonCode.getId());
        testInsertCommonCode.setName(testInitCommonCode.getName());
        testInsertCommonCode.setKey(testInitCommonCode.getKey());
        testInsertCommonCode.setValue(testInitCommonCode.getValue());
        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isConflict())
                .andDo(print());


        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isConflict())
                .andDo(print());
    }


    @Test
    public void test_033_saveList_insertCommonCode() throws Exception {
        testInsertCommonCode = new CommonCode();
        CommonCode testSaveListCommonCode = new CommonCode();
        CommonCode testSaveListCommonCode2 = new CommonCode();

        testSaveListCommonCode.setId("test_save_list_id");
        testSaveListCommonCode.setOrgId(testSaveListCommonCode.getId());
        testSaveListCommonCode.setName("test_save_list_name");
        testSaveListCommonCode.setKey("test_save_list_key");
        testSaveListCommonCode.setOrgKey(testSaveListCommonCode.getKey());
        testSaveListCommonCode.setValue("test_save_list_value");
        testSaveListCommonCode.setGroupId(testSaveListCommonCode.getId());
        testSaveListCommonCode.setUseYn(Constants.USE_YN_Y);
        testSaveListCommonCode.setUserId(testInitCommonCode.getUserId());
        testSaveListCommonCode.setReqCud(Constants.CUD_C);

        testSaveListCommonCode2.setId("test_save_list_id");
        testSaveListCommonCode2.setOrgId(testSaveListCommonCode2.getId());
        testSaveListCommonCode2.setName("test_save_list_name");
        testSaveListCommonCode2.setKey("test_save_list_key");
        testSaveListCommonCode2.setOrgKey(testSaveListCommonCode2.getKey());
        testSaveListCommonCode2.setValue("test_save_list_value");
        testSaveListCommonCode2.setGroupId(testSaveListCommonCode2.getId());
        testSaveListCommonCode2.setUseYn(Constants.USE_YN_Y);
        testSaveListCommonCode2.setUserId(testInitCommonCode.getUserId());
        testSaveListCommonCode2.setReqCud(Constants.CUD_U);

        List<CommonCode> testList = new ArrayList<>();
        testList.add(testSaveListCommonCode);
        testList.add(testSaveListCommonCode2);

        testInsertCommonCode.setCommonCodeList(testList);
        testInsertCommonCode.setGroupId(testSaveListCommonCode.getId());
        testInsertCommonCode.setUserId(testInitCommonCode.getUserId());
        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_034_checkConflict_emptyParam_saveList_insertCommonCode() throws Exception {
        testInsertCommonCode = new CommonCode();
        CommonCode testSaveListCommonCode = new CommonCode();
        CommonCode testSaveListCommonCode2 = new CommonCode();

        testSaveListCommonCode.setId("");
        testSaveListCommonCode.setOrgId(testSaveListCommonCode.getId());
        testSaveListCommonCode.setName("");
        testSaveListCommonCode.setKey("");
        testSaveListCommonCode.setOrgKey(testSaveListCommonCode.getKey());
        testSaveListCommonCode.setValue("");
        testSaveListCommonCode.setGroupId(testSaveListCommonCode.getId());
        testSaveListCommonCode.setUseYn(Constants.USE_YN_Y);
        testSaveListCommonCode.setUserId(testInitCommonCode.getUserId());
        testSaveListCommonCode.setReqCud(Constants.CUD_C);

        testSaveListCommonCode2.setId("");
        testSaveListCommonCode2.setOrgId(testSaveListCommonCode2.getId());
        testSaveListCommonCode2.setName("");
        testSaveListCommonCode2.setKey("");
        testSaveListCommonCode2.setOrgKey(testSaveListCommonCode2.getKey());
        testSaveListCommonCode2.setValue("");
        testSaveListCommonCode2.setGroupId(testSaveListCommonCode2.getId());
        testSaveListCommonCode2.setUseYn(Constants.USE_YN_Y);
        testSaveListCommonCode2.setUserId(testInitCommonCode.getUserId());
        testSaveListCommonCode2.setReqCud(Constants.CUD_U);

        List<CommonCode> testList = new ArrayList<>();
        testList.add(testSaveListCommonCode);
        testList.add(testSaveListCommonCode2);

        testInsertCommonCode.setCommonCodeList(testList);
        testInsertCommonCode.setGroupId(testSaveListCommonCode.getId());

        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isConflict())
                .andDo(print());


        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isConflict())
                .andDo(print());
    }


    @Test
    public void test_035_checkConflict_duplicatedParam_saveList_insertCommonCode() throws Exception {
        testInsertCommonCode = new CommonCode();
        CommonCode testSaveListCommonCode = new CommonCode();
        CommonCode testSaveListCommonCode2 = new CommonCode();

        testSaveListCommonCode.setId("test_save_list_id");
        testSaveListCommonCode.setOrgId(testSaveListCommonCode.getId());
        testSaveListCommonCode.setName("test_save_list_name");
        testSaveListCommonCode.setKey("test_save_list_key");
        testSaveListCommonCode.setOrgKey(testSaveListCommonCode.getKey());
        testSaveListCommonCode.setValue("test_save_list_value");
        testSaveListCommonCode.setGroupId(testSaveListCommonCode.getId());
        testSaveListCommonCode.setUseYn(Constants.USE_YN_Y);
        testSaveListCommonCode.setUserId(testInitCommonCode.getUserId());
        testSaveListCommonCode.setReqCud(Constants.CUD_C);

        testSaveListCommonCode2.setId("test_save_list_id");
        testSaveListCommonCode2.setOrgId(testSaveListCommonCode2.getId());
        testSaveListCommonCode2.setName("test_save_list_name");
        testSaveListCommonCode2.setKey("test_save_list_key");
        testSaveListCommonCode2.setOrgKey(testSaveListCommonCode2.getKey());
        testSaveListCommonCode2.setValue("test_save_list_value");
        testSaveListCommonCode2.setGroupId(testSaveListCommonCode2.getId());
        testSaveListCommonCode2.setUseYn(Constants.USE_YN_Y);
        testSaveListCommonCode2.setUserId(testInitCommonCode.getUserId());
        testSaveListCommonCode2.setReqCud(Constants.CUD_U);

        List<CommonCode> testList = new ArrayList<>();
        testList.add(testSaveListCommonCode);
        testList.add(testSaveListCommonCode2);

        testInsertCommonCode.setCommonCodeList(testList);
        testInsertCommonCode.setGroupId(testSaveListCommonCode.getId());
        testInsertCommonCode.setUserId(testInitCommonCode.getUserId());
        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testSaveListCommonCode.setId("test_id");
        testSaveListCommonCode.setOrgId("test_save_list_id");
        testSaveListCommonCode.setName("test_name");
        testSaveListCommonCode.setKey("test_key");
        testSaveListCommonCode.setOrgKey("test_save_list_key");
        testSaveListCommonCode.setValue("test_save_list_value");
        testSaveListCommonCode.setGroupId(testSaveListCommonCode.getId());
        testSaveListCommonCode.setUseYn(Constants.USE_YN_Y);
        testSaveListCommonCode.setUserId(testInitCommonCode.getUserId());
        testSaveListCommonCode.setReqCud(Constants.CUD_U);

        List<CommonCode> testList2 = new ArrayList<>();
        testList2.add(testSaveListCommonCode);

        testInsertCommonCode.setCommonCodeList(testList2);
        testInsertCommonCode.setGroupId(testSaveListCommonCode.getId());

        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isConflict())
                .andDo(print());


        testInsertCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/insertCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testInsertCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isConflict())
                .andDo(print());
    }


    @Test
    public void test_04_updateCommonCode() throws Exception {
        testUpdateCommonCode = new CommonCode();
        testUpdateCommonCode.setId("test_update_id");
        testUpdateCommonCode.setOrgId(testInitCommonCode.getId());
        testUpdateCommonCode.setName("test_update_name");
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testUpdateCommonCode.setKey("test_update_key");
        testUpdateCommonCode.setOrgKey(testInitCommonCode.getKey());
        testUpdateCommonCode.setValue("test_update_value");
        testUpdateCommonCode.setGroupId(testInitCommonCode.getId());
        testUpdateCommonCode.setUseYn(Constants.USE_YN_N);
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_041_checkInternalServerError_nullParam_updateCommonCode() throws Exception {
        testUpdateCommonCode = new CommonCode();
        testUpdateCommonCode.setKey(null);
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isInternalServerError())
                .andDo(print());


        testUpdateCommonCode.setKey(testInitCommonCode.getKey());
        testUpdateCommonCode.setOrgKey(null);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }


    @Test
    public void test_042_checkEqual_param_commonCodeGroup_updateCommonCode() throws Exception {
        testUpdateCommonCode = new CommonCode();
        testUpdateCommonCode.setId(testInitCommonCode.getId());
        testUpdateCommonCode.setOrgId(testInitCommonCode.getId());
        testUpdateCommonCode.setName("test_update_name");
        testUpdateCommonCode.setUseYn(Constants.USE_YN_N);
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testUpdateCommonCode.setId("test_check_id");
        testUpdateCommonCode.setOrgId(testInitCommonCode.getId());
        testUpdateCommonCode.setName("test_update_name");
        testUpdateCommonCode.setUseYn(Constants.USE_YN_N);
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testUpdateCommonCode.setId("test_check_id");
        testUpdateCommonCode.setOrgId(testInitCommonCode.getId());
        testUpdateCommonCode.setName("test_update_name");
        testUpdateCommonCode.setUseYn(Constants.USE_YN_N);
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isConflict())
                .andDo(print());
    }


    @Test
    public void test_043_checkEqual_param_commonCodeDetail_updateCommonCode() throws Exception {
        testUpdateCommonCode = new CommonCode();
        testUpdateCommonCode.setKey(testInitCommonCode.getKey());
        testUpdateCommonCode.setOrgKey(testInitCommonCode.getKey());
        testUpdateCommonCode.setValue("test_update_value");
        testUpdateCommonCode.setGroupId(testInitCommonCode.getId());
        testUpdateCommonCode.setUseYn(Constants.USE_YN_N);
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testUpdateCommonCode.setKey("test_check_key");
        testUpdateCommonCode.setOrgKey(testInitCommonCode.getKey());
        testUpdateCommonCode.setValue("test_update_value");
        testUpdateCommonCode.setGroupId(testInitCommonCode.getId());
        testUpdateCommonCode.setUseYn(Constants.USE_YN_N);
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());


        testUpdateCommonCode.setKey("test_check_key");
        testUpdateCommonCode.setOrgKey(testInitCommonCode.getKey());
        testUpdateCommonCode.setValue("test_update_value");
        testUpdateCommonCode.setGroupId(testInitCommonCode.getId());
        testUpdateCommonCode.setUseYn(Constants.USE_YN_N);
        testUpdateCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(put(TEST_URL + "/updateCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testUpdateCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isConflict())
                .andDo(print());
    }


    @Test
    public void test_05_deleteCommonCode() throws Exception {
        testDeleteCommonCode = new CommonCode();
        testDeleteCommonCode.setId(testInitCommonCode.getId());

        // COMMON CODE GROUP
        testDeleteCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_GROUP);

        mvc.perform(post(TEST_URL + "/deleteCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testDeleteCommonCode)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());

        testDeleteCommonCode = new CommonCode();
        testDeleteCommonCode.setKey(testInitCommonCode.getKey());

        // COMMON CODE DETAIL
        testDeleteCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/deleteCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testDeleteCommonCode)))
                .andExpect(handler().handlerType(CommonCodeController.class))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_051_deleteList_deleteCommonCode() throws Exception {
        testDeleteCommonCode = new CommonCode();

        CommonCode testDeleteListCommonCode = new CommonCode();
        testDeleteListCommonCode.setKey(testInitCommonCode.getKey());

        List<CommonCode> testList = new ArrayList<>();
        testList.add(testDeleteListCommonCode);

        testDeleteCommonCode.setCommonCodeList(testList);
        testDeleteCommonCode.setProcType(Constants.PROC_NAME_COMMON_CODE_DETAIL);

        mvc.perform(post(TEST_URL + "/deleteCommonCode")
                .contentType(MediaType.APPLICATION_JSON)
                .header(cfAuthorization, "")
                .content(gson.toJson(testDeleteCommonCode)))
                .andExpect(handler().handlerType(CommonCodeController.class))
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.RESULT", notNullValue()))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_99_getProperty() throws Exception {
        Common.getPropertyValue("test");
        Common.getPropertyValue("test", null);
        Common.getPropertyValue("test", "/config.properties");
    }

}