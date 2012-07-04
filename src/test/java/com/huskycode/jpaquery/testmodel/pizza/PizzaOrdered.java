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
	
	//private long baseTypeCode;
	private double totalPizzaPrice;
}
