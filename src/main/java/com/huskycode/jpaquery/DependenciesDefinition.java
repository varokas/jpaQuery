package com.huskycode.jpaquery;

import static com.huskycode.jpaquery.util.MapUtil.getOrCreateList;
import static com.huskycode.jpaquery.util.MapUtil.getOrCreateMap;
import static com.huskycode.jpaquery.util.MapUtil.getOrCreateSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.util.Factory;
import com.huskycode.jpaquery.util.ListFactory;
import com.huskycode.jpaquery.util.SetFactory;

/**
 * Define dependencies between entity class
 */
public class DependenciesDefinition {
    private final Link<?,?,?>[] links;
    private final Map<Class<?>, List<Link<?,?,?>>> entityDirectLinkDependencyMap;
    private final Map<Class<?>, Set<Class<?>>> entityDirectParentEntityDependencyMap;
    private final Map<Class<?>, Set<Class<?>>> entityDirectChildEntityDependencyMap;
    private final Map<Class<?>, Set<Class<?>>> entityAllParentEntityDependencyMap;
    private final Map<Class<?>, Set<Class<?>>> entityAllChildEntityDependencyMap;
    private final Map<Class<?>, Map<Class<?>, List<Link<?,?,?>>>> childFieldToParentMap;
	private final Set<Class<?>> enumTables;
	private final Set<Class<?>> triggeredTables;

	/** Uses DepBuilder to create this class. */
    DependenciesDefinition(final Link<?,?,?>[] links, final List<Class<?>> enumTables, final List<Class<?>> triggeredTables) {
        this.entityDirectLinkDependencyMap = new HashMap<Class<?>, List<Link<?, ?, ?>>>();
        this.entityDirectParentEntityDependencyMap = new HashMap<Class<?>, Set<Class<?>>>();
        this.entityDirectChildEntityDependencyMap = new HashMap<Class<?>, Set<Class<?>>>();
        this.entityAllParentEntityDependencyMap = new HashMap<Class<?>, Set<Class<?>>>();
        this.entityAllChildEntityDependencyMap = new HashMap<Class<?>, Set<Class<?>>>();
        this.childFieldToParentMap = new HashMap<Class<?>, Map<Class<?>,List<Link<?,?,?>>>>();
        for (Link<?,?,?> link : links) {
            Class<?> eFrom = link.getFrom().getEntityClass();
            Class<?> eTo = link.getTo().getEntityClass();
            getOrCreateList(entityDirectLinkDependencyMap, eFrom).add(link);
            getOrCreateSet(entityDirectParentEntityDependencyMap, eFrom).add(eTo);
            getOrCreateSet(entityDirectChildEntityDependencyMap, eTo).add(eFrom);
            getOrCreateList(getOrCreateMap(childFieldToParentMap, eFrom), eTo).add(link);
        }
        this.links = links;
        this.enumTables = new HashSet<Class<?>>(enumTables);
        this.triggeredTables = new HashSet<Class<?>>(triggeredTables);
        buildAllDependentEntitiesMap();
    }

    private void buildAllDependentEntitiesMap() {
    	for (Class<?> key : this.entityDirectParentEntityDependencyMap.keySet()) {
    		Set<Class<?>> value = getAllParentDependentEntities(key);
    		this.entityAllParentEntityDependencyMap.put(key, value);
    	}

    	for (Entry<Class<?>, Set<Class<?>>> entry : this.entityAllParentEntityDependencyMap.entrySet()) {
    		Class<?> child = entry.getKey();
    		for (Class<?> parent : entry.getValue()) {
    			getOrCreateSet(this.entityAllChildEntityDependencyMap, parent).add(child);
    		}
    	}
    }

	private Set<Class<?>> getAllParentDependentEntities(final Class<?> entityClass) {
		Set<Class<?>> visited = new HashSet<Class<?>>();
		LinkedList<Class<?>> queue = new LinkedList<Class<?>>();
		queue.add(entityClass);
		while(queue.size() > 0) {
			Class<?> e = queue.removeFirst();
			if (!visited.contains(e)) {
				visited.add(e);
				queue.addAll(getDirectParentDependencyEntity(e));
			}
		}
		//remove itself
		visited.remove(entityClass);
		return visited;
	}

    public Link<?,?,?>[] getLinks() {
        return links;
    }

    /**
     * Return link to parent entity  (with PK)  that the given entity (with FK) refers to.
     * @param entityClass
     * @return
     */
    public <E> List<Link<?,?,?>>  getDirectDependency(final Class<E> entityClass) {
        if (this.entityDirectLinkDependencyMap.containsKey(entityClass)) {
        	return this.entityDirectLinkDependencyMap.get(entityClass);
        }

        return LIST_OF_LINK_FACTORY.newInstace();
    }

    public Set<Class<?>>  getDirectParentDependencyEntity(final Class<?> entityClass) {
    	if (this.entityDirectParentEntityDependencyMap.containsKey(entityClass)) {
        	return this.entityDirectParentEntityDependencyMap.get(entityClass);
        }

        return SET_OF_CLASS_FACTORY.newInstace();
    }

    public Set<Class<?>>  getDirectChildDependencyEntity(final Class<?> entityClass) {
    	if (this.entityDirectChildEntityDependencyMap.containsKey(entityClass)) {
        	return this.entityDirectChildEntityDependencyMap.get(entityClass);
        }

        return SET_OF_CLASS_FACTORY.newInstace();
    }

    public Set<Class<?>>  getAllParentDependencyEntity(final Class<?> entityClass) {
    	if (this.entityAllParentEntityDependencyMap.containsKey(entityClass)) {
        	return entityAllParentEntityDependencyMap.get(entityClass);
        }

        return SET_OF_CLASS_FACTORY.newInstace();
    }

    public Set<Class<?>>  getAllChildDependencyEntity(final Class<?> entityClass) {
    	if (this.entityAllChildEntityDependencyMap.containsKey(entityClass)) {
        	return entityAllChildEntityDependencyMap.get(entityClass);
        }

        return SET_OF_CLASS_FACTORY.newInstace();
    }

    public List<Link<?,?,?>> getDependencyLinks(final Class<?> from, final Class<?> to) {
    	Map<Class<?>, List<Link<?,?,?>>> parentLinkMap = this.childFieldToParentMap.get(from);
    	if (parentLinkMap != null && parentLinkMap.containsKey(to)) {
    		return parentLinkMap.get(to);
    	}

    	return LIST_OF_LINK_FACTORY.newInstace();
    }

    private static final Factory<List<Link<?,?,?>>> LIST_OF_LINK_FACTORY = ListFactory.getInstance();
    private static final Factory<Set<Class<?>>> SET_OF_CLASS_FACTORY = SetFactory.getInstance();

	public Set<Class<?>> getEnumTables() {
		return enumTables;
	}

	public Set<Class<?>> getTriggeredTables() {
	    return this.triggeredTables;
	}
}
