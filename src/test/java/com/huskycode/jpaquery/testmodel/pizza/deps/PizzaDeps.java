package com.huskycode.jpaquery.testmodel.pizza.deps;

import javax.management.RuntimeErrorException;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Address_;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Customer_;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.Employee_;
import com.huskycode.jpaquery.testmodel.pizza.Order;
import com.huskycode.jpaquery.testmodel.pizza.Order_;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle_;

/**
 * @author Varokas Panusuwan
 */
public class PizzaDeps {
	@SuppressWarnings("unchecked")
	public DependenciesDefinition getDeps() {
		return DependenciesDefinition.fromLinks(new Link[] { 
			Link.from(Customer.class, Customer_.customerAddressId)
				.to(Address.class, Address_.addressId),
			Link.from(Employee.class, Employee_.employeeAddressId)
				.to(Address.class, Address_.addressId),
				
			Link.from(Order.class, Order_.customerId)
				.to(Customer.class, Customer_.customerId),
			Link.from(Order.class, Order_.takenByEmployeeId)
				.to(Employee.class, Employee_.employeeId),
			Link.from(Order.class, Order_.deliveredByEmployeeId)
				.to(Employee.class, Employee_.employeeId),
			Link.from(Order.class, Order_.vehicleId)
				.to(Vehicle.class, Vehicle_.vehicleId),
		});
	}
	
	/**
	 * default pizza dependencies by field. Using the field to 
	 * be able to run this test without jpa context
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DependenciesDefinition getDepsUsingField() {
		try {
			return DependenciesDefinition.fromLinks(new Link[] { 
				Link.from(Customer.class, Customer.class.getDeclaredField("customerAddressId"))
					.to(Address.class, Address.class.getDeclaredField("addressId")),
				Link.from(Employee.class, Employee.class.getDeclaredField("employeeAddressId"))
					.to(Address.class, Address.class.getDeclaredField("addressId")),
					
				Link.from(Order.class, Order.class.getDeclaredField("customerId"))
					.to(Customer.class, Customer.class.getDeclaredField("customerId")),
				Link.from(Order.class, Order.class.getDeclaredField("takenByEmployeeId"))
					.to(Employee.class, Employee.class.getDeclaredField("employeeId")),
				Link.from(Order.class, Order.class.getDeclaredField("deliveredByEmployeeId"))
					.to(Employee.class, Employee.class.getDeclaredField("employeeId")),
				Link.from(Order.class, Order.class.getDeclaredField("vehicleId"))
					.to(Vehicle.class, Vehicle.class.getDeclaredField("vehicleId"))
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
