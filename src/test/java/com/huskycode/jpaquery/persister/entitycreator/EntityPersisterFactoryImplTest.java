package com.huskycode.jpaquery.persister.entitycreator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.DepsBuilder;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class EntityPersisterFactoryImplTest {

	private EntityPersisterFactoryImpl factory;
	private DependenciesDefinition anyDeps;
	private EntityNode anyNode;
	private EntityManager em;

	@Before
	public void before() {
		factory = new EntityPersisterFactoryImpl();
		anyNode = EntityNode.newInstance(Integer.class);
		anyDeps = new DepsBuilder().build();
		em = Mockito.mock(EntityManager.class);
	}
	
	@Test
	public void testCreateEntityPersisterAlwaysReturnNewRowEntityPersister() {
		Assert.assertThat(factory.createEntityPersister(anyNode, anyDeps, em),
				is(instanceOf(NewRowEntityPersister.class)));
	}

}
