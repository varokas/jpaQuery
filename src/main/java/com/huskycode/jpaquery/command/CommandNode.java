package com.huskycode.jpaquery.command;

import java.util.List;

/**
 * Represent a command to create
 * 
 * @author Varokas Panusuwan
 */
public interface CommandNode {
	Class<?> getEntity();
	List<CommandNode> getChildren();
}