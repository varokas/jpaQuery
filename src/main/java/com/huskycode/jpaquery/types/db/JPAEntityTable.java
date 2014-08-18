package com.huskycode.jpaquery.types.db;

import com.huskycode.jpaquery.util.Function;
import com.huskycode.jpaquery.util.ListUtil;
import com.huskycode.jpaquery.util.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JPAEntityTable<E> implements Table {
    private final String name;
    private final List<Column> columns;
    private final Class<E> jpaEntity;
    private final Map<String, Column> columNameMap;

    public JPAEntityTable(String name, List<ColumnDefinition> columnDefinitions, Class<E> jpaEntity) {
        this.name = name;
        this.columns = toColumns(columnDefinitions);
        this.jpaEntity = jpaEntity;
        this.columNameMap = Maps.from(this.columns, COLUMN_KEY_FUNCTION);
    }

    private List<Column> toColumns(List<ColumnDefinition> columns) {
        return ListUtil.map(columns, ColumnDefinitionUtil.createToColumnFunction(this));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    @Override
    public Column column(String colName) {
        return columNameMap.get(colName);
    }

    public Class<E> getJpaEntity() {
        return jpaEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPAEntityTable that = (JPAEntityTable) o;

        return jpaEntity.equals(that.jpaEntity);

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

    private Function<Column, String> COLUMN_KEY_FUNCTION = new Function<Column, String>() {
        @Override
        public String apply(Column input) {
            return input.getName();
        }
    };
}
