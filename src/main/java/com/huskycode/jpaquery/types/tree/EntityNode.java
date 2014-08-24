package com.huskycode.jpaquery.types.tree;

import java.util.HashSet;
import java.util.Set;

import com.huskycode.jpaquery.command.CommandNode;
public interface EntityNode {

	public Set<EntityNode> getChilds();
	
	public void addChild(EntityNode entity);
	
	public void addParent(EntityNode entity);
	
	public void removeParent(EntityNode entity);
	
	public boolean hasChilds();
	
	public boolean hasParents();

	public Class<?> getEntityClass();

	public CommandNode getCommand();

	public void setCommand(CommandNode command);

	public Set<EntityNode> getParent();
}

