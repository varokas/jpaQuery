package com.huskycode.jpaquery.persister.entitycreator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.DepsBuilder;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class EntityPersisterFactoryImplTest {

	private Class<Integer> aClass = Integer.class;
	private EntityPersisterFactoryImpl factory;
	private EntityNode anyNode;
	private EntityManager em;

	@Before
	public void before() {
		factory = new EntityPersisterFactoryImpl();
		anyNode = EntityNode.newInstance(aClass);
		em = Mockito.mock(EntityManager.class);
	}
	
	@Test
	public void testCreateEntityPersisterReturnNewRowEntityPersisterIfNotInSetOfEnumTable() {
		DependenciesDefinition depsWithoutEnumTable = new DepsBuilder().build();
		
		Assert.assertThat(factory.createEntityPersister(anyNode, depsWithoutEnumTable, em),
				is(instanceOf(NewRowEntityPersister.class)));
	}
	
	@Test
	public void testCreateEntityPersisterReturnNewRowEntityPersister() {
		DependenciesDefinition depsWithMatchingEnumTable = 
				new DepsBuilder().withEnumTable(aClass).build();
		
		Assert.assertThat(factory.createEntityPersister(anyNode, depsWithMatchingEnumTable, em),
				is(instanceOf(EnumTableEntityPersister.class)));
	}

}
