//package org.openpaas.paasta.portal.api.service;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.*;
//
//public class MonitoringServiceTest {
//
//    @Mock
//    MonitoringService monitoringService;
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
//    public void testGetCpuUsage() throws Exception {
//        when(monitoringService.getCpuUsage(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(thenReturn);
//
//        Map result = monitoringService.getCpuUsage("guid", 0L, "defaultTimeRange", "groupBy", "type");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testGetMemoryUsage() throws Exception {
//        when(monitoringService.getMemoryUsage(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(thenReturn);
//
//        Map result = monitoringService.getMemoryUsage("guid", 0L, "defaultTimeRange", "groupBy", "type");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testGetNetworkByte() throws Exception {
//        when(monitoringService.getNetworkByte(anyString(), anyInt(), anyString(), anyString(), anyString())).thenReturn(thenReturn);
//
//        Map result = monitoringService.getNetworkByte("guid", 0L, "defaultTimeRange", "groupBy", "type");
//        Assert.assertEquals(thenReturn, result);
//    }
//}
//
