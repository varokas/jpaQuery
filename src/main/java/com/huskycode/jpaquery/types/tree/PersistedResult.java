package com.huskycode.jpaquery.types.tree;

import java.util.List;

import com.huskycode.jpaquery.persister.Persister;

/**
 * An object representing results that has been persisted by the {@link Persister}. Every 
 * entity in this tree will have a definite value that should already be 
 * persisted in the database.
 *
 * @author Varokas Panusuwan
 */
public class PersistedResult {
	List<Object> persistedObjects;
	
	private PersistedResult() {
		
	}
	
	public static PersistedResult newInstance(List<Object> persistedObjects) {
		PersistedResult tree = new PersistedResult();
		tree.persistedObjects = persistedObjects;
		return tree;
	}

	public List<Object> getPersistedObjects() {
		return persistedObjects;
	}
	
	
}
