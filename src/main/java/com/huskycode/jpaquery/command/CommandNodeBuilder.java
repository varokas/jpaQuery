package com.huskycode.jpaquery.command;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.metamodel.SingularAttribute;

import com.huskycode.jpaquery.command.CommandNodeFactory.CommandNodeImpl;

public interface CommandNodeBuilder {
    CommandNodeBuilder with(CommandNode... children);

    CommandNodeImpl withValues(Map<Field, ? extends Object> values);

    <E, T> CommandNodeImpl withValue(SingularAttribute<E, T> attr, T value);
}
