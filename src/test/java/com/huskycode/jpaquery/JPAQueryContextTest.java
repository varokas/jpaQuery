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
    private JPAQueryContext jpaContext;
    private RandomValuePopulator randomValuePopulator;

    @Before
    public void before() {
        entityManager = mock(EntityManager.class);
        randomValuePopulator = mock(RandomValuePopulator.class);
    }

    @Test
    public void shouldCreateWithGivenEntityManager() {
        jpaContext = JPAQueryContext.newInstance(entityManager);
        assertThat(jpaContext.getEntityManager(), is(equalTo(entityManager)));
    }

    @Test
    public void shouldCreateWithRandomValueGeneratorNotNull() {
        jpaContext = JPAQueryContext.newInstance(entityManager);
        assertThat(jpaContext.getRandomValuePopulator(), is(not(nullValue())));
    }

    @Test
    public void shouldCreateWithGivenRandomValueGenerator() {
        jpaContext = JPAQueryContext.newInstance(entityManager, randomValuePopulator);
        assertThat(jpaContext.getRandomValuePopulator(), is(equalTo(randomValuePopulator)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenEntityManagerIsNull() {
        JPAQueryContext.newInstance(null);
    }

    @Test
    public void shouldTryToPersistAClassToCreateIfNoDependencies() {
        JPAQueryContext
                .newInstance(entityManager)
                .create(EntityWithFields.class);

        verify(entityManager, times(1)).persist(Matchers.any(EntityWithFields.class));
    }

    @Test(expected = EntityInstantiationException.class)
    public void shouldThrowExceptionIfCannotInstantiateClass() {
        JPAQueryContext
                .newInstance(entityManager)
                .create(NoPublicConstructor.class);
    }

    @Test
    public void shouldRandomValueOfTheClassBeingCreated() throws ClassNotFoundException, IllegalAccessException {
        JPAQueryContext
                .newInstance(entityManager, randomValuePopulator)
                .create(EntityWithFields.class);

        verify(randomValuePopulator, times(1)).populateValue(any(EntityWithFields.class));
    }

    /** A class with no public constructor for test */
    private class NoPublicConstructor{ private NoPublicConstructor() {} }
}
