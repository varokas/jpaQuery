package com.huskycode.jpaquery.persister;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.huskycode.jpaquery.random.RandomValuePopulator;
import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.testmodel.EntityWithFields;
import com.huskycode.jpaquery.types.tree.CreationTree;
import com.huskycode.jpaquery.types.tree.PersistedTree;

public class PersisterImplTest {
	private PersisterImpl persister;
	private BeanCreator beanCreator;
	private RandomValuePopulator randomValuePopulator;
	private EntityManager em;

	@Before
	public void before() {
	    em = Mockito.mock(EntityManager.class);
		beanCreator = new BeanCreator();
		randomValuePopulator = Mockito.mock(RandomValuePopulator.class);
		
	    persister = new PersisterImpl(em, beanCreator, randomValuePopulator);
	}
	
	@Test
	public void testPersistValuePersistTheRootFromCreationTree() {
		List<CreationTree> creationTrees =
				Arrays.asList(CreationTree.newInstance(ClassA.class));
		
		List<PersistedTree> persistedTree = persister.persistValues(creationTrees);
		assertThat(persistedTree.size(), is(1));
		assertThat(persistedTree.get(0).getPersistedObjects().size(), is(1));
		assertThat(persistedTree.get(0).getPersistedObjects().get(0), CoreMatchers.instanceOf(ClassA.class));
	}
	
	@Test
	public void testPersistValueCallsRandomValue() throws IllegalAccessException {
		List<CreationTree> creationTrees =
				Arrays.asList(CreationTree.newInstance(ClassA.class));
		
		persister.persistValues(creationTrees);
		
		Mockito.verify(randomValuePopulator, Mockito.times(1)).populateValue(Mockito.any(ClassA.class));
	}
	
	@Test
	public void testPersistValuePersistAGivenClass() throws IllegalAccessException {
		List<CreationTree> creationTrees =
				Arrays.asList(CreationTree.newInstance(ClassA.class));
		
		persister.persistValues(creationTrees);
		
		verify(em, times(1)).persist(Mockito.any(ClassA.class));
	}

}
