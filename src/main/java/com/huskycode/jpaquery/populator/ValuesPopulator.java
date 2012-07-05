package com.huskycode.jpaquery.populator;

import java.util.List;

public interface ValuesPopulator {
	 <E> void populateValue(E entity, List<AttributeValue<E, ?>> attributeValues);
}
