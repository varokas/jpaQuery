package com.huskycode.jpaquery.types.tree;

import java.util.ArrayList;
import java.util.List;


public class ActionGraph {
	private final List<EntityNode> allNodes;

	private ActionGraph() {
		this.allNodes = new ArrayList<EntityNode>();
	}

	public void addEntityNode(EntityNode node) {
		this.allNodes.add(node);
	}
	
	public static ActionGraph newInstance() {
		return new ActionGraph();
	}
		
	public List<EntityNode> getAllNodes() {
		return allNodes;
	}
}
