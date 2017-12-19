//package org.openpaas.paasta.portal.api.controller;
//
//import com.google.gson.Gson;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.openpaas.paasta.portal.api.config.ApiApplication;
//import org.openpaas.paasta.portal.api.common.CommonTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.hamcrest.Matchers.notNullValue;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {ApiApplication.class})
//@WebAppConfiguration
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
////@ActiveProfiles("local")
//public class LoginControllerTest extends CommonTest{
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    private static MockMvc mvc;
//
//    @Before
//    public void init() {
//        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
//    }
//
//    @Test
//    public void login() throws Exception {
//
//        Gson gson = new Gson();
//        Map<String, Object> body  = new HashMap<>();
//
//
//        body.put("id", getPropertyValue("test.admin.id"));
//        body.put("password", getPropertyValue("test.admin.password"));
//
//
//        mvc.perform(post("/login")
//                .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(body)))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//}