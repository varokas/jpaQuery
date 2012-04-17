package com.huskycode.jpaquery;

import com.huskycode.jpaquery.random.RandomValuePopulator;
import com.huskycode.jpaquery.testmodel.EntityWithFields;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Varokas Panusuwan
 */
public class JPAQueryContextTest {
    private EntityManager entityManager;
    private DependenciesDefinition deps;
    private JPAQueryContext jpaContext;
    private RandomValuePopulator randomValuePopulator;

    @Before
    public void before() {
        entityManager = mock(EntityManager.class);
        deps = mock(DependenciesDefinition.class);
        randomValuePopulator = mock(RandomValuePopulator.class);
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
