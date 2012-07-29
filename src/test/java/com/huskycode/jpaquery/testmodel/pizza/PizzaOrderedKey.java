package com.huskycode.jpaquery.testmodel.pizza;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PizzaOrderedKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column
	private Long orderId;
	
	@Column
	private Integer pizzaSequenceNumber;
}
