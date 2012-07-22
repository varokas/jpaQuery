package com.huskycode.jpaquery.persister.entitycreator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.huskycode.jpaquery.AbstractEntityManagerWired;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleType;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class EnumTableEntityPersisterTest extends AbstractEntityManagerWired {
	private EnumTableEntityPersister persister;

	@Test
	public void testEnumPersisterGetARowFromTable() {
		persister = new EnumTableEntityPersister(entityManager);
		
		EntityNode n = EntityNode.newInstance(RefVehicleType.class);
		Object obj = persister.persistNode(n, new HashMap<Field, Object>());
		
		assertThat(obj, is(not(nullValue())));
		assertEquals(RefVehicleType.class, obj.getClass());
	}

}
