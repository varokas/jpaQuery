package com.huskycode.jpaquery.testmodel.pizza;

import java.lang.reflect.Field;

import com.huskycode.jpaquery.persister.util.BeanUtil;

public enum RefVehicleTypeEnum {
	Bike("Bike", "Bicycle"),
	MBike("MBike", "Motorbike"),
	Van("Van", "Van");
	
	private final String vehicleTypeCode;
	private final String vehicleTypeDescription;
	
	private RefVehicleTypeEnum(String vehicleTypeCode,
			String vehicleTypeDescription) {
		this.vehicleTypeCode = vehicleTypeCode;
		this.vehicleTypeDescription = vehicleTypeDescription;
	}

	public String getVehicleTypeCode() {
		return vehicleTypeCode;
	}

	public String getVehicleTypeDescription() {
		return vehicleTypeDescription;
	}
	
	public static Field getVehicleTypeCodeField() {
		return BeanUtil.getFieldByName(RefVehicleTypeEnum.class, 
				"vehicleTypeCode");
	}
}
