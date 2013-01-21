package com.huskycode.jpaquery.solver;

import java.util.HashMap;
import java.util.Map;

import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodes;

public class CommandNodesIndexBuilderImpl implements CommandNodesIndexBuilder {

    @Override
    public CommandNodesIndexResult build(final CommandNodes commands) {
        Map<Class<?>, Integer> sizeMap = new HashMap<Class<?>, Integer>();
        CommandNodesIndexResult result = new CommandNodesIndexResult();
        for (CommandNode command : commands.get()) {
            deepFirstSearchIndexing(command, sizeMap, result);
        }
        return result;
    }

    private void deepFirstSearchIndexing(final CommandNode command, final Map<Class<?>, Integer> sizeMap,
            final CommandNodesIndexResult result) {
        Class<?> c = command.getEntity();
        int size = getSize(c, sizeMap);
        sizeMap.put(c, size + 1);
        result.put(command, size);
        for (CommandNode child : command.getChildren()) {
            deepFirstSearchIndexing(child, sizeMap, result);
        }
    }

    private Integer getSize(final Class<?> clazz, final Map<Class<?>, Integer> sizeMap) {
        Integer size = 0;
        if (sizeMap.containsKey(clazz)) {
            size = sizeMap.get(clazz);
        }
        return size;
    }
}
