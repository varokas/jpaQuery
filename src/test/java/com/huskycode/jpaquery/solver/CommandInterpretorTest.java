package com.huskycode.jpaquery.solver;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static com.huskycode.jpaquery.command.CommandNodesFactory.ns;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

public class CommandInterpretorTest {
	
	private CommandPlan plan;
	private CommandNodes commands;

	@Before
	public void before() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		commands = ns(n(Address.class,
									n(PizzaOrder.class)));
		CommandInterpretor interpretor = CommandInterpretor.getInstance();
		plan = interpretor.createPlan(commands, dependenciesDefinition);
	}
	
	@Test
	public void testCreatePlanReturnPlanInCorrectOrderFromRootToLeave() {
		Assert.assertEquals(2, plan.getPlan().size());
		Assert.assertEquals(commands.get().get(0), plan.getPlan().get(0));
		Assert.assertEquals(commands.get().get(0).getChildren().get(0), plan.getPlan().get(1));
		
	}
	
	@Test
	public void testCreatePlanReturnCorrectInOrderEntityData() {
		InOrderEntityData inOrderData = plan.getInOrderEntityData();
		List<Class<?>> inOderEntities = inOrderData.getInOrderEntityList();

		Assert.assertEquals(5, inOrderData.getInOrderEntityList().size());
		Assert.assertTrue(inOrderData.getOrderIndexOf(Address.class) < 2);
		Assert.assertTrue(inOrderData.getOrderIndexOf(Vehicle.class) < 2);
		Assert.assertTrue(inOrderData.getOrderIndexOf(Customer.class) > 1);
		Assert.assertTrue(inOrderData.getOrderIndexOf(Employee.class) > 1);
		Assert.assertTrue(inOrderData.getOrderIndexOf(PizzaOrder.class) == 4);
		
		Assert.assertEquals(0, inOrderData.getOrderIndexOf(inOderEntities.get(0)));
		Assert.assertEquals(1, inOrderData.getOrderIndexOf(inOderEntities.get(1)));
		Assert.assertEquals(2, inOrderData.getOrderIndexOf(inOderEntities.get(2)));
		Assert.assertEquals(3, inOrderData.getOrderIndexOf(inOderEntities.get(3)));
		Assert.assertEquals(4, inOrderData.getOrderIndexOf(inOderEntities.get(4)));
	}
	
	@Test
	public void testCreatePlanReturnCorrectlyForMultiParentCommands() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CommandNode sharedChild = n(PizzaOrder.class);
		commands = ns(n(Customer.class, sharedChild),
								n(Employee.class, sharedChild));
		CommandInterpretor interpretor = CommandInterpretor.getInstance();
		plan = interpretor.createPlan(commands, dependenciesDefinition);
		
		List<CommandNode> commandNodePlan = plan.getPlan();
		
		Assert.assertEquals(3, commandNodePlan.size());
		Assert.assertEquals(sharedChild, commandNodePlan.get(2));
		
	}
}
