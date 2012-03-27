package com.huskycode.jpaquery;

import com.huskycode.jpaquery.random.RandomValuePopulator;
import com.huskycode.jpaquery.random.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.util.RandomizerImpl;

import javax.persistence.EntityManager;

/**
 * @author Varokas Panusuwan
 */
public class JPAQueryContext {
    private EntityManager entityManager;
    private RandomValuePopulator randomValuePopulator;
    private DependenciesDefinition dependenciesDefinition;

    private JPAQueryContext() {
    }

    /** Visible for Testing */
    static JPAQueryContext newInstance(EntityManager entityManager,
                                     DependenciesDefinition deps,
                                     RandomValuePopulator randomValuePopulator) {
        JPAQueryContext jpaContext = new JPAQueryContext();

        if(entityManager == null) {
            throw new IllegalArgumentException("Entity manager supplied cannot be null");
        }
        if(deps == null) {
            throw new IllegalArgumentException("Dependencies definition supplied cannot be null");
        }

        jpaContext.randomValuePopulator = randomValuePopulator;
        jpaContext.entityManager = entityManager;
        jpaContext.dependenciesDefinition = deps;

        return jpaContext;
    }

    
    public static JPAQueryContext newInstance(EntityManager entityManager, DependenciesDefinition deps) {
        return newInstance(entityManager,
                deps,
                new RandomValuePopulatorImpl(new RandomizerImpl()));
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public <E> void create(Class<E> entityClass) {
        try {

            E entityObject = entityClass.newInstance();
            randomValuePopulator.populateValue(entityObject);
            entityManager.persist(entityObject);

        } catch (InstantiationException e) {
            throw new EntityInstantiationException("Cannot create class: " + entityClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new EntityInstantiationException("Cannot create class: " + entityClass.getName(), e);
        }
    }

    public RandomValuePopulator getRandomValuePopulator() {
        return randomValuePopulator;
    }

    public DependenciesDefinition getDependenciesDefinition() {
        return dependenciesDefinition;
    }
}
