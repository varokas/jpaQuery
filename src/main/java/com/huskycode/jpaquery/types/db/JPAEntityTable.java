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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPAEntityTable that = (JPAEntityTable) o;

        if (!jpaEntity.equals(that.jpaEntity)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return jpaEntity.hashCode();
    }

    @Override
    public String toString() {
        return "JPAEntityTable{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                ", jpaEntity=" + jpaEntity +
                '}';
    }
}
