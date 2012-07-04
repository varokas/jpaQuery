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
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long customerId;
	private long customerAddressId;
	//private long paymentMethodCode
	private String customerName;
	private String customerPhone;
	private Date dateOfFirstOrder;
	private String otherCustomerDetails;
}
