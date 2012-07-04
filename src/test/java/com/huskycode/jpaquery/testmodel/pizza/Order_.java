package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Order.class)
public class Order_ {
	public static volatile SingularAttribute<Order, Long> orderId;
	public static volatile SingularAttribute<Order, Long> customerId;
	public static volatile SingularAttribute<Order, Long> takenByEmployeeId;
	public static volatile SingularAttribute<Order, Long> deliveredByEmployeeId;
	public static volatile SingularAttribute<Order, Long> vehicleId;
}
