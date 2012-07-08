package com.huskycode.jpaquery.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The way to create Command Node
 * 
 * @author Varokas Panusuwan
 */
public class CommandNodeFactory {
	public static CommandNode n(Class<?> entity) {
		return new CommandNodeImpl(entity, new ArrayList<CommandNode>());
	}
	
	public static CommandNode n(Class<?> entity, CommandNode... children) {
		return new CommandNodeImpl(entity, Arrays.asList(children));
	}
	
	/**
	 * Implementation of a Command Node
	 */
	private static class CommandNodeImpl implements CommandNode {
		private final Class<?> entity;
		private final List<CommandNode> children;
		
		public CommandNodeImpl(Class<?> entity, List<CommandNode> children) {
			this.entity = entity;
			this.children = children;
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
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((children == null) ? 0 : children.hashCode());
			result = prime * result + ((entity == null) ? 0 : entity.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CommandNodeImpl other = (CommandNodeImpl) obj;
			if (children == null) {
				if (other.children != null)
					return false;
			} else if (!children.equals(other.children))
				return false;
			if (entity == null) {
				if (other.entity != null)
					return false;
			} else if (!entity.equals(other.entity))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "CommandNodeImpl [entity=" + entity.getSimpleName() + ", children="
					+ children + "]";
		}
	}
}