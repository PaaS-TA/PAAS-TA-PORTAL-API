//package org.openpaas.paasta.portal.api.model;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Map;
//
//import static org.mockito.Mockito.*;
//
//public class QuotaTest {
//    //Field guid of type UUID - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    //Field orginazationGuid of type UUID - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    //Field spaceGuid of type UUID - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    @Mock
//    Map<String, Object> map;
//    @Mock
//    Entity.Meta meta;
//    @InjectMocks
//    Quota quota;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testToString() throws Exception {
//        when(meta.getGuid()).thenReturn(null);
//
//        String result = quota.toString();
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//}
//
////Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme