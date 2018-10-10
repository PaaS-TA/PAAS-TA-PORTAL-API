package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.users.GetUserResponse;
import org.cloudfoundry.uaa.users.Meta;
import org.cloudfoundry.uaa.users.Name;
import org.cloudfoundry.uaa.users.UpdateUserResponse;
import org.cloudfoundry.uaa.users.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.model.UserDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    UserService userService;

    Map thenReturn;

    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(any())).thenReturn(thenReturn);

        Map result = userService.createUser(new UserDetail(new HashMap() {{
            put("String", "String");
        }}));
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testUpdateUser() throws Exception {
        when(userService.updateUser(any(), anyString())).thenReturn(1);

        int result = userService.updateUser(new UserDetail(new HashMap() {{
            put("String", "String");
        }}), "token");
        Assert.assertEquals(1, result);
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
        when(userService.updateUserPassword(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(thenReturn);

        Map result = userService.updateUserPassword("userId", "userGuid","oldPassword", "newPassword", "token");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testResetPassword() throws Exception {
        when(userService.resetPassword(anyString(), anyString())).thenReturn(thenReturn);

        Map result = userService.resetPassword("userId", "password");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testExpiredPassword() throws Exception {
        when(userService.expiredPassword(anyString())).thenReturn(thenReturn);

        Map result = userService.expiredPassword("userGuid");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyString())).thenReturn(thenReturn);

        Map result = userService.deleteUser("userId");
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testGetUsernameFromToken() throws Exception {
        when(userService.getUsernameFromToken(anyString())).thenReturn("msg");

        String result = userService.getUsernameFromToken("token");
        Assert.assertEquals("msg", result);
    }

    @Test
    public void testGetUser() throws Exception {
        User user = User.builder().active(true).meta(Meta.builder().created("created").lastModified("lastModified").version(1).build()).name(Name.builder().build()).passwordLastModified("passwordLastModified").verified(true).zoneId("zoneId").id("id").origin("origin").userName("userName").build();
        when(userService.getUser(anyString())).thenReturn(user);

        User result = userService.getUser("token");
        Assert.assertEquals(user, result);
    }

    @Test
    public void testGetUser2() throws Exception {
        GetUserResponse getUserResponse = GetUserResponse.builder().build();
        when(userService.getUser(anyString(), anyString())).thenReturn(getUserResponse);

        GetUserResponse result = userService.getUser("userGuid", "token");
        Assert.assertEquals(getUserResponse, result);
    }

    @Test
    public void testAllUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        when(userService.allUsers()).thenReturn(userList);

        List<User> result = userService.allUsers();
        Assert.assertEquals(userList, result);
    }

    @Test
    public void testCreate() throws Exception {
        when(userService.create(any())).thenReturn(true);
        boolean result = userService.create(new HashMap() {{
            put("String", "String");
        }});
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetUserIdByUsername() throws Exception {
        when(userService.getUserIdByUsername(anyString())).thenReturn("msg");

        String result = userService.getUserIdByUsername("username");
        Assert.assertEquals("msg", result);
    }

    @Test
    public void testGetUsernameByUserId() throws Exception {
        when(userService.getUsernameByUserId(anyString())).thenReturn("msg");

        String result = userService.getUsernameByUserId("userId");
        Assert.assertEquals("msg", result);
    }

    @Test
    public void testGetUserSummary() throws Exception {
        User user = User.builder().active(true).meta(Meta.builder().created("created").lastModified("lastModified").version(1).build()).name(Name.builder().build()).passwordLastModified("passwordLastModified").verified(true).zoneId("zoneId").id("id").origin("origin").userName("userName").build();
        when(userService.getUserSummary(anyString())).thenReturn(user);

        User result = userService.getUserSummary("userId");
        Assert.assertEquals(user, result);
    }

    @Test
    public void testGetUserSummaryByUsername() throws Exception {

        User user = User.builder().active(true).meta(Meta.builder().created("created").lastModified("lastModified").version(1).build()).name(Name.builder().build()).passwordLastModified("passwordLastModified").verified(true).zoneId("zoneId").id("id").origin("origin").userName("userName").build();
        when(userService.getUserSummaryByUsername(anyString())).thenReturn(user);

        User result = userService.getUserSummaryByUsername("userId");
        Assert.assertEquals(user, result);
    }

    @Test
    public void testUpdateUserActive() throws Exception {
        UpdateUserResponse updateUserResponse = UpdateUserResponse.builder().active(true).meta(Meta.builder().created("created").lastModified("lastModified").version(1).build()).name(Name.builder().build()).passwordLastModified("passwordLastModified").verified(true).zoneId("zoneId").id("id").origin("origin").userName("userName").build();
        when(userService.UpdateUserActive(anyString())).thenReturn(updateUserResponse);

        UpdateUserResponse result = userService.UpdateUserActive("userGuid");
        Assert.assertEquals(updateUserResponse, result);
    }


}
