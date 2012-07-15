package com.huskycode.jpaquery.persister;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.solver.SolverImpl;
import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;
import com.huskycode.jpaquery.types.tree.ActionGraph;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;
import com.huskycode.jpaquery.types.tree.PersistedResult;

public class PersisterImplTest {
	private PersisterImpl persister;
	private EntityManager em;
	private DependenciesDefinition deps;
	private Random any = new Random();

	@Before
	public void before() {
	    em = Mockito.mock(EntityManager.class);
	    Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				Object obj = args[0];
				Field idField = findIdField(obj.getClass());
				if (idField != null) {
					idField.setAccessible(true);
					if (idField.getType().equals(Long.class)) {
						idField.set(obj, any.nextLong());
					}
				}
				return null;
			}
		}).when(em).persist(Mockito.any());
		deps = new PizzaDeps().getDepsUsingField();
		
	    persister = new PersisterImpl(em, deps);
	}
	
	@Test
	public void testPersistValuePersistTheRootFromCreationTree() {
        ActionGraph actionGraph = ActionGraph.newInstance();
        actionGraph.addEntityNode(EntityNode.newInstance(ClassA.class));
		CreationPlan plan = CreationPlan.newInstance(actionGraph);
		
		PersistedResult persistedTree = persister.persistValues(plan);
		assertThat(persistedTree.getPersistedObjects().size(), is(1));
		assertThat(persistedTree.getPersistedObjects().get(0), CoreMatchers.instanceOf(ClassA.class));
	}
	
//	@Test
//	public void testPersistValueCallsRandomValue() throws IllegalAccessException {
//		ActionGraph actionGraph = ActionGraph.newInstance();
//	    actionGraph.addEntityNode(EntityNode.newInstance(ClassA.class));
//        CreationPlan plan = CreationPlan.newInstance(actionGraph);
//		
//		persister.persistValues(plan);
//		
//		Mockito.verify(randomValuePopulator, Mockito.times(1)).populateValue(Mockito.any(ClassA.class));
//	}
	
	@Test
	public void testPersistValuePersistAGivenClass() throws IllegalAccessException {
		ActionGraph actionGraph = ActionGraph.newInstance();
	    actionGraph.addEntityNode(EntityNode.newInstance(ClassA.class));
        CreationPlan plan = CreationPlan.newInstance(actionGraph);
		
		persister.persistValues(plan);
		
		verify(em, times(1)).persist(Mockito.any(ClassA.class));
	}
	
	@Test
	public void testPersistValuesCreateAndPersistAllEntityWithCorrectForeignKeysFromParents() {
		//set up
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		CommandNode command = n(Address.class,
								n(Customer.class, n(PizzaOrder.class)),
								n(Customer.class, n(PizzaOrder.class)));
		CreationPlan creationPlan = SolverImpl.newInstance().solveFor(command, dependenciesDefinition);
		
		PersisterImpl persister = new PersisterImpl(em, dependenciesDefinition);
		
		//execute
		PersistedResult result = persister.persistValues(creationPlan);
		
		//get data
		Address address  = null;
		Vehicle vehicle = null;
		Customer customer1 = null;
		Employee employee = null;
		Customer customer2 = null;
		PizzaOrder order1 = null;
		PizzaOrder order2 =  null;
		for (Object obj : result.getPersistedObjects()) {
			if (obj instanceof Address) {
				address = (Address) obj;
			}
			if (obj instanceof Vehicle) {
				vehicle = (Vehicle) obj;
			}
			if (obj instanceof Customer
					&& customer1 == null) {
				customer1 = (Customer) obj;
			} else if (obj instanceof Customer
					&& customer2 == null) {
				customer2 = (Customer) obj;
			}
			if (obj instanceof Employee) {
				employee = (Employee) obj;
			}
			if (obj instanceof PizzaOrder
					&& order1 == null) {
				order1 = (PizzaOrder) obj;
			} else if (obj instanceof PizzaOrder
							&& order2 == null) {
				order2 = (PizzaOrder) obj;
			}
		}
		
		//verify
		Assert.assertNotNull(address);
		Assert.assertNotNull(vehicle);
		Assert.assertNotNull(customer1);
		Assert.assertNotNull(employee);
		Assert.assertNotNull(customer2);
		Assert.assertNotNull(order1);
		Assert.assertNotNull(order2);
		
		Assert.assertEquals(customer1.getCustomerAddressId(), address.getAddressId().longValue());
		Assert.assertEquals(customer2.getCustomerAddressId(), address.getAddressId().longValue());
		Assert.assertEquals(employee.getEmployeeAddressId(), address.getAddressId().longValue());
		Assert.assertEquals(employee.getEmployeeId().longValue(), order1.getDeliveredByEmployeeId());
		Assert.assertEquals(employee.getEmployeeId().longValue(), order1.getTakenByEmployeeId());
		Assert.assertEquals(employee.getEmployeeId().longValue(), order2.getDeliveredByEmployeeId());
		Assert.assertEquals(employee.getEmployeeId().longValue(), order2.getTakenByEmployeeId());
		
		if (order1.getCustomerId() == customer1.getCustomerId()) {
			Assert.assertEquals(customer2.getCustomerId().longValue(), order2.getCustomerId());
		} else {
			Assert.assertEquals(customer2.getCustomerId().longValue(), order1.getCustomerId());
			Assert.assertEquals(customer1.getCustomerId().longValue(), order2.getCustomerId());
		}
	}
	
	private Field findIdField(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		for(Field field : fields) {
			if(field.getAnnotation(Id.class) != null) {
				return field;
			}
		}
		return null;
	}
}
