package com.huskycode.jpaquery.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MapsTest {
	
	@Test
	public void testCreateMapCorrectlyForOneEntry() {
		Object key = new Object();
		Object value = new Object();
		
		Map<Object, Object> map = Maps.of(key, value);
		
		Assert.assertSame(value, map.get(key));
	}
	
	@Test
	public void testCreateMapCorrectlyForMoreThanOneEntry() {
		Object key1 = new Object();
		Object value1 = new Object();
		Object key2 = new Object();
		Object value2 = new Object();
		
		Map<Object, Object> map = Maps.of(key1, value1, key2, value2);
		
		Assert.assertSame(value1, map.get(key1));
		Assert.assertSame(value2, map.get(key2));
	}

    @Test
    public void testCreateMapFromIterables() {
        List<TestValue> values = Arrays.asList(
                new TestValue("k1"),
                new TestValue("k2"));

        Map<String, TestValue> mapResult = Maps.from(values, KEY_FUNC);

        Assert.assertEquals(2, mapResult.size());
        Assert.assertSame(values.get(0), mapResult.get(values.get(0).key));
        Assert.assertSame(values.get(1), mapResult.get(values.get(1).key));
    }

    @Test(expected = RuntimeException.class)
    public void testThrowExceptionIfThereAreDuplicateKeysWhenCreateMapFromIterables() {
        List<TestValue> values = Arrays.asList(
                new TestValue("k1"),
                new TestValue("k1"));

        Maps.from(values, KEY_FUNC);
    }

    private static class TestValue {
        private String key;

        public TestValue(String key) {
            this.key = key;
        }
    }

    private Function<TestValue, String> KEY_FUNC = new Function<TestValue, String>() {
        @Override
        public String apply(TestValue input) {
            return input.key;
        }
    };

}
