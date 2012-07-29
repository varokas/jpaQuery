package com.huskycode.jpaquery.testmodel.pizza;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(PizzaOrder.class)
public class PizzaOrder_ {
	public static volatile SingularAttribute<PizzaOrder, Long> orderId;
	public static volatile SingularAttribute<PizzaOrder, Long> customerId;
	public static volatile SingularAttribute<PizzaOrder, Long> takenByEmployeeId;
	public static volatile SingularAttribute<PizzaOrder, Long> deliveredByEmployeeId;
	public static volatile SingularAttribute<PizzaOrder, Long> vehicleId;
	
	public static volatile SingularAttribute<PizzaOrder, String> deliveryStatusCode;
	
	public static volatile SingularAttribute<PizzaOrder, Date> dateTimeOrderTaken;
	public static volatile SingularAttribute<PizzaOrder, Date> dateTimeOrderDelivered;
	public static volatile SingularAttribute<PizzaOrder, Double> totalOrderPrice;
	public static volatile SingularAttribute<PizzaOrder, Double> otherOrderDetails;
}
