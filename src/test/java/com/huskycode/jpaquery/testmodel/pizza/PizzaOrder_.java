package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(PizzaOrder.class)
public class PizzaOrder_ {
	public static volatile SingularAttribute<PizzaOrder, Long> orderId;
	public static volatile SingularAttribute<PizzaOrder, Long> customerId;
	public static volatile SingularAttribute<PizzaOrder, Long> takenByEmployeeId;
	public static volatile SingularAttribute<PizzaOrder, Long> deliveredByEmployeeId;
	public static volatile SingularAttribute<PizzaOrder, Long> vehicleId;
}
