package com.huskycode.jpaquery.populator;

import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.util.Randomizer;
import com.huskycode.jpaquery.util.RandomizerImpl;
import org.junit.Test;

import javax.persistence.Column;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(aa.field2.length() == 10);
        assertNotNull(paa.parentField1);
        assertNotNull(paa.parentField2);
    }


    private class Parent {
        private Integer parentField1;
        private String parentField2;
    }
    
    private class TestClass extends Parent {
        private int field1;

        @Column(length = 10)
        private String field2;
    }
}
