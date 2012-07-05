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
public class Employee {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long employeeId;
	private long employeeAddressId;
	
	private String employeeName;
	private String employeePhone;
	private String otherEmployeeDetails;
	
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public long getEmployeeAddressId() {
		return employeeAddressId;
	}
	public void setEmployeeAddressId(long employeeAddressId) {
		this.employeeAddressId = employeeAddressId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeePhone() {
		return employeePhone;
	}
	public void setEmployeePhone(String employeePhone) {
		this.employeePhone = employeePhone;
	}
	public String getOtherEmployeeDetails() {
		return otherEmployeeDetails;
	}
	public void setOtherEmployeeDetails(String otherEmployeeDetails) {
		this.otherEmployeeDetails = otherEmployeeDetails;
	}
	
	
}
