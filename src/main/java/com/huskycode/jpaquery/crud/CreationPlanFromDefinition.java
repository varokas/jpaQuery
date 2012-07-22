package com.huskycode.jpaquery.crud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class CreationPlanFromDefinition {
	public CreationPlan from (DependenciesDefinition deps) {
		ActionGraph actionGraph = ActionGraph.newInstance();
		Set<Class<?>> allEntities = getAllEntities(deps);
		Map<Class<?>, EntityNode> container = new HashMap<Class<?>, EntityNode>();
		
		for (Class<?> e : allEntities) {
			EntityNode thisNode = getOrCreate(e, container);
			linkChildren(thisNode, deps.getDirectChildDependencyEntity(e), container);
			linkParents(thisNode, deps.getDirectParentDependencyEntity(e), container);
			actionGraph.addEntityNode(thisNode);
		}
		return CreationPlan.newInstance(actionGraph);
	}
	
	private void linkChildren(EntityNode thisNode, Set<Class<?>> childSet, Map<Class<?>, EntityNode> container) {
		for (Class<?> childC : childSet) {
			EntityNode child = getOrCreate(childC, container);
			thisNode.addChild(child);
		}
	}
	
	private void linkParents(EntityNode thisNode, Set<Class<?>> parents, Map<Class<?>, EntityNode> container) {
		for (Class<?> parentC : parents) {
			EntityNode parent = getOrCreate(parentC, container);
			thisNode.addParent(parent);
		}
	}
	
	private EntityNode getOrCreate(Class<?> entityClass, Map<Class<?>, EntityNode> container) { 
		EntityNode result = container.get(entityClass);
		if (result == null) {
			result = EntityNode.newInstance(entityClass);
			container.put(entityClass, result);
		}
		return result;
	}
	
	private Set<Class<?>> getAllEntities(DependenciesDefinition deps) {
		Set<Class<?>> result = new HashSet<Class<?>>();
		for (Link<?, ?, ?> link : deps.getLinks()) {
			result.add(link.getFrom().getEntityClass());
			result.add(link.getTo().getEntityClass());
		}
		return result;
	}
}