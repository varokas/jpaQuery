package com.huskycode.jpaquery.populator;

import java.lang.reflect.Field;
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
    
    private boolean resetLength255 = true;

    RandomValuePopulatorImpl(final Randomizer randomizer) {
        this.randomizer = randomizer;
        this.specificFieldRandomizersMap = new HashMap<Field, FieldValueRandomizer<?>>();
    }

    public RandomValuePopulatorImpl() {
        this(new RandomizerImpl());
    }

    public boolean isResetLength255() {
		return resetLength255;
	}

	public void setResetLength255(boolean resetLength255) {
		this.resetLength255 = resetLength255;
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
                length = checkDefaultLengthValueOfLength(anno.length());
            }
            setValue(entity, field, randomizer.getRandomOfType(field.getType(), length));
        }

    }
    
    /**
     * Currently, it cannot distinguish between set length value of 255 or the default value of @Column annotation.
     * Many systems do not use the length value yet and value of 255 does not really apply. Therefore, if we find
     * the value of length == 255, we will reset it to zero which is equivalent to not specified. 
     * @param length
     * @return
     */
    private int checkDefaultLengthValueOfLength(int length) {
    	if (resetLength255 && length == 255) {
    		return 0;
    	}
    	return length;
    }

    private <E, T> void setValue(final E entity, final Field field, final T value) throws IllegalAccessException {
        field.set(entity, value);
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
