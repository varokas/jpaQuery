package com.huskycode.jpaquery;

import com.huskycode.jpaquery.link.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Define dependencies between entity class
 */
public class DependenciesDefinition {
    private Link<?,?,?>[] links;
    private Map<Class<?>, List<Link<?,?,?>>> entityDependencyMap;

    private DependenciesDefinition(Link<?,?,?>[] links) {
        this.entityDependencyMap = new HashMap<Class<?>, List<Link<?, ?, ?>>>();
        for (Link<?,?,?> link : links) {
            Class<?> eFrom = link.getFrom().getEntityClass();
            List<Link<?,?,?>> eFromLinks = entityDependencyMap.get(eFrom);
            if (eFromLinks == null) {
                eFromLinks = new ArrayList<Link<?, ?, ?>>();
                entityDependencyMap.put(eFrom, eFromLinks);
            }
            eFromLinks.add(link);
        }
        this.links = links;
    }

    public static DependenciesDefinition fromLinks(Link<?,?,?>[] links) {
        DependenciesDefinition deps = new DependenciesDefinition(links);
        return deps;
    }

    public Link<?,?,?>[] getLinks() {
        return links;
    }
    
    public <E> List<Link<?,?,?>>  getDirectDependency(Class<E> entityClass) {
        if (entityDependencyMap.containsKey(entityClass)) {
        	return entityDependencyMap.get(entityClass);
        }
        
        return new LinkedList<Link<?,?,?>>();
    }
    
}
