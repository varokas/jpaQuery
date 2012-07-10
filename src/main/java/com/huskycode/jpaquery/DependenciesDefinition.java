package com.huskycode.jpaquery;

import com.huskycode.jpaquery.link.Link;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Define dependencies between entity class
 */
public class DependenciesDefinition {
    private final Link<?,?,?>[] links;
    private final Map<Class<?>, List<Link<?,?,?>>> entityDirectLinkDependencyMap;
    private final Map<Class<?>, Set<Class<?>>> entityDirectEntityDependencyMap;
    private final Map<Class<?>, Set<Class<?>>> entityAllEntityDependencyMap;
    private Map<Class<?>, Map<Class<?>, List<Link<?,?,?>>>> childFieldToParentMap;

    private DependenciesDefinition(Link<?,?,?>[] links) {
        this.entityDirectLinkDependencyMap = new HashMap<Class<?>, List<Link<?, ?, ?>>>();
        this.entityDirectEntityDependencyMap = new HashMap<Class<?>, Set<Class<?>>>();
        this.entityAllEntityDependencyMap = new HashMap<Class<?>, Set<Class<?>>>();
        this.childFieldToParentMap = new HashMap<Class<?>, Map<Class<?>,List<Link<?,?,?>>>>();
        for (Link<?,?,?> link : links) {
            Class<?> eFrom = link.getFrom().getEntityClass();
            Class<?> eTo = link.getTo().getEntityClass();
            getOrCreate(entityDirectLinkDependencyMap, eFrom, ListOfLinkFactory.INSTANCE).add(link);
            getOrCreate(entityDirectEntityDependencyMap, eFrom, SetOfClassFactory.INSTANCE).add(eTo);
            getOrCreate(getOrCreate(childFieldToParentMap, eFrom, MapOfClassLinkListFactory.INSTANCE),
            				eTo, ListOfLinkFactory.INSTANCE).add(link);
            				
       
        }
        this.links = links;
        buildAllDependentEntitiesMap();
    }

    private void buildAllDependentEntitiesMap() {
    	for (Class<?> key : this.entityDirectEntityDependencyMap.keySet()) {
    		Set<Class<?>> value = getAllParentDependentEntities(key);
    		this.entityAllEntityDependencyMap.put(key, value);
    	}
    }

	private Set<Class<?>> getAllParentDependentEntities(Class<?> entityClass) {
		Set<Class<?>> visited = new HashSet<Class<?>>();
		LinkedList<Class<?>> queue = new LinkedList<Class<?>>();
		queue.add(entityClass);
		while(queue.size() > 0) {
			Class<?> e = queue.removeFirst();
			if (!visited.contains(e)) {
				visited.add(e);
				queue.addAll(getDirectDependencyEntity(e));
			}
		}
		//remove itself
		visited.remove(entityClass);
		return visited;
	}
    
    private <T> T getOrCreate(Map<Class<?>, T> map, Class<?> key, ValueContainerFactory<T> factory) {
    	T value = map.get(key);
    	if (value == null) {
    		value = factory.newInstance();
    		map.put(key, value);
    	}
    	return value;
    }

    public static DependenciesDefinition fromLinks(Link<?,?,?>[] links) {
        DependenciesDefinition deps = new DependenciesDefinition(links);
        return deps;
    }

    public Link<?,?,?>[] getLinks() {
        return links;
    }
    
    /**
     * Return link to parent entity  (with PK)  that the given entity (with FK) refers to.
     * @param entityClass
     * @return
     */
    public <E> List<Link<?,?,?>>  getDirectDependency(Class<E> entityClass) {
        if (this.entityDirectLinkDependencyMap.containsKey(entityClass)) {
        	return this.entityDirectLinkDependencyMap.get(entityClass);
        }
        
        return ListOfLinkFactory.INSTANCE.newInstance();
    }
    
    public Set<Class<?>>  getDirectDependencyEntity(Class<?> entityClass) {
    	if (this.entityDirectEntityDependencyMap.containsKey(entityClass)) {
        	return this.entityDirectEntityDependencyMap.get(entityClass);
        }
        
        return SetOfClassFactory.INSTANCE.newInstance();
    }
    
    public Set<Class<?>>  getAllDependencyEntity(Class<?> entityClass) {
    	if (this.entityAllEntityDependencyMap.containsKey(entityClass)) {
        	return entityAllEntityDependencyMap.get(entityClass);
        }
        
        return SetOfClassFactory.INSTANCE.newInstance();
    }
    
    public List<Link<?,?,?>> getDependencyLinks(Class<?> from, Class<?> to) {
    	Map<Class<?>, List<Link<?,?,?>>> parentLinkMap = this.childFieldToParentMap.get(from);
    	if (parentLinkMap != null && parentLinkMap.containsKey(to)) {
    		return parentLinkMap.get(to);
    	}
    	
    	return new ArrayList<Link<?,?,?>>(0);
    }
    	
    
    private static class MapOfClassLinkListFactory implements ValueContainerFactory<Map<Class<?>, List<Link<?,?,?>>>> {

    	private static final MapOfClassLinkListFactory INSTANCE = new MapOfClassLinkListFactory();
    	
		@Override
		public Map<Class<?>, List<Link<?,?,?>>> newInstance() {
			return new HashMap<Class<?>, List<Link<?,?,?>>>();
		}
    	
    }
    
    private static class ListOfLinkFactory implements ValueContainerFactory<List<Link<?,?,?>>> {
    	
    	private static final ListOfLinkFactory INSTANCE = new ListOfLinkFactory();

		@Override
		public List<Link<?, ?, ?>> newInstance() {
			return new ArrayList<Link<?,?,?>>();
		}
    	
    }
    
    private static class SetOfClassFactory implements ValueContainerFactory<Set<Class<?>>> {
    	
    	private static final SetOfClassFactory INSTANCE = new SetOfClassFactory();
    	
		@Override
		public Set<Class<?>> newInstance() {
			return new HashSet<Class<?>>();
		}
    	
    }
    
    private interface ValueContainerFactory<T> {
    	T newInstance();
    }
    
}
