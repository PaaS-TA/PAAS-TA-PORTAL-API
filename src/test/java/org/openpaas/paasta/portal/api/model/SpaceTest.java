//package org.openpaas.paasta.portal.api.model;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Date;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//public class SpaceTest {
//    //Field guid of type UUID - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
//    @Mock
//    Date created;
//    @Mock
//    Date lastModified;
//    @Mock
//    List<App> apps;
//    @Mock
//    List<Service> services;
//    @Mock
//    Space.Entity entity;
//    @InjectMocks
//    Space space;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testSetGuid() throws Exception {
//        space.setGuid(null);
//    }
//
//    @Test
//    public void testSetSpaceGuid() throws Exception {
//        space.setSpaceGuid("spaceGuid");
//    }
//}
//
////Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme