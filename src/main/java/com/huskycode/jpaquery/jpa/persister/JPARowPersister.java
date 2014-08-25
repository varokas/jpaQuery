package com.huskycode.jpaquery.jpa.persister;

import com.huskycode.jpaquery.persister.RowPersister;
import com.huskycode.jpaquery.types.db.Row;
import com.huskycode.jpaquery.types.db.Table;

import javax.persistence.EntityManager;
import java.util.Map;

public class JPARowPersister implements RowPersister {
    private final EntityManager entityManager;
    private final Map<Table, Class<?>> tableToEntityClassMap;

    public JPARowPersister(EntityManager entityManager, Map<Table, Class<?>> tableToEntityClassMap) {
        this.entityManager = entityManager;
        this.tableToEntityClassMap = tableToEntityClassMap;
    }

    @Override
    public void save(Row row) {
        Class entityClass = tableToEntityClassMap.get(row.getTable());
        Object entityObject = createObjectOrThrowException(entityClass);

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
}
