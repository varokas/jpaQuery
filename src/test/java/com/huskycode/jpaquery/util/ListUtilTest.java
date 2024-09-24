package com.huskycode.jpaquery.util;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import java.util.function.Function;

/**
 * Created by surachat on 8/17/14.
 */
public class ListUtilTest {
    @Test
    public void testMapping() {
        List<String> numberStrings = Arrays.asList("1", "2", "3");

        //execute
        List<Integer> intResult = ListUtil.map(numberStrings, createStringIntegerFunction());
        List<String>  strResult = ListUtil.map(intResult, createIntegerStringFunction());

        assertThat(strResult, Matchers.equalTo(numberStrings));


    }

    @Test
    public void testFromIterable() {
        Iterable<String> numberStrings = Arrays.asList("1", "2", "3");

        List<String> result = ListUtil.from(numberStrings);

        assertThat(result, Matchers.equalTo(numberStrings));
    }


    private Function<Integer, String> createIntegerStringFunction() {
        return String::valueOf;
    }

    private Function<String, Integer> createStringIntegerFunction() {
        return Integer::valueOf;
    }


}
