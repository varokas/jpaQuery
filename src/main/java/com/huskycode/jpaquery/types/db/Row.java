package com.huskycode.jpaquery.types.db;

public class Row {
    private final Iterable<ColumnValue> columnValue;

    public Row(Iterable<ColumnValue> columnValue) {
        this.columnValue = columnValue;
    }

    public Iterable<ColumnValue> getColumnValue() {
        return columnValue;
    }
}
