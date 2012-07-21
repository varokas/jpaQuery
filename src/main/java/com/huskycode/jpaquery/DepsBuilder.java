package com.huskycode.jpaquery;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.huskycode.jpaquery.link.Link;

/**
 * A builder for DependenciesDefinition
 * @author varokas
 */
public class DepsBuilder {
	private final List<Link<?,?,?>> links;
	
	/** which classes are designated enums */
	private final List<Class<?>> enumTables;

	public DepsBuilder() {
		links = new LinkedList<Link<?,?,?>>();
		enumTables = new LinkedList<Class<?>>();
	}

	public DepsBuilder withLink(Link<?,?,?> link) {
		links.add(link);
		return this;
	}
	
	public DepsBuilder withLinks(Link<?, ?, ?>[] linkArray) {
		links.addAll(Arrays.asList(linkArray));
		return this;
	}
	
	public DepsBuilder withEnumTable(Class<?> e) {
		enumTables.add(e);
		return this;
	}
	
	public DepsBuilder withEnumTables(Class<?>[] es) {
		enumTables.addAll(Arrays.asList(es));
		return this;
	}

	public DependenciesDefinition build() {
		return new DependenciesDefinition(
				links.toArray(new Link<?,?,?>[0]),
				enumTables
				);
	}


	
	
}
