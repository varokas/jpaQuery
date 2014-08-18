package com.huskycode.jpaquery.types.db;

import com.huskycode.jpaquery.util.Function;
import junit.framework.TestCase;
import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.mockito.Mockito;

public class ColumnDefinitionUtilTest extends TestCase {

    public void testCreateToColumnFunction() throws Exception {
        Table table = Mockito.mock(Table.class);
        String name = RandomStringUtils.randomAlphanumeric(5);
        Class type = String.class;

        //execute
        Function<ColumnDefinition, Column> toColumnFunction = ColumnDefinitionUtil.createToColumnFunction(table);
        Column result = toColumnFunction.apply(new ColumnDefinition(name, type));

        //verify
        Assert.assertThat(result.getName(), Matchers.equalTo(name));
        Assert.assertThat(result.getType(), Matchers.equalTo(type));
        Assert.assertThat(result.getTable(), Matchers.equalTo(table));
    }
}