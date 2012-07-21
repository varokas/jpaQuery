package com.huskycode.jpaquery.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodeFactory;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.command.CommandNodesFactory;
import com.huskycode.jpaquery.persister.store.InstanceValueStore;
import com.huskycode.jpaquery.persister.store.InstanceWrapper;
import com.huskycode.jpaquery.persister.store.PropogatedValueStore;
import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;
import com.huskycode.jpaquery.util.CollectionUtil;
import com.huskycode.jpaquery.util.Factory;
import com.huskycode.jpaquery.util.MapUtil;
import com.huskycode.jpaquery.util.ValueStore;

public class SolverImpl implements Solver {
	
	private CommandInterpretor commandInterpretor;
	private DependenciesDefinition deps;
	private SolverImpl(DependenciesDefinition deps) {
		commandInterpretor = CommandInterpretor.getInstance();
		this.deps = deps;
	}
	
	public static SolverImpl newInstance(DependenciesDefinition deps) { 
		return new SolverImpl(deps);
	}

	@Deprecated
	@Override
	public <E> CreationPlan solveFor(Class<E> entityClass) {
		CommandNode command = CommandNodeFactory.n(entityClass);
		return solveFor(CommandNodesFactory.ns(command));
	}
	

	@Override
	public <E> CreationPlan solveFor(CommandNodes commands) {
		ActionGraph actionGraph = createActionGraph(commands);
		return CreationPlan.newInstance(actionGraph);
	}
	
	protected ActionGraph createActionGraph(CommandNodes commands) {
		
		ActionGraph actionGraph = ActionGraph.newInstance();
		CommandPlan plan = commandInterpretor.createPlan(commands, deps);
		createActionGraph(plan,
							actionGraph);
		return actionGraph;
	}
	
	
	
	private void createActionGraph(CommandPlan plan,
									ActionGraph actionGraph) {
		
		//Context
		PropogatedValueStore<CommandNode, Class<?>, EntityNode> context = PropogatedValueStore.newInstance();
		InstanceValueStore<CommandNode, ListIterator<Class<?>>> commandNodeIteratorMap = InstanceValueStore.newInstance();
		DummyEntityContainer dummyContainer = DummyEntityContainer.newInstance(deps);
		Map<ContextKey, EntityNode> instanceContainer = new HashMap<ContextKey, EntityNode>();

		InOrderEntityData inOrderEntityData = plan.getInOrderEntityData();
		EntityListIteratorFactory entityListIteratorFactory = EntityListIteratorFactory.newInstance(inOrderEntityData.getInOrderEntityList());

		for (CommandNode currentCommand : plan.getPlan()) {
			ListIterator<Class<?>> entityClassIterator = MapUtil.getOrCreate(commandNodeIteratorMap, currentCommand, entityListIteratorFactory);
			Class<?> currentEntityClass = entityClassIterator.getCurrent();
			Map<Class<?>, EntityNode> currentContext = context.get(currentCommand);
			
			while (!currentCommand.getEntity().equals(currentEntityClass)) {
				Set<Class<?>> parents = getDirectParentDependency(currentEntityClass);
				if (parents.size() == 0
						|| !CollectionUtil.containAny(parents, currentContext.keySet())) {
					if (!dummyContainer.contain(currentEntityClass)) {
						EntityNode thisNode = dummyContainer.create(currentEntityClass);
						actionGraph.addEntityNode(thisNode);
					}
				} else if (!currentContext.keySet().contains(currentEntityClass)) {
					ContextKey key = createContextKey(currentEntityClass, currentContext, dummyContainer);
					EntityNode thisNode = instanceContainer.get(key);
					if (thisNode == null) {
						thisNode = createEntityNodeAndUpdateContext(currentEntityClass, currentContext, dummyContainer);
						actionGraph.addEntityNode(thisNode);
						instanceContainer.put(key, thisNode);
					}
					currentContext.put(currentEntityClass, thisNode);
				} else {
					throw new InvalidCommandHierarchy("Context contain the entity before it expects");
				}
				
				if (entityClassIterator.hasNext()) {
					currentEntityClass = entityClassIterator.next().getCurrent();
				} else {
					throw new InvalidCommandHierarchy(currentCommand.toString());
				}	
			}
			//
			Set<Class<?>> parents = getDirectParentDependency(currentEntityClass);
			if (currentCommand.getEntity().equals(currentEntityClass)) {
				EntityNode thisNode = createEntityNodeAndUpdateContext(currentCommand, currentContext, dummyContainer, parents);
				actionGraph.addEntityNode(thisNode);
				updateChildrenIterator(currentCommand.getChildren(), entityClassIterator, commandNodeIteratorMap);
				updateChildrenContext(currentCommand.getChildren(), currentContext, context);
			}			
		}
	}

