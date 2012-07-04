package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	private long vehicleId;
	//private long vehicleTypeCode;
	private String vehicleLicenseNumber;
	private String vehicleDetails;
}
