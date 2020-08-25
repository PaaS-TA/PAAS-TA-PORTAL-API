//package org.openpaas.paasta.portal.api.service;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//
//
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class AppAutoscalingServiceTest {
//
//    @Mock
//    AppAutoscalingService appAutoscalingService;
//
//
//    Map thenReturn;
//
//    @Before
//    public void setUp() {
//        thenReturn = new HashMap();
//        thenReturn.put("result", true);
//        thenReturn.put("msg", "You have successfully completed the task.");
//        MockitoAnnotations.initMocks(this);
//    }
//
//
//    @Test
//    public void testGetAutoscaling() throws Exception {
//
//        when(appAutoscalingService.getAutoscaling(any())).thenReturn(thenReturn);
//
//        Map result = appAutoscalingService.getAutoscaling("appGuid");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testUpdateAutoscaling() throws Exception {
//        when(appAutoscalingService.updateAutoscaling(any())).thenReturn(thenReturn);
//
//        Map result = appAutoscalingService.updateAutoscaling(thenReturn);
//        Assert.assertEquals(thenReturn, result);
//    }
//}
//
