package com.huskycode.jpaquery.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CollectionUtilTest {
	
	@Test
	public void testContainAnyReturnTrueWhenContainAnySharedObject() {
		Object shareObject = new Object();
		
		Set<Object> c1 = new HashSet<Object>();
		c1.add(shareObject);
		List<Object> c2 = new ArrayList<Object>();
		c2.add(shareObject);
		
		Assertions.assertTrue(CollectionUtil.containAny(c1, c2));
		Assertions.assertTrue(CollectionUtil.containAny(c2, c1));
	}
	
	@Test
	public void testContainAnyReturnFalseWhenAtLeaseOneOfThemEmpty() {
		Set<Object> c1 = new HashSet<Object>();
		List<Object> c2 = new ArrayList<Object>();
		
		Assertions.assertFalse(CollectionUtil.containAny(c1, c2));
		Assertions.assertFalse(CollectionUtil.containAny(c2, c1));
		
		c2.add(new Object());
		
		Assertions.assertFalse(CollectionUtil.containAny(c1, c2));
		Assertions.assertFalse(CollectionUtil.containAny(c2, c1));
	}
	
	@Test
	public void testContainAnyReturnFalseWhenNoShareObjectContained() {
		Set<Object> c1 = new HashSet<Object>();
		c1.add(new Object());
		List<Object> c2 = new ArrayList<Object>();
		c2.add(new Object());

		Assertions.assertFalse(CollectionUtil.containAny(c1, c2));
		Assertions.assertFalse(CollectionUtil.containAny(c2, c1));
	}
	
	
}
