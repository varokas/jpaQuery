package com.huskycode.jpaquery.util;

import java.util.ArrayList;
import java.util.List;

public class ListFactory<T> implements Factory<List<T>> {
	
	@SuppressWarnings("rawtypes")
	private static final ListFactory INSTANCE = new ListFactory();
	
	private ListFactory() {};
	
	@SuppressWarnings("unchecked")
	public static final <T> ListFactory<T> getInstance() {
		return INSTANCE;
	}
	
	private static <T> List<T> create() {
		return new ArrayList<T>();
	}
	
	@Override
	public List<T> newInstace() {
		return create();
	}

}
