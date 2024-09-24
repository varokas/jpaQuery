package com.huskycode.jpaquery.testmodel.pizza;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefDeliveryStatus.class)
public class RefDeliveryStatus_ {
	public static volatile SingularAttribute<RefDeliveryStatus, String> deliveryStatusCode;
	public static volatile SingularAttribute<RefDeliveryStatus, String> deliveryStatusDescription;
}
