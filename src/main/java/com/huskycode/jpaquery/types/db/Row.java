package com.huskycode.jpaquery.types.db;

import java.util.List;

public class Row {
    private final Table table;
    private final List<ColumnValue> columnValue;

    Row(Table table, List<ColumnValue> columnValue) {
        this.table = table;
        this.columnValue = columnValue;
    }

    public List<ColumnValue> getColumnValue() {
        return columnValue;
    }

    public Table getTable() {
        return table;
    }
}
