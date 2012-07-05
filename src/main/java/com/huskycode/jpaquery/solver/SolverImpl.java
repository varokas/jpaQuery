package com.huskycode.jpaquery.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;

import com.huskycode.acceptancetest.jpaquery.annotation.VisibleForTesting;
import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.types.tree.CreationPlan;

public class SolverImpl implements Solver {
	private SolverImpl() {
		
	}
	
	public static SolverImpl newInstance() { 
		return new SolverImpl();
	}

	@Override
	public <E> CreationPlan solveFor(Class<E> entityClass,
			DependenciesDefinition deps) {
		List<EntityAndDependencyCount> entitiesDependencyCounts = getAllDependentEntitiesWithDependencyCount(entityClass, deps);
		EntityAndDependencyCount[] arrayData = entitiesDependencyCounts.toArray(new EntityAndDependencyCount[0]);
		Arrays.sort(arrayData);
		
		return CreationPlan.newInstance(toEntityList(arrayData));
	}
	
	private List<Class<?>> toEntityList(EntityAndDependencyCount[] arrayData) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (EntityAndDependencyCount ec : arrayData) {
			result.add(ec.getEntityClass());
		}
		return result;
	}

	@VisibleForTesting
	protected List<EntityAndDependencyCount> getAllDependentEntitiesWithDependencyCount(Class<?> entityClass, DependenciesDefinition deps) {
		List<EntityAndDependencyCount> result = new ArrayList<SolverImpl.EntityAndDependencyCount>();
		Set<Class<?>> visited = new HashSet<Class<?>>();
		LinkedList<Class<?>> queue = new LinkedList<Class<?>>();
		queue.add(entityClass);
		while(queue.size() > 0) {
			Class<?> e = queue.removeFirst();
			if (!visited.contains(e)) {
				List<Link<?,?,?>> dependencies = deps.getDirectDependency(e);
				visited.add(e);
				Set<Class<?>> count = new HashSet<Class<?>>();
				for (Link<?,?,?> link : dependencies) {
					Class<?> e2 = link.getTo().getEntityClass();
					queue.addLast(e2);
					count.add(e2);
				}
				result.add(new EntityAndDependencyCount(e, count.size()));
			}
		}
		
		return result;
	}
	
	public static class EntityAndDependencyCount implements Comparable<EntityAndDependencyCount> {
		private final Class<?> entityClass;
		private final int count;
		
		public EntityAndDependencyCount(Class<?> entityClass, int count) {
			this.entityClass = entityClass;
			this.count = count;
		}
		
		public Class<?> getEntityClass() {
			return entityClass;
		}

		public int getCount() {
			return this.count;
		}

		@Override
		public int compareTo(EntityAndDependencyCount o) {
			int diff = this.count - o.count;
			if (diff == 0) {
				diff = this.entityClass.getName().compareTo(o.entityClass.getName());
			}
			return diff;
		}
	}

}