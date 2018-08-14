package org.openpaas.paasta.portal.api.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class EntityTest {
    @Mock
    Map<String, Object> map;
    @Mock
    Entity.Meta meta;
    @InjectMocks
    Entity entity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    public Entity setEntity(Entity entity) {

        Map map = new HashMap();
        entity.setMap(map);
        Entity.Meta meta = new Entity.Meta();
        entity.setMeta(meta);
        entity.setName("entity");
        return entity;
    }

    @Test
    public void testEntityModel() throws Exception {
        Entity entity1 = new Entity();
        entity1 = setEntity(entity1);
        Entity entity2 = new Entity();
        entity2 = setEntity(entity2);

        Assert.assertEquals(entity1.getMap(), entity2.getMap());
        Assert.assertEquals(entity1.getMeta().getCreated(), entity2.getMeta().getCreated());
        Assert.assertEquals(entity1.getMeta().getGuid(), entity2.getMeta().getGuid());
        Assert.assertEquals(entity1.getMeta().getUpdated(), entity2.getMeta().getUpdated());
        Assert.assertEquals(entity1.getMeta().getUrl(), entity2.getMeta().getUrl());
        Assert.assertEquals(entity1.getName(), entity2.getName());
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme