package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefVehicleType.class)
public class RefVehicleType_ {
	public static volatile SingularAttribute<RefVehicleType, String> vehicleTypeCode;
	public static volatile SingularAttribute<RefVehicleType, String> vehicleTypeDescription;
}
