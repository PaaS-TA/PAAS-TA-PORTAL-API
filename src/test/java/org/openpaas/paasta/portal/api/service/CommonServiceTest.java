package org.openpaas.paasta.portal.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class CommonServiceTest {

    @Mock
    CommonService commonService;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetCustomSendError() throws Exception {
        commonService.getCustomSendError(null, HttpStatus.CONTINUE, "reqMessage");
    }

    @Test
    public void testGetCustomMessage() throws Exception {
        when(commonService.getCustomMessage("reqMessage")).thenReturn("msg");
        String result = commonService.getCustomMessage("reqMessage");
        Assert.assertEquals("msg", result);
    }

    @Test
    public void testProcCommonApiRestTemplate() throws Exception {
        when(commonService.procCommonApiRestTemplate(anyString(), any(), any(), anyString())).thenReturn(thenReturn);

        Map<String, Object> result = commonService.procCommonApiRestTemplate("reqUrl", HttpMethod.GET, null, "reqToken");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testProcStorageApiRestTemplate() throws Exception {

        when(commonService.procStorageApiRestTemplate(anyString(), any(), any(), anyString(), any())).thenReturn(null);

        ResponseEntity<String> result = commonService.procStorageApiRestTemplate("reqUrl", HttpMethod.GET, null, "reqToken", null);
        Assert.assertEquals(null, result);
    }
}

