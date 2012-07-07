package com.huskycode.jpaquery.populator;

import java.lang.reflect.Field;
import java.util.Map;

public class ValuesPopulatorImpl implements ValuesPopulator {
	
	private static final ValuesPopulatorImpl INSTANCE = new ValuesPopulatorImpl();
	
	public static ValuesPopulatorImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public <E> void populateValue(E entity, Map<Field, Object> attributeValues) {
		for (Map.Entry<Field, Object> attrValue : attributeValues.entrySet()) {
			Field field = attrValue.getKey();
			
			try {
				field.setAccessible(true);
				field.set(entity, attrValue.getValue());
			} catch (IllegalArgumentException e) {
				throw new CannotSetValueException(e);
			} catch (IllegalAccessException e) {
				throw new CannotSetValueException(e);
			}
		}
	}

}
