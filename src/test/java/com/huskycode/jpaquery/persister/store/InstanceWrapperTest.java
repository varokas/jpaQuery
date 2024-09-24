package com.huskycode.jpaquery.persister.store;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class InstanceWrapperTest {
	
	@Test
	public void testTwoInstanceIsEqualWhenItWrapTheSameInstance() {
		Object obj = new Object();
		InstanceWrapper<Object> one = InstanceWrapper.newInstance(obj);
		InstanceWrapper<Object> two = InstanceWrapper.newInstance(obj);
		
		assertSame(obj, one.get());
		assertSame(obj, two.get());
		assertEquals(one, two);	
	}
	
	@Test
	public void testTwoInstanceIsNotEqualWhenItWrapDifferntInstanceEvenIfTheyAreEqual() {
		Integer obj1 = new Integer(1);
		Integer obj2 = new Integer(1);
		InstanceWrapper<Integer> one = InstanceWrapper.newInstance(obj1);
		InstanceWrapper<Integer> two = InstanceWrapper.newInstance(obj2);
		
		
		assertEquals(obj1, obj2);
		assertFalse(one.equals(two));	
	}
}
