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
import com.huskycode.jpaquery.testmodel.pizza.RefBaseType;
import com.huskycode.jpaquery.testmodel.pizza.RefBaseType_;
import com.huskycode.jpaquery.testmodel.pizza.RefDeliveryStatus;
import com.huskycode.jpaquery.testmodel.pizza.RefDeliveryStatus_;
import com.huskycode.jpaquery.testmodel.pizza.RefPaymentMethod;
import com.huskycode.jpaquery.testmodel.pizza.RefPaymentMethod_;
import com.huskycode.jpaquery.testmodel.pizza.RefTopping;
import com.huskycode.jpaquery.testmodel.pizza.RefTopping_;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleType;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleType_;
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
		
		assertThat(RefBaseType_.baseTypeCode, is(not(nullValue())));
		assertThat(RefDeliveryStatus_.deliveryStatusCode, is(not(nullValue())));
		assertThat(RefPaymentMethod_.paymentMethodCode, is(not(nullValue())));
		assertThat(RefTopping_.toppingCode, is(not(nullValue())));
		assertThat(RefVehicleType_.vehicleTypeCode, is(not(nullValue())));
	}
	
	@Test
	public void testRefTablesPopulated() {
		assertThat(entityManager.find(RefBaseType.class, "TH"), is(not(nullValue())));
		assertThat(entityManager.find(RefBaseType.class, "DD"), is(not(nullValue())));
		
		assertThat(entityManager.find(RefPaymentMethod.class, "Cash"), is(not(nullValue())));
		assertThat(entityManager.find(RefPaymentMethod.class, "CC"), is(not(nullValue())));
		assertThat(entityManager.find(RefPaymentMethod.class, "Check"), is(not(nullValue())));
		
		assertThat(entityManager.find(RefDeliveryStatus.class, "Completed"), is(not(nullValue())));
		assertThat(entityManager.find(RefDeliveryStatus.class, "Returned"), is(not(nullValue())));
		
		assertThat(entityManager.find(RefTopping.class, "Ham"), is(not(nullValue())));
		assertThat(entityManager.find(RefTopping.class, "PA"), is(not(nullValue())));
		
		assertThat(entityManager.find(RefVehicleType.class, "Bike"), is(not(nullValue())));
		assertThat(entityManager.find(RefVehicleType.class, "MBike"), is(not(nullValue())));
		assertThat(entityManager.find(RefVehicleType.class, "Van"), is(not(nullValue())));
	}
}