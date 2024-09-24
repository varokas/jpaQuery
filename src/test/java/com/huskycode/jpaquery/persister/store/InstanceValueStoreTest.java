package com.huskycode.jpaquery.persister.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InstanceValueStoreTest {
	
	@Test
	public void testGetValueReturnCorrectValueAssociatedWithTheGivenInstance() {
		Object value = new Object();
		Integer instance1 = new Integer(1);
		Integer instance2 = new Integer(1);
		 
		InstanceValueStore<Object, Object> valueStore = InstanceValueStore.newInstance();
		valueStore.putValue(instance1, value);
		
		Assertions.assertEquals(instance1, instance2);
		Assertions.assertNotSame(instance1, instance2);
		Assertions.assertNull(valueStore.get(instance2), "Shuold return null because it is not the same instance");
		Assertions.assertSame(value, valueStore.get(instance1));
	}
}
