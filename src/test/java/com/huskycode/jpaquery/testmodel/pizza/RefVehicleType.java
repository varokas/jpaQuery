package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RefVehicleType {
	@Id
	private String vehicleTypeCode;
	
	private String vehicleTypeDescription;

	public String getVehicleTypeCode() {
		return vehicleTypeCode;
	}

	public void setVehicleTypeCode(String vehicleTypeCode) {
		this.vehicleTypeCode = vehicleTypeCode;
	}

	public String getVehicleTypeDescription() {
		return vehicleTypeDescription;
	}

	public void setVehicleTypeDescription(String vehicleTypeDescription) {
		this.vehicleTypeDescription = vehicleTypeDescription;
	}
}
