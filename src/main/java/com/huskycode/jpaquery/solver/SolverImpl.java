package com.huskycode.jpaquery.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodeFactory;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.command.CommandNodesFactory;
import com.huskycode.jpaquery.persister.store.InstanceWrapper;
import com.huskycode.jpaquery.persister.store.PropogatedValueStore;
import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;
import com.huskycode.jpaquery.util.CollectionUtil;
import com.huskycode.jpaquery.util.Factory;

public class SolverImpl implements Solver {

	private final CommandInterpretor commandInterpretor;
	private final DependenciesDefinition deps;

	private static final boolean isDebug = false;

	private SolverImpl(final DependenciesDefinition deps) {
		commandInterpretor = CommandInterpretor.getInstance();
		this.deps = deps;
	}

	public static SolverImpl newInstance(final DependenciesDefinition deps) {
		return new SolverImpl(deps);
	}

	@Deprecated
	@Override
	public <E> CreationPlan solveFor(final Class<E> entityClass) {
		CommandNode command = CommandNodeFactory.n(entityClass);
		return solveFor(CommandNodesFactory.ns(command));
	}


	@Override
	public <E> CreationPlan solveFor(final CommandNodes commands) {
		ActionGraph actionGraph = createActionGraph(commands);
		return CreationPlan.newInstance(actionGraph);
	}

	protected ActionGraph createActionGraph(final CommandNodes commands) {
		ActionGraph actionGraph = ActionGraph.newInstance();
		CommandPlan plan = commandInterpretor.createPlan(commands, deps);
		printPlan(plan);
		createActionGraph2(plan, actionGraph);
		return actionGraph;
	}

	private void printPlan(final CommandPlan plan) {
	    printIfDebug("Graph Order:");
	    for (Class<?> c : plan.getInOrderEntityData().getInOrderEntityList()) {
	        printIfDebug(c.toString());
	    }
	    printIfDebug("Command Order:");
	    for (CommandNode currentCommand : plan.getPlan()) {
	        printIfDebug(currentCommand);
	    }
	}

    private void createActionGraph2(final CommandPlan plan, final ActionGraph actionGraph) {

        // Context
        PropogatedValueStore<CommandNode, Class<?>, EntityNode> context = PropogatedValueStore.newInstance();
        DummyEntityContainer globallySharedContextContainer = DummyEntityContainer.newInstance(deps);
        Map<ContextKey, EntityNode> contextSpecificContainer = new HashMap<ContextKey, EntityNode>();

        InOrderEntityData inOrderEntityData = plan.getInOrderEntityData();
        EntityListIteratorFactory entityListIteratorFactory = EntityListIteratorFactory.newInstance(inOrderEntityData
                .getInOrderEntityList());

        for (CommandNode currentCommand : plan.getPlan()) {
            printIfDebug("Command: " + currentCommand);

            ListIterator<Class<?>> entityClassIterator = entityListIteratorFactory.newInstace();
            Map<Class<?>, EntityNode> currentContext = context.get(currentCommand);

            createAllRequiredParentDependencyIfNeeded(entityClassIterator, currentCommand, currentContext, globallySharedContextContainer, contextSpecificContainer, actionGraph);
            createEntityNodeOfThisCommand(currentCommand, currentContext, globallySharedContextContainer, contextSpecificContainer, actionGraph);
            updateChildrenContext(currentCommand.getChildren(), currentContext, context);

            printIfDebug("");
        }
    }

    private void createEntityNodeOfThisCommand(final CommandNode currentCommand,
                                                final Map<Class<?>, EntityNode> currentContext,
                                                final DummyEntityContainer dummyContainer,
                                                final Map<ContextKey, EntityNode> contextSpecificContainer,
                                                final ActionGraph actionGraph) {
        Set<Class<?>> parents = getDirectParentDependency(currentCommand.getEntity());
        EntityNode thisNode = createEntityNodeAndUpdateContext(currentCommand, currentContext, dummyContainer,
                parents, contextSpecificContainer);
        actionGraph.addEntityNode(thisNode);
        ContextKey key = createContextKey(currentCommand.getEntity(), currentContext, dummyContainer, contextSpecificContainer);
        contextSpecificContainer.put(key, thisNode);
    }



