package com.huskycode.jpaquery.link;

import com.huskycode.jpaquery.AbstractEntityManagerWired;
import com.huskycode.jpaquery.testmodel.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Varokas Panusuwan
 */
public class LinkTest extends AbstractEntityManagerWired {
    private Link<ClassA, ClassB, Integer> link;

    @Before
    public void before() {
        link = Link.from(ClassA.class, ClassA_.foreignId).to(ClassB.class, ClassB_.id);
    }

    @Test
    public void testLinkDefinition() throws NoSuchFieldException {
        assertThat(link.getFrom().getEntityClass(), equalTo(ClassA.class));
        assertThat(link.getTo().getEntityClass(), equalTo(ClassB.class));
        assertThat(link.getFrom().getType(), equalTo(Integer.class));
        assertThat(link.getTo().getType(), equalTo(Integer.class));
        assertThat(link.getFrom().getField(), equalTo(BaseClass.class.getDeclaredField("foreignId")));
        assertThat(link.getTo().getField(), equalTo(BaseClass.class.getDeclaredField("id")));
    }
}
