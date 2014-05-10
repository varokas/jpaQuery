package com.huskycode.jpaquery.types.db;

import java.util.Collections;
import java.util.List;

public class JPAEntityTable<E> implements Table {
    private final String name;
    private final List<Column> columns;
    private final Class<E> jpaEntity;

    public JPAEntityTable(String name, List<Column> columns, Class<E> jpaEntity) {
        this.name = name;
        this.columns = columns;
        this.jpaEntity = jpaEntity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public Class<E> getJpaEntity() {
        return jpaEntity;
    }
}
