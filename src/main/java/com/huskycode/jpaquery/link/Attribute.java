package com.huskycode.jpaquery.link;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: Ta
 * Date: 3/26/12
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Attribute<E, T> {
    Class<E> getEntityClass();
    Field getField();
}
