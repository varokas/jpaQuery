package com.huskycode.jpaquery.types.db;

import java.util.function.Function;

/**
 * Created by surachat on 8/17/14.
 */
public class ColumnDefinitionUtil {
    public static Function<ColumnDefinition, Column> createToColumnFunction(final Table table) {
        return input -> new ColumnImpl(table, input.getName(), input.getType());
    }
}
