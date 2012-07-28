package com.huskycode.acceptancetest.jpaquery;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static com.huskycode.jpaquery.command.CommandNodesFactory.ns;
import static com.huskycode.jpaquery.util.ClassMap.mapByClass;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.huskycode.jpaquery.AbstractEntityManagerWired;
import com.huskycode.jpaquery.JPAQueryContext;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;
import com.huskycode.jpaquery.types.tree.PersistedResult;
import com.huskycode.jpaquery.util.ClassMap;
import com.huskycode.jpaquery.util.MapUtil;

/**
 * @author Varokas Panusuwan
 */
@Transactional
public class JPAQueryIT extends AbstractEntityManagerWired {
    private PizzaDeps pizzaDeps;
    private JPAQueryContext context;
    
    @Before
    public void before() {
    	pizzaDeps = new PizzaDeps();
    	context = JPAQueryContext.newInstance(entityManager, pizzaDeps.getDeps());
    }
	
	@Test
    public void testCreateClassWithNoDeps() {
        PersistedResult result = context.create(Address.class);
        
        assertThat(result.getPersistedObjects().size(), is(1));
        Object persistedObj = result.getPersistedObjects().get(0);
        
        assertThat(persistedObj, is(instanceOf(Address.class)));
        assertThat(((Address)persistedObj).getAddressId(), is(not(nullValue())));
    }
	
	@Test 
    public void testCreateClassWithDeps() {        
        PersistedResult result = context.create(PizzaOrder.class);
        
        ClassMap resultMap = mapByClass(result.getPersistedObjects());
        
        Address address = resultMap.getForClass(Address.class).get(0);
        Employee employee = resultMap.getForClass(Employee.class).get(0);
        Customer customer = resultMap.getForClass(Customer.class).get(0);
        PizzaOrder pizzaOrder = resultMap.getForClass(PizzaOrder.class).get(0);
        
        assertThat(employee.getEmployeeAddressId(), is(address.getAddressId()));
        assertThat(customer.getCustomerAddressId(), is(address.getAddressId()));
        assertThat(pizzaOrder.getCustomerId(), is(customer.getCustomerId()));
        assertThat(pizzaOrder.getDeliveredByEmployeeId(), is(employee.getEmployeeId()));
        assertThat(pizzaOrder.getTakenByEmployeeId(), is(employee.getEmployeeId()));
    }
	
	@Test 
    public void testCreateFromDependencyDefinition() {
		PersistedResult result = context.createFromDependencyDefinition();
		
		ClassMap resultMap = mapByClass(result.getPersistedObjects());
		 
		Address address = resultMap.getForClass(Address.class).get(0);
        Employee employee = resultMap.getForClass(Employee.class).get(0);
        Customer customer = resultMap.getForClass(Customer.class).get(0);
        PizzaOrder pizzaOrder = resultMap.getForClass(PizzaOrder.class).get(0);
        
        assertThat(employee.getEmployeeAddressId(), is(address.getAddressId()));
        assertThat(customer.getCustomerAddressId(), is(address.getAddressId()));
        assertThat(pizzaOrder.getCustomerId(), is(customer.getCustomerId()));
        assertThat(pizzaOrder.getDeliveredByEmployeeId(), is(employee.getEmployeeId()));
        assertThat(pizzaOrder.getTakenByEmployeeId(), is(employee.getEmployeeId()));
	}
	
	@Test 
    public void testCreateFromCommands() {
		CommandNodes commands = ns(n(Address.class,
									n(PizzaOrder.class),
									n(PizzaOrder.class)));
		PersistedResult result = context.create(commands);
		
		ClassMap resultMap = mapByClass(result.getPersistedObjects());
		 
		Address address = resultMap.getForClass(Address.class).get(0);
        Employee employee = resultMap.getForClass(Employee.class).get(0);
        Customer customer = resultMap.getForClass(Customer.class).get(0);
        List<PizzaOrder> pizzaOrder = resultMap.getForClass(PizzaOrder.class);
        
        assertThat(employee.getEmployeeAddressId(), is(address.getAddressId()));
        assertThat(customer.getCustomerAddressId(), is(address.getAddressId()));
        assertThat(pizzaOrder.get(0).getCustomerId(), is(customer.getCustomerId()));
        assertThat(pizzaOrder.get(0).getDeliveredByEmployeeId(), is(employee.getEmployeeId()));
        assertThat(pizzaOrder.get(0).getTakenByEmployeeId(), is(employee.getEmployeeId()));
        assertThat(pizzaOrder.get(1).getCustomerId(), is(customer.getCustomerId()));
        assertThat(pizzaOrder.get(1).getDeliveredByEmployeeId(), is(employee.getEmployeeId()));
        assertThat(pizzaOrder.get(1).getTakenByEmployeeId(), is(employee.getEmployeeId()));
	}
	
	
}
