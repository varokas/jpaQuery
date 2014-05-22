package com.huskycode.jpaquery.util;

import java.util.HashSet;
import java.util.Set;

public class SetFactory<T> implements Factory<Set<T>> {
	@SuppressWarnings("rawtypes")
	private static final SetFactory INSTANCE = new SetFactory();
	
	private SetFactory() {}
	
	@SuppressWarnings("unchecked")
	public static final <T> SetFactory<T> getInstance() {
		return INSTANCE;
	}
	
	private static <T> Set<T> create() {
		return new HashSet<T>();
	}
	
	@Override
	public Set<T> newInstace() {
		return create();
	}
}
