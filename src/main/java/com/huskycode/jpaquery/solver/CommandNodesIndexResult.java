package com.huskycode.jpaquery.solver;

import java.util.HashMap;
import java.util.Map;

import com.huskycode.jpaquery.command.CommandNode;

public class CommandNodesIndexResult {
    private final Map<CommandNode, Integer> indexMap;

    CommandNodesIndexResult() {
        this.indexMap = new HashMap<CommandNode, Integer>();
    }

    public static CommandNodesIndexResult newInstance() {
        return new CommandNodesIndexResult();
    }

    public Integer getIndexOf(final CommandNode command) {
        return this.indexMap.get(command);
    }

    public void put(final CommandNode command, final Integer index) {
        this.indexMap.put(command, index);
    }
}
