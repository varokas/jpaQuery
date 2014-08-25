package com.huskycode.jpaquery.types.tree;

import com.huskycode.jpaquery.command.CommandNode;

import java.util.HashSet;
import java.util.Set;

public class EntityNodeImpl implements EntityNode {
    private final Class<?> entityClass;
    private final Set<EntityNode> parent;
    private final Set<EntityNode> childs;

	private CommandNode command;

	//will have specific values to be set  in the future

	private EntityNodeImpl(Class<?> entityClass) {
		this.entityClass = entityClass;
		this.childs = new HashSet<EntityNode>();
		this.parent = new HashSet<EntityNode>();
	}

	public static EntityNode newInstance(Class<?> entityClass) {
		return new EntityNodeImpl(entityClass);
	}

	public Set<EntityNode> getChilds() {
		return childs;
	}
	
	public void addChild(EntityNode entity) {
		this.childs.add(entity);
	}
	
	public void addParent(EntityNode entity) {
		this.parent.add(entity);
	}
	
	public void removeParent(EntityNode entity) {
		this.parent.remove(entity);
	}
	
	public boolean hasChilds() {
		return this.childs.size() > 0;
	}
	
	public boolean hasParents() {
		return this.parent.size() > 0;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public CommandNode getCommand() {
		return command;
	}

	public void setCommand(CommandNode command) {
		this.command = command;
	}

	public Set<EntityNode> getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return "EntityNode [entityClass=" + entityClass.getSimpleName()
				+ ", command=" + command + "]";
	}
}

