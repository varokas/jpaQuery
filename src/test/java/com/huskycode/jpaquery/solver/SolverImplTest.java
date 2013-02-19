package com.huskycode.jpaquery.solver;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static com.huskycode.jpaquery.command.CommandNodesFactory.ns;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.DepsBuilder;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodeFactory.CommandNodeImpl;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.RefDeliveryStatus;
import com.huskycode.jpaquery.testmodel.pizza.RefPaymentMethod;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleTypeEnum;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;


public class SolverImplTest {

	@Test
	public void testSolveForClassWithEmptyDepsReturnsOnlyRoot() {
		CommandNodes commands = ns(n(ClassA.class));
		CreationPlan result = SolverImpl.newInstance(new DepsBuilder().build()).solveFor(commands);

		assertThat(result.getActionGraph().getAllNodes().size(), is(1));
		assertEquals(result.getActionGraph().getAllNodes().get(0).getEntityClass(), ClassA.class);
	}

	@Test
	public void testSolveForClassWithDependenciesForOneCommand() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CommandNodes commands = ns(n(PizzaOrder.class));
		CreationPlan result = SolverImpl.newInstance(dependenciesDefinition).solveFor(commands);

		assertThat(result.getActionGraph().getAllNodes().size(), is(8));
		Set<Class<?>> expectedEntitiesInActionGraph = new HashSet<Class<?>>();
		expectedEntitiesInActionGraph.add(Address.class);
		expectedEntitiesInActionGraph.add(Vehicle.class);
		expectedEntitiesInActionGraph.add(Customer.class);
		expectedEntitiesInActionGraph.add(Employee.class);
		expectedEntitiesInActionGraph.add(PizzaOrder.class);
		expectedEntitiesInActionGraph.add(RefVehicleTypeEnum.class);
		expectedEntitiesInActionGraph.add(RefPaymentMethod.class);
		expectedEntitiesInActionGraph.add(RefDeliveryStatus.class);

