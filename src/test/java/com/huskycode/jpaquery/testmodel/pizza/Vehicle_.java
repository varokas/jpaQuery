package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Vehicle.class)
public class Vehicle_ {
	public static volatile SingularAttribute<Vehicle, Long> vehicleId;
	
	public static volatile SingularAttribute<Vehicle, String> vehicleLicenseNumber;
	public static volatile SingularAttribute<Vehicle, String> vehicleDetails;
	public static volatile SingularAttribute<Vehicle, String> vehicleTypeCode;
}
