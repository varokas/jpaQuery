package com.huskycode.jpaquery.types.db;

public class ColumnImpl implements Column {
    private final Table table;
    private final String name;
    private final Class<?> type;

    public ColumnImpl(Table table, String name, Class<?> type) {
        this.table = table;
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnImpl column = (ColumnImpl) o;

        if (!name.equals(column.name)) return false;
        if (!table.equals(column.table)) return false;
        if (!type.equals(column.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = table.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ColumnImpl{" +
                "table=" + table.getName() +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