	private Set<Class<?>> getDirectParentDependency(Class<?> currentEntityClass) {
		return deps.getDirectParentDependencyEntity(currentEntityClass);
	}

	private ContextKey createContextKey(Class<?> currentEntityClass,
										Map<Class<?>, EntityNode> context,
										DummyEntityContainer dummyContainer) {
		ContextKey key = ContextKey.newInstance(currentEntityClass);
		for (Class<?> parent : getDirectParentDependency(currentEntityClass)) {
			EntityNode parentNode  = context.get(parent);
			if (parentNode == null) {
				parentNode = dummyContainer.getDummyEntity(parent);
			}
			key.addEntityNode(parentNode);
		}
		return key;
	}

	private void updateChildrenContext(List<CommandNode> children,
										Map<Class<?>, EntityNode> parentContext,
										PropogatedValueStore<CommandNode, Class<?>, EntityNode> context) {
		for (CommandNode child : children) {
			context.get(child).putAll(parentContext);
		}
	}
	
	private void updateChildrenIterator(List<CommandNode> children,
										ListIterator<Class<?>> entityClassIterator,
										ValueStore<CommandNode, ListIterator<Class<?>>> commandNodeIteratorMap) {
		//update parent iterator to next one
		entityClassIterator.next();
		//copy to children
		for (CommandNode child : children) {
			ListIterator<Class<?>> childItr = commandNodeIteratorMap.get(child);
			if (childItr == null) {
				commandNodeIteratorMap.putValue(child, ListIterator.copy(entityClassIterator));
			} else {
				childItr.updateLatest(entityClassIterator);
			}
		}
	}	
	
	private EntityNode createEntityNodeAndUpdateContext(Class<?> entityClass,
										Map<Class<?>, EntityNode> context,
										DummyEntityContainer dummyContainer) {
		EntityNode thisNode = EntityNode.newInstance(entityClass);
		linkDependency(thisNode, context, dummyContainer);
		context.put(entityClass, thisNode);
		return thisNode;
	}
	
	private EntityNode createEntityNodeAndUpdateContext(CommandNode command,
											Map<Class<?>, EntityNode> context,
											DummyEntityContainer dummyContainer,
											Set<Class<?>> parents) {
		EntityNode thisNode = createEntityNodeAndUpdateContext(command.getEntity(), context, dummyContainer);
		thisNode.setCommand(command);
		return thisNode;
	}
	
	private void linkDependency(EntityNode thisNode, Map<Class<?>, EntityNode> context,
								DummyEntityContainer dummyContainer) {
		for (Class<?> entityClass : getDirectParentDependency(thisNode.getEntityClass())) {
			EntityNode parentNode  = context.get(entityClass);
			if (parentNode == null) {
				parentNode = dummyContainer.getDummyEntity(entityClass);
			}
			thisNode.addParent(parentNode);
			parentNode.addChild(thisNode);
		}
	}
	
	
	private static class EntityListIteratorFactory implements Factory<ListIterator<Class<?>>> {
		
		private final List<Class<?>> plan;
		
