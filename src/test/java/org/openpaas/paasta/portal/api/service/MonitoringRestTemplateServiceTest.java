//package org.openpaas.paasta.portal.api.service;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpMethod;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.*;
//
//public class MonitoringRestTemplateServiceTest {
//
//    @Mock
//    MonitoringRestTemplateService monitoringRestTemplateService;
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
//    @Test
//    public void testSend() throws Exception {
//        when(monitoringRestTemplateService.send(anyString(), any(), any())).thenReturn(thenReturn);
//
//        Map result = monitoringRestTemplateService.send("reqUrl", HttpMethod.GET, null);
//        Assert.assertEquals(thenReturn, result);
//    }
//
//
//}
