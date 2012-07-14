package com.huskycode.jpaquery.persister.util;

import com.huskycode.jpaquery.persister.exception.EntityInstantiationException;

/**
 * @author Varokas Panusuwan
 */
public class BeanUtil {
	public static <E> E newInstance(Class<E> beanClass) {
        try {
            return beanClass.newInstance();
        } catch (InstantiationException e) {
            throw new EntityInstantiationException("Cannot create class: " + beanClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new EntityInstantiationException("Cannot create class: " + beanClass.getName(), e);
        }
	}
}