		for (EntityNode n : result.getActionGraph().getAllNodes()) {
			expectedEntitiesInActionGraph.remove(n.getEntityClass());
			if (n.getEntityClass().equals(Address.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(0, n.getParent().size());
			}
			if (n.getEntityClass().equals(Vehicle.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
			}
			if (n.getEntityClass().equals(Customer.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(2, n.getParent().size());
			}
			if (n.getEntityClass().equals(Employee.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
			}
			if (n.getEntityClass().equals(PizzaOrder.class)) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				Assert.assertSame(commands.get().get(0), n.getCommand());
			}
			if (n.getEntityClass().equals(RefVehicleTypeEnum.class)) {
				Assert.assertEquals(1, n.getChilds().size());
			}
			if (n.getEntityClass().equals(RefPaymentMethod.class)) {
				Assert.assertEquals(1, n.getChilds().size());
			}
			if (n.getEntityClass().equals(RefDeliveryStatus.class)) {
				Assert.assertEquals(1, n.getChilds().size());
			}
		}

		Assert.assertEquals("Could not resolved for all entity types", 0, expectedEntitiesInActionGraph.size());
	}

	@Test
	public void testSolveForClassWithDependenciesForOneHierachyCommand() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CommandNodes commands = ns(n(Address.class,
									n(PizzaOrder.class)));
		CreationPlan result = SolverImpl.newInstance(dependenciesDefinition).solveFor(commands);
		assertThat(result.getActionGraph().getAllNodes().size(), is(8));
		Set<Class<?>> expectedEntitiesInActionGraph = new HashSet<Class<?>>();
		expectedEntitiesInActionGraph.add(Address.class);
		expectedEntitiesInActionGraph.add(Vehicle.class);
		expectedEntitiesInActionGraph.add(Customer.class);
		expectedEntitiesInActionGraph.add(Employee.class);
		expectedEntitiesInActionGraph.add(PizzaOrder.class);
		expectedEntitiesInActionGraph.add(RefVehicleTypeEnum.class);
		expectedEntitiesInActionGraph.add(RefPaymentMethod.class);
		expectedEntitiesInActionGraph.add(RefDeliveryStatus.class);

		for (EntityNode n : result.getActionGraph().getAllNodes()) {
			expectedEntitiesInActionGraph.remove(n.getEntityClass());
			if (n.getEntityClass().equals(Address.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(0, n.getParent().size());
				Assert.assertSame(commands.get().get(0), n.getCommand());
			}
			if (n.getEntityClass().equals(Vehicle.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
			}
			if (n.getEntityClass().equals(Customer.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(2, n.getParent().size());
			}
			if (n.getEntityClass().equals(Employee.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
			}
			if (n.getEntityClass().equals(PizzaOrder.class)) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				Assert.assertSame(commands.get().get(0).getChildren().get(0), n.getCommand());
			}
			if (n.getEntityClass().equals(RefVehicleTypeEnum.class)) {
				Assert.assertEquals(1, n.getChilds().size());
			}
			if (n.getEntityClass().equals(RefPaymentMethod.class)) {
				Assert.assertEquals(1, n.getChilds().size());
			}
			if (n.getEntityClass().equals(RefDeliveryStatus.class)) {
				Assert.assertEquals(1, n.getChilds().size());
			}
		}

		Assert.assertEquals("Could not resolved for all entity types", 0, expectedEntitiesInActionGraph.size());
	}

	@Test
	public void testSolveForClassWithDependenciesForTwoHierachyCommand() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CommandNodes commands = ns(n(Address.class,
								n(PizzaOrder.class),
								n(PizzaOrder.class)));
		CreationPlan result = SolverImpl.newInstance(dependenciesDefinition).solveFor(commands);
		int expectedCount = 9;
		assertThat(result.getActionGraph().getAllNodes().size(), is(expectedCount));

		int count = 0;
		for (EntityNode n : result.getActionGraph().getAllNodes()) {
			if (n.getEntityClass().equals(Address.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(0, n.getParent().size());
				Assert.assertSame(commands.get().get(0), n.getCommand());
				count++;
			}
			if (n.getEntityClass().equals(Vehicle.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
				count++;
			}
			if (n.getEntityClass().equals(Customer.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(2, n.getParent().size());
				count++;
			}
			if (n.getEntityClass().equals(Employee.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
				count++;
			}
			if (n.getEntityClass().equals(PizzaOrder.class)
					&& commands.get().get(0).getChildren().get(0) == n.getCommand()) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				count++;
			}

			if (n.getEntityClass().equals(PizzaOrder.class)
					&& commands.get().get(0).getChildren().get(1) == n.getCommand()) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				count++;
			}
			if (n.getEntityClass().equals(RefVehicleTypeEnum.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				count++;
			}
			if (n.getEntityClass().equals(RefPaymentMethod.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				count++;
			}
			if (n.getEntityClass().equals(RefDeliveryStatus.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				count++;
			}
		}

		Assert.assertEquals("Could not resolved for all entity types", expectedCount, count);
	}

	@Test
	public void testSolveForClassWithDependenciesForThreeHierachyCommand() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CommandNodes commands = ns(n(Address.class,
								n(Customer.class, n(PizzaOrder.class)),
								n(Customer.class, n(PizzaOrder.class))));
		CreationPlan result = SolverImpl.newInstance(dependenciesDefinition).solveFor(commands);
		int expectedCount = 10;
		assertThat(result.getActionGraph().getAllNodes().size(), is(expectedCount));

		int count = 0;
		EntityNode employeeNode = null;
		EntityNode customer1 = null;
		EntityNode customer2 = null;
		EntityNode pizzaOrder1 = null;
		EntityNode pizzaOrder2 = null;
		EntityNode vehical = null;
		EntityNode vehicalType = null;
		EntityNode deliveryStatus = null;
		EntityNode paymentMethod = null;
		for (EntityNode n : result.getActionGraph().getAllNodes()) {
			if (n.getEntityClass().equals(Address.class)) {
				Assert.assertEquals(3, n.getChilds().size());
				Assert.assertEquals(0, n.getParent().size());
				Assert.assertSame(commands.get().get(0), n.getCommand());
				count++;
			}
			if (n.getEntityClass().equals(Vehicle.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
				vehical = n;
				count++;
			}
			if (n.getEntityClass().equals(Customer.class)
					&& commands.get().get(0).getChildren().get(0) == n.getCommand()) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(2, n.getParent().size());
				customer1 = n;
				count++;
			}
			if (n.getEntityClass().equals(Customer.class)
					&& commands.get().get(0).getChildren().get(1) == n.getCommand()) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(2, n.getParent().size());
				customer2 = n;
				count++;
			}
			if (n.getEntityClass().equals(Employee.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
				employeeNode  = n;
				count++;
			}
			if (n.getEntityClass().equals(PizzaOrder.class)
					&& commands.get().get(0).getChildren().get(0).getChildren().get(0) == n.getCommand()) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				pizzaOrder1 = n;
				count++;
			}

			if (n.getEntityClass().equals(PizzaOrder.class)
					&& commands.get().get(0).getChildren().get(1).getChildren().get(0) == n.getCommand()) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				pizzaOrder2 = n;
				count++;
			}
			if (n.getEntityClass().equals(RefVehicleTypeEnum.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				vehicalType = n;
				count++;
			}
			if (n.getEntityClass().equals(RefPaymentMethod.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				paymentMethod = n;
				count++;
			}
			if (n.getEntityClass().equals(RefDeliveryStatus.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				deliveryStatus = n;
				count++;
			}
		}

		Assert.assertEquals("Could not resolved for all entity types", expectedCount, count);
		Assert.assertTrue("Does not contain all pizza order", employeeNode.getChilds().contains(pizzaOrder1));
		Assert.assertTrue("Does not contain all pizza order", employeeNode.getChilds().contains(pizzaOrder2));
		Assert.assertTrue("Does not contain correct pizza order", customer1.getChilds().contains(pizzaOrder1));
		Assert.assertTrue("Does not contain correct pizza order", customer2.getChilds().contains(pizzaOrder2));
		Assert.assertTrue("Does not contain correct vehical", vehicalType.getChilds().contains(vehical));
		Assert.assertTrue("Does not contain pizza order", deliveryStatus.getChilds().contains(pizzaOrder1));
		Assert.assertTrue("Does not contain pizza order", deliveryStatus.getChilds().contains(pizzaOrder2));
		Assert.assertTrue("Does not contain customer", paymentMethod.getChilds().contains(customer1));
		Assert.assertTrue("Does not contain customer", paymentMethod.getChilds().contains(customer2));
	}


	@Test
	public void testSolveForClassesWithTwoGraphs() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CommandNodes commands = ns(n(Customer.class, n(PizzaOrder.class)),
								n(Customer.class, n(PizzaOrder.class)));
		CreationPlan result = SolverImpl.newInstance(dependenciesDefinition).solveFor(commands);
		int expectedCount = 10;
		assertThat(result.getActionGraph().getAllNodes().size(), is(expectedCount));

		int count = 0;
		EntityNode employeeNode = null;
		EntityNode customer1 = null;
		EntityNode customer2 = null;
		EntityNode pizzaOrder1 = null;
		EntityNode pizzaOrder2 = null;
		EntityNode vehical = null;
		EntityNode vehicalType = null;
		EntityNode deliveryStatus = null;
		EntityNode paymentMethod = null;
		for (EntityNode n : result.getActionGraph().getAllNodes()) {
			if (n.getEntityClass().equals(Address.class)) {
				Assert.assertEquals(3, n.getChilds().size());
				Assert.assertEquals(0, n.getParent().size());
				count++;
			}
			if (n.getEntityClass().equals(Vehicle.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
				vehical = n;
				count++;
			}
			if (n.getEntityClass().equals(Customer.class)
					&& commands.get().get(0) == n.getCommand()) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(2, n.getParent().size());
				customer1 = n;
				count++;
			}
			if (n.getEntityClass().equals(Customer.class)
					&& commands.get().get(1) == n.getCommand()) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(2, n.getParent().size());
				customer2 = n;
				count++;
			}
			if (n.getEntityClass().equals(Employee.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
				employeeNode  = n;
				count++;
			}
			if (n.getEntityClass().equals(PizzaOrder.class)
					&& commands.get().get(0).getChildren().get(0) == n.getCommand()) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				pizzaOrder1 = n;
				count++;
			}

			if (n.getEntityClass().equals(PizzaOrder.class)
					&& commands.get().get(1).getChildren().get(0) == n.getCommand()) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				pizzaOrder2 = n;
				count++;
			}
			if (n.getEntityClass().equals(RefVehicleTypeEnum.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				vehicalType = n;
				count++;
			}
			if (n.getEntityClass().equals(RefPaymentMethod.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				paymentMethod = n;
				count++;
			}
			if (n.getEntityClass().equals(RefDeliveryStatus.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				deliveryStatus = n;
				count++;
			}
		}

		Assert.assertEquals("Could not resolved for all entity types", expectedCount, count);
		Assert.assertTrue("Does not contain all pizza order", employeeNode.getChilds().contains(pizzaOrder1));
		Assert.assertTrue("Does not contain all pizza order", employeeNode.getChilds().contains(pizzaOrder2));
		Assert.assertTrue("Does not contain correct pizza order", customer1.getChilds().contains(pizzaOrder1));
		Assert.assertTrue("Does not contain correct pizza order", customer2.getChilds().contains(pizzaOrder2));
		Assert.assertTrue("Does not contain correct vehical", vehicalType.getChilds().contains(vehical));
		Assert.assertTrue("Does not contain pizza order", deliveryStatus.getChilds().contains(pizzaOrder1));
		Assert.assertTrue("Does not contain pizza order", deliveryStatus.getChilds().contains(pizzaOrder2));
		Assert.assertTrue("Does not contain customer", paymentMethod.getChilds().contains(customer1));
		Assert.assertTrue("Does not contain customer", paymentMethod.getChilds().contains(customer2));
	}


	@Test
	public void testSolveForClassesWithAMultipleParetnsGraph() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CommandNode sharedChild = n(PizzaOrder.class);
		CommandNodes commands = ns(n(Customer.class, sharedChild),
								n(Employee.class, sharedChild));

		CreationPlan result = SolverImpl.newInstance(dependenciesDefinition).solveFor(commands);

		assertThat(result.getActionGraph().getAllNodes().size(), is(8));

		int count = 0;
		EntityNode employee = null;
		EntityNode customer = null;
		EntityNode pizzaOrder = null;
		for (EntityNode n : result.getActionGraph().getAllNodes()) {
			if (n.getEntityClass().equals(Address.class)) {
				Assert.assertEquals(2, n.getChilds().size());
				Assert.assertEquals(0, n.getParent().size());
				count++;
			}
			if (n.getEntityClass().equals(Vehicle.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
				count++;
			}
			if (n.getEntityClass().equals(Customer.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(2, n.getParent().size());
				Assert.assertEquals(commands.get().get(0), n.getCommand());
				customer = n;
				count++;
			}
			if (n.getEntityClass().equals(Employee.class)) {
				Assert.assertEquals(1, n.getChilds().size());
				Assert.assertEquals(1, n.getParent().size());
				Assert.assertEquals(commands.get().get(1), n.getCommand());
				employee = n;
				count++;
			}
			if (n.getEntityClass().equals(PizzaOrder.class)) {
				Assert.assertEquals(0, n.getChilds().size());
				Assert.assertEquals(4, n.getParent().size());
				Assert.assertSame(commands.get().get(0).getChildren().get(0), n.getCommand());
				pizzaOrder = n;
				count++;
			}
		}

		Assert.assertEquals("Could not resolved for all entity types", 5, count);

		Assert.assertTrue("Does not contain pizza order", customer.getChilds().contains(pizzaOrder));
		Assert.assertTrue("Does not contain pizza order", employee.getChilds().contains(pizzaOrder));
	}

	@Test
	public void testSolveJoinDescendantByTheParentsWithSharedAscendants() throws SecurityException, NoSuchFieldException {
	    DependenciesDefinition dependenciesDefinition = getJoinDescendantDefinition();
        CommandNode sharedChild = n(L3.class);
        CommandNodeImpl l1_1Command = n(L1_1.class);
        CommandNodes commands = ns(l1_1Command.with(sharedChild),
                                    n(L1_2.class).with(sharedChild));

        CreationPlan result = SolverImpl.newInstance(dependenciesDefinition).solveFor(commands);

        //Assert no duplication of L2
        assertThat(result.getActionGraph().getAllNodes().size(), is(4));

        EntityNode l2Node = result.getActionGraph().getAllNodes().get(2);
        Assert.assertEquals(l2Node.getEntityClass(), L2.class);
        //L2 entity depends on command L1_1 correctly.
        Assert.assertSame(l2Node.getParent().toArray(new EntityNode[0])[0].getCommand(), l1_1Command);
	}

	@SuppressWarnings("unchecked")
    private DependenciesDefinition getJoinDescendantDefinition() throws SecurityException, NoSuchFieldException {
	    return new DepsBuilder().withLinks(new Link[]{
	            Link.from(L2.class, L2.class.getDeclaredField("referenceL1_1"))
	                .to(L1_1.class, L1_1.class.getDeclaredField("id")),
	            Link.from(L3.class, L3.class.getDeclaredField("referenceL1_1"))
                    .to(L1_1.class, L1_1.class.getDeclaredField("id")),
                Link.from(L3.class, L3.class.getDeclaredField("referenceL1_2"))
                    .to(L1_2.class, L1_2.class.getDeclaredField("id")),
                Link.from(L3.class, L3.class.getDeclaredField("referenceL2"))
                    .to(L2.class, L2.class.getDeclaredField("id"))
	    }).build();
	}

	private static class L1_1 {
	    private int id;
	}

	private static class L1_2 {
        private int id;
    }

	private static class L2 {
        private int id;
        private int referenceL1_1;
    }

	private static class L3 {
	    private int id;
	    private int referenceL1_1;
	    private int referenceL1_2;
	    private int referenceL2;

	}
}
