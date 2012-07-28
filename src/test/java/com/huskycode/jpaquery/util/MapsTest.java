package com.huskycode.jpaquery.util;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MapsTest {
	
	@Test
	public void testCreateMapCorrectlyForOneEntry() {
		Object key = new Object();
		Object value = new Object();
		
		Map<Object, Object> map = Maps.of(key, value);
		
		Assert.assertSame(value, map.get(key));
	}
	
	@Test
	public void testCreateMapCorrectlyForMoreThanOneEntry() {
		Object key1 = new Object();
		Object value1 = new Object();
		Object key2 = new Object();
		Object value2 = new Object();
		
		Map<Object, Object> map = Maps.of(key1, value1, key2, value2);
		
		Assert.assertSame(value1, map.get(key1));
		Assert.assertSame(value2, map.get(key2));
	}
}
