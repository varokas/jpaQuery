package com.huskycode.jpaquery.populator;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import javax.persistence.Column;
import javax.persistence.metamodel.SingularAttribute;

import com.huskycode.jpaquery.util.Randomizer;
import com.huskycode.jpaquery.util.RandomizerImpl;

/**
 * @author Varokas Panusuwan
 */
public class RandomValuePopulatorImpl implements RandomValuePopulator {

    private final Randomizer randomizer;

    private final Map<Field, FieldValueRandomizer<?>> specificFieldRandomizersMap;

    RandomValuePopulatorImpl(final Randomizer randomizer) {
        this.randomizer = randomizer;
        this.specificFieldRandomizersMap = new HashMap<Field, FieldValueRandomizer<?>>();
    }

    public RandomValuePopulatorImpl() {
        this(new RandomizerImpl());
    }

    @Override
    public <E> void populateValue(final E entity) {
        List<Field> allFields = getAllFieldsInHierarchy(entity);
        for (Field f : allFields) {
            if ((f.getModifiers() & Modifier.FINAL) != Modifier.FINAL
                    && (f.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
                try {
                    setRandomValue(entity, f);
                } catch (IllegalAccessException e) {
                    throw new CannotSetValueException(e);
                }
            }
        }
    }

    private <E> List<Field> getAllFieldsInHierarchy(final E entity) {
        Queue<Class<?>> working = new LinkedList<Class<?>>();
        List<Field> allFields = new LinkedList<Field>();
        working.add(entity.getClass());
        while (working.size() > 0) {
            Class<?> c = working.poll();
            Class<?> sper = c.getSuperclass();
            if (c.getSuperclass() != null) {
                working.add(sper);
            }
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                allFields.add(f);
            }
        }
        return allFields;
    }

    public <E, T> void setRandomValue(final E entity, final Field field) throws IllegalAccessException {
        field.setAccessible(true);
        int length = 0;
        FieldValueRandomizer<?> fieldRandomizer = this.specificFieldRandomizersMap.get(field);
        if (fieldRandomizer != null) {
            setValue(entity, field, fieldRandomizer.get());
        } else {
            if (field.getType().equals(String.class) && field.isAnnotationPresent(Column.class)) {
                Column anno = field.getAnnotation(Column.class);
                length = anno.length();
            }
            setValue(entity, field, randomizer.getRandomOfType(field.getType(), length));
        }

    }

    private <E, T> void setValue(final E entity, final Field field, final T value) throws IllegalAccessException {
        field.set(entity, value);
    }

    private <E, T> T getValue(final E entity, final Field field) throws IllegalAccessException {
        return (T)field.get(entity);
    }

    private <E, T> void setRandomValue(final E entity, final SingularAttribute<?, T> field) {
        // ParameterizedType parameters =
        // (ParameterizedType)field.getGenericType();
        // Type[] types = parameters.getActualTypeArguments();
        // Type declaringClass = types[0];
        // Type dataType = types[1];
        setValue(entity, field, randomizer.getRandomOfType(field.getJavaType()));
    }

    private <E, T> void setValue(final E entity, final SingularAttribute<?, T> field, final T value) {
        try {
            Member member = field.getJavaMember();
            if (member instanceof Method) {
                // TODO: how to get set Method
            } else if (member instanceof Field) {
                ((Field)member).set(entity, value);
            } else {
                throw new IllegalArgumentException("Unexpected java member type. Expecting method or field, found: "
                        + member);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <E, T> T getValue(final E entity, final SingularAttribute<?, T> field) {
        try {
            Member member = field.getJavaMember();
            if (member instanceof Method) {
                return (T)((Method)member).invoke(entity);
            } else if (member instanceof Field) {
                return (T)((Field)member).get(entity);
            } else {
                throw new IllegalArgumentException("Unexpected java member type. Expecting method or field, found: "
                        + member);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFieldRandomizer(final Field field, final FieldValueRandomizer<?> randomizer) {
        this.specificFieldRandomizersMap.put(field, randomizer);
    }

    @Override
    public <E, T> void addFieldRandomizer(final SingularAttribute<E, T> attr, final FieldValueRandomizer<T> randomizer) {
        Field field = (Field)attr.getJavaMember();
        addFieldRandomizer(field, randomizer);
    }

    @Override
    public <E, T> void addFieldRandomizers(final Map<SingularAttribute, FieldValueRandomizer> map) {
        for (Entry<SingularAttribute, FieldValueRandomizer> entry : map.entrySet()) {
            addFieldRandomizer(entry.getKey(), entry.getValue());
        }
    }
}
