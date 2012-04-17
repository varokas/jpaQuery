package com.huskycode.jpaquery.types.tree;

import java.util.List;

import com.huskycode.jpaquery.persister.Persister;

/**
 * A tree of objects that has been persisted by the {@link Persister}. Every 
 * entity in this tree will have a definite value that should already be 
 * persisted in the database.
 *
 * @author Varokas Panusuwan
 */
public class PersistedTree {
	List<Object> persistedObjects;
	
	private PersistedTree() {
		
	}
	
	public static PersistedTree newInstance(List<Object> persistedObjects) {
		PersistedTree tree = new PersistedTree();
		tree.persistedObjects = persistedObjects;
		return tree;
	}

	public List<Object> getPersistedObjects() {
		return persistedObjects;
	}
	
	
}
