package com.huskycode.jpaquery.persister;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.PersistedResult;

/**
 * @author Varokas Panusuwan
 */
public class PersisterImpl implements Persister {
	private EntityManager em;
	private BeanCreator beanCreator;
    private RandomValuePopulator randomValuePopulator;
    private DependenciesDefinition deps;
	
	private PersisterImpl(EntityManager em, DependenciesDefinition deps) {
		this.em = em;
		this.deps = deps;
	}
	
	//VisibleForTesting
	PersisterImpl(EntityManager em, BeanCreator beanCreator,
			RandomValuePopulator randomValuePopulator, 
			DependenciesDefinition deps) {
		this(em, deps);
		this.beanCreator = beanCreator;
		this.randomValuePopulator = randomValuePopulator;
	}
	
	public static PersisterImpl newInstance(EntityManager em, DependenciesDefinition deps) {
		PersisterImpl persisterImpl = new PersisterImpl(em, deps);
		persisterImpl.beanCreator = new BeanCreator();
		persisterImpl.randomValuePopulator = new RandomValuePopulatorImpl();
		return persisterImpl;
	}

    @Override
    public PersistedResult persistValues(CreationPlan plan) {
        List<Object> objects = new ArrayList<Object>();
        
        //TODO: This won't work for multiple classes case for sure.
        Map<Class<?>, Object> persistedValueLookup = new HashMap<Class<?>, Object>();
        
        for(Class<?> c: plan.getClasses()) {
            Object obj = beanCreator.newInstance(c);
            randomValuePopulator.populateValue(obj);
            
            populateValueFromHierarchy(obj, c, persistedValueLookup);
            
            em.merge(obj);
            
            objects.add(obj);
            persistedValueLookup.put(c, obj);
        }

        return PersistedResult.newInstance(objects);
    }

	private void populateValueFromHierarchy(Object obj, Class<?> c,
		Map<Class<?>, Object> persistedValueLookup) {
	
		List<Link<?, ?, ?>> directDependencies = deps.getDirectDependency(c);
		for(Link<?, ?, ?> directDep : directDependencies) {
			Class<?> parentClass = directDep.getTo().getEntityClass();
			
			//This should always has value if resolver resolved in correct order
			Object parentObj = persistedValueLookup.get(parentClass);
			populateValue(parentObj, obj, directDep);
		}
	}
	
	private void populateValue(Object parentObj, Object childObj, Link<?,?,?> link) {
		Field parentField = link.getTo().getField();
		Field childField = link.getFrom().getField();
		
		try {
			Object theValue = parentField.get(parentObj);
			childField.set(childObj, theValue);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
