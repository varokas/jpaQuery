package com.huskycode.jpaquery.jpa;

import com.huskycode.jpaquery.GenericDependenciesDefinition;
import com.huskycode.jpaquery.persister.RowPersister;

/**
 * A product of jpa dependencies definition builder. Contains a dependencies definition
 * and a RowPersister that can persist that tables defined correctly.
 */
public class DependenciesContext {
    private final GenericDependenciesDefinition dependenciesDefinition;
    private final RowPersister rowPersister;

    public DependenciesContext(GenericDependenciesDefinition dependenciesDefinition, RowPersister rowPersister) {
        this.dependenciesDefinition = dependenciesDefinition;
        this.rowPersister = rowPersister;
    }

    public GenericDependenciesDefinition getDependenciesDefinition() {
        return dependenciesDefinition;
    }

    public RowPersister getRowPersister() {
        return rowPersister;
    }
}
