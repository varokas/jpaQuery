package com.huskycode.integration;

import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Varokas Panusuwan
 */
public class LinkTest {
    private Link<ClassA, ClassB, Integer> link;

    @BeforeEach
    public void setUp() throws Exception {
        //for meta model annotation
        TestEntityManager.INSTANCE.getEntityManager();
    }

    @BeforeEach
    public void before() {
        link = Link.from(ClassA.class, ClassA_.foreignId).to(ClassB.class, ClassB_.id);
    }

    @Test
    public void testLinkDefinition() throws NoSuchFieldException {
        assertThat(link.getFrom().getEntityClass(), equalTo(ClassA.class));
        assertThat(link.getTo().getEntityClass(), equalTo(ClassB.class));
        assertThat(link.getFrom().getField(), equalTo(BaseClass.class.getDeclaredField("foreignId")));
        assertThat(link.getTo().getField(), equalTo(BaseClass.class.getDeclaredField("id")));
    }
}
