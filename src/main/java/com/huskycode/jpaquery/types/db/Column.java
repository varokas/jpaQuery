package com.huskycode.jpaquery.types.db;

import com.huskycode.jpaquery.util.Function;

public interface Column {
    String getName();

    Class<?> getType();

    Table getTable();

    static Function<Column, String> NAME_MAPPER = new Function<Column, String>() {
        @Override
        public String apply(Column input) {
            return input.getName();
        }
    };
}
