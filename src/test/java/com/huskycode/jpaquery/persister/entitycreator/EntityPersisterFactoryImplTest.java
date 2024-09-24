package com.huskycode.jpaquery.persister.entitycreator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import jakarta.persistence.EntityManager;

import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.types.tree.EntityNodeImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.DepsBuilder;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class EntityPersisterFactoryImplTest {

	private Class<ClassA> aClass = ClassA.class;
	private enum AnEnum { AValue }
	private EntityPersisterFactoryImpl factory;
	private EntityNode anyNode;
	private EntityManager em;

	@BeforeEach
	public void before() {
		factory = new EntityPersisterFactoryImpl();
		anyNode = EntityNodeImpl.newInstance(aClass);
		em = Mockito.mock(EntityManager.class);
	}
	
	@Test
	public void testCreateEntityPersisterReturnNewRowEntityPersisterIfNotInSetOfEnumTable() {
		DependenciesDefinition depsWithoutEnumTable = new DepsBuilder().build();
		
		assertThat(factory.createEntityPersister(anyNode, depsWithoutEnumTable, em),
				is(instanceOf(NewRowEntityPersister.class)));
	}
	
	@Test
	public void testCreateEntityPersisterReturnEnumTableIfSpecificAndNotEnumClass() {
		DependenciesDefinition depsWithMatchingEnumTable = 
				new DepsBuilder().withEnumTable(aClass).build();
		
		assertThat(factory.createEntityPersister(anyNode, depsWithMatchingEnumTable, em),
				is(instanceOf(EnumTableEntityPersister.class)));
	}
	
	@Test
    @Disabled("temporary remove support for enum class")
	public void testCreateEntityPersisterReturnEnumClassIfDeclaredSo() {
		anyNode = EntityNodeImpl.newInstance(AnEnum.class);
		DependenciesDefinition depsWithMatchingEnumClass = 
				new DepsBuilder().withEnumTable(AnEnum.class).build();
		
		assertThat(factory.createEntityPersister(anyNode, depsWithMatchingEnumClass, em),
				is(instanceOf(EnumClassEntityPersister.class)));
	}

}
