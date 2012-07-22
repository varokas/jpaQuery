package com.huskycode.jpaquery.testmodel.pizza;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefDeliveryStatus.class)
public class RefDeliveryStatus_ {
	public static volatile SingularAttribute<RefDeliveryStatus, String> deliveryStatusCode;
	public static volatile SingularAttribute<RefDeliveryStatus, String> deliveryStatusDescription;
}
