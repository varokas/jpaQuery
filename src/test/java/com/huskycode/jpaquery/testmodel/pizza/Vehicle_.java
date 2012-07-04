package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Vehicle.class)
public class Vehicle_ {
	public static volatile SingularAttribute<Employee, Long> vehicleId;
}
