//package org.openpaas.paasta.portal.api.service;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class AppAutoscalingServiceTest {
//
//    @MockBean
//    AppAutoscalingService appAutoscalingService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testGetAutoscaling() throws Exception {
//        Map map = new HashMap() {{
//            put("String", "String");
//        }};
//
//        when(appAutoscalingService.getAutoscaling(any())).thenReturn(map);
//
//        Map result = appAutoscalingService.getAutoscaling("appGuid");
//        Assert.assertEquals(map, result);
//    }
//
//    @Test
//    public void testUpdateAutoscaling() throws Exception {
//        Map map = new HashMap() {{
//            put("String", "String");
//        }};
//        when(appAutoscalingService.updateAutoscaling(any())).thenReturn(map);
//
//        Map result = appAutoscalingService.updateAutoscaling(new HashMap() {{
//            put("String", "String");
//        }});
//        Assert.assertEquals(map, result);
//    }
//}
//
