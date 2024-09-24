package com.huskycode.jpaquery.persister;

import com.huskycode.jpaquery.types.tree.EntityNodeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.huskycode.jpaquery.populator.CreationPlanTraverser;
import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;

import java.util.List;

public class CreationPlanTraverserTest {
	
	private CreationPlanTraverser creationPlanTraverser;

	@BeforeEach
	public void before() {
		creationPlanTraverser = new CreationPlanTraverser();
	}
	
	@Test
	public void testPlanIsCreatedCorrectlyInOrderOfDependency() {
		//set up mock network
		EntityNode a = EntityNodeImpl.newInstance(null);
		EntityNode bNeedA = EntityNodeImpl.newInstance(null);
		EntityNode cNeedAandB = EntityNodeImpl.newInstance(null);
		EntityNode dNeedBandC = EntityNodeImpl.newInstance(null);
		
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
		Assertions.assertEquals(4, planSteps.size());
		Assertions.assertSame(a, planSteps.get(0));
		Assertions.assertSame(bNeedA, planSteps.get(1));
		Assertions.assertSame(cNeedAandB, planSteps.get(2));
		Assertions.assertSame(dNeedBandC, planSteps.get(3));
	}

}
