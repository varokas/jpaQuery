package com.huskycode.jpaquery.util;

import java.util.HashMap;
import java.util.List;

public class ClassMap extends HashMap<Class<?>, List<Object>> {

	private static final long serialVersionUID = 1L;
	
	private ClassMap() {super();}

	@SuppressWarnings("unchecked")
	public <E> List<E> getForClass(Class<E> clazz) {
		return (List<E>) this.get(clazz);
	}

	
	public static ClassMap mapByClass(List<Object> objects) {
		ClassMap map = new ClassMap();
		for(Object obj : objects) {
			MapUtil.getOrCreateList(map, obj.getClass()).add(obj);
		}
		
		return map;
	}
}
