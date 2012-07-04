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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long addressId;
    
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	private String city;
	private String zipCode;
	private String stateProviceCounty;
	private String countryCode;
	private String otherAddressDetails;
	
	
}
