package com.huskycode.jpaquery.testmodel.pizza;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Model taken from the design here
 * 
 * http://www.databaseanswers.org/data_models/pizza_deliveries/index.htm
 * 
 * @author Varokas Panusuwan
 *
 */
@Entity
public class Vehicle {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long vehicleId;
	private String vehicleTypeCode;
	private String vehicleLicenseNumber;
	private String vehicleDetails;
	
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehicleLicenseNumber() {
		return vehicleLicenseNumber;
	}
	public void setVehicleLicenseNumber(String vehicleLicenseNumber) {
		this.vehicleLicenseNumber = vehicleLicenseNumber;
	}
	public String getVehicleDetails() {
		return vehicleDetails;
	}
	public void setVehicleDetails(String vehicleDetails) {
		this.vehicleDetails = vehicleDetails;
	}
	public String getVehicleTypeCode() {
		return vehicleTypeCode;
	}
	public void setVehicleTypeCode(String vehicleTypeCode) {
		this.vehicleTypeCode = vehicleTypeCode;
	}
}
