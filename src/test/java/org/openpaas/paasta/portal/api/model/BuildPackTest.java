package org.openpaas.paasta.portal.api.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

public class BuildPackTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    public BuildPack setBuildpacks(BuildPack buildPack) {
        buildPack.setName("buildpacks");
        buildPack.setEnable(true);
        buildPack.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        buildPack.setLock(true);
        buildPack.setPosition(1);
        return buildPack;
    }

    @Test
    public void testBuildpackModel() {
        BuildPack buildPack1 = new BuildPack();
        buildPack1 = setBuildpacks(buildPack1);

        BuildPack buildPack2 = new BuildPack();
        buildPack2 = setBuildpacks(buildPack2);


        Assert.assertEquals(buildPack1.getEnable(), buildPack2.getEnable());
        Assert.assertEquals(buildPack1.getGuid(), buildPack2.getGuid());
        Assert.assertEquals(buildPack1.getLock(), buildPack2.getLock());
        Assert.assertEquals(buildPack1.getName(), buildPack2.getName());
        Assert.assertEquals(buildPack1.getPosition(), buildPack2.getPosition());
    }
}

