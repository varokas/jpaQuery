package com.huskycode.jpaquery.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


/** */
public class RandomizerTest {

    private Random any = new Random();
    private static final int MAX_LENGTH = 100;

    @Test
    public void testRandomByType() {
        RandomizerImpl randomizer = new RandomizerImpl();
        
        assertThat(randomizer.getRandomOfType(String.class), instanceOf(String.class));
        assertThat(randomizer.getRandomOfType(Integer.class), instanceOf(Integer.class));
        assertThat(randomizer.getRandomOfType(Double.class), instanceOf(Double.class));
        assertThat(randomizer.getRandomOfType(Float.class), instanceOf(Float.class));
        assertThat(randomizer.getRandomOfType(Long.class), instanceOf(Long.class));
        assertThat(randomizer.getRandomOfType(Boolean.class), instanceOf(Boolean.class));
        assertThat(randomizer.getRandomOfType(Character.class), instanceOf(Character.class));
    }
    
    @Test
    public void testRandomByStringTypeRespectDefaultLength() {
    	RandomizerImpl randomizer = new RandomizerImpl();
    	int defaultLength = 1;
    	randomizer.setDefaultLength(defaultLength);
        
    	String result = randomizer.getRandomOfType(String.class);
        assertEquals(defaultLength, result.length());
    }
    
    @Test
    public void testGetString() {
        int length = Math.abs(any.nextInt(MAX_LENGTH));
        String expected = RandomizerImpl.getString(length);

        assertThat(expected,instanceOf(String.class));
        assertThat(expected.length(), equalTo(length));

        length = 0;
        String expect = "";
        assertEquals(RandomizerImpl.getString(length), expect);
    }

    @Test
    public void testGetAsciiString() {
        int length = Math.abs(any.nextInt(MAX_LENGTH));
        String actual = RandomizerImpl.getAlphanumericString(length);

        assertThat(actual,instanceOf(String.class));
        assertThat(actual.length(), equalTo(length));

        length = 0;
        String expect = "";
        assertEquals(RandomizerImpl.getAlphanumericString(length), expect);
    }

    @Test
    public void testGetInt() {
        assertThat(RandomizerImpl.getInt(),instanceOf(Integer.class));
    }

    @Test
    public void testGetNonNegativeInt() {
        int actual = RandomizerImpl.getNonNegativeInt();
        assertThat(actual,instanceOf(Integer.class));
        assertTrue(actual >= 0);
    }

    @Test
    public void testGetLong() {
        assertThat(RandomizerImpl.getLong(),instanceOf(Long.class));
    }
    
    @Test
    public void testGetLongNeverReturnValueLargerThanInt() {
    	assertThat(RandomizerImpl.getLong(),lessThan((long)Integer.MAX_VALUE));
    } 

    @Test
    public void testGetNonNegativeLong() {
        long actual = RandomizerImpl.getNonNegativeLong();
        assertThat(actual,instanceOf(Long.class));
        assertTrue(actual >= 0);
    }

    @Test
    public void testGetDouble() {
        assertThat(RandomizerImpl.getDouble(),instanceOf(Double.class));
    }

    @Test
    public void testGetBoolean() {
        assertThat(RandomizerImpl.getBoolean(),instanceOf(Boolean.class));
    }

    @Test
    public void testGetRandomFromArray() {
        Integer[] data = {any.nextInt(), any.nextInt(), any.nextInt(), any.nextInt()};
        
        int selectData =  RandomizerImpl.getRandomFromArray(data);
        
        assertThat(selectData, isIn(data));
    }


    @Test
    public void testGetRandomFromList() {
        List<Integer> data = new ArrayList<Integer>();
        data.add(any.nextInt());
        data.add(any.nextInt());
        data.add(any.nextInt());

        int selectData =  RandomizerImpl.getRandomFromList(data);

        assertThat(selectData, isIn(data));
    }
}
