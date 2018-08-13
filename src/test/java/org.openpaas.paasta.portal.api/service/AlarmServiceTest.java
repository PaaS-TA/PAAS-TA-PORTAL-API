package org.openpaas.paasta.portal.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AlarmServiceTest extends TestConfig {

    @Mock
    AlarmService alarmService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAlarmList() throws Exception {
        Map map = new HashMap() {{
            put("String", "String");
        }};
        when(alarmService.getAlarmList(any(), any(), any(), any(), any())).thenReturn(map);

        Map result = alarmService.getAlarmList("appGuid", "pageItems", "pageIndex", "resourceType", "alarmLevel");
        Assert.assertEquals(map, result);
    }

    @Test
    public void testGetAlarm() throws Exception {
        Map map = new HashMap() {{
            put("String", "String");
        }};
        when(alarmService.getAlarm(any())).thenReturn(map);

        Map result = alarmService.getAlarm("appGuid");
        Assert.assertEquals(map, result);
    }

    @Test
    public void testUpdateAlarm() throws Exception {
        Map map = new HashMap() {{
            put("String", "String");
        }};
        when(alarmService.updateAlarm(any())).thenReturn(map);

        Map result = alarmService.updateAlarm(new HashMap() {{
            put("String", "String");
        }});
        Assert.assertEquals(map, result);
    }
}

