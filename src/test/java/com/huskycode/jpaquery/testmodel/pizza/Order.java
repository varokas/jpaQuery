package com.huskycode.jpaquery.testmodel.pizza;

import java.util.Date;

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
public class Order {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long orderId;
	
	private long customerId;
	private long takenByEmployeeId;
	private long deliveredByEmployeeId;
	//private long deliveryStatusCode;
	private long vehicleId;
	
	private Date dateTimeOrderTaken;
	private Date dateTimeOrderDelivered;
	private double totalOrderPrice;
	private String otherOrderDetails;
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getTakenByEmployeeId() {
		return takenByEmployeeId;
	}
	public void setTakenByEmployeeId(long takenByEmployeeId) {
		this.takenByEmployeeId = takenByEmployeeId;
	}
	public long getDeliveredByEmployeeId() {
		return deliveredByEmployeeId;
	}
	public void setDeliveredByEmployeeId(long deliveredByEmployeeId) {
		this.deliveredByEmployeeId = deliveredByEmployeeId;
	}
	public long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Date getDateTimeOrderTaken() {
		return dateTimeOrderTaken;
	}
	public void setDateTimeOrderTaken(Date dateTimeOrderTaken) {
		this.dateTimeOrderTaken = dateTimeOrderTaken;
	}
	public Date getDateTimeOrderDelivered() {
		return dateTimeOrderDelivered;
	}
	public void setDateTimeOrderDelivered(Date dateTimeOrderDelivered) {
		this.dateTimeOrderDelivered = dateTimeOrderDelivered;
	}
	public double getTotalOrderPrice() {
		return totalOrderPrice;
	}
	public void setTotalOrderPrice(double totalOrderPrice) {
		this.totalOrderPrice = totalOrderPrice;
	}
	public String getOtherOrderDetails() {
		return otherOrderDetails;
	}
	public void setOtherOrderDetails(String otherOrderDetails) {
		this.otherOrderDetails = otherOrderDetails;
	}
}
