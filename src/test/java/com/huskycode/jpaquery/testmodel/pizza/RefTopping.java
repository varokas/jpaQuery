package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RefTopping {
	@Id
	private String toppingCode;
	
	private Double toppingPrice;
	private String toppingDescription;
	
	
	
	public RefTopping() {
	}

	public RefTopping(String toppingCode, Double toppingPrice,
			String toppingDescription) {
		this.toppingCode = toppingCode;
		this.toppingPrice = toppingPrice;
		this.toppingDescription = toppingDescription;
	}

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
	public String getToppingDescription() {
		return toppingDescription;
	}
	public void setToppingDescription(String toppingDescription) {
		this.toppingDescription = toppingDescription;
	}
	
	
}
