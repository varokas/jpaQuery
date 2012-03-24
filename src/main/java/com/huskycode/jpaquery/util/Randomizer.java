package com.huskycode.jpaquery.util;

/**
 * Created by IntelliJ IDEA.
 * User: Ta
 * Date: 3/22/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Randomizer {
    public <T> T getRandomOfType(Class<T> type, int ... length);
}
