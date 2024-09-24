package com.huskycode.jpaquery.testmodel.pizza;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefPaymentMethod.class)
public class RefPaymentMethod_ {
	public static volatile SingularAttribute<RefPaymentMethod, String> paymentMethodCode;
	public static volatile SingularAttribute<RefPaymentMethod, String> paymentMethodDescription;
}
