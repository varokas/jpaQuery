package com.huskycode.integration;

import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

/**
 * Joshua Bloch : " a single-element enum type is the best way to implement a singleton."
 *
 * @author Varokas Panusuwan
 */
public enum TestEntityManager {
    INSTANCE;

    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        entityManager = Persistence.createEntityManagerFactory("testPersistentUnit").createEntityManager();
        new PizzaDeps().populateInitialData(entityManager);

        return entityManager;
    }
}
