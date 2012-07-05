package com.huskycode.jpaquery.testmodel.pizza.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.huskycode.jpaquery.AbstractEntityManagerWired;
import com.huskycode.jpaquery.testmodel.pizza.Address_;
import com.huskycode.jpaquery.testmodel.pizza.Customer_;
import com.huskycode.jpaquery.testmodel.pizza.Employee_;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder_;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle_;

public class PizzaEntityTest extends AbstractEntityManagerWired {
	@Test
	public void testMetaModelPopulated() {
		assertThat(Customer_.customerId, is(not(nullValue())));
		assertThat(Customer_.customerAddressId, is(not(nullValue())));
		
		assertThat(Address_.addressId, is(not(nullValue())));
		
		assertThat(PizzaOrder_.orderId, is(not(nullValue())));
		assertThat(PizzaOrder_.customerId, is(not(nullValue())));
		assertThat(PizzaOrder_.takenByEmployeeId, is(not(nullValue())));
		assertThat(PizzaOrder_.deliveredByEmployeeId, is(not(nullValue())));
		assertThat(PizzaOrder_.vehicleId, is(not(nullValue())));
		
		assertThat(Employee_.employeeId, is(not(nullValue())));
		assertThat(Employee_.employeeAddressId, is(not(nullValue())));
		
		assertThat(Vehicle_.vehicleId, is(not(nullValue())));
	}
}