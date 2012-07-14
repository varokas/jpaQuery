package com.huskycode.jpaquery.populator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * Traverse creationPlan into a list of 
 * class for the persister.
 * 
 * @author varokas
 */
public class CreationPlanTraverser {
	/** 
	 * Get entity node in order that is ready for the 
	 * persister to persist.
	 * 
	 * @param plan
	 * @return 
	 */
	public List<EntityNode> getEntityNodes(CreationPlan plan) {
		ActionGraph actionGraph = plan.getActionGraph();
		computeNodeLevel(actionGraph);
		return getEntityNodeInOrderOfLevelFrom(actionGraph);
	}
	
	private void computeNodeLevel(ActionGraph actionGraph) {
		int max = actionGraph.getAllNodes().size();
		int count = 0;
		while(true && count++ < max) {
			boolean noChange = true;
			for (EntityNode node : actionGraph.getAllNodes()) {
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
	
	private List<EntityNode> getEntityNodeInOrderOfLevelFrom(ActionGraph actionGraph) {
		EntityNode[] arrayData = actionGraph.getAllNodes().toArray(new EntityNode[0]);
		Arrays.sort(arrayData, ENTITY_LEVEL_COMPARATOR);
		return Arrays.asList(arrayData);
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
	
	private static final Comparator<EntityNode> ENTITY_LEVEL_COMPARATOR  = new EntityNodeLevelComparator();
	
	private static class EntityNodeLevelComparator implements Comparator<EntityNode> {
		@Override
		public int compare(EntityNode o1, EntityNode o2) {
			return o1.getLevel() - o2.getLevel();
		}	
	}
}
