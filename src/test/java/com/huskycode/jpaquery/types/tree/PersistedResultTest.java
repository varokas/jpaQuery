package com.huskycode.jpaquery.types.tree;

import static org.hamcrest.CoreMatchers.sameInstance;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;

public class PersistedResultTest {

	@Test
	public void shouldBeAbleToCreateAndRetrieve() {
		List<Object> persistedObjects = Arrays.asList(new Object());
		PersistedResult pt = PersistedResult.newInstance(persistedObjects);
		Assert.assertThat(pt.getPersistedObjects(), sameInstance(persistedObjects));
	}
	
	@Test
	public void shouldReturnAllObjectOfClass() {
		List<Object> persistedObjects = Arrays.asList(new Address(),
													  new Address(),
													  new Customer(),
													  new PizzaOrder(),
													  new PizzaOrder(),
													  new PizzaOrder());
		PersistedResult pt = PersistedResult.newInstance(persistedObjects);
		Assert.assertEquals(2, pt.getForClass(Address.class).size());
		Assert.assertEquals(persistedObjects.subList(0, 2), pt.getForClass(Address.class));
		Assert.assertEquals(1, pt.getForClass(Customer.class).size());
		Assert.assertEquals(persistedObjects.subList(2, 3), pt.getForClass(Customer.class));
		Assert.assertEquals(3, pt.getForClass(PizzaOrder.class).size());
		Assert.assertEquals(persistedObjects.subList(3, 6), pt.getForClass(PizzaOrder.class));
	}

}
