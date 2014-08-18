package com.huskycode.jpaquery.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by surachat on 8/17/14.
 */
public class ListUtil {
    public static <F, T> List<T> map(List<F> values, Function<F, T> mapper) {
        List<T> result = new ArrayList<T>(values.size());
        for (F v : values) {
            result.add(mapper.apply(v));
        }
        return result;
    }

    public static <T> List<T> from(Iterable<T> values) {
        List<T> result = new ArrayList<T>();
        for (T v : values) {
            result.add(v);
        }
        return result;
    }
}
