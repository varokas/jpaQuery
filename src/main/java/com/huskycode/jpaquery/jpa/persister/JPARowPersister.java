package com.huskycode.jpaquery.jpa.persister;

import com.huskycode.jpaquery.persister.RowPersister;
import com.huskycode.jpaquery.types.db.Column;
import com.huskycode.jpaquery.types.db.ColumnValue;
import com.huskycode.jpaquery.types.db.Row;
import com.huskycode.jpaquery.types.db.Table;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.Map;

public class JPARowPersister implements RowPersister {
    private final EntityManager entityManager;
    private final Map<Table, Class<?>> tableToEntityClassMap;
    private final Map<Column, Field> collectedColumnAndField;

    public JPARowPersister(EntityManager entityManager, Map<Table, Class<?>> tableToEntityClassMap, final Map<Column, Field> collectedColumnAndField) {
        this.entityManager = entityManager;
        this.tableToEntityClassMap = tableToEntityClassMap;
        this.collectedColumnAndField = collectedColumnAndField;
    }

    @Override
    public void save(Row row) {
        Class entityClass = tableToEntityClassMap.get(row.getTable());
        Object entityObject = createObjectOrThrowException(entityClass);

        for(ColumnValue columnValue : row.getColumnValue()) {
            Field field = collectedColumnAndField.get(columnValue.getColumn());
            field.setAccessible(true);

            try {
                field.set(entityObject, columnValue.getValue());
            } catch (IllegalAccessException e) {
                throw new CannotSetValueException(
                        "Cannot set value: " + columnValue.getValue() +
                        ",for field: " + field.getName(),
                        e);
            }
        }

        entityManager.persist(entityObject);
    }

    private Object createObjectOrThrowException(Class entityClass) {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new CannotInstantiateClassException(e);
        }
    }

    private class CannotInstantiateClassException extends RuntimeException {
        public CannotInstantiateClassException(final Exception e) {
            super(e);
        }
    }

    private class CannotSetValueException extends RuntimeException {
        public CannotSetValueException(final String message, final Exception e) {
            super(message, e);
        }
    }
}
