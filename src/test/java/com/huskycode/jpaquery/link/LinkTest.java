package com.huskycode.jpaquery.link;

import com.huskycode.jpaquery.AbstractEntityManagerWired;
import com.huskycode.jpaquery.testmodel.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Varokas Panusuwan
 */
public class LinkTest extends AbstractEntityManagerWired {
    private Link<BaseClass, BaseClass, Integer> link;

    @Before
    public void before() {
        link = Link.from(ClassA_.foreignId).to(ClassB_.id);
    }

    @Test
    public void testLinkDefinition() {
        assertThat(link.getFrom(), is(ClassA_.foreignId));
        assertThat(link.getTo(), is(ClassB_.id));
    }

    @Test
    @Ignore
    public void testGetFromEntityJavaType() {
        assertThat(link.getFromEntityJavaType(), is(ClassA.class));
    }
}
