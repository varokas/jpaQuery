package com.huskycode.jpaquery.types.db;

import com.huskycode.jpaquery.util.Function;

/**
 * Created by surachat on 8/17/14.
 */
public class ColumnDefinitionUtil {
    public static Function<ColumnDefinition, Column> createToColumnFunction(final Table table) {
        return new Function<ColumnDefinition, Column>() {
            @Override
            public Column apply(ColumnDefinition input) {
                return new ColumnImpl(table, input.getName(), input.getType());
            }
        };
    }
}