    private void createAllRequiredParentDependencyIfNeeded(final ListIterator<Class<?>> entityClassIterator,
                                                            final CommandNode currentCommand,
                                                            final Map<Class<?>, EntityNode> currentContext,
                                                            final DummyEntityContainer globallySharedContextContainer,
                                                            final Map<ContextKey, EntityNode> contextSpecificContainer,
                                                            final ActionGraph actionGraph) {
        Class<?> currentEntityClass = entityClassIterator.getCurrent();
        Set<Class<?>> allParentDependency = this.deps.getAllParentDependencyEntity(currentCommand.getEntity());
        printIfDebug(" Current Command Context: " + currentContext.keySet());
        while (!currentCommand.getEntity().equals(currentEntityClass)) {
            printIfDebug("  Current Entity: " + currentEntityClass);
            if (allParentDependency.contains(currentEntityClass)
                    && !currentContext.containsKey(currentEntityClass)) {
                printIfDebug("     Current Entity is required and not in context");
                Set<Class<?>> parents = getDirectParentDependency(currentEntityClass);
                if (parents.size() == 0
                        || !CollectionUtil.containAny(parents, currentContext.keySet())) {
                    printIfDebug("      Not context specific and can be shared");
                    if (!globallySharedContextContainer.contain(currentEntityClass)) {
                        printIfDebug("        No Dummpy and should create it");
                        EntityNode thisNode = globallySharedContextContainer.create(currentEntityClass);
                        actionGraph.addEntityNode(thisNode);
                    }
                }  else {
                    printIfDebug("      Context Specific Entity");
                    ContextKey key = createContextKey(currentEntityClass, currentContext, globallySharedContextContainer, contextSpecificContainer);
                    EntityNode thisNode = contextSpecificContainer.get(key);
                    if (thisNode == null) {
                        printIfDebug("        Create new Context Entity");
                        thisNode = createEntityNodeAndUpdateContext(currentEntityClass, currentContext, globallySharedContextContainer, contextSpecificContainer);
                        actionGraph.addEntityNode(thisNode);
                        contextSpecificContainer.put(key, thisNode);
                    }
                    currentContext.put(currentEntityClass, thisNode);
                }
            }

            if (entityClassIterator.hasNext()) {
                currentEntityClass = entityClassIterator.next().getCurrent();
            } else {
                throw new InvalidCommandHierarchy(currentCommand.toString());
            }
        }
        printIfDebug(" Current Command Context: " + currentContext.keySet());
        printIfDebug("End While: " + currentEntityClass);
    }

	private Set<Class<?>> getDirectParentDependency(final Class<?> currentEntityClass) {
		return deps.getDirectParentDependencyEntity(currentEntityClass);
	}

	private ContextKey createContextKey(final Class<?> currentEntityClass,
										final Map<Class<?>, EntityNode> context,
										final DummyEntityContainer globallySharedContextContainer,
										final Map<ContextKey, EntityNode> contextSpecificContainer) {
		ContextKey key = ContextKey.newInstance(currentEntityClass);
		for (Class<?> parent : getDirectParentDependency(currentEntityClass)) {
			EntityNode parentNode  = context.get(parent);
			if (parentNode == null) {
			    ContextKey parentKey = createContextKey(parent, context, globallySharedContextContainer, contextSpecificContainer);
			    parentNode = contextSpecificContainer.get(parentKey);
			}
			if (parentNode == null) {
				parentNode = globallySharedContextContainer.getDummyEntity(parent);
			}
			if (parentNode == null) {
			    throw new RuntimeException("Cannot find parent entity " + parent + " in all contexts and global context for " + currentEntityClass);
			}
			key.addEntityNode(parentNode);
		}
		return key;
	}

	private void updateChildrenContext(final List<CommandNode> children,
										final Map<Class<?>, EntityNode> parentContext,
										final PropogatedValueStore<CommandNode, Class<?>, EntityNode> context) {
		for (CommandNode child : children) {
			for (Entry<Class<?>, EntityNode> entry : parentContext.entrySet()) {
				Map<Class<?>, EntityNode> childContext = context.get(child);
				if (!childContext.containsKey(entry.getKey())) {
					childContext.put(entry.getKey(), entry.getValue());
				}
				//TODO else if there is conflict, throw exception?
			}
		}
	}

	private EntityNode createEntityNodeAndUpdateContext(final Class<?> entityClass,
										final Map<Class<?>, EntityNode> context,
										final DummyEntityContainer globallySharedContextContainer,
										final Map<ContextKey, EntityNode> contextSpecificContainer) {
		EntityNode thisNode = EntityNode.newInstance(entityClass);
		linkDependency(thisNode, context, globallySharedContextContainer, contextSpecificContainer);
		context.put(entityClass, thisNode);
		return thisNode;
	}

