package com.huskycode.jpaquery.persister.util;

import java.lang.reflect.Field;

import javax.persistence.Id;

import com.huskycode.jpaquery.persister.exception.EntityInstantiationException;
import com.huskycode.jpaquery.populator.CannotSetValueException;

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
	
	public static Field getFieldByName(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Object getValue(Object obj, Field field) {
    	field.setAccessible(true);
		try {
			return field.get(obj);
		} catch (IllegalArgumentException e) {
			throw new CannotSetValueException(e);
		} catch (IllegalAccessException e) {
			throw new CannotSetValueException(e);
		}
    }
	
	public static Field findIdField(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		for(Field field : fields) {
			if(field.getAnnotation(Id.class) != null) {
				return field;
			}
		}
		return null;
	}
}
