package com.huskycode.jpaquery.types.db;

/**
 * Created by surachat on 8/17/14.
 */
public class ColumnDefinition {
    private final String name;
    private final Class<?> type;

    public ColumnDefinition(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }
}
