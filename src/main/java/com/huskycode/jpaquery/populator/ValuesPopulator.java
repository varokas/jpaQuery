package com.huskycode.jpaquery.populator;

import java.lang.reflect.Field;
import java.util.Map;

public interface ValuesPopulator {
	 <E> void populateValue(E entity, Map<Field, Object> attributeValues);
}
