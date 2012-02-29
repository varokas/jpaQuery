package com.huskycode.jpaquery.util;

import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/** */
public class RandomizerTest {

    private Random any = new Random();
    private static final int MAX_LENGTH = 100;
    
    @Test
    public void testGetString() {
        int length = Math.abs(any.nextInt(MAX_LENGTH));
        String expected = Randomizer.getString(length);

        assertThat(expected,instanceOf(String.class));
        assertThat(expected.length(), equalTo(length));

        length = 0;
        String expect = "";
        assertEquals(Randomizer.getString(length), expect);
    }

    @Test
    public void testGetAsciiString() {
        int length = Math.abs(any.nextInt(MAX_LENGTH));
        String actual = Randomizer.getAsciiString(length);

        assertThat(actual,instanceOf(String.class));
        assertThat(actual.length(), equalTo(length));

        length = 0;
        String expect = "";
        assertEquals(Randomizer.getAsciiString(length), expect);
    }

    @Test
    public void testGetInt() {
        assertThat(Randomizer.getInt(),instanceOf(Integer.class));
    }

    @Test
    public void testGetNonNegativeInt() {
        int actual = Randomizer.getNonNegativeInt();
        assertThat(actual,instanceOf(Integer.class));
        assertTrue(actual >= 0);
    }

    @Test
    public void testGetLong() {
        assertThat(Randomizer.getLong(),instanceOf(Long.class));
    }

    @Test
    public void testGetNonNegativeLong() {
        long actual = Randomizer.getNonNegativeLong();
        assertThat(actual,instanceOf(Long.class));
        assertTrue(actual >= 0);
    }

    @Test
    public void testGetDouble() {
        assertThat(Randomizer.getDouble(),instanceOf(Double.class));
    }

    @Test
    public void testGetBoolean() {
        assertThat(Randomizer.getBoolean(),instanceOf(Boolean.class));
    }
}
