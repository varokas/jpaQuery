package com.huskycode.jpaquery.solver;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static com.huskycode.jpaquery.command.CommandNodesFactory.ns;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.RefDeliveryStatus;
import com.huskycode.jpaquery.testmodel.pizza.RefPaymentMethod;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleTypeEnum;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

public class CommandInterpretorTest {
	
	private CommandPlan plan;
	private CommandNodes commands;

	@BeforeEach
	public void before() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		commands = ns(n(Address.class,
									n(PizzaOrder.class)));
		CommandInterpretor interpretor = CommandInterpretor.getInstance();
		plan = interpretor.createPlan(commands, dependenciesDefinition);
	}
	
	@Test
	public void testCreatePlanReturnPlanInCorrectOrderFromRootToLeave() {
		assertEquals(2, plan.getPlan().size());
		assertEquals(commands.get().get(0), plan.getPlan().get(0));
		assertEquals(commands.get().get(0).getChildren().get(0), plan.getPlan().get(1));
		
	}
	
	@Test
	public void testCreatePlanReturnCorrectInOrderEntityData() {
		InOrderEntityData inOrderData = plan.getInOrderEntityData();

		assertEquals(8, inOrderData.getInOrderEntityList().size());
		int refPaymentMethodIndex = inOrderData.getOrderIndexOf(RefPaymentMethod.class);
		int refDeliveryStatusIndex = inOrderData.getOrderIndexOf(RefDeliveryStatus.class);
		int refVehicleTypeIndex = inOrderData.getOrderIndexOf(RefVehicleTypeEnum.class);
		
		int addressIndex = inOrderData.getOrderIndexOf(Address.class);
		int vehicleIndex = inOrderData.getOrderIndexOf(Vehicle.class);
		
		int customerIndex = inOrderData.getOrderIndexOf(Customer.class);
		int employeeIndex = inOrderData.getOrderIndexOf(Employee.class);
		int pizzaOrderIndex = inOrderData.getOrderIndexOf(PizzaOrder.class);
		
		assertTrue(customerIndex > addressIndex);
		assertTrue(customerIndex > refPaymentMethodIndex);
		
		assertTrue(employeeIndex > addressIndex);
		
		assertTrue(vehicleIndex > refVehicleTypeIndex);
		
		assertTrue(pizzaOrderIndex > refDeliveryStatusIndex);
		assertTrue(pizzaOrderIndex > customerIndex);
		assertTrue(pizzaOrderIndex > employeeIndex);
		assertTrue(pizzaOrderIndex > vehicleIndex);
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
		
		assertEquals(3, commandNodePlan.size());
		assertEquals(sharedChild, commandNodePlan.get(2));
		
	}
}
