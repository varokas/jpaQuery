package com.huskycode.jpaquery.types.db.factory;

import com.huskycode.jpaquery.jpa.util.JPAUtil;
import com.huskycode.jpaquery.types.db.ColumnDefinition;
import com.huskycode.jpaquery.types.db.Table;
import com.huskycode.jpaquery.types.db.TableImpl;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableFactory {
    public Table createFromJPAEntity(Class<?> jpaEntity) {
        if(jpaEntity.getAnnotation(Entity.class) == null)
            throw new NotJPAEntityException(jpaEntity);

        String tableName = JPAUtil.getTableName(jpaEntity);

        return new TableImpl(tableName, getColumnDefinitions(jpaEntity));
    }

    private List<ColumnDefinition> getColumnDefinitions(Class<?> jpaEntity) {
        List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
        for(Field field : jpaEntity.getDeclaredFields()) {
            if(!isTransient(field)) {
                String columnName = JPAUtil.getColumnNameOrDefault(field);

                columns.add(new ColumnDefinition(columnName, field.getType()));
            }
        }
        return columns;
    }

    private boolean isTransient(Field field) {
        return field.getAnnotation(Transient.class) != null;
    }

    public static class NotJPAEntityException extends RuntimeException {
        public NotJPAEntityException(Class<?>  entity) {
            super("Class is not a JPA entity: " + entity.getCanonicalName());
        }
    }

}
