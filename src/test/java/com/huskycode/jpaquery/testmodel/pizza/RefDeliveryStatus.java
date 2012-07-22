package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RefDeliveryStatus {
	@Id
	private String deliveryStatusCode;
	
	private String deliveryStatusDescription;
	
	
	

	public RefDeliveryStatus() {
	}

	public RefDeliveryStatus(String deliveryStatusCode,
			String deliveryStatusDescription) {
		this.deliveryStatusCode = deliveryStatusCode;
		this.deliveryStatusDescription = deliveryStatusDescription;
	}

	public String getDeliveryStatusCode() {
		return deliveryStatusCode;
	}

	public void setDeliveryStatusCode(String deliveryStatusCode) {
		this.deliveryStatusCode = deliveryStatusCode;
	}

	public String getDeliveryStatusDescription() {
		return deliveryStatusDescription;
	}

	public void setDeliveryStatusDescription(String deliveryStatusDescription) {
		this.deliveryStatusDescription = deliveryStatusDescription;
	}
}
