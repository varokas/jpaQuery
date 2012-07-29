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
	private long orderId;
	private int pizzaSequenceNumber;
	
	private long baseTypeCode;
	private double totalPizzaPrice;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getPizzaSequenceNumber() {
		return pizzaSequenceNumber;
	}

	public void setPizzaSequenceNumber(int pizzaSequenceNumber) {
		this.pizzaSequenceNumber = pizzaSequenceNumber;
	}

	public double getTotalPizzaPrice() {
		return totalPizzaPrice;
	}

	public void setTotalPizzaPrice(double totalPizzaPrice) {
		this.totalPizzaPrice = totalPizzaPrice;
	}

	public long getBaseTypeCode() {
		return baseTypeCode;
	}

	public void setBaseTypeCode(long baseTypeCode) {
		this.baseTypeCode = baseTypeCode;
	}
	
	
}
