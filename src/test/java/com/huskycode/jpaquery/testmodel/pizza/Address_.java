package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Address.class)
public class Address_ {
	public static volatile SingularAttribute<Address, Long> addressId;
	
	public static volatile SingularAttribute<Address, String> line1;
	public static volatile SingularAttribute<Address, String> line2;
	public static volatile SingularAttribute<Address, String> line3;
	public static volatile SingularAttribute<Address, String> line4;
	public static volatile SingularAttribute<Address, String> city;
	public static volatile SingularAttribute<Address, String> zipCode;
	public static volatile SingularAttribute<Address, String> stateProviceCounty;
	public static volatile SingularAttribute<Address, String> countryCode;
	public static volatile SingularAttribute<Address, String> otherAddressDetails;
}