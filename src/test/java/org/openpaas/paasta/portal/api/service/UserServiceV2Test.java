//package org.openpaas.paasta.portal.api.service;
//
//import org.cloudfoundry.client.v2.users.GetUserResponse;
//import org.cloudfoundry.uaa.users.Meta;
//import org.cloudfoundry.uaa.users.Name;
//import org.cloudfoundry.uaa.users.UpdateUserResponse;
//import org.cloudfoundry.uaa.users.User;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.openpaas.paasta.portal.api.model.UserDetail;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//
//public class UserServiceV2Test {
//
//    @Mock
//    UserServiceV2 userServiceV2;
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
//    public void testCreateUser() throws Exception {
//        when(userServiceV2.createUser(any())).thenReturn(thenReturn);
//
//        Map result = userServiceV2.createUser(new UserDetail(new HashMap() {{
//            put("String", "String");
//        }}));
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testUpdateUser() throws Exception {
//        when(userServiceV2.updateUser(any(), anyString())).thenReturn(1);
//
//        int result = userServiceV2.updateUser(new UserDetail(new HashMap() {{
//            put("String", "String");
//        }}), "token");
//        Assert.assertEquals(1, result);
//    }
//
//    @Test
//    public void testUpdateUserPassword() throws Exception {
//        when(userServiceV2.updateUserPassword(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(thenReturn);
//
//        Map result = userServiceV2.updateUserPassword("userId", "userGuid","oldPassword", "newPassword", "token");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testResetPassword() throws Exception {
//        when(userServiceV2.resetPassword(anyString(), anyString())).thenReturn(thenReturn);
//
//        Map result = userServiceV2.resetPassword("userId", "password");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testExpiredPassword() throws Exception {
//        when(userServiceV2.expiredPassword(anyString())).thenReturn(thenReturn);
//
//        Map result = userServiceV2.expiredPassword("userGuid");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testDeleteUser() throws Exception {
//        when(userServiceV2.deleteUser(anyString())).thenReturn(thenReturn);
//
//        Map result = userServiceV2.deleteUser("userId");
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testGetUsernameFromToken() throws Exception {
//        when(userServiceV2.getUsernameFromToken(anyString())).thenReturn("msg");
//
//        String result = userServiceV2.getUsernameFromToken("token");
//        Assert.assertEquals("msg", result);
//    }
//
//    @Test
//    public void testGetUser() throws Exception {
//        User user = User.builder().active(true).meta(Meta.builder().created("created").lastModified("lastModified").version(1).build()).name(Name.builder().build()).passwordLastModified("passwordLastModified").verified(true).zoneId("zoneId").id("id").origin("origin").userName("userName").build();
//        when(userServiceV2.getUser(anyString())).thenReturn(user);
//
//        User result = userServiceV2.getUser("token");
//        Assert.assertEquals(user, result);
//    }
//
//    @Test
//    public void testGetUser2() throws Exception {
//        GetUserResponse getUserResponse = GetUserResponse.builder().build();
//        when(userServiceV2.getUser(anyString(), anyString())).thenReturn(getUserResponse);
//
//        GetUserResponse result = userServiceV2.getUser("userGuid", "token");
//        Assert.assertEquals(getUserResponse, result);
//    }
//
//    @Test
//    public void testAllUsers() throws Exception {
//        List<User> userList = new ArrayList<>();
//        when(userServiceV2.allUsers()).thenReturn(userList);
//
//        List<User> result = userServiceV2.allUsers();
//        Assert.assertEquals(userList, result);
//    }
//
//    @Test
//    public void testCreate() throws Exception {
//        when(userServiceV2.create(any())).thenReturn(true);
//        boolean result = userServiceV2.create(new HashMap() {{
//            put("String", "String");
//        }});
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testGetUserIdByUsername() throws Exception {
//        when(userServiceV2.getUserIdByUsername(anyString())).thenReturn("msg");
//
//        String result = userServiceV2.getUserIdByUsername("username");
//        Assert.assertEquals("msg", result);
//    }
//
//    @Test
//    public void testGetUsernameByUserId() throws Exception {
//        when(userServiceV2.getUsernameByUserId(anyString())).thenReturn("msg");
//
//        String result = userServiceV2.getUsernameByUserId("userId");
//        Assert.assertEquals("msg", result);
//    }
//
//    @Test
//    public void testGetUserSummary() throws Exception {
//        User user = User.builder().active(true).meta(Meta.builder().created("created").lastModified("lastModified").version(1).build()).name(Name.builder().build()).passwordLastModified("passwordLastModified").verified(true).zoneId("zoneId").id("id").origin("origin").userName("userName").build();
//        when(userServiceV2.getUserSummary(anyString())).thenReturn(user);
//
//        User result = userServiceV2.getUserSummary("userId");
//        Assert.assertEquals(user, result);
//    }
//
//    @Test
//    public void testGetUserSummaryByUsername() throws Exception {
//
//        User user = User.builder().active(true).meta(Meta.builder().created("created").lastModified("lastModified").version(1).build()).name(Name.builder().build()).passwordLastModified("passwordLastModified").verified(true).zoneId("zoneId").id("id").origin("origin").userName("userName").build();
//        when(userServiceV2.getUserSummaryByUsername(anyString())).thenReturn(user);
//
//        User result = userServiceV2.getUserSummaryByUsername("userId");
//        Assert.assertEquals(user, result);
//    }
//
//    @Test
//    public void testUpdateUserActive() throws Exception {
//        UpdateUserResponse updateUserResponse = UpdateUserResponse.builder().active(true).meta(Meta.builder().created("created").lastModified("lastModified").version(1).build()).name(Name.builder().build()).passwordLastModified("passwordLastModified").verified(true).zoneId("zoneId").id("id").origin("origin").userName("userName").build();
//        when(userServiceV2.UpdateUserActive(anyString())).thenReturn(updateUserResponse);
//
//        UpdateUserResponse result = userServiceV2.UpdateUserActive("userGuid");
//        Assert.assertEquals(updateUserResponse, result);
//    }
//
//
//}
