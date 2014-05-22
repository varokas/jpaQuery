package com.huskycode.jpaquery.solver;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.persister.store.InstanceWrapper;
import com.huskycode.jpaquery.util.MapUtil;

public class CommandInterpretor {
	
	private static final CommandInterpretor INSTANCE = new CommandInterpretor();
	
	private CommandInterpretor() {}
	
	public static CommandInterpretor getInstance() {
		return INSTANCE;
	}

	public CommandPlan createPlan(CommandNodes commands, DependenciesDefinition deps) {
		CommandValidatorCommandVisitor validator = new CommandValidatorCommandVisitor(deps);
		AllCommandNodePopulatorCommandVisitor allCommandNodes = new AllCommandNodePopulatorCommandVisitor();
		AllDependencyEntityGraphPopulatorCommandVisitor  allDependencyEntityGraph
			= new AllDependencyEntityGraphPopulatorCommandVisitor(deps);
		
		visitCommandNode(commands, CommandVisitors.newInstance(validator, allCommandNodes, allDependencyEntityGraph));
		
		allDependencyEntityGraph.get().computeNodeLevel();
		List<Class<?>> allDependencyEntityClasses = allDependencyEntityGraph.get().getInorderNodeAscendent();
		InOrderEntityData inOrderEntityData = new InOrderEntityData(allDependencyEntityClasses);
		List<CommandNode> inOrderAllCommandNodes = sort(allCommandNodes.get(), inOrderEntityData);
		
		return new CommandPlanImpl(inOrderAllCommandNodes, inOrderEntityData);
	}
	
	private List<CommandNode> sort(final List<CommandNode> allCommandNodes, final InOrderEntityData inOrderEntityData) {
		CommandNode[] arrayData = allCommandNodes.toArray(new CommandNode[0]);
		Arrays.sort(arrayData, new Comparator<CommandNode>() {

			@Override
			public int compare(CommandNode o1, CommandNode o2) {
				int index1 = inOrderEntityData.getOrderIndexOf(o1.getEntity());
				int index2 = inOrderEntityData.getOrderIndexOf(o2.getEntity());
				return index1 - index2;
			}
		});
		return Arrays.asList(arrayData);
	}
	
	private void visitCommandNode(CommandNodes commands, CommandVisitor visitor) {
		for (CommandNode command : commands.get()) {
			visitCommandNode(command, visitor);
		}
	}
		
	private void visitCommandNode(CommandNode command,
			 						CommandVisitor visitor) {
		visitor.visit(command);
		for (CommandNode child : command.getChildren()) {
			visitor.visit(command, child);
			visitCommandNode(child, visitor);
		}
	}
	
	private static class CommandVisitors implements CommandVisitor {

		private List<CommandVisitor> vistors;
		
		private CommandVisitors(CommandVisitor ... visitors) {
			vistors = Arrays.asList(visitors);
		}
		
		public static CommandVisitors newInstance(CommandVisitor ... visitors) {
			return new CommandVisitors(visitors);
		}
		
		@Override
		public void visit(CommandNode node) {
			for (CommandVisitor v : vistors) {
				v.visit(node);
			}
		}

		@Override
		public void visit(CommandNode parent, CommandNode child) {
			for (CommandVisitor v : vistors) {
				v.visit(parent, child);
			}
		}
	}
	
	private static class CommandValidatorCommandVisitor implements CommandVisitor {

		private final DependenciesDefinition deps;
		
		private CommandValidatorCommandVisitor(DependenciesDefinition deps) {
			this.deps = deps;
		}
		
		@Override
		public void visit(CommandNode node) {
			// do nothing
		}

		@Override
		public void visit(CommandNode parent, CommandNode child) {
			if (!deps.getAllParentDependencyEntity(child.getEntity()).contains(parent.getEntity())) {
				throw new InvalidCommandHierarchy(child.getEntity() + "does not depend on " + parent.getEntity());
			}
		}
		
	}
	
	private static class AllDependencyEntityGraphPopulatorCommandVisitor implements CommandVisitor {
		private final DependenciesDefinition deps;
		private final Set<Class<?>> allEntities = new HashSet<Class<?>>();
		private DirectedGraph<Class<?>> graph;
		
		private AllDependencyEntityGraphPopulatorCommandVisitor(DependenciesDefinition deps) {
			this.deps = deps;
		}

		@Override
		public void visit(CommandNode node) {
			allEntities.add(node.getEntity());
			allEntities.addAll(deps.getAllParentDependencyEntity(node.getEntity()));
		}

		@Override
		public void visit(CommandNode parent, CommandNode child) {
			// do nothing
		}
		
		public DirectedGraph<Class<?>> get() {
			if (graph == null) {
				graph = createGraph();
			}
			return graph;
		}
		
		private DirectedGraph<Class<?>> createGraph() {
			DirectedGraph<Class<?>> graph = DirectedGraph.newInstance();
			for (Class<?> child : this.allEntities) {
				graph.addNode(child);
				for (Class<?> parent : this.deps.getDirectParentDependencyEntity(child)) {
					graph.addRelation(child, parent);
				}
			}
			return graph;
		}
	}
	
	private static class AllCommandNodePopulatorCommandVisitor implements CommandVisitor {
		private final Set<InstanceWrapper<CommandNode>> allCommandNode = new HashSet<InstanceWrapper<CommandNode>>();

		@Override
		public void visit(CommandNode parent, CommandNode child) {
			// do nothing
		}
		
		public List<CommandNode> get() {
			return InstanceWrapper.toInstanceList(allCommandNode);
		}

		@Override
		public void visit(CommandNode node) {
			allCommandNode.add(InstanceWrapper.newInstance(node));
		}
	}
	
	interface CommandVisitor {
		void visit(CommandNode node);
		void visit(CommandNode parent, CommandNode child);
	}

	private static class CommandPlanImpl implements CommandPlan {
		
		private final List<CommandNode> plan;
		private final InOrderEntityData inOrderEntityData;
		private final Map<Class<?>, List<CommandNode>> entityCommandNodeMap;
		
		private CommandPlanImpl(List<CommandNode> plan , InOrderEntityData inOrderEntityData) {
			this.plan = plan;
			this.inOrderEntityData = inOrderEntityData;
			this.entityCommandNodeMap = new HashMap<Class<?>, List<CommandNode>>();
			initialize();
		}
		
		private void initialize() {
			for (CommandNode command : this.plan) {
				MapUtil.getOrCreateList(entityCommandNodeMap, command.getEntity()).add(command);
			}
		}

		@Override
		public List<CommandNode> getPlan() {
			return plan;
		}

		@Override
		public InOrderEntityData getInOrderEntityData() {
			return inOrderEntityData;
		}

		@Override
		public List<CommandNode> getCommandNodeListByEntity(Class<?> entityClass) {
			return MapUtil.getOrCreateList(entityCommandNodeMap, entityClass);
		}
		
	}
}
