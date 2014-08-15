package com.huskycode.jpaquery.command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.metamodel.SingularAttribute;

import com.huskycode.jpaquery.persister.util.BeanUtil;

/**
 * The way to create Command Node
 * 
 * @author Varokas Panusuwan
 */
public class CommandNodeFactory {

    public static CommandNodeImpl n(final Class<?> entity, final CommandNode... children) {
        ArrayList<CommandNode> list = new ArrayList<CommandNode>();
        list.addAll(Arrays.asList(children));
        return new CommandNodeImpl(entity, list);
    }

    /**
     * Implementation of a Command Node
     */
    public static class CommandNodeImpl implements CommandNode, CommandNodeBuilder {
        private final Class<?> entity;
        private final List<CommandNode> children;
        private final Map<Field, Object> values;

        public CommandNodeImpl(final Class<?> entity, final List<CommandNode> children) {
            this.entity = entity;
            this.children = children;
            values = new HashMap<Field, Object>(0);
        }

        @Override
        public Class<?> getEntity() {
            return entity;
        }

        @Override
        public List<CommandNode> getChildren() {
            return children;
        }

        @Override
        public Map<Field, Object> getFieldValues() {
            return values;
        }

        @Override
        public String toString() {
            return "CommandNodeImpl [entity=" + entity.getSimpleName() + ", children=" + children + ", values="
                    + values + "]";
        }

        @Override
        public CommandNodeImpl with(final CommandNode... childrens) {
            this.children.addAll(Arrays.asList(childrens));
            return this;
        }

        @Override
        public CommandNodeImpl withValues(final Map<Field, ?> values) {
            this.values.putAll(values);
            return this;
        }

        @Override
        public <E, T> CommandNodeImpl withValue(final SingularAttribute<E, T> attr, final T value) {
            this.values.put(BeanUtil.getField(attr), value);
            return this;
        }
    }
}