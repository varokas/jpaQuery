package com.huskycode.jpaquery.jpa.util;

import java.lang.reflect.Field;

/**
 * Created by varokas.
 */
public class JPAUtil {
    public static String getColumnNameOrDefault(Field field) {
        jakarta.persistence.Column column = field.getAnnotation(jakarta.persistence.Column.class);

        if(column == null) {
            return field.getName();
        }

        String columnName = column.name();
        if(columnName.equals("")) {
            columnName = field.getName();
        }
        return columnName;
    }

    public static  String getTableName(Class<?> jpaEntity) {
        String tableName = jpaEntity.getSimpleName();

        jakarta.persistence.Table tableAnnotation = jpaEntity.getAnnotation(jakarta.persistence.Table.class);
        if(tableAnnotation != null) {
            tableName = tableAnnotation.name();
        }
        return tableName;
    }
}
