package com.huskycode.jpaquery.types.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;

public class CreationPlanTest {

//	@SuppressWarnings("rawtypes")
//	@Test
//	public void testCreateWithRoot() {
//		ActionGraph actionGraph = ActionGraph.newInstance();
//        actionGraph.addEntityNode(EntityNode.newInstance(Object.class));
//		CreationPlan plan = CreationPlan.newInstance(actionGraph);
//        Assert.assertThat(plan.getClasses(), is(sameInstance(plan)));
//
//    }
	
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
		List<EntityNode> planSteps = plan.getPlan();
		
		//verify
		Assert.assertEquals(4, planSteps.size());
		Assert.assertSame(a, planSteps.get(0));
		Assert.assertSame(bNeedA, planSteps.get(1));
		Assert.assertSame(cNeedAandB, planSteps.get(2));
		Assert.assertSame(dNeedBandC, planSteps.get(3));
		
		Assert.assertEquals(0, a.getLevel());
		Assert.assertEquals(1, bNeedA.getLevel());
		Assert.assertEquals(2, cNeedAandB.getLevel());
		Assert.assertEquals(3, dNeedBandC.getLevel());
	}

}
