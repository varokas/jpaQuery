package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Model taken from the design here
 * 
 * http://www.databaseanswers.org/data_models/pizza_deliveries/index.htm
 * 
 * @author Varokas Panusuwan
 *
 */
@Entity
@IdClass(PizzaOrderedKey.class)
public class PizzaOrdered {
	@Id
	private Long orderId;
	private Integer pizzaSequenceNumber;
	
	private String baseTypeCode;
	private double totalPizzaPrice;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getPizzaSequenceNumber() {
		return pizzaSequenceNumber;
	}

	public void setPizzaSequenceNumber(Integer pizzaSequenceNumber) {
		this.pizzaSequenceNumber = pizzaSequenceNumber;
	}

	public double getTotalPizzaPrice() {
		return totalPizzaPrice;
	}

	public void setTotalPizzaPrice(double totalPizzaPrice) {
		this.totalPizzaPrice = totalPizzaPrice;
	}

	public String getBaseTypeCode() {
		return baseTypeCode;
	}

	public void setBaseTypeCode(String baseTypeCode) {
		this.baseTypeCode = baseTypeCode;
	}
	
	
}
