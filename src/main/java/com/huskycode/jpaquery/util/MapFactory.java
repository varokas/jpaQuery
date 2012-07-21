package com.huskycode.jpaquery.util;

import java.util.HashMap;
import java.util.Map;

public class MapFactory<K,V> implements Factory<Map<K,V>> {
	@SuppressWarnings("rawtypes")
	private static final MapFactory INSTANCE = new MapFactory();
	
	private MapFactory() {};
	
	@SuppressWarnings("unchecked")
	public static final <K,V> MapFactory<K,V> getInstance() {
		return INSTANCE;
	}
	
	private static <K,V> Map<K,V> create() {
		return new HashMap<K,V>();
	}
	
	@Override
	public Map<K,V> newInstace() {
		return create();
	}
}