		private EntityListIteratorFactory(List<Class<?>> plan) {
			this.plan = plan;
		}
		
		public static EntityListIteratorFactory newInstance(List<Class<?>> plan) {
			return new EntityListIteratorFactory(plan);
		}

		@Override
		public ListIterator<Class<?>> newInstace() {
			return ListIterator.of(plan);
		}		
	}
	
	private static class DummyEntityContainer {
		private final Map<Class<?>, EntityNode> dummyContainer = new HashMap<Class<?>, EntityNode>();
		private final DependenciesDefinition deps;
		
		private  DummyEntityContainer(DependenciesDefinition deps) {
			this.deps = deps;
		}
		
		public static DummyEntityContainer newInstance(DependenciesDefinition deps) {
			return new DummyEntityContainer(deps);
		}
		
		public EntityNode create(Class<?> entityClass) {
			EntityNode result = dummyContainer.get(entityClass);
			if (result == null) {
				result = EntityNode.newInstance(entityClass);
				linkDependency(result);
				dummyContainer.put(entityClass, result);
			}
			return result;
		}
		
		public boolean contain(Class<?> entityClass) {
			return this.dummyContainer.containsKey(entityClass);
		}
		
		public EntityNode getDummyEntity(Class<?> entityClass) {
			return dummyContainer.get(entityClass);
		}

		private void linkDependency(EntityNode thisNode) {
			for (Class<?> parent : deps.getDirectParentDependencyEntity(thisNode.getEntityClass())) {
				EntityNode parentNode  = dummyContainer.get(parent);
				thisNode.addParent(parentNode);
				parentNode.addChild(thisNode);
			}
		}
	}

	private static class ListIterator<T> {
		private java.util.ListIterator<T> itr;
		private List<T> list;
		private T current;
		private int currentIndex;
		
		private ListIterator(List<T> list, int index) {
			initialize(list, index);
		}
		
		private void initialize(List<T> list, int index) {
			this.list = list;
			this.itr = list.listIterator(index);
			next();
		}
		
		public static <T> ListIterator<T> of(List<T> list) {
			return new ListIterator<T>(list, 0);
		}
		
		public static <T> ListIterator<T> copy(ListIterator<T> other) {
			return new ListIterator<T>(other.list, other.currentIndex);
		}
		
		public void updateLatest(ListIterator<T> other) {
			if (this.currentIndex < other.currentIndex) {
				initialize(this.list, other.currentIndex);
			}
		}
		
		public boolean hasNext() {
			return this.itr.hasNext();
		}
		
		private ListIterator<T> next() {
			try {
				this.currentIndex = this.itr.nextIndex();
				this.current = this.itr.next();
				return this;
			} catch (NoSuchElementException e) {
				this.current = null;
				return this;
			}
		}
		
		public T getCurrent() {
			return this.current;
		}
	}
	
	private static class ContextKey {
		private final Class<?> entityClass;
		private final List<InstanceWrapper<EntityNode>> entityNodes;
		
		private ContextKey(Class<?> entityClass) {
			this.entityClass = entityClass;
			this.entityNodes = new ArrayList<InstanceWrapper<EntityNode>>();
		}
		
		public static ContextKey newInstance(Class<?> entityClass) {
			return new ContextKey(entityClass);
		}
		
		public void addEntityNode(EntityNode node) {
			this.entityNodes.add(new InstanceWrapper<EntityNode>(node));
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((entityClass == null) ? 0 : entityClass.hashCode());
			result = prime * result
					+ ((entityNodes == null) ? 0 : entityNodes.hashCode());
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
			ContextKey other = (ContextKey) obj;
			if (entityClass == null) {
				if (other.entityClass != null)
					return false;
			} else if (!entityClass.equals(other.entityClass))
				return false;
			if (entityNodes == null) {
				if (other.entityNodes != null)
					return false;
			} else if (!entityNodes.equals(other.entityNodes))
				return false;
			return true;
		}	
	}
}