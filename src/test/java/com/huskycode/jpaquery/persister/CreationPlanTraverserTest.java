package com.huskycode.jpaquery.persister;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.huskycode.jpaquery.populator.CreationPlanTraverser;
import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;

import java.util.List;

public class CreationPlanTraverserTest {
	
	private CreationPlanTraverser creationPlanTraverser;

	@Before
	public void before() {
		creationPlanTraverser = new CreationPlanTraverser();
	}
	
	@Test
	public void testPlanIsCreatedCorrectlyInOrderOfDependency() {
		//set up mock network
		EntityNode a = EntityNode.newInstance(null);
		EntityNode bNeedA = EntityNode.newInstance(null);
		EntityNode cNeedAandB = EntityNode.newInstance(null);
		EntityNode dNeedBandC = EntityNode.newInstance(null);
		
		a.addChild(bNeedA);
		a.addChild(cNeedAandB);
		
		bNeedA.addParent(a);
		bNeedA.addChild(cNeedAandB);
		bNeedA.addChild(dNeedBandC);
		
		cNeedAandB.addParent(a);
		cNeedAandB.addParent(bNeedA);
		cNeedAandB.addChild(dNeedBandC);
		
		dNeedBandC.addParent(bNeedA);
		dNeedBandC.addParent(cNeedAandB);
		
		ActionGraph actionG = ActionGraph.newInstance();
		actionG.addEntityNode(a);
		actionG.addEntityNode(bNeedA);
		actionG.addEntityNode(cNeedAandB);
		actionG.addEntityNode(dNeedBandC);
		
		//create action plan
		CreationPlan plan = CreationPlan.newInstance(actionG);
		List<EntityNode> planSteps = creationPlanTraverser.getEntityNodes(plan);
		
		//verify
		Assert.assertEquals(4, planSteps.size());
		Assert.assertSame(a, planSteps.get(0));
		Assert.assertSame(bNeedA, planSteps.get(1));
		Assert.assertSame(cNeedAandB, planSteps.get(2));
		Assert.assertSame(dNeedBandC, planSteps.get(3));
	}

}
