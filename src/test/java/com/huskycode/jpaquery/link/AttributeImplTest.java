package com.huskycode.jpaquery.link;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: Ta
 * Date: 3/26/12
 * Time: 8:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttributeImplTest {

    @Test
    public void testCreate() throws NoSuchFieldException {
        Class<TestClass> c = TestClass.class;
        Field field = c.getDeclaredField("a");

        Attribute<TestClass, Integer> attr = AttributeImpl.newInstance(c, field);

        Assert.assertEquals(c, attr.getEntityClass());
        Assert.assertEquals(c.getDeclaredField("a"), attr.getField());
        Assert.assertEquals(int.class, attr.getType());

    }

    private class TestClass {
        private int a;
    }

}
