package com.huskycode.jpaquery.populator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Column;

import org.junit.Assert;
import org.junit.Test;

import com.huskycode.jpaquery.util.Randomizer;
import com.huskycode.jpaquery.util.RandomizerImpl;

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
