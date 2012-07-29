package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.Column;
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
@IdClass(ToppingKey.class)
public class Topping {
	@Id @Column private Long orderId;
	@Column private Integer pizzaSequenceNumber;
	@Column private Integer toppingSequenceNumber;
	
	private String toppingCode;
	
	private String toppingName;

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

	public Integer getToppingSequenceNumber() {
		return toppingSequenceNumber;
	}

	public void setToppingSequenceNumber(Integer toppingSequenceNumber) {
		this.toppingSequenceNumber = toppingSequenceNumber;
	}

	public String getToppingCode() {
		return toppingCode;
	}

	public void setToppingCode(String toppingCode) {
		this.toppingCode = toppingCode;
	}

	public String getToppingName() {
		return toppingName;
	}

	public void setToppingName(String toppingName) {
		this.toppingName = toppingName;
	}

	

}