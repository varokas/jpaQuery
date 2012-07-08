package com.huskycode.jpaquery.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huskycode.jpaquery.solver.SolverImpl.EntityAndDependencySet;
import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class InOrderEntityDependencyData {
	private final List<EntityAndDependencySet> inOrderEntityAndDependencyList;
	private final Map<Class<?>, EntityAndDependencySet> entityDepencyMap;
	private final Map<Class<?>, Integer> entityIndex;
	private final Map<Class<?>, EntityNode> entityDummyMap;
	private final ActionGraph actionGraph;
	
	public InOrderEntityDependencyData(Collection<EntityAndDependencySet> allEntityAndDependencySets, ActionGraph actionGraph) {
		this.actionGraph = actionGraph;
		this.entityDummyMap = new HashMap<Class<?>, EntityNode>();
		EntityAndDependencySet[] arrayData = allEntityAndDependencySets.toArray(new EntityAndDependencySet[0]);
		Arrays.sort(arrayData);
		inOrderEntityAndDependencyList = new ArrayList<SolverImpl.EntityAndDependencySet>();
		entityDepencyMap = new HashMap<Class<?>,EntityAndDependencySet>();
		entityIndex = new HashMap<Class<?>, Integer>();
		for (int i = 0; i < arrayData.length; i++) {
			inOrderEntityAndDependencyList.add(arrayData[i]);
			entityDepencyMap.put(arrayData[i].getEntityClass(), arrayData[i]);
			entityIndex.put(arrayData[i].getEntityClass(), i);
		}
	}
	
	public EntityNode getDummyEntity(Class<?> entityClass) {
		EntityNode result = entityDummyMap.get(entityClass);
		if (result == null) {
			result = EntityNode.newInstance(entityClass);
			entityDummyMap.put(entityClass, result);
			actionGraph.addEntityNode(result);
		}
		return result;
	}
	
	public EntityAndDependencySet getEntityAndDependencySetByIndex(int index) {
		if (index < this.inOrderEntityAndDependencyList.size()) {
			return this.inOrderEntityAndDependencyList.get(index);
		}
		return null;
	}
	
	public EntityAndDependencySet getEntityAndDependencySet(Class<?> entityClass) {
		return this.entityDepencyMap.get(entityClass);
	}

	public List<EntityAndDependencySet> getInOrderEntityAndDependencyList() {
		return inOrderEntityAndDependencyList;
	}
	
	public List<EntityAndDependencySet> getEntityAndDependencyBefore(Class<?> entityClass) {
		int descendantIndex = this.entityIndex.get(entityClass);
		return getSubList(0, descendantIndex);
	}
	
	public List<EntityAndDependencySet> getEntityAndDependencyBetweenEntityClasses(Class<?> ancestor, Class<?> descendant) {
		int ancestorIndex = this.entityIndex.get(ancestor);
		int descendantIndex = this.entityIndex.get(descendant);
		return getSubList(ancestorIndex+1, descendantIndex);
	}
	
	private List<EntityAndDependencySet> getSubList(int start, int end) {
		List<EntityAndDependencySet> result = new ArrayList<SolverImpl.EntityAndDependencySet>();
		for (int i = start; i < end; i++) {
			result.add(inOrderEntityAndDependencyList.get(i));
		}
		return result;
	}
}
