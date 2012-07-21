package com.huskycode.jpaquery.util;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class MapUtil {
	
	public static <K, K2, V> Map<K2, V> getOrCreateMap(Map<K, Map<K2, V>> map, K key) {
		Factory<Map<K2, V>> factory = MapFactory.getInstance();
		return getOrCreate(map, key, factory);
	}
	
	public static <K, K2, V> Map<K2, V> getOrCreateMap(ValueStore<K, Map<K2, V>> map, K key) {
		Factory<Map<K2, V>> factory = MapFactory.getInstance();
		return getOrCreate(map, key, factory);
	}
	
	public static <K, E> Set<E> getOrCreateSet(Map<K, Set<E>> map, K key) {
		Factory<Set<E>> factory = SetFactory.getInstance();
		return getOrCreate(map, key, factory);
	}
	
	public static <K, E> List<E> getOrCreateList(Map<K, List<E>> map, K key) {
		Factory<List<E>> factory = ListFactory.getInstance();
		return getOrCreate(map, key, factory);
	}
	
	public static <K, V> V getOrCreate(Map<K,V> map, K key , Factory<V> factory) {
		return getOrCreate(MapValueStoreWapper.newInstance(map), key, factory);
	}
	
	public static <K, V> V getOrCreate(ValueStore<K, V> store, K key, Factory<V> factory) {
		V value = store.get(key);
		if (value == null) {
			value = factory.newInstace();
			store.putValue(key, value);
		}
		return value;
	}
	
	private static class MapValueStoreWapper<T, V> implements ValueStore<T, V> {
		
		private Map<T, V> map;
		
		private MapValueStoreWapper(Map<T, V> map) {
			this.map = map;
		}
		
		public static <T, V> MapValueStoreWapper<T, V> newInstance(Map<T, V> map) {
			return new MapValueStoreWapper<T, V>(map);
		}
		
		@Override
		public V putValue(T t, V value) {
			return map.put(t, value);
		}

		@Override
		public V get(T t) {
			return map.get(t);
		}
		
	}
}
