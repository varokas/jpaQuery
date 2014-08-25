package com.huskycode.jpaquery.types.db;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RowBuilderTest {

    private Table table = new TableImpl("aTable",
                Arrays.asList(new ColumnDefinition("aColumn", String.class)));

    @Test
    public void testBuildRowWithNoColumnValues() throws Exception {
        Row row = new RowBuilder(table).build();

        assertThat(row.getTable(), is(table));
        assertThat(row.getColumnValue(), hasSize(0));
    }

    @Test
    public void testBuildRowWithColumnValue() throws Exception {
        RowBuilder builder = new RowBuilder(table);
        Row row = builder.withColumnValue(table.getColumns().get(0), "ABC")
                .build();

        assertThat(row.getTable(), is(table));
        assertThat(row.getColumnValue(), hasSize(1));
        assertThat(row.getColumnValue().get(0).getColumn(), is(table.column("aColumn")));
        assertThat(row.getColumnValue().get(0).getValue(), is((Object)"ABC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildRowThrowsIllegalArgumentExceptionWhenColumnNotExistsInTable() throws Exception {
        RowBuilder builder = new RowBuilder(table);
        Table anotherTable = new TableImpl("anotherTable",
                Arrays.asList(new ColumnDefinition("anotherColumn", String.class)));

        builder.withColumnValue(anotherTable.getColumns().get(0), "someValue")
                .build();
    }
}
