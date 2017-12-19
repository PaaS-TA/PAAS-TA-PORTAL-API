package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.common.CommonTest;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.AppAutoScale;
import org.openpaas.paasta.portal.api.model.WebIdeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ApiApplication.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebIdeUserServiceTest extends CommonTest {

    @Autowired
    private WebApplicationContext wac;

    private static MockMvc mvc;
    WebIdeUser webIdeUser = new WebIdeUser();

    @Before
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getWebIdeUser() throws Exception {
        Gson gson = new Gson();
        AppAutoScale appAutoScale = new AppAutoScale();
        appAutoScale.setGuid("guid");
        mvc.perform(post("/webIdeUser/getUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(appAutoScale)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void a_insertWebIdeUser() throws Exception {
        Gson gson = new Gson();

        webIdeUser.setUserId("test_id");
        webIdeUser.setOrgName("test_org");

        mvc.perform(post("/webIdeUser/insertUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(webIdeUser)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void x_updateWebIdeUser() throws Exception {
        Gson gson = new Gson();

        webIdeUser.setUserId("test_id");
        webIdeUser.setOrgName("test_org");
        webIdeUser.setUseYn("Y");

        mvc.perform(post("/webIdeUser/updateUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(webIdeUser)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void z_deleteWebIdeUser() throws Exception {
        Gson gson = new Gson();

        webIdeUser.setUserId("test_id");
        webIdeUser.setOrgName("test_org");

        mvc.perform(post("/webIdeUser/deleteUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(webIdeUser)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}