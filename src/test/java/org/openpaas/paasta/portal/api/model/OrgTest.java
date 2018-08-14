//package org.openpaas.paasta.portal.api.model;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.Mockito.*;
//
//public class OrgTest {
//    //Field guid of type UUID - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    @Mock
//    List<Space> spaces;
//    @Mock
//    Quota quota;
//    @Mock
//    Map<String, Object> map;
//    @Mock
//    Entity.Meta meta;
//    @InjectMocks
//    Org org;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testGetQuotaGuid() throws Exception {
//        when(quota.getGuid()).thenReturn(null);
//
//        String result = org.getQuotaGuid();
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testSetQuotaGuid() throws Exception {
//        when(quota.getGuid()).thenReturn(null);
//
//        org.setQuotaGuid("quotaGuid");
//    }
//
//    @Test
//    public void testToString() throws Exception {
//        when(quota.getName()).thenReturn("getNameResponse");
//        when(meta.getGuid()).thenReturn(null);
//
//        String result = org.toString();
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//}
//
////Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme