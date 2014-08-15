package com.huskycode.jpaquery.types.db.factory;

import com.huskycode.jpaquery.types.db.Column;
import com.huskycode.jpaquery.types.db.JPAEntityTable;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableFactory {
    public <E> JPAEntityTable<E> createFromJPAEntity(Class<E> jpaEntity) {
        if(jpaEntity.getAnnotation(Entity.class) == null)
            throw new NotJPAEntityException(jpaEntity);

        String tableName = jpaEntity.getSimpleName();

        javax.persistence.Table tableAnnotation = jpaEntity.getAnnotation(javax.persistence.Table.class);
        if(tableAnnotation != null) {
            tableName = tableAnnotation.name();
        }

        return new JPAEntityTable(tableName, getColumns(jpaEntity), jpaEntity);
    }

    private List<Column> getColumns(Class<?> jpaEntity) {
        List<Column> columns = new ArrayList<Column>();
        for(Field field : jpaEntity.getDeclaredFields()) {
            javax.persistence.Column column = field.getAnnotation(javax.persistence.Column.class);
            if(column != null) {
                String columnName = getColumnNameOrDefault(column, field);

                columns.add(new Column(columnName, field.getType()));
            }
        }
        return columns;
    }

    private String getColumnNameOrDefault(javax.persistence.Column column, Field field) {
        String columnName = column.name();
        if(columnName.equals("")) {
            columnName = field.getName();
        }
        return columnName;
    }

    public static class NotJPAEntityException extends RuntimeException {
        private Class<?> entity;

        public NotJPAEntityException(Class<?>  entity) {
            super("Class is not a JPA entity: " + entity.getCanonicalName());
        }
    }

}
