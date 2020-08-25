//package org.openpaas.paasta.portal.api.service;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//
//import java.util.Date;
//import java.util.Map;
//import java.util.Set;
//
//import static org.mockito.Mockito.*;
//
//public class LoginServiceV1Test {
//
//    @Mock
//    LoginServiceV1 loginServiceV1;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testLogin() throws Exception {
//        when(loginServiceV1.getTargetURL(any())).thenReturn(null);
//
//        OAuth2AccessToken result = loginServiceV1.login("id", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testRefresh() throws Exception {
//        when(loginServiceV1.refresh(anyString(), anyString())).thenReturn(null);
//
//        OAuth2AccessToken result = loginServiceV1.refresh("token", "refreshToken");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testRefresh2() throws Exception {
//        when(loginServiceV1.refresh(any(OAuth2AccessToken.class))).thenReturn(null);
//
//        OAuth2AccessToken result = loginServiceV1.refresh(new OAuth2AccessToken() {
//            @Override
//            public Map<String, Object> getAdditionalInformation() {
//                return null;
//            }
//
//            @Override
//            public Set<String> getScope() {
//                return null;
//            }
//
//            @Override
//            public OAuth2RefreshToken getRefreshToken() {
//                return null;
//            }
//
//            @Override
//            public String getTokenType() {
//                return null;
//            }
//
//            @Override
//            public boolean isExpired() {
//                return false;
//            }
//
//            @Override
//            public Date getExpiration() {
//                return null;
//            }
//
//            @Override
//            public int getExpiresIn() {
//                return 0;
//            }
//
//            @Override
//            public String getValue() {
//                return null;
//            }
//        });
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testRefresh3() throws Exception {
//        when(loginServiceV1.refresh(anyString())).thenReturn(null);
//
//        OAuth2AccessToken result = loginServiceV1.refresh("oldToken");
//        Assert.assertEquals(null, result);
//    }
//
//
//}
//
