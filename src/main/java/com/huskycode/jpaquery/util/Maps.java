package com.huskycode.jpaquery.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Maps {
	
	public static <K, V> Map<K, V> of(K k1, V v1) {
		Map<K, V> result = newMap();
		putValues(result, a(k1), a(v1));
		return result;
	}
	
	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
		Map<K, V> result = newMap();
		putValues(result, a(k1, k2), a(v1, v2));
		return result;
	}
	
	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> result = newMap();
		putValues(result, a(k1, k2, k3), a(v1, v2, v3));
		return result;
	}
	
	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3,  K k4, V v4) {
		Map<K, V> result = newMap();
		putValues(result, a(k1, k2, k3, k4), a(v1, v2, v3, v4));
		return result;
	}
	
	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3,  K k4, V v4, K k5, V v5) {
		Map<K, V> result = newMap();
		putValues(result, a(k1, k2, k3, k4, k5), a(v1, v2, v3, v4, v5));
		return result;
	}
	
	private static <K,V> Map<K, V> newMap() {
		return new HashMap<K, V>(); 
	}
	
	private static <T> T[] a(T ... t) {
		return t;
	}
	
	private static <K, V>void putValues(Map<K, V> map, K [] keys, V [] values) {
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			map.put(keys[i], values[i]);
		}
	}
}
