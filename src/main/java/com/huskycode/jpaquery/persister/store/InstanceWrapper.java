package com.huskycode.jpaquery.persister.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A wrapper for an instance of EntityNode
 */
public class InstanceWrapper<T> {
	private final T t;

	public InstanceWrapper(T t) {
		this.t = t;
	}
	
	public static <T> InstanceWrapper<T> newInstance(T t) {
		return new InstanceWrapper<T>(t);
	}

	public T get() {
		return t;
	}

	@Override
	public int hashCode() {
		return t.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceWrapper<T> other = (InstanceWrapper<T>) obj;
		return (this.t == other.t);
	}
	
	public static <T> List<T> toInstanceList(Collection<InstanceWrapper<T>> col) {
		List<T> result = new ArrayList<T>(col.size());
		for (InstanceWrapper<T> w : col) {
			result.add(w.get());
		}
		return result;
	}
}
