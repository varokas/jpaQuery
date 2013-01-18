package com.huskycode.jpaquery.populator;

import static org.junit.Assert.assertNotNull;

import javax.persistence.Column;

import org.junit.Test;

import com.huskycode.jpaquery.util.Randomizer;
import com.huskycode.jpaquery.util.RandomizerImpl;

/**
 * @author Varokas Panusuwan
 */
public class RandomValuePopulatorImplTest {
    @Test
    public void test() throws ClassNotFoundException, IllegalAccessException {
        TestClass aa = new TestClass();
        Parent paa = aa;
        Randomizer randomizer = new RandomizerImpl();
        RandomValuePopulatorImpl populator = new RandomValuePopulatorImpl(randomizer);

        populator.populateValue(aa);

        assertNotNull(aa.field1);
        assertNotNull(aa.field2);
        // TODO
        // assertTrue(aa.field2.length() == 10); Need to work with @Column
        // default
        // length value of 255 first. Now it will random string with length of
        // 2.
        assertNotNull(paa.parentField1);
        assertNotNull(paa.parentField2);
    }

    private class Parent {
        private Integer parentField1;
        private String parentField2;
    }

    private class TestClass extends Parent {
        private int field1;

        @Column(length = 10) private String field2;
    }
}
