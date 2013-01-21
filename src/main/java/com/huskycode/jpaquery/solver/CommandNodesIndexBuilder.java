package com.huskycode.jpaquery.solver;

import com.huskycode.jpaquery.command.CommandNodes;

public interface CommandNodesIndexBuilder {
    CommandNodesIndexResult build(final CommandNodes commands);
}
