package com.huskycode.jpaquery.persister.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.metamodel.SingularAttribute;

import com.huskycode.jpaquery.persister.exception.EntityInstantiationException;
import com.huskycode.jpaquery.populator.CannotSetValueException;

/**
 * @author Varokas Panusuwan
 */
public class BeanUtil {
    public static <E> E newInstance(final Class<E> beanClass) {
        try {
            Constructor<E> c = searchForDefaultConstructor(beanClass);
            if (c != null) {
                c.setAccessible(true);
                return c.newInstance();
            } else {
                return beanClass.newInstance();
            }
        } catch (InstantiationException e) {
            throw new EntityInstantiationException("Cannot create class: " + beanClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new EntityInstantiationException("Cannot create class: " + beanClass.getName(), e);
        } catch (IllegalArgumentException e) {
            throw new EntityInstantiationException("Cannot create class: " + beanClass.getName(), e);
        } catch (InvocationTargetException e) {
            throw new EntityInstantiationException("Cannot create class: " + beanClass.getName(), e);
        }
    }

    private static <E> Constructor<E> searchForDefaultConstructor(final Class<E> beanClass) {
        Constructor<E>[] constructors = (Constructor<E>[])beanClass.getDeclaredConstructors();
        for (int i = 0; i < constructors.length; i++) {
            if (constructors[i].getParameterTypes().length == 0) {
                return constructors[i];
            }
        }
        return null;
    }

    public static Field getFieldByName(final Class<?> clazz, final String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getValue(final Object obj, final Field field) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalArgumentException e) {
            throw new CannotSetValueException(e);
        } catch (IllegalAccessException e) {
            throw new CannotSetValueException(e);
        }
    }

    public static Field findIdField(final Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                return field;
            }
        }
        return null;
    }

    public static boolean isAnnotatedWithGenerated(final Field field) {
        return field.getAnnotation(GeneratedValue.class) != null;
    }

    public static <T> Field getField(final SingularAttribute<?, T> attr) {
        // A Hack for Composite Key
        Class<?> declaredJavaType = attr.getDeclaringType().getJavaType();

        Field originalField = (Field)attr.getJavaMember();
        if (originalField.getDeclaringClass().equals(declaredJavaType)) {
            return originalField;
        } else {
            try {
                return declaredJavaType.getDeclaredField(originalField.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
