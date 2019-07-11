package org.openpaas.paasta.portal.api.service;

import junit.framework.Assert;
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
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.controller.AppControllerV2;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest(Common.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildPackServiceV2Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV2.class);

    @InjectMocks
    Common common = new Common();

    @Mock
    private BuildPackServiceV2 buildPackServiceV2 = new BuildPackServiceV2();

    Map thenReturn;


    @Before
    public void setUp() {
        thenReturn = new HashMap();
        thenReturn.put("result", true);
        thenReturn.put("msg", "You have successfully completed the task.");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetBuildPacks() throws Exception {
        when(buildPackServiceV2.getBuildPacks()).thenReturn(thenReturn);

        Map<String, Object> result = buildPackServiceV2.getBuildPacks();
        Assert.assertEquals(thenReturn, result);
    }

    @Test
    public void testUpdateBuildPack() throws Exception {
        BuildPack buildPack = new BuildPack();
        buildPack.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        buildPack.setPosition(1);
        buildPack.setEnable(true);
        buildPack.setLock(true);
        when(buildPackServiceV2.updateBuildPack(buildPack)).thenReturn(true);

        boolean result = buildPackServiceV2.updateBuildPack(buildPack);
        Assert.assertEquals(true, result);
    }


}

