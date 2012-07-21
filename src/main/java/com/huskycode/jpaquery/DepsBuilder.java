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

	public void withLink(Link<?,?,?> link) {
		links.add(link);
	}
	
	public void withLinks(Link<?, ?, ?>[] linkArray) {
		links.addAll(Arrays.asList(linkArray));
	}
	
	public void withEnumTable(Class<?> e) {
		enumTables.add(e);
	}
	
	public void withEnumTables(Class<?>[] es) {
		enumTables.addAll(Arrays.asList(es));
	}

	public DependenciesDefinition build() {
		return new DependenciesDefinition(
				links.toArray(new Link<?,?,?>[0]),
				enumTables
				);
	}


	
	
}
