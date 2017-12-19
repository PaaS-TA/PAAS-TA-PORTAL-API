package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by YJKim on 2016-07-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ApiApplication.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(defaultRollback = true)
@Transactional("portalTransactionManager")
public class SupportQnAServiceTest extends CommonTest {

    private static final String AUTHORIZATION_HEADER_KEY = "cf-Authorization";
    private static Gson gson = new Gson();
    private static String TEST_URL = "/support";
    private static Support testInitSupportAnswer = null;
    private static Support testSelectSupport = null;
    private static Support testInsertSupport = null;
    private static Support testUpdateSupport = null;
    private static Support testDeleteSupport = null;
    private static int questionNo = 46;
    private static String TEST_ADMIN_TOKEN = "";
    private static String testUserId = "";
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private SupportQnAService supportQnAService;
    private MockMvc mvc;

    @BeforeClass
    public static void init() throws Exception {
        testInitSupportAnswer = new Support();
        testInitSupportAnswer.setContent("test_content");
        testInitSupportAnswer.setFileName("test_fileName");
        testInitSupportAnswer.setFilePath("test_filePath");
        testInitSupportAnswer.setQuestionNo(questionNo);
        testInitSupportAnswer.setStatus("answered");
        testInitSupportAnswer.setAnswerer("answerer");

        String cfApiTarget = getPropertyValue("test.apiTarget");

        CloudCredentials adminCredentials = new CloudCredentials(getPropertyValue("test.admin.id"), getPropertyValue("test.admin.password"));
        TEST_ADMIN_TOKEN = new CloudFoundryClient(adminCredentials, getTargetURL(cfApiTarget), true).login().getValue();
        testUserId = getPropertyValue("test.clientUserName");
    }

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        mvc.perform(post(TEST_URL + "/insertAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testInitSupportAnswer)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test_01_getQnAList() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setSearchKeyword("test");
        testSelectSupport.setSearchTypeColumn("ALL");
        testSelectSupport.setSearchTypeStatus("waiting");


        mvc.perform(post(TEST_URL + "/getQnAList")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_02_getQuestion() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setNo(questionNo);

        mvc.perform(post(TEST_URL + "/getQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_03_getAnswer() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setQuestionNo(questionNo);

        mvc.perform(post(TEST_URL + "/getAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_04_insertAnswer() throws Exception {
        testInsertSupport = new Support();
        testInsertSupport.setContent("test_content");
        testInsertSupport.setFileName("test_fileName");
        testInsertSupport.setFilePath("test_filePath");
        testInsertSupport.setQuestionNo(questionNo);
        testInsertSupport.setStatus("answered");
        testInsertSupport.setAnswerer("answerer");

        mvc.perform(post(TEST_URL + "/insertAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testInsertSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_05_updateAnswer() throws Exception {
        testUpdateSupport = new Support();
        testUpdateSupport.setContent("test_content");
        testUpdateSupport.setFileName("test_fileName");
        testUpdateSupport.setFilePath("test_filePath");
        testUpdateSupport.setQuestionNo(questionNo);
        testUpdateSupport.setAnswerer("answerer");

        mvc.perform(put(TEST_URL + "/updateAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testUpdateSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_06_deleteAnswer() throws Exception {
        testDeleteSupport = new Support();
        testDeleteSupport.setQuestionNo(questionNo);
        testDeleteSupport.setStatus("waiting");

        mvc.perform(post(TEST_URL + "/deleteAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testDeleteSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test_07_getMyQuestions_TEST() throws Exception {
        supportQnAService.getMyQuestionsInMyAccount(TEST_ADMIN_TOKEN);
    }


    @Test
    public void test_08_getMyQuestionList() throws Exception {
        Support testInitParam = new Support();

        testInitParam.setPageNo(Constants.PAGE_NO);
        testInitParam.setPageSize(Constants.PAGE_SIZE);

        supportQnAService.getMyQuestionList(testInitParam);
    }


    @Test
    public void test_09_checkPageNumber_getMyQuestionList() throws Exception {
        Support testInitParam = new Support();

        testInitParam.setPageNo(0);
        testInitParam.setPageSize(0);

        supportQnAService.getMyQuestionList(testInitParam);
    }


    @Test
    public void test_10_upload_delete_File() throws Exception {
        File testFile = new File(getPropertyValue("test.file.path"));

        FileInputStream testFileInputStream = new FileInputStream(testFile);
        MultipartFile testMultipartFile = new MockMultipartFile("file", testFile.getName(), "text/plain", IOUtils.toByteArray(testFileInputStream));

        // UPLOAD FILE
        Map<String, Object> resultMap = supportQnAService.uploadFile(testMultipartFile);

        // DELETE FILE
        supportQnAService.deleteFile(resultMap.get("path").toString());
    }


    @Test
    public void test_11_insertMyQuestion() throws Exception {
        Support testInsertParam = new Support();

        testInsertParam.setTitle("test_insert_title");
        testInsertParam.setClassification("test_insert_classification");
        testInsertParam.setUserId(testUserId);
        testInsertParam.setContent("test_insert_content");
        testInsertParam.setStatus(Constants.MY_QUESTION_STATUS_WAITING);

        supportQnAService.insertMyQuestion(testInsertParam);
    }


    @Test
    public void test_12_updateMyQuestion() throws Exception {
        Support testUpdateParam = new Support();

        testUpdateParam.setNo(99999999);
        testUpdateParam.setTitle("test_update_title");
        testUpdateParam.setClassification("test_update_classification");
        testUpdateParam.setUserId(testUserId);
        testUpdateParam.setContent("test_update_content");

        supportQnAService.updateMyQuestion(testUpdateParam);
    }


    @Test
    public void test_13_deleteMyQuestion() throws Exception {
        Support testDeleteParam = new Support();
        testDeleteParam.setNo(99999999);

        supportQnAService.deleteMyQuestion(testDeleteParam);
    }


}
