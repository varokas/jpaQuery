package com.huskycode.jpaquery.util;

/**
 * Created by surachat on 8/17/14.
 */
public interface Function<I, O> {
    O apply(I input);
}
