package com.huskycode.jpaquery.crud;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class CreationPlanFromDefinitionTest {
	
	@Test
	public void testCreatePlanFromDefinition() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CreationPlanFromDefinition creator = CreationPlanFromDefinition.getInstance();
		
		CreationPlan plan = creator.from(dependenciesDefinition);
		
		assertThat(plan.getActionGraph().getAllNodes().size(), is(12));
		Set<Class<?>> expectedEntitiesInActionGraph = new HashSet<Class<?>>();
		expectedEntitiesInActionGraph.add(Address.class);
		expectedEntitiesInActionGraph.add(Vehicle.class);
		expectedEntitiesInActionGraph.add(Customer.class);
		expectedEntitiesInActionGraph.add(Employee.class);
		expectedEntitiesInActionGraph.add(PizzaOrder.class);
		
		for (EntityNode n : plan.getActionGraph().getAllNodes()) {
			expectedEntitiesInActionGraph.remove(n.getEntityClass());
			if (n.getEntityClass().equals(Address.class)) {
				Assertions.assertEquals(2, n.getChilds().size());
				Assertions.assertEquals(0, n.getParent().size());
			}
			if (n.getEntityClass().equals(Vehicle.class)) {
				Assertions.assertEquals(1, n.getChilds().size());
				Assertions.assertEquals(1, n.getParent().size());
			}
			if (n.getEntityClass().equals(Customer.class)) {
				Assertions.assertEquals(1, n.getChilds().size());
				Assertions.assertEquals(2, n.getParent().size());
			}
			if (n.getEntityClass().equals(Employee.class)) {
				Assertions.assertEquals(1, n.getChilds().size());
				Assertions.assertEquals(1, n.getParent().size());
			}
			if (n.getEntityClass().equals(PizzaOrder.class)) {
				Assertions.assertEquals(1, n.getChilds().size());
				Assertions.assertEquals(4, n.getParent().size());
			}
		}
		
		Assertions.assertEquals(0, expectedEntitiesInActionGraph.size(), "Could not resolved for all entity types");
		
	}
}
