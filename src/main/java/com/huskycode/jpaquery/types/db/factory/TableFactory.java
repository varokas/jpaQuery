package com.huskycode.jpaquery.types.db.factory;

import com.huskycode.jpaquery.jpa.util.JPAUtil;
import com.huskycode.jpaquery.types.db.Column;
import com.huskycode.jpaquery.types.db.ColumnDefinition;
import com.huskycode.jpaquery.types.db.Table;
import com.huskycode.jpaquery.types.db.TableImpl;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableFactory {
    public Table createFromJPAEntity(Class<?> jpaEntity) {
        return createWithMappingFromJPAEntity(jpaEntity).getTable();
    }

    public TableWithMapping createWithMappingFromJPAEntity(Class<?> jpaEntity) {
        if(jpaEntity.getAnnotation(Entity.class) == null)
            throw new NotJPAEntityException(jpaEntity);

        String tableName = JPAUtil.getTableName(jpaEntity);

        List<ColumnDefinitionWithField> defs = getColumnDefinitions(jpaEntity);

        final TableImpl table = new TableImpl(tableName, getColumnDefinitions(defs));
        Map<Column, Field> map = createColumnToFieldMapping(table, defs);


        return new TableWithMapping(table, map);
    }

    private Map<Column, Field> createColumnToFieldMapping(final Table table, final List<ColumnDefinitionWithField> defs) {
        final HashMap<Column, Field> map = new HashMap<Column, Field>();

        for(ColumnDefinitionWithField def : defs) {
            map.put(table.column(def.columnDefinition.getName()), def.field);
        }

        return map;
    }

    private List<ColumnDefinitionWithField> getColumnDefinitions(Class<?> jpaEntity) {
        List<ColumnDefinitionWithField> columns = new ArrayList<ColumnDefinitionWithField>();
        for(Field field : jpaEntity.getDeclaredFields()) {
            if(isInnerClassMarkerField(field) || isTransient(field)) {
                continue;
            }

            String columnName = JPAUtil.getColumnNameOrDefault(field);

            columns.add(
                new ColumnDefinitionWithField(
                    new ColumnDefinition(columnName, field.getType()),
                    field
            ));
        }
        return columns;
    }

    private boolean isInnerClassMarkerField(final Field field) {
        return field.getName().startsWith("this$");
    }

    private List<ColumnDefinition> getColumnDefinitions(List<ColumnDefinitionWithField> columnDefinitionWithFields) {
        List<ColumnDefinition> definitions = new ArrayList<ColumnDefinition>(columnDefinitionWithFields.size());
        for(ColumnDefinitionWithField defWithField : columnDefinitionWithFields) {
            definitions.add(defWithField.columnDefinition);
        }
        return definitions;
    }

    private boolean isTransient(Field field) {
        return field.getAnnotation(Transient.class) != null;
    }

    private class ColumnDefinitionWithField {
        ColumnDefinition columnDefinition;
        Field field;
        private ColumnDefinitionWithField(final ColumnDefinition columnDefinition, final Field field) {
            this.columnDefinition = columnDefinition;
            this.field = field;
        }
    }

    public class TableWithMapping {
        private Table table;
        private Map<Column, Field> columnFieldMap;
        public TableWithMapping(final Table table, final Map<Column, Field> columnFieldMap) {
            this.table = table;
            this.columnFieldMap = columnFieldMap;
        }

        public Table getTable() {
            return table;
        }

        public Map<Column, Field> getColumnFieldMap() {
            return columnFieldMap;
        }

    }

    public static class NotJPAEntityException extends RuntimeException {
        public NotJPAEntityException(Class<?>  entity) {
            super("Class is not a JPA entity: " + entity.getCanonicalName());
        }
    }
}
