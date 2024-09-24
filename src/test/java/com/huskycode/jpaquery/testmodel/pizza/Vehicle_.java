package com.huskycode.jpaquery.testmodel.pizza;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Vehicle.class)
public class Vehicle_ {
	public static volatile SingularAttribute<Vehicle, Long> vehicleId;
	
	public static volatile SingularAttribute<Vehicle, String> vehicleLicenseNumber;
	public static volatile SingularAttribute<Vehicle, String> vehicleDetails;
	public static volatile SingularAttribute<Vehicle, String> vehicleTypeCode;
}
