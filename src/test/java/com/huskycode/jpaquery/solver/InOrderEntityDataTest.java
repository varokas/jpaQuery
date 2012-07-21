package com.huskycode.jpaquery.solver;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;

public class InOrderEntityDataTest {
	
	@Test
	public void testGetOrderIndexReturnCorrectIndexForThatGivenEntity() {
		Class<?> zero = Customer.class;
		Class<?> one = Employee.class;
		Class<?> two = PizzaOrder.class;
		InOrderEntityData inOrder = new InOrderEntityData(Arrays.asList(zero, one, two));
		
		Assert.assertEquals(0, inOrder.getOrderIndexOf(zero));
		Assert.assertEquals(1, inOrder.getOrderIndexOf(one));
		Assert.assertEquals(2, inOrder.getOrderIndexOf(two));
		
	}
	
	@Test(expected=NullPointerException.class)
	public void testThrowNullPointerExceptionIfGetOrderIndexOfNonExistingClass() {
		Class<?> one = Customer.class;
		Class<?> two = Employee.class;
		Class<?> three = PizzaOrder.class;
		List<Class<?>> orderData = Arrays.asList(one, two);
		InOrderEntityData inOrder = new InOrderEntityData(orderData);
		
		inOrder.getOrderIndexOf(three);
		Assert.fail("Do not throw Null pointer exception for non-existing entity class");
	}
}
