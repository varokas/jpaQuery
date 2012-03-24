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

    private JPAQueryContext() {
    }

    /** Visible for Testing */
    static JPAQueryContext newInstance(EntityManager entityManager,
                                     RandomValuePopulator randomValuePopulator) {
        JPAQueryContext jpaContext = new JPAQueryContext();

        if(entityManager == null) {
            throw new IllegalArgumentException("Entity manager supplied cannot be null");
        }

        jpaContext.randomValuePopulator = randomValuePopulator;
        jpaContext.entityManager = entityManager;

        return jpaContext;
    }

    
    public static JPAQueryContext newInstance(EntityManager entityManager) {
        return newInstance(entityManager, new RandomValuePopulatorImpl(new RandomizerImpl()));
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
}
