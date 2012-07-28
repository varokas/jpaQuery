package com.huskycode.jpaquery.command;

import java.lang.reflect.Field;
import java.util.Map;

public interface CommandNodeBuilder {
	CommandNodeBuilder with(CommandNode... children);
	CommandNodeBuilder withValues(Map<Field, Object> values);
}
