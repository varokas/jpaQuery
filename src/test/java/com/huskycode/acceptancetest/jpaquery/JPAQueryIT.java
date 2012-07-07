package com.huskycode.acceptancetest.jpaquery;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.huskycode.jpaquery.AbstractEntityManagerWired;
import com.huskycode.jpaquery.JPAQueryContext;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;
import com.huskycode.jpaquery.types.tree.PersistedResult;

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
        
        Address address = resultMap.getForClass(Address.class);
        Employee employee = resultMap.getForClass(Employee.class);
        Customer customer = resultMap.getForClass(Customer.class);
        PizzaOrder pizzaOrder = resultMap.getForClass(PizzaOrder.class);
        
        assertThat(employee.getEmployeeAddressId(), is(address.getAddressId()));
        assertThat(customer.getCustomerAddressId(), is(address.getAddressId()));
        assertThat(pizzaOrder.getCustomerId(), is(customer.getCustomerId()));
        assertThat(pizzaOrder.getDeliveredByEmployeeId(), is(employee.getEmployeeId()));
        assertThat(pizzaOrder.getTakenByEmployeeId(), is(employee.getEmployeeId()));
    }
	
	public class ClassMap extends HashMap<Class<?>, Object> {
		private static final long serialVersionUID = 1L;
	
		@SuppressWarnings("unchecked")
		public <E> E getForClass(Class<E> clazz) {
			return (E) this.get(clazz);
		}
	}
	
	private ClassMap mapByClass(List<Object> objects) {
		ClassMap map = new ClassMap();
		for(Object obj : objects) {
			map.put(obj.getClass(), obj);
		}
		
		return map;
	}
}
