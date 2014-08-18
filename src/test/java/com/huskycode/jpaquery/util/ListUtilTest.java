package com.huskycode.jpaquery.util;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

        Assert.assertThat(strResult, Matchers.equalTo(numberStrings));


    }

    @Test
    public void testFromIterable() {
        Iterable<String> numberStrings = Arrays.asList("1", "2", "3");

        List<String> result = ListUtil.from(numberStrings);

        Assert.assertThat(result, Matchers.equalTo(numberStrings));
    }


    private Function<Integer, String> createIntegerStringFunction() {
        return new Function<Integer, String>() {
                @Override
                public String apply(Integer input) {
                    return String.valueOf(input);
                }
            };
    }

    private Function<String, Integer> createStringIntegerFunction() {
        return new Function<String, Integer>() {
                @Override
                public Integer apply(String input) {
                    return Integer.valueOf(input);
                }
            };
    }


}
