package com.huskycode.jpaquery.solver;

import java.util.List;

import com.huskycode.jpaquery.types.tree.EntityNodeImpl;
import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertEquals(4, ascResult.size());
		Assert.assertSame(a, ascResult.get(0));
		Assert.assertSame(bNeedA, ascResult.get(1));
		Assert.assertSame(cNeedAandB, ascResult.get(2));
		Assert.assertSame(dNeedBandC, ascResult.get(3));
		//verify descendent
		Assert.assertEquals(4, descResult.size());
		Assert.assertSame(a, descResult.get(3));
		Assert.assertSame(bNeedA, descResult.get(2));
		Assert.assertSame(cNeedAandB, descResult.get(1));
		Assert.assertSame(dNeedBandC, descResult.get(0));
	}
}
