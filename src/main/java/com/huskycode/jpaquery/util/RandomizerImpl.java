package com.huskycode.jpaquery.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

/** */
public class RandomizerImpl implements Randomizer {

    private static final Random any = new Random();
    public static int DEFAULT_LENGTH = 2;
    
    private int defaultLength = DEFAULT_LENGTH;

    public int getDefaultLength() {
		return defaultLength;
	}

	public void setDefaultLength(int defaultLength) {
		this.defaultLength = defaultLength;
	}

	@Override
    public <T> T getRandomOfType(final Class<T> type, final int length) {
        if (type.equals(String.class)) {
            return length > 0 ? (T)(getAlphanumericString(length)) : (T)(getAlphanumericString(this.defaultLength));
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return (T)(getInt());
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return (T)(getDouble());
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return (T)(getFloat());
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return (T)(getLong());
        } else if (type.equals(Character.class) || type.equals(char.class)) {
            return (T)(getCharacter());
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return (T)(getBoolean());
        } else if (type.equals(Date.class)) {
            return (T)(getDate());
        } else if (type.equals(BigDecimal.class)) {
            return (T)(getBigDecimal());
        } else if (type.equals(Timestamp.class)) {
            return (T)(getTimestamp());
        } else if (type.isEnum()) {
            return (getRandomEnum(type));
        } else {
            System.out.println("Not primitive: " + type);
            throw new UnsupportedOperationException("Not a primitive class: " + type);
        }
    }

    private Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static <T> T getRandomEnum(final Class<T> enumType) {
        return enumType.getEnumConstants()[0];
    }

    public static BigDecimal getBigDecimal() {
        return new BigDecimal(any.nextDouble());
    }

    @Override
    public <T> T getRandomOfType(final Class<T> type) {
        return getRandomOfType(type, 0);
    }

    public static String getString(final int length) {
        return (length == 0) ? "" : RandomStringUtils.randomAlphanumeric(Math.abs(length));
    }

    public static String getString() {
        return getString(any.nextInt(DEFAULT_LENGTH));
    }

    public static String getAlphanumericString(final int length) {
        return (length == 0) ? "" : RandomStringUtils.randomAlphanumeric(Math.abs(length));
    }

    public static Character getCharacter() {
        return RandomStringUtils.randomAlphanumeric(1).charAt(0);
    }

    public static String getAlphanumericString() {
        return getAlphanumericString(any.nextInt(DEFAULT_LENGTH));
    }

    public static Integer getInt() {
        return any.nextInt();
    }

    public static Integer getNonNegativeInt() {
        return Math.abs(any.nextInt());
    }

    /**
     * @return return value in range of Integer so that it is compatible and system with
     * 			inconsistency of the application type and database type.
     */
    public static Long getLong() {
        return new Long(any.nextInt());
    }

    /**
     * @return return value in range of Integer so that it is compatible and system with
     * 			inconsistency of the application type and database type.
     */
    public static Long getNonNegativeLong() {
        return Math.abs(getLong());
    }

    public static Double getDouble() {
        return any.nextDouble();
    }

    public static Float getFloat() {
        return any.nextFloat();
    }

    public static Boolean getBoolean() {
        return any.nextBoolean();
    }

    public static Date getDate() {
        return new Date();
    }

    public static <T> T getRandomFromArray(final T[] array) {
        int length = array.length;
        return array[any.nextInt(length)];
    }

    public static <T> T getRandomFromList(final List<T> list) {
        int length = list.size();
        return list.get(any.nextInt(length));
    }
}
