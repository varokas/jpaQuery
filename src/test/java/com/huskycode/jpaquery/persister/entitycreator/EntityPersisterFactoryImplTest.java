package com.huskycode.jpaquery.persister.entitycreator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class EntityPersisterFactoryImplTest {

	private EntityPersisterFactoryImpl factory;
	private DependenciesDefinition anyDeps;
	private EntityNode anyNode;

	@Before
	public void before() {
		factory = new EntityPersisterFactoryImpl();
		anyNode = EntityNode.newInstance(Integer.class);
		anyDeps = DependenciesDefinition.fromLinks(new Link[0]);
	}
	
	@Test
	public void testCreateEntityPersisterAlwaysReturnNewRowEntityPersister() {
		Assert.assertThat(factory.createEntityPersister(anyNode, anyDeps), is(instanceOf(NewRowEntityPersister.class)));
	}

}
