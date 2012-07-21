package com.huskycode.jpaquery.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class CollectionUtilTest {
	
	@Test
	public void testContainAnyReturnTrueWhenContainAnySharedObject() {
		Object shareObject = new Object();
		
		Set<Object> c1 = new HashSet<Object>();
		c1.add(shareObject);
		List<Object> c2 = new ArrayList<Object>();
		c2.add(shareObject);
		
		Assert.assertTrue(CollectionUtil.containAny(c1, c2));
		Assert.assertTrue(CollectionUtil.containAny(c2, c1));
	}
	
	@Test
	public void testContainAnyReturnFalseWhenAtLeaseOneOfThemEmpty() {
		Set<Object> c1 = new HashSet<Object>();
		List<Object> c2 = new ArrayList<Object>();
		
		Assert.assertFalse(CollectionUtil.containAny(c1, c2));
		Assert.assertFalse(CollectionUtil.containAny(c2, c1));
		
		c2.add(new Object());
		
		Assert.assertFalse(CollectionUtil.containAny(c1, c2));
		Assert.assertFalse(CollectionUtil.containAny(c2, c1));
	}
	
	@Test
	public void testContainAnyReturnFalseWhenNoShareObjectContained() {
		Set<Object> c1 = new HashSet<Object>();
		c1.add(new Object());
		List<Object> c2 = new ArrayList<Object>();
		c2.add(new Object());

		Assert.assertFalse(CollectionUtil.containAny(c1, c2));
		Assert.assertFalse(CollectionUtil.containAny(c2, c1));
	}
	
	
}
