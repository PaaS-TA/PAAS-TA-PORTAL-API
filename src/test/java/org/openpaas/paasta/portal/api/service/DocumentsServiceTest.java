package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
public class DocumentsServiceTest {

    private static final String AUTHORIZATION_HEADER_KEY = "cf-Authorization";
    private static Gson gson = new Gson();
    private static String TEST_URL = "/documents";
    private static Support testInitSupport = null;
    private static Support testSelectSupport = null;
    private static Support testInsertSupport = null;
    private static Support testUpdateSupport = null;
    private static Support testDeleteSupport = null;
    private static int documentsNo = 7;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;

    @BeforeClass
    public static void init() throws Exception {
        testInitSupport = new Support();
        testInitSupport.setTitle("test_title");
        testInitSupport.setUserId("test_userId");
        testInitSupport.setClassification("test_classification");
        testInitSupport.setUseYn("Y");
        testInitSupport.setContent("test_content");
        testInitSupport.setFileName("test_fileName");
        testInitSupport.setFilePath("test_filePath");
    }

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

//        mvc.perform(post(TEST_URL + "/insertNotice")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(AUTHORIZATION_HEADER_KEY, "")
//                .content(gson.toJson(testInitSupport)))
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(status().isOk())
//                .andDo(print());
    }

    @Test
    public void test01_getDocumentsList() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setPageOffset(0);

        mvc.perform(post(TEST_URL + "/getDocumentsList")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(status().isOk())
                .andDo(print());
    }

/*    @Test
    public void test01_getDocumentsListUser() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setPageOffset(0);

        mvc.perform(post(TEST_URL + "/getDocumentsList")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(status().isOk())
                .andDo(print());
    }*/

    @Test
    public void test02_getDocument() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setNo(documentsNo);

        mvc.perform(post(TEST_URL + "/getDocument")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test03_insertDocument() throws Exception {
        testInsertSupport = new Support();
        testInsertSupport.setTitle("test_title");
        testInsertSupport.setUserId("test_id");
        testInsertSupport.setClassification("test_class");
        testInsertSupport.setUseYn("Y");
        testInsertSupport.setContent("test_content");
        testInsertSupport.setFileName("test_fName");
        testInsertSupport.setFilePath("test_fPath");
        // testInsertSupport.setSearchStartDate("2016/01/01");
        // testInsertSupport.setSearchEndDate("2016/01/01");


        mvc.perform(post(TEST_URL + "/insertDocument")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testInsertSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test04_updateDocument() throws Exception {
        testUpdateSupport = new Support();
        testUpdateSupport.setNo(documentsNo);
        testUpdateSupport.setTitle("test_title");
        testUpdateSupport.setUserId("test_id");
        testUpdateSupport.setClassification("test_class");
        testUpdateSupport.setUseYn("Y");
        testUpdateSupport.setContent("test_content");
        testUpdateSupport.setFileName("test_fName");
        testUpdateSupport.setFilePath("test_fPath");
        //      testUpdateSupport.setStartDate("2016/01/01");
        //     testUpdateSupport.setEndDate("2016/01/01");

        mvc.perform(put(TEST_URL + "/updateDocument")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testUpdateSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test05_deleteDocument() throws Exception {
        testDeleteSupport = new Support();
        testDeleteSupport.setNo(documentsNo);

        mvc.perform(post(TEST_URL + "/deleteDocument")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testDeleteSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
