package com.huskycode.jpaquery.jpa;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author varokas
 */
public class JPADepsBuilderTest {
    private JPADepsBuilder jpaDepsBuilder = new JPADepsBuilder();

    @Test
    public void testDepsBuilderBuildDepsContext() {
        DependenciesContext dependenciesContext = jpaDepsBuilder.build();

        assertThat(dependenciesContext.getDependenciesDefinition(), notNullValue());
        assertThat(dependenciesContext.getRowPersister(), notNullValue());
    }
}
