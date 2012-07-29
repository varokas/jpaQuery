package com.huskycode.jpaquery.types.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huskycode.jpaquery.persister.Persister;
import com.huskycode.jpaquery.util.MapUtil;

/**
 * An object representing results that has been persisted by the {@link Persister}. Every 
 * entity in this tree will have a definite value that should already be 
 * persisted in the database.
 *
 * @author Varokas Panusuwan
 */
public class PersistedResult {
	private List<Object> persistedObjects;
	private Map<Class<?>, List<Object>> classInstanceMap;
	
	private PersistedResult(List<Object> persistedObjects) {
		this.persistedObjects = persistedObjects;
		classInstanceMap = new HashMap<Class<?>, List<Object>>();
		initialize();
	}
	
	private void initialize() {
		for(Object obj : persistedObjects) {
			MapUtil.getOrCreateList(classInstanceMap, obj.getClass()).add(obj);
		}
	}
	
	public static PersistedResult newInstance(List<Object> persistedObjects) {
		PersistedResult tree = new PersistedResult(persistedObjects);
		return tree;
	}

	public List<Object> getPersistedObjects() {
		return persistedObjects;
	}
	
	@SuppressWarnings("unchecked")
	public <E> List<E> getForClass(Class<E> clazz) {
		return (List<E>) this.classInstanceMap.get(clazz);
	}
}
