package com.huskycode.jpaquery.types.db;


import java.util.function.Function;

public interface Column {
    String getName();

    Class<?> getType();

    Table getTable();

    static Function<Column, String> NAME_MAPPER = Column::getName;
}
