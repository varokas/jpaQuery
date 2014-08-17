package com.huskycode.jpaquery;

import com.huskycode.jpaquery.types.db.Link;
import com.huskycode.jpaquery.types.db.Table;

import java.util.List;
import java.util.Set;

/**
 * Created by varokas.
 */
public class GenericDependenciesDefinition {
    private final List<Link> links;
    private final Set<Table> enumTables;
    private final Set<Table> triggeredTables;

    public GenericDependenciesDefinition(List<Link> links, Set<Table> enumTables, Set<Table> triggeredTables) {
        this.links = links;
        this.enumTables = enumTables;
        this.triggeredTables = triggeredTables;
    }
}
