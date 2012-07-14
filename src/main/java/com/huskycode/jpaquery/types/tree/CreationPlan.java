package com.huskycode.jpaquery.types.tree;

import com.huskycode.jpaquery.solver.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A tree of object to be created along with confined values. The missing 
 * link between entities should already been solved by the {@link Solver}
 * 
 * @author Varokas Panusuwan
 */
public class CreationPlan {
	private ActionGraph actionGraph;
	private final List<EntityNode> plan;

	private CreationPlan() {
		plan = new ArrayList<EntityNode>();
	}
	
	public static CreationPlan newInstance(ActionGraph actionGraph) {
		CreationPlan plan = new CreationPlan();
		plan.actionGraph = actionGraph;
		plan.generatePlan();
		return plan;
	}
	
	private void generatePlan() {
		computeNodeLevel();
		sortActionGraphInOrderOfLevel();
	}
	
	private void computeNodeLevel() {
		int max = this.actionGraph.getAllNodes().size();
		int count = 0;
		while(true && count++ < max) {
			boolean noChange = true;
			for (EntityNode node : this.actionGraph.getAllNodes()) {
				int maxParentLevel = getMaxLevelOfParent(node);
				int toBeNodeLevel = maxParentLevel + 1;
				if (toBeNodeLevel > node.getLevel()) {
					node.setLevel(toBeNodeLevel);
					noChange = false;
				}
			}
			
			if (noChange) {
				break;
			}
		}
	}
	
	private void sortActionGraphInOrderOfLevel() {
		EntityNode[] arrayData = actionGraph.getAllNodes().toArray(new EntityNode[0]);
		Arrays.sort(arrayData, ENTITY_LEVEL_COMPARATOR);
		for (EntityNode node : arrayData) {
			this.plan.add(node);
		}
	}
	
	private int getMaxLevelOfParent(EntityNode node) {
		int max = -1;
		for (EntityNode parent : node.getParent()) {
			if (max < parent.getLevel()) {
				max = parent.getLevel();
			}
		}
		return max;
	}

	public List<EntityNode> getPlan() {
		return plan;
	}

	private static final Comparator<EntityNode> ENTITY_LEVEL_COMPARATOR  = new EntityNodeLevelComparator();
	
	private static class EntityNodeLevelComparator implements Comparator<EntityNode> {
		@Override
		public int compare(EntityNode o1, EntityNode o2) {
			return o1.getLevel() - o2.getLevel();
		}	
	}
}
