package com.huskycode.jpaquery.command;

import java.lang.reflect.Field;
import java.util.Map;

import com.huskycode.jpaquery.command.CommandNodeFactory.CommandNodeImpl;

public interface CommandNodeBuilder {
	CommandNodeBuilder with(CommandNode... children);
	CommandNodeImpl withValues(Map<Field, ?> values);
}
