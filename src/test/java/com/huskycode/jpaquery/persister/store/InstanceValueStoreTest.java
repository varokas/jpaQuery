package com.huskycode.jpaquery.persister.store;

import org.junit.Assert;
import org.junit.Test;

public class InstanceValueStoreTest {
	
	@Test
	public void testGetValueReturnCorrectValueAssociatedWithTheGivenInstance() {
		Object value = new Object();
		Integer instance1 = new Integer(1);
		Integer instance2 = new Integer(1);
		 
		InstanceValueStore<Object, Object> valueStore = InstanceValueStore.newInstance();
		valueStore.putValue(instance1, value);
		
		Assert.assertEquals(instance1, instance2);
		Assert.assertNotSame(instance1, instance2);
		Assert.assertNull("Shuold return null because it is not the same instance", valueStore.get(instance2));
		Assert.assertSame(value, valueStore.get(instance1));
	}
}
