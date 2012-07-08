package com.huskycode.jpaquery.types.tree;

import java.util.ArrayList;
import java.util.List;

public class EntityNode {
	private final Class<?> entityClass;
	private final List<EntityNode> children;
	
	private EntityNode(Class<?> entity) {
		super();
		this.entityClass = entity;
		this.children = new ArrayList<EntityNode>();
	}
	
	public static EntityNode newInstance(Class<?> entityClass) {
		return new EntityNode(entityClass);
	}
}
