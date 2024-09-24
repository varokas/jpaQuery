package com.huskycode.jpaquery.testmodel.pizza;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RefBaseType.class)
public class RefBaseType_ {
	public static volatile SingularAttribute<RefBaseType, String> baseTypeCode;
	public static volatile SingularAttribute<RefBaseType, Double> baseTypePrice;
	public static volatile SingularAttribute<RefBaseType, String> baseTypeDescription;
}
