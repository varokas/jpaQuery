package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefPaymentMethod.class)
public class RefPaymentMethod_ {
	public static volatile SingularAttribute<RefPaymentMethod, String> paymentMethodCode;
	public static volatile SingularAttribute<RefPaymentMethod, String> paymentMethodDescription;
}
