package com.huskycode.jpaquery.types.db;

import static org.hamcrest.MatcherAssert.assertThat;
import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Function;

public class ColumnDefinitionUtilTest {

	@Test
	public void testCreateToColumnFunction() throws Exception {
        Table table = Mockito.mock(Table.class);
        String name = RandomStringUtils.randomAlphanumeric(5);
        Class type = String.class;

        //execute
        Function<ColumnDefinition, Column> toColumnFunction = ColumnDefinitionUtil.createToColumnFunction(table);
        Column result = toColumnFunction.apply(new ColumnDefinition(name, type));

        //verify
        assertThat(result.getName(), Matchers.equalTo(name));
        assertThat(result.getType(), Matchers.equalTo(type));
        assertThat(result.getTable(), Matchers.equalTo(table));
    }
}