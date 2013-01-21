package com.huskycode.jpaquery;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.solver.CommandNodesIndexBuilder;

/**
 * @author Varokas Panusuwan
 */
public class JPAQueryContextTest {
    private EntityManager entityManager;
    private DependenciesDefinition deps;
    private JPAQueryContext jpaContext;
    private RandomValuePopulator randomValuePopulator;
    private CommandNodesIndexBuilder indexBuilder;
    @Before
    public void before() {
        entityManager = mock(EntityManager.class);
        deps = mock(DependenciesDefinition.class);
        randomValuePopulator = mock(RandomValuePopulator.class);
        indexBuilder = mock(CommandNodesIndexBuilder.class);
    }

    @Test
    public void shouldCreateWithGivenEntityManager() {
        jpaContext = JPAQueryContext.newInstance(entityManager, deps);
        assertThat(jpaContext.getEntityManager(), is(equalTo(entityManager)));
    }

    @Test
    public void shouldCreateWithGivenDependencies() {
        jpaContext = JPAQueryContext.newInstance(entityManager, deps);
        assertThat(jpaContext.getDependenciesDefinition(), is(equalTo(deps)));
    }

    @Test
    public void shouldCreateWithRandomValueGeneratorNotNull() {
        jpaContext = JPAQueryContext.newInstance(entityManager, deps);
        assertThat(jpaContext.getRandomValuePopulator(), is(not(nullValue())));
    }

    @Test
    public void shouldCreateWithGivenRandomValueGenerator() {
        jpaContext = JPAQueryContext.newInstance(entityManager, deps, randomValuePopulator);
        assertThat(jpaContext.getRandomValuePopulator(), is(equalTo(randomValuePopulator)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenEntityManagerIsNull() {
        JPAQueryContext.newInstance(null, deps);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenDependenciesIsNull() {
        JPAQueryContext.newInstance(entityManager, null);
    }

}
