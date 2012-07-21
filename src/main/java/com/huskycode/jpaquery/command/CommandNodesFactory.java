package com.huskycode.jpaquery.command;

import java.util.Arrays;
import java.util.List;

public class CommandNodesFactory {

	public static CommandNodes ns(CommandNode ... commands) {
		return new CommandNodesImpl(Arrays.asList(commands));
	}

	private static class CommandNodesImpl implements CommandNodes {
		private final List<CommandNode> commands;
	
		public CommandNodesImpl(List<CommandNode> commands) {
			this.commands = commands;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((commands == null) ? 0 : commands.hashCode());
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
			CommandNodesImpl other = (CommandNodesImpl) obj;
			if (commands == null) {
				if (other.commands != null)
					return false;
			} else if (!commands.equals(other.commands))
				return false;
			return true;
		}

		@Override
		public List<CommandNode> get() {
			return this.commands;
		}	
	}
}
