package org.openpaas.paasta.portal.api.controller;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.openpaas.paasta.portal.api.model.AppAutoScale;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ijlee on 2016-07-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {ApiApplication.class})
@WebAppConfiguration
@TransactionConfiguration(defaultRollback = true)
@Transactional("portalTransactionManager")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppAutoScaleModalControllerTest {

    private static MockMvc mvc;
    AppAutoScale appAutoScale  = new AppAutoScale();
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void a_insertAppAutoScale() throws Exception {
        Gson gson = new Gson();

        appAutoScale.setGuid("guid");
        appAutoScale.setOrg("org");
        appAutoScale.setSpace("space");
        appAutoScale.setApp("name");
        appAutoScale.setInstanceMinCnt(2);
        appAutoScale.setInstanceMaxCnt(10);
        appAutoScale.setCpuThresholdMinPer(0.2);
        appAutoScale.setCpu_threshold_max_per(0.95);
        appAutoScale.setCheckTimeSec(5);
        mvc.perform(post("/app/insertAppAutoScale")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(appAutoScale)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateAppAutoScale() throws Exception{

        appAutoScale.setGuid("guid");
        appAutoScale.setOrg("org");
        appAutoScale.setSpace("space");
        appAutoScale.setApp("name");
        appAutoScale.setInstanceMinCnt(2);
        appAutoScale.setInstanceMaxCnt(10);
        appAutoScale.setCpuThresholdMinPer(0.2);
        appAutoScale.setCpu_threshold_max_per(0.95);
        appAutoScale.setCheckTimeSec(5);
        Gson gson = new Gson();
        mvc.perform(post("/app/updateAppAutoScale")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(appAutoScale)))
                .andExpect(status().isOk())
                .andDo(print());
    }




    @Test
    public void getAppAutoScaleInfo() throws Exception {
        Gson gson = new Gson();
        AppAutoScale appAutoScale  = new AppAutoScale();
        appAutoScale.setGuid("guid");
        mvc.perform(post("/app/getAppAutoScaleInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(appAutoScale)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    public void z_deleteAppAutoScale() throws Exception {
        Gson gson = new Gson();
        appAutoScale.setGuid("guid");
        mvc.perform(post("/app/deleteAppAutoScale")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(appAutoScale)))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

}