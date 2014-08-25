package com.huskycode.jpaquery.types.tree;

import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.types.db.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by surachat on 8/24/14.
 */
public class TableEntityNode implements EntityNode {
    private final Table entityClass;
    private final Set<EntityNode> parent;
    private final Set<EntityNode> childs;

    private CommandNode command;

    //will have specific values to be set  in the future

    private TableEntityNode(Table entityClass) {
        this.entityClass = entityClass;
        this.childs = new HashSet<EntityNode>();
        this.parent = new HashSet<EntityNode>();
    }

    public static EntityNode newInstance(Table entityClass) {
        return new TableEntityNode(entityClass);
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
        /* TODO: Return Table instead or use Adapter that can map from table to class */
        return null;
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
        return "EntityNode [entityClass=" + entityClass.getName()
                + ", command=" + command + "]";
    }
}
