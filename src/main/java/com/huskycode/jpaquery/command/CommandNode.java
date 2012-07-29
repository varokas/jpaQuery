package com.huskycode.jpaquery.command;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Represent a command to create
 * 
 * @author Varokas Panusuwan
 */
public interface CommandNode {
	Class<?> getEntity();
	List<CommandNode> getChildren();
	Map<Field, Object> getFieldValues();
}