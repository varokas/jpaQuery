package com.huskycode.jpaquery.populator;

import java.lang.reflect.Field;
import java.util.List;

public class ValuesPopulatorImpl implements ValuesPopulator {
	
	private static final ValuesPopulatorImpl INSTANCE = new ValuesPopulatorImpl();
	
	public static ValuesPopulatorImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public <E> void populateValue(E entity, List<AttributeValue<E, ?>> attributeValues) {
		for (AttributeValue<E, ?> attrValue : attributeValues) {
			Field field = attrValue.getAttribute().getField();
			try {
				field.setAccessible(true);
				field.set(entity, attrValue.getValue());
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
