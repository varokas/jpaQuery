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
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public String getLine3() {
		return line3;
	}
	public void setLine3(String line3) {
		this.line3 = line3;
	}
	public String getLine4() {
		return line4;
	}
	public void setLine4(String line4) {
		this.line4 = line4;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getStateProviceCounty() {
		return stateProviceCounty;
	}
	public void setStateProviceCounty(String stateProviceCounty) {
		this.stateProviceCounty = stateProviceCounty;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getOtherAddressDetails() {
		return otherAddressDetails;
	}
	public void setOtherAddressDetails(String otherAddressDetails) {
		this.otherAddressDetails = otherAddressDetails;
	}
}
