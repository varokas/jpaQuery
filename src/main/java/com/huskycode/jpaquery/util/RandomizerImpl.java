package com.huskycode.jpaquery.util;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

/** */
public class RandomizerImpl implements Randomizer{
    
    private static final Random any = new Random();
    public static Integer DEFAULT_LENGTH = 10;
    
    @Override
    public <T> T getRandomOfType(Class<T> type, int ... length) {
        if (type.equals(String.class)) {
            return length != null
                    && length.length > 0
                    && length[0] > 0 ? (T)(getAsciiString(length[0])) : (T)(getAsciiString());
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return (T)(getInt());
        } else if (type.equals(Double.class)  || type.equals(double.class)) {
            return (T)(getDouble());
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return (T)(getFloat());
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return (T)(getLong());
        } else if (type.equals(Character.class) || type.equals(char.class)) {
            return (T)(getCharacter());
        }  else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return (T)(getBoolean());
        } else if (type.equals(Date.class)) {
            return (T)(getDate());
        } else {
            System.out.println("Not primitive: " + type);
            throw new UnsupportedOperationException("Not a primitive class: " + type);
        }
    }
    
    public static String getString(int length) {
        return (length == 0) ? "" : RandomStringUtils.random(Math.abs(length));
    }

    public static String getString() {
        return getString(any.nextInt(DEFAULT_LENGTH));
    }

    public static String getAsciiString(int length) {
        return (length == 0) ? "" : RandomStringUtils.randomAscii(Math.abs(length));
    }

    public static Character getCharacter() {
        return RandomStringUtils.randomAscii(1).charAt(0);
    }

    public static String getAsciiString() {
        return getAsciiString(any.nextInt(DEFAULT_LENGTH));
    }
    
    public static Integer getInt() {
        return any.nextInt();
    }

    public static Integer getNonNegativeInt() {
        return Math.abs(any.nextInt());
    }

    public static Long getLong() {
        return any.nextLong();
    }

    public static Long getNonNegativeLong() {
        return Math.abs(any.nextLong());
    }

    public static Double getDouble() {
        return any.nextDouble();
    }

    public static Float getFloat() {
        return any.nextFloat();
    }

    public static Boolean getBoolean() {
        return any.nextDouble() > 0.5;
    }
    
    public static Date getDate() {
        return new Date(getLong());
    }
    
    public static <T> T  getRandomFromArray(T[] array) {
        int length = array.length;
        return array[any.nextInt(length)];
    }

    public static <T> T  getRandomFromList(List<T> list) {
        int length = list.size();
        return list.get(any.nextInt(length));
    }
}
