package com.huskycode.jpaquery.types.db;

import junit.framework.TestCase;
import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class TableImplTest {

    private TableImpl table;
    private String name = RandomStringUtils.randomAlphanumeric(5);

    private ColumnDefinition colDef1 = new ColumnDefinition(RandomStringUtils.randomAlphanumeric(5), String.class);
    private ColumnDefinition colDef2 = new ColumnDefinition(RandomStringUtils.randomAlphanumeric(5), Integer.class);

    @Before
    public void before() {
        table = new TableImpl(name, colDef1, colDef2);
    }

    @Test
    public void testGetName() {
        Assert.assertThat(table.getName(), Matchers.equalTo(name));
    }

    @Test
    public void testGetColumns() {
        List<Column> columns = table.getColumns();

        Assert.assertThat(columns.size(), Matchers.equalTo(2));
        Assert.assertThat(columns, Matchers.hasItem(getColumnDefMatcher(colDef1)));
        Assert.assertThat(columns, Matchers.hasItem(getColumnDefMatcher(colDef2)));
    }

    private Matcher<Column> getColumnDefMatcher(final ColumnDefinition def) {
        return new IsEqual<Column>(Mockito.mock(Column.class)) {
            public boolean matches(java.lang.Object actualValue) {
                Column c  = (Column)actualValue;
                return def.getName() == c.getName()
                        && def.getType() == c.getType();
            }
        };
    }

    @Test
    public void testGetColumnByName() {
        verifyColumn(table.column(colDef1.getName()), colDef1);
        verifyColumn(table.column(colDef2.getName()), colDef2);
    }

    @Test
    public void testEquals() throws Exception {
        Assert.assertThat(table, Matchers.equalTo(new TableImpl(name, colDef1, colDef2)));

        Assert.assertThat(table, Matchers.not(Matchers.equalTo(new TableImpl(name, colDef1))));
        Assert.assertThat(table, Matchers.not(Matchers.equalTo(new TableImpl(name + "any", colDef1, colDef2))));

    }

    @Test
    public void testHashCode() throws Exception {
        Assert.assertThat(table.hashCode(), Matchers.equalTo(new TableImpl(name, colDef1, colDef2).hashCode()));

        Assert.assertThat(table.hashCode(), Matchers.not(Matchers.equalTo(new TableImpl(name, colDef1).hashCode())));
        Assert.assertThat(table.hashCode(), Matchers.not(Matchers.equalTo(new TableImpl(name + "any", colDef1, colDef2).hashCode())));
    }

    private void verifyColumn(Column column, ColumnDefinition def) {
        Assert.assertThat(column.getName(), Matchers.equalTo(def.getName()));
        Assert.assertThat(column.getType(), Matchers.equalTo((Class)def.getType()));
        Assert.assertThat(column.getTable(), Matchers.sameInstance((Table) this.table));
    }
}