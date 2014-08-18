package com.huskycode.jpaquery.types.db;

import com.huskycode.jpaquery.util.ListUtil;
import com.huskycode.jpaquery.util.Maps;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TableImpl implements Table {
    private final String name;
    private final Map<String, Column> columnNameMap;

    public TableImpl(String name, List<ColumnDefinition> columnDefinitions) {
        this.name = name;
        this.columnNameMap = Maps.from(toColumns(columnDefinitions), Column.NAME_MAPPER);
    }

    public TableImpl(String name, ColumnDefinition ... columnDefinitions) {
        this(name, Arrays.asList(columnDefinitions));
    }

    private List<Column> toColumns(List<ColumnDefinition> columns) {
        return ListUtil.map(columns, ColumnDefinitionUtil.createToColumnFunction(this));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Column> getColumns() {
        return Collections.unmodifiableList(ListUtil.from(this.columnNameMap.values()));
    }

    @Override
    public Column column(String colName) {
        return columnNameMap.get(colName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableImpl table = (TableImpl) o;

        if (columnNameMap != null ? !columnNameMap.equals(table.columnNameMap) : table.columnNameMap != null)
            return false;
        if (name != null ? !name.equals(table.name) : table.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (columnNameMap != null ? columnNameMap.hashCode() : 0);
        return result;
    }
}
