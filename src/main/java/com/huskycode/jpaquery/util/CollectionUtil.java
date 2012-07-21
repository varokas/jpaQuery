package com.huskycode.jpaquery.util;

import java.util.Collection;

public class CollectionUtil {
	public static <T> boolean containAny(Collection<T> container, Collection<T> dataToCheck) {
		for (T e : dataToCheck) {
			if (container.contains(e)) {
				return true;
			}
		}
		return false;
	}
}
