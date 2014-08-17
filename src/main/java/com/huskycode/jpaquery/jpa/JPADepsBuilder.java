package com.huskycode.jpaquery.jpa;

import com.huskycode.jpaquery.GenericDependenciesDefinition;
import com.huskycode.jpaquery.jpa.persister.JPARowPersister;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.types.db.Table;

import java.util.*;

public class JPADepsBuilder {

    private final List<Link<?,?,?>> links;
    private final List<Class<?>> enumTables;
    private final List<Class<?>> triggeredTables;

    public JPADepsBuilder() {
        links = new LinkedList<Link<?,?,?>>();
        enumTables = new LinkedList<Class<?>>();
        triggeredTables = new LinkedList<Class<?>>();
    }

    public JPADepsBuilder withLink(final Link<?,?,?> link) {
        links.add(link);
        return this;
    }

    public JPADepsBuilder withLinks(final Link<?, ?, ?>[] linkArray) {
        links.addAll(Arrays.asList(linkArray));
        return this;
    }

    public JPADepsBuilder withEnumTable(final Class<?> e) {
        enumTables.add(e);
        return this;
    }

    public JPADepsBuilder withEnumTables(final Class<?>[] es) {
        enumTables.addAll(Arrays.asList(es));
        return this;
    }

    public JPADepsBuilder withTriggeredTable(final Class<?> e) {
        triggeredTables.add(e);
        return this;
    }

    public JPADepsBuilder withTriggeredTables(final Class<?>[] es) {
        triggeredTables.addAll(Arrays.asList(es));
        return this;
    }

    public DependenciesContext build() {
        return new DependenciesContext(new GenericDependenciesDefinition(
                new ArrayList<com.huskycode.jpaquery.types.db.Link>(),
                new HashSet<Table>(),
                new HashSet<Table>()
        ), new JPARowPersister());
    }
}
