package com.huskycode.jpaquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.types.db.Table;
import com.huskycode.jpaquery.types.db.factory.TableFactory;

/**
 * A builder for DependenciesDefinition
 * @author varokas
 */
public class DepsBuilder {
    private final TableFactory tableFactory = new TableFactory();

	private final List<Link<?,?,?>> links;

	/** which classes are designated enums */
	private final List<Class<?>> enumTables;

	private final List<Class<?>> triggeredTables;

	public DepsBuilder() {
		links = new LinkedList<Link<?,?,?>>();
		enumTables = new LinkedList<Class<?>>();
		triggeredTables = new LinkedList<Class<?>>();
	}

	public DepsBuilder withLink(final Link<?,?,?> link) {
		links.add(link);
		return this;
	}

	public DepsBuilder withLinks(final Link<?, ?, ?>[] linkArray) {
		links.addAll(Arrays.asList(linkArray));
		return this;
	}

	public DepsBuilder withEnumTable(final Class<?> e) {
		enumTables.add(e);
		return this;
	}

	public DepsBuilder withEnumTables(final Class<?>[] es) {
		enumTables.addAll(Arrays.asList(es));
		return this;
	}

	public DepsBuilder withTriggeredTable(final Class<?> e) {
	    triggeredTables.add(e);
        return this;
    }

    public DepsBuilder withTriggeredTables(final Class<?>[] es) {
        triggeredTables.addAll(Arrays.asList(es));
        return this;
    }

	public DependenciesDefinition build() {
		return new DependenciesDefinition(
				links.toArray(new Link<?,?,?>[0]),
				convertToTables(enumTables),
                convertToTables(triggeredTables)
				);
	}

    private List<Table> convertToTables(List<Class<?>> jpaEntityClasses) {
        List<Table> tables = new ArrayList<Table>();
        for(Class<?> enumTable : jpaEntityClasses) {
            tables.add(tableFactory.createFromJPAEntity(enumTable));
        }
        return tables;
    }


}
