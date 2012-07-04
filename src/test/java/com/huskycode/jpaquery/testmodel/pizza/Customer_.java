package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Customer.class)
public class Customer_ {
	public static volatile SingularAttribute<Customer, Long> customerId;
	public static volatile SingularAttribute<Customer, Long> customerAddressId;
}
