package com.huskycode.jpaquery.solver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import com.huskycode.jpaquery.testmodel.pizza.Customer;

import org.junit.jupiter.api.Test;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;

public class InOrderEntityDataTest {
	
	@Test
	public void testGetOrderIndexReturnCorrectIndexForThatGivenEntity() {
		Class<?> zero = Customer.class;
		Class<?> one = Employee.class;
		Class<?> two = PizzaOrder.class;
		InOrderEntityData inOrder = new InOrderEntityData(Arrays.asList(zero, one, two));
		
		assertEquals(0, inOrder.getOrderIndexOf(zero));
		assertEquals(1, inOrder.getOrderIndexOf(one));
		assertEquals(2, inOrder.getOrderIndexOf(two));
		
	}
	
	@Test
	public void testThrowNullPointerExceptionIfGetOrderIndexOfNonExistingClass() {
		assertThrows(NullPointerException.class, () -> {
			Class<?> one = Customer.class;
			Class<?> two = Employee.class;
			Class<?> three = PizzaOrder.class;
			List<Class<?>> orderData = Arrays.asList(one, two);
			InOrderEntityData inOrder = new InOrderEntityData(orderData);

			inOrder.getOrderIndexOf(three);
			fail("Do not throw Null pointer exception for non-existing entity class");
		});
	}
}
