package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RefTopping {
	@Id
	private String toppingCode;
	
	private Double toppingPrice;
	private Double toppingDescription;
	
	
	public String getToppingCode() {
		return toppingCode;
	}
	
	public void setToppingCode(String toppingCode) {
		this.toppingCode = toppingCode;
	}
	public Double getToppingPrice() {
		return toppingPrice;
	}
	public void setToppingPrice(Double toppingPrice) {
		this.toppingPrice = toppingPrice;
	}
	public Double getToppingDescription() {
		return toppingDescription;
	}
	public void setToppingDescription(Double toppingDescription) {
		this.toppingDescription = toppingDescription;
	}
	
	
}
