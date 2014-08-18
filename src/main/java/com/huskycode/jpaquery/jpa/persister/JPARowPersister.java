package com.huskycode.jpaquery.jpa.persister;

import com.huskycode.jpaquery.persister.RowPersister;
import com.huskycode.jpaquery.types.db.Row;

import javax.persistence.EntityManager;

public class JPARowPersister implements RowPersister {
    private final EntityManager entityManager;

    public JPARowPersister(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Row row) {

    }
}
