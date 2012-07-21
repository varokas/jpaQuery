package com.huskycode.jpaquery.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class MapUtilTest {
	
	@Test
	public void testGetOrCreateSetNotReturnEvenNotContainTheKey() {
		Map<Object, Set<Object>> map = new HashMap<Object, Set<Object>>();
		
		Set<Object> result = MapUtil.getOrCreateSet(map, new Object());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGetOrCreateListNotReturnEvenNotContainTheKey() {
		Map<Object, List<Object>> map = new HashMap<Object, List<Object>>();
		
		List<Object> result = MapUtil.getOrCreateList(map, new Object());
		Assert.assertNotNull(result);
	}
	
	@Test
	public void testGetOrCreateMapNotReturnEvenNotContainTheKey() {
		Map<Object, Map<Object, Object>> map = new HashMap<Object, Map<Object, Object>>();
		
		Map<Object, Object> result = MapUtil.getOrCreateMap(map, new Object());
		Assert.assertNotNull(result);
	}
}