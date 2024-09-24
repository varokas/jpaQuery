package com.huskycode.jpaquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    @BeforeEach
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

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenEntityManagerIsNull() {
		assertThrows(IllegalArgumentException.class, () ->
			JPAQueryContext.newInstance(null, deps));
	}

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenDependenciesIsNull() {
		assertThrows(IllegalArgumentException.class, () ->
			JPAQueryContext.newInstance(entityManager, null));
	}

}
