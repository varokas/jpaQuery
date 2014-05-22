package com.huskycode.jpaquery.persister.store;

import org.junit.Test;

import junit.framework.Assert;

public class InstanceWrapperTest {
	
	@Test
	public void testTwoInstanceIsEqualWhenItWrapTheSameInstance() {
		Object obj = new Object();
		InstanceWrapper<Object> one = InstanceWrapper.newInstance(obj);
		InstanceWrapper<Object> two = InstanceWrapper.newInstance(obj);
		
		Assert.assertSame(obj, one.get());
		Assert.assertSame(obj, two.get());
		Assert.assertEquals(one, two);	
	}
	
	@Test
	public void testTwoINstanceIsNotEqualWhenItWrapDifferntInstanceEvenIfTheyAreEqual() {
		Integer obj1 = new Integer(1);
		Integer obj2 = new Integer(1);
		InstanceWrapper<Integer> one = InstanceWrapper.newInstance(obj1);
		InstanceWrapper<Integer> two = InstanceWrapper.newInstance(obj2);
		
		
		Assert.assertEquals(obj1, obj2);
		Assert.assertFalse(one.equals(two));	
	}
}
