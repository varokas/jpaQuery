package com.huskycode.jpaquery.populator;

import java.util.List;

import com.huskycode.jpaquery.solver.DirectedGraph;
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
		DirectedGraph<EntityNode> g = creatGraph(actionGraph);
		g.computeNodeLevel();
		return g.getInorderNodeAscendent();
	}
	
	private DirectedGraph<EntityNode> creatGraph(ActionGraph actionGraph) {
		DirectedGraph<EntityNode> graph = DirectedGraph.newInstance();
		for (EntityNode node : actionGraph.getAllNodes()) {
			graph.addNode(node);
			for (EntityNode child : node.getChilds()) {
				graph.addRelation(child, node);
			}	
		}
		return graph;
	}
}
