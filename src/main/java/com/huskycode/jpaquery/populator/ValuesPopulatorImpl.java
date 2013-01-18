package com.huskycode.jpaquery.populator;

import java.lang.reflect.Field;
import java.util.Map;

public class ValuesPopulatorImpl implements ValuesPopulator {

    private static final ValuesPopulatorImpl INSTANCE = new ValuesPopulatorImpl();

    public static ValuesPopulatorImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public <E> void populateValue(final E entity, final Map<Field, Object> attributeValues) {
        for (Map.Entry<Field, Object> attrValue : attributeValues.entrySet()) {
            Field field = attrValue.getKey();

            try {
                field.setAccessible(true);
                if (attrValue.getValue() != null) {
                    if (field.getType().equals(Integer.class)) {
                        field.set(entity, Integer.valueOf(attrValue.getValue().toString()));
                    } else if (field.getType().equals(Long.class)) {
                        field.set(entity, Long.valueOf(attrValue.getValue().toString()));
                    } else {
                        field.set(entity, attrValue.getValue());
                    }
                } else {
                    field.set(entity, attrValue.getValue());
                }

            } catch (IllegalArgumentException e) {
                throw new CannotSetValueException(e);
            } catch (IllegalAccessException e) {
                throw new CannotSetValueException(e);
            }
        }
    }
}
