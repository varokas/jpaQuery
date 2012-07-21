package com.huskycode.jpaquery.solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InOrderEntityData {
	private final List<Class<?>> inOrderEntityList;
	private final Map<Class<?>, Integer> entityIndex;
	
	public InOrderEntityData(List<Class<?>> inOrderEntityList) {
		this.inOrderEntityList = inOrderEntityList;
		entityIndex = new HashMap<Class<?>, Integer>();
		for (int i = 0; i < inOrderEntityList.size(); i++) {
			entityIndex.put(inOrderEntityList.get(i), i);
		}
	}

	public List<Class<?>> getInOrderEntityList() {
		return inOrderEntityList;
	}
	
	public int getOrderIndexOf(Class<?> entityClass) {
		return this.entityIndex.get(entityClass);
	}
}
