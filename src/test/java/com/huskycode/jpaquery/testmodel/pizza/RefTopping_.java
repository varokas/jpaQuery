package com.huskycode.jpaquery.testmodel.pizza;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefTopping.class)
public class RefTopping_ {
	public static volatile SingularAttribute<RefTopping, String> toppingCode;
	public static volatile SingularAttribute<RefTopping, Double> toppingPrice;
	public static volatile SingularAttribute<RefTopping, String> toppingDescription;
	
}
