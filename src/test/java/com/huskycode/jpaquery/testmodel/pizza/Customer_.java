package com.huskycode.jpaquery.testmodel.pizza;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Customer.class)
public class Customer_ {
	public static volatile SingularAttribute<Customer, Long> customerId;
	public static volatile SingularAttribute<Customer, Long> customerAddressId;
	
	public static volatile SingularAttribute<Customer, String> paymentMethodCode;
	
	public static volatile SingularAttribute<Customer, String> customerName;	
	public static volatile SingularAttribute<Customer, String> customerPhone;
	public static volatile SingularAttribute<Customer, Date> dateOfFirstOrder;
	public static volatile SingularAttribute<Customer, String> otherCustomerDetails;
}
