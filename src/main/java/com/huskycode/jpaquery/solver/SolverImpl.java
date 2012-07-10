package com.huskycode.jpaquery.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodeFactory;
import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class SolverImpl implements Solver {
	private SolverImpl() {
		
	}
	
	public static SolverImpl newInstance() { 
		return new SolverImpl();
	}

	@Deprecated
	@Override
	public <E> CreationPlan solveFor(Class<E> entityClass,
			DependenciesDefinition deps) {
		CommandNode command = CommandNodeFactory.n(entityClass);
		return solveFor(command, deps);
	}
	

	@Override
	public <E> CreationPlan solveFor(CommandNode command, DependenciesDefinition deps) {
		ActionGraph actionGraph = createActionGraph(command, deps);
		return CreationPlan.newInstance(actionGraph);
	}
	
	private List<Class<?>> toEntityList(List<EntityAndDependencySet> EntityAndDependencySetList) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (EntityAndDependencySet ec : EntityAndDependencySetList) {
			result.add(ec.getEntityClass());
		}
		return result;
	}
	
	protected ActionGraph createActionGraph(CommandNode command, DependenciesDefinition deps) {
		
		ActionGraph actionGraph = ActionGraph.newInstance();
		Set<Class<?>> allEntityInCommand = getAllEntitiesInCommand(command);
		Set<EntityAndDependencySet> allEntityAndDependencySets = getAllEntityAndDependencySet(allEntityInCommand, deps);
		InOrderEntityDependencyData inOrderEntityAndDependencyList = new InOrderEntityDependencyData(allEntityAndDependencySets, actionGraph);
		createActionGraph(command, actionGraph, inOrderEntityAndDependencyList, new HashMap<Class<?>, EntityNode>(), 0);
		return actionGraph;
	}
	
	private void createActionGraph(CommandNode command,
									ActionGraph actionGraph,
									InOrderEntityDependencyData entityDepencyData,
									Map<Class<?>, EntityNode> context,
									int currentIndex) {
		EntityAndDependencySet currentEntity = entityDepencyData.getEntityAndDependencySetByIndex(currentIndex);
		if (currentEntity != null) {
			EntityNode thisNode = createEntityNode(command, currentEntity, entityDepencyData, actionGraph);
			linkDependencyFromContext(thisNode,
								     currentEntity.getDirectDependencySet(),
									 context,
									 entityDepencyData);
			
			if (command.getEntity().equals(currentEntity.getEntityClass())) {
				if (command.getChildren().size() > 0) {
					Iterator<CommandNode> itr = command.getChildren().iterator();
					CommandNode firstChild = itr.next();
					context.put(currentEntity.getEntityClass(), thisNode);
					createActionGraph(firstChild, actionGraph, entityDepencyData, context, currentIndex+1);

					while (itr.hasNext()) {
						CommandNode child = itr.next();
						Map<Class<?>, EntityNode> clonedContext = new HashMap<Class<?>, EntityNode>(context);
						createActionGraph(child, actionGraph, entityDepencyData, clonedContext, currentIndex+1);
					}
				}
			} else {
				context.put(currentEntity.getEntityClass(), thisNode);
				createActionGraph(command, actionGraph, entityDepencyData, context, currentIndex+1);
			}
			
		} else {
			throw new InvalidCommandHierarchy();
		}

	}
	
	private EntityNode createEntityNode(CommandNode command,
										EntityAndDependencySet currentEntity,
										InOrderEntityDependencyData entityDepencyData,
										ActionGraph actionGraph) {
		EntityNode thisNode;
		if (command.getEntity().equals(currentEntity.getEntityClass())) {
			thisNode = EntityNode.newInstance(currentEntity.getEntityClass());
			thisNode.setCommand(command);
			actionGraph.addEntityNode(thisNode);
		} else {
			thisNode = entityDepencyData.getDummyEntity(currentEntity.getEntityClass());
		}
		return thisNode;
	}

	private void linkDependencyFromContext(EntityNode thisNode,
											Set<Class<?>> dependencySet,
											Map<Class<?>, EntityNode> context,
											InOrderEntityDependencyData entityDepencyData) {
		for (Class<?> entityClass : dependencySet) {
			EntityNode parentNode  = context.get(entityClass);
			if (parentNode == null) {
				parentNode = entityDepencyData.getDummyEntity(entityClass);
			}
			thisNode.addParent(parentNode);
			parentNode.addChild(thisNode);
		}		
	}

	private Set<EntityAndDependencySet> getAllEntityAndDependencySet(Set<Class<?>> entityClasses, DependenciesDefinition deps) {
		Set<EntityAndDependencySet> result = new HashSet<EntityAndDependencySet>();
		for (Class<?> entityClass : entityClasses) {
			result.addAll(getAllDependentEntitiesWithDependencySet(entityClass, deps));
		}
		return result;
	}
	
	private Set<Class<?>> getAllEntitiesInCommand(CommandNode command) {
		Set<Class<?>> allEntitiesInCommand = new HashSet<Class<?>>();
		getAllEntities(command, allEntitiesInCommand);
		return allEntitiesInCommand;
	}

	private void getAllEntities(CommandNode command, Set<Class<?>> allEntities) {
		allEntities.add(command.getEntity());
		//recursive call to child node to get all entities
		for (CommandNode child : command.getChildren()) {
			getAllEntities(child, allEntities);
		}
	}
	
	/**
	 * Get all EntityAndDependencySet for this entity class including itself
	 * @param entityClass
	 * @param deps
	 * @return
	 */
	protected List<EntityAndDependencySet> getAllDependentEntitiesWithDependencySet(Class<?> entityClass,
																					DependenciesDefinition deps) {
		Set<Class<?>> thisDependencySet = deps.getAllDependencyEntity(entityClass);
		Set<Class<?>> thisDirectDependencySet = deps.getDirectDependencyEntity(entityClass);
		List<EntityAndDependencySet> result = getAllDependentEntitiesWithDependencySet(thisDependencySet, deps);
		result.add(new EntityAndDependencySet(entityClass, thisDependencySet, thisDirectDependencySet));
		return result;
	}
	
	protected List<EntityAndDependencySet> getAllDependentEntitiesWithDependencySet(Set<Class<?>> allEntityClass,
																					DependenciesDefinition deps) {
		List<EntityAndDependencySet> result = new ArrayList<SolverImpl.EntityAndDependencySet>();
		for (Class<?> entityClass : allEntityClass) {
			Set<Class<?>> dependencySet = deps.getAllDependencyEntity(entityClass);
			Set<Class<?>> directDependencySet = deps.getDirectDependencyEntity(entityClass);
			result.add(new EntityAndDependencySet(entityClass, dependencySet, directDependencySet));
		}
		return result;
	}

	
	
	public static class EntityAndDependencySet implements Comparable<EntityAndDependencySet> {
		private final Class<?> entityClass;
		private final Set<Class<?>> dependencySet;
		private final Set<Class<?>> directDependencySet;
		
		public EntityAndDependencySet(Class<?> entityClass,
										Set<Class<?>> dependencySet,
										Set<Class<?>> directDependencySet) {
			this.entityClass = entityClass;
			this.dependencySet = dependencySet;
			this.directDependencySet = directDependencySet;
		}
		
		public Class<?> getEntityClass() {
			return entityClass;
		}

		public Set<Class<?>> getDependencySet() {
			return dependencySet;
		}

		public Set<Class<?>> getDirectDependencySet() {
			return directDependencySet;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((entityClass == null) ? 0 : entityClass.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EntityAndDependencySet other = (EntityAndDependencySet) obj;
			if (entityClass == null) {
				if (other.entityClass != null)
					return false;
			} else if (!entityClass.equals(other.entityClass))
				return false;
			return true;
		}

		@Override
		public int compareTo(EntityAndDependencySet o) {
			
			if (this.equals(o)) {
				return 0;
			}
			
			if (this.dependencySet.contains(o.getEntityClass())
					&& !o.dependencySet.contains(this.entityClass)) {
				return 1;
			} else if (o.dependencySet.contains(this.entityClass)
						&& !this.dependencySet.contains(o.getEntityClass())) {
				return -1;
			} else {
				return this.entityClass.getName().compareTo(o.entityClass.getName());
			}
		}
	}
}