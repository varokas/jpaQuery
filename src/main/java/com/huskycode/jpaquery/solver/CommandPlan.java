package com.huskycode.jpaquery.solver;

import java.util.List;

import com.huskycode.jpaquery.command.CommandNode;

public interface CommandPlan {
	List<CommandNode> getPlan();
	InOrderEntityData getInOrderEntityData();
	List<CommandNode> getCommandNodeListByEntity(Class<?> entityClass);
}
