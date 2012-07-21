package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefTopping.class)
public class RefTopping_ {
	public static volatile SingularAttribute<RefTopping, String> toppingCode;
	public static volatile SingularAttribute<RefTopping, Double> toppingPrice;
	public static volatile SingularAttribute<RefTopping, String> toppingDescription;
	
}
