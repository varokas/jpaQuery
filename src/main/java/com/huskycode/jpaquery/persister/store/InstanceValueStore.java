package com.huskycode.jpaquery.persister.store;

import java.util.HashMap;
import java.util.Map;

import com.huskycode.jpaquery.util.ValueStore;

public class InstanceValueStore<T, V> implements ValueStore<T, V> {
	
	private final Map<InstanceWrapper<T>, V> dataStore 
	 	= new HashMap<InstanceWrapper<T>, V>();

	private InstanceValueStore() {}
	
	public static <T, V> InstanceValueStore<T, V> newInstance() {
		return new InstanceValueStore<T, V>();
	}

	@Override
	public V putValue(T t, V value) {
		InstanceWrapper<T> key = createKey(t);	
		return dataStore.put(key, value);
	}
	
	private InstanceWrapper<T> createKey(T t) { 
		return new InstanceWrapper<T>(t);
	}

	@Override
	public V get(T t) {
		InstanceWrapper<T> k = createKey(t);
		return dataStore.get(k);
	}
	
	
}
