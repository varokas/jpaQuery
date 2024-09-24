package com.huskycode.jpaquery.testmodel.pizza;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(PizzaOrdered.class)
public class PizzaOrdered_ {
	public static volatile SingularAttribute<PizzaOrdered, Long> orderId;
	
	public static volatile SingularAttribute<PizzaOrdered, Integer> pizzaSequenceNumber;
	public static volatile SingularAttribute<PizzaOrdered, String> baseTypeCode;
	public static volatile SingularAttribute<PizzaOrdered, String> totalPizzaPrice;
}
