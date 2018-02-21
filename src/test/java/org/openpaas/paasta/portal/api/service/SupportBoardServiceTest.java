package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.Support;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by YJKim on 2016-07-28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TransactionConfiguration(defaultRollback = true)
@Transactional("portalTransactionManager")
public class SupportBoardServiceTest {

    private static final String AUTHORIZATION_HEADER_KEY = "cf-Authorization";
    private static Gson gson = new Gson();
    private static String TEST_URL = "/support";
    private static Support testInitSupport = null;
    private static Support testSelectSupport = null;
    private static Support testInsertSupport = null;
    private static Support testUpdateSupport = null;
    private static Support testDeleteSupport = null;
    private static int boardNo = 7;
    private static int commentNo = 1;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;

    @BeforeClass
    public static void init() throws Exception {
        testInitSupport = new Support();
        testInitSupport.setTitle("test_title");
        testInitSupport.setUserId("test_userId");
        testInitSupport.setContent("test_content");
        testInitSupport.setFileName("test_fileName");
        testInitSupport.setFilePath("test_filePath");

        testInitSupport.setParentNo(-1);
        testInitSupport.setGroupNo(7);
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
    public void test01_getBoardList() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setPageOffset(0);

        mvc.perform(post(TEST_URL + "/getBoardList")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test02_getBoard() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setNo(boardNo);

        mvc.perform(post(TEST_URL + "/getBoard")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test03_getBoardCommentList() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setNo(boardNo);


        mvc.perform(post(TEST_URL + "/getBoardCommentList")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test04_insertBoard() throws Exception {
        testInsertSupport = new Support();
        testInsertSupport.setTitle("test_title");
        testInsertSupport.setUserId("test_userId");
        testInsertSupport.setContent("test_content");
        testInsertSupport.setFileName("test_fName");
        testInsertSupport.setFilePath("test_fPath");
        testInsertSupport.setFileSize(10000);


        mvc.perform(post(TEST_URL + "/insertBoard")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testInsertSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test05_updateBoard() throws Exception {
        testUpdateSupport = new Support();
        testUpdateSupport.setTitle("test_title");
        testUpdateSupport.setUserId("test_userId");
        testUpdateSupport.setContent("test_content");
        testUpdateSupport.setFileName("test_fName");
        testUpdateSupport.setFilePath("test_fPath");
        testUpdateSupport.setFileSize(10000);

        mvc.perform(put(TEST_URL + "/updateBoard")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testUpdateSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test06_getReplyNum() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setNo(boardNo);

        mvc.perform(post(TEST_URL + "/getReplyNum")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test06_deleteBoard() throws Exception {
        testDeleteSupport = new Support();
        testDeleteSupport.setNo(boardNo);

        mvc.perform(post(TEST_URL + "/deleteBoard")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testDeleteSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void test07_insertBoardComment() throws Exception {
        testInsertSupport = new Support();

        testInsertSupport.setUserId("test_userId");
        testInsertSupport.setContent("test_content");
        testInsertSupport.setBoardNo(boardNo);
        testInsertSupport.setParentNo(commentNo);
        testInsertSupport.setGroupNo(commentNo);

        mvc.perform(post(TEST_URL + "/insertBoardComment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testInsertSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test08_updateBoardComment() throws Exception {
        testUpdateSupport = new Support();

        testUpdateSupport.setUserId("test_userId");
        testUpdateSupport.setContent("test_content");
        testUpdateSupport.setNo(commentNo);


        mvc.perform(put(TEST_URL + "/updateBoardComment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testUpdateSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test09_getCommentReplyNum() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setNo(commentNo);

        mvc.perform(post(TEST_URL + "/getCommentReplyNum")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test09_deleteBoardComment() throws Exception {
        testDeleteSupport = new Support();
        testDeleteSupport.setNo(commentNo);

        mvc.perform(post(TEST_URL + "/deleteBoardComment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testDeleteSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
