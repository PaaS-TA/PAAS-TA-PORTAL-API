//package org.openpaas.paasta.portal.api.model;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Set;
//
//import static org.mockito.Mockito.*;
//
//public class UserRoleTest {
//    @Mock
//    Set<String> roles;
//    @InjectMocks
//    UserRole userRole;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testAddRole() throws Exception {
//        boolean result = userRole.addRole("argRole");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testEquals() throws Exception {
//        boolean result = userRole.equals(null);
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testCompareTo() throws Exception {
//        int result = userRole.compareTo(null);
//        Assert.assertEquals(0, result);
//    }
//
//    @Test
//    public void testHashCode() throws Exception {
//        int result = userRole.hashCode();
//        Assert.assertEquals(0, result);
//    }
//
//    @Test
//    public void testToString() throws Exception {
//        String result = userRole.toString();
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testBuilder() throws Exception {
//        UserRole.Builder result = UserRole.builder();
//        Assert.assertEquals(new UserRole.Builder(), result);
//    }
//
//    @Test
//    public void testFromJson() throws Exception {
//        UserRole result = UserRole.fromJson(new UserRole.Json());
//        Assert.assertEquals(null, result);
//    }
//}
//
////Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme