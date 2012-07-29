package com.huskycode.jpaquery.testmodel.pizza.deps;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.DepsBuilder;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Address_;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Customer_;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.Employee_;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder_;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrdered;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrdered_;
import com.huskycode.jpaquery.testmodel.pizza.RefBaseType;
import com.huskycode.jpaquery.testmodel.pizza.RefBaseType_;
import com.huskycode.jpaquery.testmodel.pizza.RefDeliveryStatus;
import com.huskycode.jpaquery.testmodel.pizza.RefDeliveryStatus_;
import com.huskycode.jpaquery.testmodel.pizza.RefPaymentMethod;
import com.huskycode.jpaquery.testmodel.pizza.RefPaymentMethod_;
import com.huskycode.jpaquery.testmodel.pizza.RefTopping;
import com.huskycode.jpaquery.testmodel.pizza.RefTopping_;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleType;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleTypeEnum;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleType_;
import com.huskycode.jpaquery.testmodel.pizza.Topping;
import com.huskycode.jpaquery.testmodel.pizza.Topping_;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle_;

/**
 * @author Varokas Panusuwan
 */
public class PizzaDeps {
	@SuppressWarnings("unchecked")
	public DependenciesDefinition getDeps() {
		return new DepsBuilder().withLinks(new Link[] { 
			Link.from(Customer.class, Customer_.customerAddressId)
				.to(Address.class, Address_.addressId),
			Link.from(Employee.class, Employee_.employeeAddressId)
				.to(Address.class, Address_.addressId),
				
			Link.from(PizzaOrder.class, PizzaOrder_.customerId)
				.to(Customer.class, Customer_.customerId),
			Link.from(PizzaOrder.class, PizzaOrder_.takenByEmployeeId)
				.to(Employee.class, Employee_.employeeId),
			Link.from(PizzaOrder.class, PizzaOrder_.deliveredByEmployeeId)
				.to(Employee.class, Employee_.employeeId),
			Link.from(PizzaOrder.class, PizzaOrder_.vehicleId)
				.to(Vehicle.class, Vehicle_.vehicleId),
			Link.from(PizzaOrdered.class, PizzaOrdered_.orderId)
				.to(PizzaOrder.class, PizzaOrder_.orderId),
			Link.from(Topping.class, Topping_.orderId)
				.to(PizzaOrder.class, PizzaOrder_.orderId),
				
			Link.from(Vehicle.class, Vehicle_.vehicleTypeCode)
				.to(RefVehicleTypeEnum.class, RefVehicleTypeEnum.getVehicleTypeCodeField()),
			Link.from(Customer.class, Customer_.paymentMethodCode)
				.to(RefPaymentMethod.class, RefPaymentMethod_.paymentMethodCode),
			Link.from(PizzaOrder.class, PizzaOrder_.deliveryStatusCode)
				.to(RefDeliveryStatus.class, RefDeliveryStatus_.deliveryStatusCode),
			Link.from(PizzaOrdered.class, PizzaOrdered_.baseTypeCode)
				.to(RefBaseType.class, RefBaseType_.baseTypeCode),
			Link.from(Topping.class, Topping_.toppingCode)
				.to(RefTopping.class, RefTopping_.toppingCode)
		})
		.withEnumTables(new Class<?>[] {
			RefPaymentMethod.class,
			RefDeliveryStatus.class,
			RefBaseType.class,
			RefTopping.class
		})
		.build();
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
			return new DepsBuilder().withLinks(new Link[] { 
				Link.from(Customer.class, Customer.class.getDeclaredField("customerAddressId"))
					.to(Address.class, Address.class.getDeclaredField("addressId")),
				Link.from(Employee.class, Employee.class.getDeclaredField("employeeAddressId"))
					.to(Address.class, Address.class.getDeclaredField("addressId")),
					
				Link.from(PizzaOrder.class, PizzaOrder.class.getDeclaredField("customerId"))
					.to(Customer.class, Customer.class.getDeclaredField("customerId")),
				Link.from(PizzaOrder.class, PizzaOrder.class.getDeclaredField("takenByEmployeeId"))
					.to(Employee.class, Employee.class.getDeclaredField("employeeId")),
				Link.from(PizzaOrder.class, PizzaOrder.class.getDeclaredField("deliveredByEmployeeId"))
					.to(Employee.class, Employee.class.getDeclaredField("employeeId")),
				Link.from(PizzaOrder.class, PizzaOrder.class.getDeclaredField("vehicleId"))
					.to(Vehicle.class, Vehicle.class.getDeclaredField("vehicleId")),
				Link.from(PizzaOrdered.class, PizzaOrdered_.class.getDeclaredField("orderId"))
					.to(PizzaOrder.class, PizzaOrder_.class.getDeclaredField("orderId")),
				Link.from(Topping.class, Topping_.class.getDeclaredField("orderId"))
					.to(PizzaOrder.class, PizzaOrder_.class.getDeclaredField("orderId")),
					
				Link.from(Vehicle.class, Vehicle.class.getDeclaredField("vehicleTypeCode"))
					.to(RefVehicleTypeEnum.class, RefVehicleTypeEnum.getVehicleTypeCodeField()),
				Link.from(Customer.class, Customer.class.getDeclaredField("paymentMethodCode"))
					.to(RefPaymentMethod.class, RefPaymentMethod_.class.getDeclaredField("paymentMethodCode")),
				Link.from(PizzaOrder.class, PizzaOrder_.class.getDeclaredField("deliveryStatusCode"))
					.to(RefDeliveryStatus.class, RefDeliveryStatus_.class.getDeclaredField("deliveryStatusCode")),
				Link.from(PizzaOrdered.class, PizzaOrdered_.class.getDeclaredField("baseTypeCode"))
					.to(RefBaseType.class, RefBaseType_.class.getDeclaredField("baseTypeCode")),
				Link.from(Topping.class, Topping_.class.getDeclaredField("toppingCode"))
					.to(RefTopping.class, RefTopping_.class.getDeclaredField("toppingCode"))
			})
			.withEnumTables(new Class<?>[] {
				RefPaymentMethod.class,
				RefDeliveryStatus.class,
				RefBaseType.class, 
				RefTopping.class
			})
			.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void populateInitialData(EntityManager em) {
		em.merge(new RefBaseType("TH", 1.0, "Thin"));
		em.merge(new RefBaseType("DD", 1.2, "Deep Dish"));
		
		em.merge(new RefPaymentMethod("Cash","Cash"));
		em.merge(new RefPaymentMethod("CC","Credit Card"));
		em.merge(new RefPaymentMethod("Check","Check"));
		
		em.merge(new RefDeliveryStatus("Completed","Successfully Delivered to customer"));
		em.merge(new RefDeliveryStatus("Returned","Customer returned the order"));
		
		em.merge(new RefTopping("PA", 1.0, "Pine Apple"));
		em.merge(new RefTopping("Ham", 1.5, "Ham"));
		
		em.merge(new RefVehicleType("Bike", "Bicycle"));
		em.merge(new RefVehicleType("MBike", "Motorbike"));
		em.merge(new RefVehicleType("Van", "Van"));
	}
}
