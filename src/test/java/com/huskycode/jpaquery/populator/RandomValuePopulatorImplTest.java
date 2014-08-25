package com.huskycode.jpaquery.populator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.huskycode.jpaquery.types.db.Row;
import com.huskycode.jpaquery.types.db.RowBuilder;
import com.huskycode.jpaquery.types.db.Table;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.huskycode.jpaquery.util.Randomizer;
import com.huskycode.jpaquery.util.RandomizerImpl;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.persistence.Column;
import java.util.Arrays;

/**
 * @author Varokas Panusuwan
 */
public class RandomValuePopulatorImplTest {
    @Test
    public void testHappyPath() throws ClassNotFoundException, IllegalAccessException {
        TestClass aa = new TestClass();
        Parent paa = aa;
        Randomizer randomizer = new RandomizerImpl();
        RandomValuePopulatorImpl populator = new RandomValuePopulatorImpl(randomizer);

        populator.populateValue(aa);

        assertNotNull(aa.field1);
        assertNotNull(aa.field2);

        assertTrue(aa.field2.length() == 10);
        assertNotNull(paa.parentField1);
        assertNotNull(paa.parentField2);
        assertTrue(paa.parentField2.length() == randomizer.getDefaultLength());
    }
    
    @Test
    public void testDoNotReset255Mode() throws ClassNotFoundException, IllegalAccessException {
        Parent paa = new Parent();
        Randomizer randomizer = new RandomizerImpl();
        RandomValuePopulatorImpl populator = new RandomValuePopulatorImpl(randomizer);
        populator.setResetLength255(false);

        populator.populateValue(paa);
        assertNotNull(paa.parentField1);
        assertNotNull(paa.parentField2);
        assertTrue(paa.parentField2.length() == 255);
    }

    @Test
    public void testUseSpecificFieldRandomizerIfSpecified() throws NoSuchFieldException, SecurityException {
    	final String expectedValue = "Specific Value";
    	Parent paa = new Parent();
        Randomizer randomizer = new RandomizerImpl();
        RandomValuePopulatorImpl populator = new RandomValuePopulatorImpl(randomizer);
        populator.addFieldRandomizer(Parent.class.getDeclaredField("parentField2"),
        							new FieldValueRandomizer<String>() {
							@Override
							public String get() {
								return expectedValue;
							}
		});
        
        populator.populateValue(paa);
        assertNotNull(paa.parentField2);
        Assert.assertEquals(expectedValue, paa.parentField2);
    }

    @Test
    public void testCreateRandomValueRowFromTable() {
        String strValue = RandomStringUtils.randomAlphanumeric(5);
        int intValue = RandomUtils.nextInt();
        Table table = Mockito.mock(Table.class);
        com.huskycode.jpaquery.types.db.Column col1 = createMockColumn(RandomStringUtils.randomAlphanumeric(3), Integer.class, table);
        com.huskycode.jpaquery.types.db.Column col2 = createMockColumn(RandomStringUtils.randomAlphanumeric(3), String.class, table);
        mockTableValues(table, col1, col2);
        Row row = new RowBuilder(table).withColumnValue(col1, strValue).withColumnValue(col2, intValue).build();

        Randomizer randomizer = new RandomizerImpl();
        RandomValuePopulatorImpl populator = new RandomValuePopulatorImpl(randomizer);

        Row result = populator.random(table);

        Assert.assertThat(result.getTable(), Matchers.sameInstance(table));
        Assert.assertThat(result.getColumnValue().size(), Matchers.equalTo(2));
        Assert.assertThat(result.getColumnValue().get(0).getColumn(), Matchers.equalTo(col1));
        Assert.assertThat(result.getColumnValue().get(0).getValue(), Matchers.instanceOf(Integer.class));
        Assert.assertThat(result.getColumnValue().get(1).getColumn(), Matchers.equalTo(col2));
        Assert.assertThat(result.getColumnValue().get(1).getValue(), Matchers.instanceOf(String.class));
    }

    private void mockTableValues(Table table, com.huskycode.jpaquery.types.db.Column col1, com.huskycode.jpaquery.types.db.Column col2) {
        Mockito.when(table.getColumns()).thenReturn(Arrays.asList(col1, col2));
    }

    private com.huskycode.jpaquery.types.db.Column createMockColumn(String name, Class type, Table parent) {
        com.huskycode.jpaquery.types.db.Column col = Mockito.mock(com.huskycode.jpaquery.types.db.Column.class);
        Mockito.when(col.getName()).thenReturn(name);
        Mockito.when(col.getTable()).thenReturn(parent);
        Mockito.when(col.getType()).thenReturn(type);

        return col;
    }


    private class Parent {
        private Integer parentField1;
        @Column
        private String parentField2;
    }

    private class TestClass extends Parent {
        private int field1;

        @Column(length = 10) private String field2;
    }
}
