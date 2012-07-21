package com.huskycode.jpaquery.util;

public interface ValueStore<T, V> {

	public V putValue(T t, V value);

	public V get(T t);

}