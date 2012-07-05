package com.huskycode.jpaquery.solver;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Or;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.solver.SolverImpl.EntityAndDependencyCount;
import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.Order;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;
import com.huskycode.jpaquery.types.tree.CreationPlan;

public class SolverImplTest {
	private SolverImpl solverImpl;

	@Before
	public void before() {
		solverImpl = SolverImpl.newInstance();
	}
	
	@Test
	public void testSolveForClassWithEmptyDepsReturnsOnlyRoot() {
		CreationPlan result = solverImpl.solveFor(ClassA.class, DependenciesDefinition.fromLinks(new Link[0]));
		
		assertThat(result.getClasses().size(), is(1));
		assertEquals(result.getClasses().get(0), ClassA.class);
	}
	
	@Test
	public void testSolveForClassWithDependencies() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CreationPlan result = solverImpl.solveFor(Order.class, dependenciesDefinition);
		
		assertThat(result.getClasses().size(), is(5));
		assertEquals(result.getClasses().get(0), Address.class);
		assertEquals(result.getClasses().get(1), Vehicle.class);
		assertEquals(result.getClasses().get(2), Customer.class);
		assertEquals(result.getClasses().get(3), Employee.class);
		assertEquals(result.getClasses().get(4), Order.class);
	}
	
	@Test
	public void testGetAllDependentEntitiesWithDependencyCountWhenThereIsNoDependency() {
		DependenciesDefinition dependenciesDefinition = DependenciesDefinition.fromLinks(new Link[0]);
		List<EntityAndDependencyCount> result = solverImpl.getAllDependentEntitiesWithDependencyCount(ClassA.class,
												dependenciesDefinition);	
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0, result.get(0).getCount());

		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testGetAllDependentEntitiesWithDependencyCountWhenThereIsOnlyDirectDependency() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		Set<Class<?>> expectedClasses = new HashSet<Class<?>>();
		expectedClasses.add(Address.class);
		expectedClasses.add(Employee.class);
		List<EntityAndDependencyCount> result = solverImpl.getAllDependentEntitiesWithDependencyCount(Employee.class,
												dependenciesDefinition);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		
		Assert.assertThat(expectedClasses, hasItems(
												result.get(0).getEntityClass(),
												result.get(1).getEntityClass()
											));
		
		for (EntityAndDependencyCount entityCount : result) {
			if (entityCount.getEntityClass() == Address.class) {
				Assert.assertEquals(0, entityCount.getCount());
			} else if (entityCount.getEntityClass() == Employee.class) {
				Assert.assertEquals(1, entityCount.getCount());
			}
		}
	}

}
