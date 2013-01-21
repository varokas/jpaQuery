package com.huskycode.jpaquery.types.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.persister.Persister;
import com.huskycode.jpaquery.solver.CommandNodesIndexResult;
import com.huskycode.jpaquery.util.MapUtil;

/**
 * An object representing results that has been persisted by the {@link Persister}. Every
 * entity in this tree will have a definite value that should already be
 * persisted in the database.
 *
 * @author Varokas Panusuwan
 */
public class PersistedResult {
	private final List<Object> persistedObjects;
	private final Map<Class<?>, List<Object>> classInstanceMap;
	private final Map<Class<?>, Map<Integer, Object>> commandNodeObjectIndexMap;

	private PersistedResult(final List<Object> persistedObjects) {
		this.persistedObjects = persistedObjects;
		classInstanceMap = new HashMap<Class<?>, List<Object>>();
		commandNodeObjectIndexMap = new HashMap<Class<?>, Map<Integer,Object>>();

	}

	private static void initialize(final PersistedResult result) {
		for(Object obj : result.persistedObjects) {
			MapUtil.getOrCreateList(result.classInstanceMap, obj.getClass()).add(obj);
		}
	}

	public static PersistedResult newInstance(final List<Object> persistedObjects) {
		PersistedResult tree = new PersistedResult(persistedObjects);
		initialize(tree);
		return tree;
	}

	public List<Object> getPersistedObjects() {
		return persistedObjects;
	}

	@SuppressWarnings("unchecked")
	public <E> List<E> getForClass(final Class<E> clazz) {
		return (List<E>) this.classInstanceMap.get(clazz);
	}

	@SuppressWarnings("unchecked")
    public <E> E getForClassByCommandIndex(final Class<E> clazz, final int index) {
	    Map<Integer, Object> indexObjectMap = commandNodeObjectIndexMap.get(clazz);
	    if (indexObjectMap == null) {
	        throw new RuntimeException("Class " + clazz + " does not get created with the given commands");
	    }

	    Object obj = indexObjectMap.get(index);
	    if (obj == null) {
	        throw new ArrayIndexOutOfBoundsException("Index " + index + " of class " + clazz);
	    }

	    return (E)obj;
    }

    public static PersistedResult newInstance(final List<Object> persistedObjects, final CommandNodesIndexResult commandIndexes,
            final Map<CommandNode, Object> commandObjectMap) {
        PersistedResult tree = new PersistedResult(persistedObjects);
        initialize(tree);
        initializeIndexMap(tree, commandIndexes, commandObjectMap);
        return tree;
    }

    private static void initializeIndexMap(final PersistedResult tree, final CommandNodesIndexResult commandIndexes,
            final Map<CommandNode, Object> commandObjectMap) {
        for (Entry<CommandNode, Object> commandObject : commandObjectMap.entrySet()) {
            CommandNode command = commandObject.getKey();
            int index = commandIndexes.getIndexOf(command);
            MapUtil.getOrCreateMap(tree.commandNodeObjectIndexMap, command.getEntity()).put(index, commandObject.getValue());
        }
    }
}
