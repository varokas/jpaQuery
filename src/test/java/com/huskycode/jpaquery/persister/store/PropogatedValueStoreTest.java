package com.huskycode.jpaquery.persister.store;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * @author Varokas Panusuwan
 */
public class PropogatedValueStoreTest {
	private PropogatedValueStore<EntityNode, Field, Object> valueStore;
	private EntityNode node;
	
	@Before
	public void before() {
		valueStore = PropogatedValueStore.newInstance();
		node = EntityNode.newInstance(Integer.class);
	}
	
	@Test
	public void testGetNonExistingKeyReturnEmptyMap() {
		Map<Field, Object> result = valueStore.get(node);
		
		assertThat(result, is(not(nullValue())));
		assertThat(result.size(), is(0));
	}
	
	@Test
	public void testPutAndThenGetReturnCorrectValue() {
		Field f = Integer.class.getDeclaredFields()[0];
		Object value = 1;
		
		valueStore.putValue(node, f, value);
		Map<Field, Object> retVal = valueStore.get(node);
		
		assertThat(retVal.size(), is(1));
		assertThat(retVal.get(f), is(value));
	}
	
	@Test
	public void testPutTwoValuesGetsStoredCorrectly() {
		Field f1 = Integer.class.getDeclaredFields()[0];
		Object v1 = 1;
		
		Field f2 = String.class.getDeclaredFields()[0];
		Object v2 = 2;
		
		valueStore.putValue(node, f1, v1);
		valueStore.putValue(node, f2, v2);
		Map<Field, Object> retVal = valueStore.get(node);
		
		assertThat(retVal.size(), is(2));
		assertThat(retVal.get(f1), is(v1));
		assertThat(retVal.get(f2), is(v2));
	}

}
