package com.huskycode.jpaquery.types.db;

import java.util.ArrayList;
import java.util.List;

public class RowBuilder {
    private final List<ColumnValue> collectedColumns = new ArrayList<ColumnValue>();
    private final Table collectedTable;

    public RowBuilder withColumnValue(Column column, Object value) {
        ColumnValue columnValue = new ColumnValue(column, value);

        //checkCorrectTable(columnValue.getColumn().getTable());
        checkColumnExistsInTable(columnValue.getColumn());

        collectedColumns.add(columnValue);

        return this;
    }

    private void checkColumnExistsInTable(final Column column) {
        if(!collectedTable.getColumns().contains(column)) {
            throw new IllegalArgumentException("Column: " + column.getName() + " is not part of a table: " + collectedTable.getName());
        }
    }

    private void checkCorrectTable(final Table tableFromColumn) {
        if(!tableFromColumn.equals(collectedTable)) {
            throw new IllegalArgumentException("Invalid table: " + tableFromColumn.getName() + ", expected: " + collectedTable.getName());
        }
    }

    public RowBuilder(Table table) {
        collectedTable = table;
    }


    public Row build() {
        return new Row(collectedTable, collectedColumns);
    }
}