	private EntityNode createEntityNodeAndUpdateContext(final CommandNode command,
											final Map<Class<?>, EntityNode> context,
											final DummyEntityContainer globallySharedContextContainer,
											final Set<Class<?>> parents,
											final Map<ContextKey, EntityNode> instanceContainer) {
		EntityNode thisNode = createEntityNodeAndUpdateContext(command.getEntity(), context, globallySharedContextContainer, instanceContainer);
		thisNode.setCommand(command);
		return thisNode;
	}

	private void linkDependency(final EntityNode thisNode, final Map<Class<?>, EntityNode> context,
								final DummyEntityContainer globallySharedContextContainer, final Map<ContextKey, EntityNode> contextSpecificContainer) {
		for (Class<?> entityClass : getDirectParentDependency(thisNode.getEntityClass())) {
			EntityNode parentNode  = context.get(entityClass);
			if (parentNode == null) {
                ContextKey parentKey = createContextKey(entityClass, context, globallySharedContextContainer, contextSpecificContainer);
                parentNode = contextSpecificContainer.get(parentKey);
            }
			if (parentNode == null) {
				parentNode = globallySharedContextContainer.getDummyEntity(entityClass);
			}
			if (parentNode == null) {
                throw new RuntimeException("Cannot find parent entity " + entityClass
                        + " in all contexts and global context for " + thisNode.getEntityClass());
            }
			thisNode.addParent(parentNode);
			parentNode.addChild(thisNode);
		}
	}

	private static void printIfDebug(final Object msg) {
	    if (isDebug) {
	        System.out.println(msg);
	    }
	}

	private static class EntityListIteratorFactory implements Factory<ListIterator<Class<?>>> {

		private final List<Class<?>> plan;

		private EntityListIteratorFactory(final List<Class<?>> plan) {
			this.plan = plan;
		}

		public static EntityListIteratorFactory newInstance(final List<Class<?>> plan) {
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

		private  DummyEntityContainer(final DependenciesDefinition deps) {
			this.deps = deps;
		}

		public static DummyEntityContainer newInstance(final DependenciesDefinition deps) {
			return new DummyEntityContainer(deps);
		}

		public EntityNode create(final Class<?> entityClass) {
			EntityNode result = dummyContainer.get(entityClass);
			if (result == null) {
				result = EntityNode.newInstance(entityClass);
				linkDependency(result);
				dummyContainer.put(entityClass, result);
			}
			return result;
		}

		public boolean contain(final Class<?> entityClass) {
			return this.dummyContainer.containsKey(entityClass);
		}

		public EntityNode getDummyEntity(final Class<?> entityClass) {
			return dummyContainer.get(entityClass);
		}

		private void linkDependency(final EntityNode thisNode) {
			for (Class<?> parent : deps.getDirectParentDependencyEntity(thisNode.getEntityClass())) {
				EntityNode parentNode  = dummyContainer.get(parent);
				printIfDebug("Get Dummy Parent " + parent + " for " + thisNode.getEntityClass());
				if (parentNode == null) {
				    throw new RuntimeException("Cannot find parent entity " + parent + " for entity "
				            + thisNode.getEntityClass() + " in globally shared context.");
				}
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

		private ListIterator(final List<T> list, final int index) {
			initialize(list, index);
		}

		private void initialize(final List<T> list, final int index) {
			this.list = list;
			this.itr = list.listIterator(index);
			next();
		}

		public static <T> ListIterator<T> of(final List<T> list) {
			return new ListIterator<T>(list, 0);
		}

		public static <T> ListIterator<T> copy(final ListIterator<T> other) {
			return new ListIterator<T>(other.list, other.currentIndex);
		}

		public void updateLatest(final ListIterator<T> other) {
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

		private ContextKey(final Class<?> entityClass) {
			this.entityClass = entityClass;
			this.entityNodes = new ArrayList<InstanceWrapper<EntityNode>>();
		}

		public static ContextKey newInstance(final Class<?> entityClass) {
			return new ContextKey(entityClass);
		}

		public void addEntityNode(final EntityNode node) {
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
		public boolean equals(final Object obj) {
			if (this == obj) {
                return true;
            }
			if (obj == null) {
                return false;
            }
			if (getClass() != obj.getClass()) {
                return false;
            }
			ContextKey other = (ContextKey) obj;
			if (entityClass == null) {
				if (other.entityClass != null) {
                    return false;
                }
			} else if (!entityClass.equals(other.entityClass)) {
                return false;
            }
			if (entityNodes == null) {
				if (other.entityNodes != null) {
                    return false;
                }
			} else if (!entityNodes.equals(other.entityNodes)) {
                return false;
            }
			return true;
		}
	}
}