package com.huskycode.jpaquery.populator;

import com.huskycode.jpaquery.link.Attribute;

public class AttributeValue<E, T> {
	private final Attribute<E, T> attribute;
	private final T value;
	private AttributeValue(Attribute<E, T> attribute, T value) {
		this.attribute = attribute;
		this.value = value;
	}
	
	public static <E, T> AttributeValue<E, T> newInstance(Attribute<E, T> attribute,  T value) {
		return new AttributeValue<E, T>(attribute, value);
	}

	public Attribute<E, T> getAttribute() {
		return attribute;
	}

	public T getValue() {
		return value;
	}
}
