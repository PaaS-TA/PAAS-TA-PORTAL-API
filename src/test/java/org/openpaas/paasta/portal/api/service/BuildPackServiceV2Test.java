package org.openpaas.paasta.portal.api.service;

import junit.framework.Assert;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.common.Common;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest(Common.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildPackServiceV2Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildPackServiceV2Test.class);


    @Value("${cloudfoundry.cc.api.url}")
    public String apiTarget;


    @Value("${cloudfoundry.cc.api.uaaUrl}")
    public String uaaTarget;

    @Value("${cloudfoundry.cc.api.sslSkipValidation}")
    public boolean cfskipSSLValidation;

    @Value("${cloudfoundry.user.admin.username}")
    public String adminUserName;

    @Value("${cloudfoundry.user.admin.password}")
    public String adminPassword;

    public static final String AUTHORIZATION_HEADER_KEY = "cf-Authorization";

    @Value("${cloudfoundry.user.uaaClient.clientId}")
    public String uaaClientId;

    @Value("${cloudfoundry.user.uaaClient.clientSecret}")
    public String uaaClientSecret;

    @Value("${cloudfoundry.user.uaaClient.adminClientId}")
    public String uaaAdminClientId;

    @Value("${cloudfoundry.user.uaaClient.adminClientSecret}")
    public String uaaAdminClientSecret;

    @Value("${cloudfoundry.user.uaaClient.skipSSLValidation}")
    public boolean uaaskipSSLValidation;



    @Mock
    Common common = new Common();


    @InjectMocks
    private BuildPackServiceV2 buildPackServiceV2 = new BuildPackServiceV2();

    Map thenReturn;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(common, "apiTarget", apiTarget);
        ReflectionTestUtils.setField(common, "uaaTarget", uaaTarget);
        ReflectionTestUtils.setField(common, "cfskipSSLValidation", cfskipSSLValidation);
        ReflectionTestUtils.setField(common, "adminUserName", adminUserName);
        ReflectionTestUtils.setField(common, "adminPassword", adminPassword);
        ReflectionTestUtils.setField(common, "uaaClientId", uaaClientId);
        ReflectionTestUtils.setField(common, "uaaClientSecret", uaaClientSecret);
        ReflectionTestUtils.setField(common, "uaaAdminClientId", uaaAdminClientId);
        ReflectionTestUtils.setField(common, "uaaAdminClientSecret", uaaAdminClientSecret);
        ReflectionTestUtils.setField(common, "uaaskipSSLValidation", uaaskipSSLValidation);


        DefaultConnectionContext connectionContext = DefaultConnectionContext.builder().apiHost("xx.xx.xx.xxx").build();
        TokenProvider tokenProvider = mock(TokenProvider.class, RETURNS_SMART_NULLS);
        CloudFoundryClient cloudFoundryClient = mock(CloudFoundryClient.class, RETURNS_SMART_NULLS);
        ReactorCloudFoundryClient reactorCloudFoundryClient = ReactorCloudFoundryClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider).build();

        when(common.connectionContext()).thenReturn(connectionContext);
        when(common.cloudFoundryClient()).thenReturn(reactorCloudFoundryClient);

        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");


    }

    @Test
    public void testGetBuildPacks() throws Exception {
//        when(buildPackServiceV2.getBuildPacks()).thenReturn(thenReturn);

        Map<String, Object> result = buildPackServiceV2.getBuildPacks();
        Assert.assertEquals(thenReturn, result);
    }

//    @Test
//    public void testUpdateBuildPack() throws Exception {
//        BuildPack buildPack = new BuildPack();
//        buildPack.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
//        buildPack.setPosition(1);
//        buildPack.setEnable(true);
//        buildPack.setLock(true);
//        when(buildPackServiceV2.updateBuildPack(buildPack)).thenReturn(true);
//
//        boolean result = buildPackServiceV2.updateBuildPack(buildPack);
//        Assert.assertEquals(true, result);
//    }


}
//
//package org.openpaas.paasta.portal.api.service;
//
//import junit.framework.Assert;
//import org.cloudfoundry.reactor.TokenProvider;
//import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.openpaas.paasta.portal.api.common.Common;
//import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
//import org.openpaas.paasta.portal.api.controller.AppControllerV2;
//import org.openpaas.paasta.portal.api.model.BuildPack;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.mockito.Mockito.when;
//import static org.powermock.api.mockito.PowerMockito.when;
//
//@PrepareForTest(Common.class)
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class BuildPackServiceV2Test {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV2.class);
//
//    @InjectMocks
//    Common common = new Common();
//
//    @Mock
//    private BuildPackServiceV2 buildPackServiceV2 = new BuildPackServiceV2();
//
//    Map thenReturn;
//
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
//    public void testGetBuildPacks() throws Exception {
//        when(buildPackServiceV2.getBuildPacks()).thenReturn(thenReturn);
//
//        Map<String, Object> result = buildPackServiceV2.getBuildPacks();
//        Assert.assertEquals(thenReturn, result);
//    }
//
//    @Test
//    public void testUpdateBuildPack() throws Exception {
//        BuildPack buildPack = new BuildPack();
//        buildPack.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
//        buildPack.setPosition(1);
//        buildPack.setEnable(true);
//        buildPack.setLock(true);
//        when(buildPackServiceV2.updateBuildPack(buildPack)).thenReturn(true);
//
//        boolean result = buildPackServiceV2.updateBuildPack(buildPack);
//        Assert.assertEquals(true, result);
//    }
//
//
//}

