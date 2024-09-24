package com.huskycode.jpaquery.solver;

import java.util.List;

import com.huskycode.jpaquery.types.tree.EntityNodeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.huskycode.jpaquery.types.tree.EntityNode;

public class DirectedGraphTest {
	
	@Test
	public void testGraph() {
		//set up mock network
		EntityNode a = EntityNodeImpl.newInstance(null);
		EntityNode bNeedA = EntityNodeImpl.newInstance(null);
		EntityNode cNeedAandB = EntityNodeImpl.newInstance(null);
		EntityNode dNeedBandC = EntityNodeImpl.newInstance(null);
		
		DirectedGraph<EntityNode> graph = DirectedGraph.newInstance();
		graph.addRelation(bNeedA, a);
		graph.addRelation(cNeedAandB, a);
		graph.addRelation(cNeedAandB, bNeedA);
		graph.addRelation(dNeedBandC, bNeedA);
		graph.addRelation(dNeedBandC, cNeedAandB);
		
		graph.computeNodeLevel();
		
		List<EntityNode> ascResult = graph.getInorderNodeAscendent();
		List<EntityNode> descResult = graph.getInorderNodeDescendent();
		
		//verify ascendent
		Assertions.assertEquals(4, ascResult.size());
		Assertions.assertSame(a, ascResult.get(0));
		Assertions.assertSame(bNeedA, ascResult.get(1));
		Assertions.assertSame(cNeedAandB, ascResult.get(2));
		Assertions.assertSame(dNeedBandC, ascResult.get(3));
		//verify descendent
		Assertions.assertEquals(4, descResult.size());
		Assertions.assertSame(a, descResult.get(3));
		Assertions.assertSame(bNeedA, descResult.get(2));
		Assertions.assertSame(cNeedAandB, descResult.get(1));
		Assertions.assertSame(dNeedBandC, descResult.get(0));
	}
}
