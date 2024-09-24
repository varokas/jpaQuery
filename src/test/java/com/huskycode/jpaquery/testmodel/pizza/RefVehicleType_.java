package com.huskycode.jpaquery.testmodel.pizza;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefVehicleType.class)
public class RefVehicleType_ {
	public static volatile SingularAttribute<RefVehicleType, String> vehicleTypeCode;
	public static volatile SingularAttribute<RefVehicleType, String> vehicleTypeDescription;
}
