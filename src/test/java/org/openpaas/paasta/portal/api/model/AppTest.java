package org.openpaas.paasta.portal.api.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AppTest {


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    public App setApp(App app) {
        app.setAppInstanceIndex(1);
        app.setBuildPack("buildpack");
        app.setUpdatedAt("20180813");
        app.setCreatedAt("20180813");
        app.setDiskQuota(1024);
        app.setDomainName("xxx.xxx.xxx.xxx.xip.io");
        app.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        app.setHost("APP");
        app.setInstances(1);
        app.setMemory(1024);
        app.setName("APP");
        app.setNewName("NEW_APP");
        app.setOrgName("ORG");
        app.setServiceGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        app.setServiceName("SERVICE");
        app.setServiceNewName("NEW_SERVICE");
        app.setSpaceName("SPACE");
        app.setStackName("STACK");
        app.setState("STATE");
        app.setTotalUserCount(1);
        app.setUrls(Arrays.<String>asList("url"));
        app.setStaging(null);
        Map env = new HashMap();
        app.setEnvironment(env);
        app.setServices(Arrays.<String>asList("service"));

        return app;
    }

    @Test
    public void testAppModel() {
        App app1 = new App();
        app1 = setApp(app1);

        App app2 = new App();
        app2 = setApp(app2);


        Assert.assertEquals(app1.getAppInstanceIndex(), app2.getAppInstanceIndex());
        Assert.assertEquals(app1.getBuildPack(), app2.getBuildPack());
        Assert.assertEquals(app1.getCreatedAt(), app2.getCreatedAt());
        Assert.assertEquals(app1.getDiskQuota(), app2.getDiskQuota());
        Assert.assertEquals(app1.getDomainName(), app2.getDomainName());
        Assert.assertEquals(app1.getEnvironment(), app2.getEnvironment());
        Assert.assertEquals(app1.getGuid(), app2.getGuid());
        Assert.assertEquals(app1.getHost(), app2.getHost());
        Assert.assertEquals(app1.getInstances(), app2.getInstances());
        Assert.assertEquals(app1.getMemory(), app2.getMemory());
        Assert.assertEquals(app1.getName(), app2.getName());
        Assert.assertEquals(app1.getNewName(), app2.getNewName());
        Assert.assertEquals(app1.getOrgName(), app2.getOrgName());
        Assert.assertEquals(app1.getServiceGuid(), app2.getServiceGuid());
        Assert.assertEquals(app1.getServices(), app2.getServices());
        Assert.assertEquals(app1.getSpaceName(), app2.getSpaceName());
        Assert.assertEquals(app1.getStackName(), app2.getStackName());
        Assert.assertEquals(app1.getStaging(), app2.getStaging());
        Assert.assertEquals(app1.getState(), app2.getState());
        Assert.assertEquals(app1.getTotalUserCount(), app2.getTotalUserCount());
        Assert.assertEquals(app1.getUpdatedAt(), app2.getUpdatedAt());
        Assert.assertEquals(app1.getUrls(), app2.getUrls());

    }

}

