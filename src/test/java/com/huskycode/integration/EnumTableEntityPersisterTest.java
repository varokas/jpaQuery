package com.huskycode.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.huskycode.jpaquery.persister.entitycreator.EnumTableEntityPersister;
import com.huskycode.jpaquery.types.tree.EntityNodeImpl;

import com.huskycode.jpaquery.testmodel.pizza.RefVehicleType;
import com.huskycode.jpaquery.types.tree.EntityNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import org.junit.jupiter.api.Test;

public class EnumTableEntityPersisterTest {
	private EnumTableEntityPersister persister;
    private EntityManager entityManager;

    @Test
	public void testEnumPersisterGetARowFromTable() {
        entityManager = TestEntityManager.INSTANCE.getEntityManager();
        persister = new EnumTableEntityPersister(entityManager);

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();


		EntityNode n = EntityNodeImpl.newInstance(RefVehicleType.class);
		Object obj = persister.persistNode(n, new HashMap<Field, Object>());
		
		assertThat(obj, is(not(nullValue())));
		assertEquals(RefVehicleType.class, obj.getClass());

        tx.rollback();
	}

}
