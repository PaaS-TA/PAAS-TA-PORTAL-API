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
public class SupportNoticeServiceTest {

    private static final String AUTHORIZATION_HEADER_KEY = "cf-Authorization";

    //  @Autowired
    //  SupportNoticeService supportNoticeService;

    //@Autowired
    private static Gson gson = new Gson();
    private static String TEST_URL = "/support";
    private static Support testInitSupport = null;
    private static Support testSelectSupport = null;
    private static Support testInsertSupport = null;
    private static Support testUpdateSupport = null;
    private static Support testDeleteSupport = null;
    private static int noticeNo = 35;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;

    @BeforeClass
    public static void init() throws Exception {
        testInitSupport = new Support();
        testInitSupport.setTitle("test_title");
        testInitSupport.setImportant("Y");
        testInitSupport.setClassification("test_classification");
        testInitSupport.setUseYn("Y");
        testInitSupport.setContent("test_content");
        testInitSupport.setFileName("test_fileName");
        testInitSupport.setFilePath("test_filePath");
        testInitSupport.setStartDate("2016/01/01");
        testInitSupport.setEndDate("2016/01/01");
        testInitSupport.setSearchStartDate("2016/01/01");
        testInitSupport.setSearchEndDate("2016/01/01");
    }

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

        mvc.perform(post(TEST_URL + "/insertNotice")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testInitSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test01_getNoticeList() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setSearchTypeColumn("ALL");
        testSelectSupport.setSearchTypeUseYn("Y");
        testSelectSupport.setSearchStartDate("2016/08/01");
        testSelectSupport.setSearchEndDate("2016/08/31");
        testSelectSupport.setSearchKeyword("test");

        mvc.perform(post(TEST_URL + "/getNoticeList")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test02_getNotice() throws Exception {
        testSelectSupport = new Support();
        testSelectSupport.setNo(noticeNo);

        mvc.perform(post(TEST_URL + "/getNotice")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testSelectSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test03_insertNotice() throws Exception {
        testInsertSupport = new Support();
        testInsertSupport.setTitle("test_title");
        testInsertSupport.setImportant("N");
        testInsertSupport.setClassification("test_class");
        testInsertSupport.setUseYn("Y");
        testInsertSupport.setFileName("test_fName");
        testInsertSupport.setFilePath("test_fPath");
        testInsertSupport.setStartDate("2016/01/01");
        testInsertSupport.setEndDate("2016/01/01");
        testInsertSupport.setSearchStartDate("2016/01/01");
        testInsertSupport.setSearchEndDate("2016/01/01");


        mvc.perform(post(TEST_URL + "/insertNotice")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testInsertSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test04_updateNotice() throws Exception {
        testUpdateSupport = new Support();
        testUpdateSupport.setNo(noticeNo);
        testUpdateSupport.setTitle("test_title");
        testUpdateSupport.setImportant("N");
        testUpdateSupport.setClassification("test_class");
        testUpdateSupport.setUseYn("Y");
        testUpdateSupport.setFileName("test_fName");
        testUpdateSupport.setFilePath("test_fPath");
        testUpdateSupport.setStartDate("2016/01/01");
        testUpdateSupport.setEndDate("2016/01/01");

        mvc.perform(put(TEST_URL + "/updateNotice")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testUpdateSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void test05_deleteNotice() throws Exception {
        testDeleteSupport = new Support();
        testDeleteSupport.setNo(noticeNo);

        mvc.perform(post(TEST_URL + "/deleteNotice")
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER_KEY, "")
                .content(gson.toJson(testDeleteSupport)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

//    @Test
//    public void test06_uploadAndDeleteFileByUrl_200OK() throws Exception {
//        String url      = "/support/uploadFile/";
//
//        File file = new File("./src/test/java/resources/images/test.jpg");
//
//        FileInputStream input = new FileInputStream(file);
//        MockMultipartFile multipartFile = new MockMultipartFile("file",
//                file.getName(), "text/plain", IOUtils.toByteArray(input));
//
//        MediaType mediaType = new MediaType("multipart", "form-data");
//
//
//        MvcResult result = mvc.perform(fileUpload(url).file(multipartFile)
//                .contentType(mediaType))
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(SupportNoticeController.class))
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andDo(print())
//                .andReturn();
//
//        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
//        String path = (String) json.get("path");
//        testSelectSupport = new Support();
//        testSelectSupport.setFilePath(path);
//
//        url      = "/support/deleteFile/";
//
//        mvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(AUTHORIZATION_HEADER_KEY, "")
//                .content(gson.toJson(testSelectSupport)))
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(SupportNoticeController.class))
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andDo(print());
//
//    }
//

}
