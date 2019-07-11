//package org.openpaas.paasta.portal.api.service;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.powermock.core.classloader.annotations.PowerMockIgnore;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//
//
//@ActiveProfiles("dev")
//@PowerMockIgnore({"org.apache.http.conn.ssl.*", "javax.net.ssl.*", "javax.crypto.*", "org.openpaas.paasta.portal.api.common.*", "org.openpaas.paasta.portal.api.config.*"})
//@TestPropertySource(properties = {"spring.config.location = classpath:/application.yml","eureka.client.enabled=false"}) // Push 용
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class AlarmServiceTest {
//
//    @Mock
//    AlarmService alarmService;
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
//    public void testGetAlarmList() throws Exception {
//        when(alarmService.getAlarmList(any(), any(), any(), any(), any())).thenReturn(thenReturn);
//
//        Map result = alarmService.getAlarmList("appGuid", "pageItems", "pageIndex", "resourceType", "alarmLevel");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testGetAlarm() throws Exception {
//
//        when(alarmService.getAlarm(any())).thenReturn(thenReturn);
//
//        Map result = alarmService.getAlarm("appGuid");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testUpdateAlarm() throws Exception {
//        when(alarmService.updateAlarm(any())).thenReturn(thenReturn);
//
//        Map result = alarmService.updateAlarm(new HashMap() {{
//            put("String", "String");
//        }});
//        Assert.assertEquals(thenReturn, result);
//    }
//}
//
