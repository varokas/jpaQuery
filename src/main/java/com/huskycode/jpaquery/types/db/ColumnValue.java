package com.huskycode.jpaquery.types.db;

/**
 * Created by varokas.
 */
public class ColumnValue {
    private final Column column;
    private final Object value;

    public ColumnValue(Column column, Object value) {
        this.column = column;
        this.value = value;
    }

    public Column getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}
