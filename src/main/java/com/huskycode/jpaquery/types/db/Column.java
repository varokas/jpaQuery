package com.huskycode.jpaquery.types.db;

public class Column {
    private final String name;

    public Column(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        return !(name != null ? !name.equals(column.name) : column.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                '}';
    }
}
