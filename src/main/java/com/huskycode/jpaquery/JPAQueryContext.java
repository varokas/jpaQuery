package com.huskycode.jpaquery;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.persister.Persister;
import com.huskycode.jpaquery.persister.PersisterImpl;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.solver.Solver;
import com.huskycode.jpaquery.solver.SolverImpl;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.PersistedResult;

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
                new RandomValuePopulatorImpl());
    }

    public <E> PersistedResult create(Class<E> entityClass) {
            Solver solver = SolverImpl.newInstance();
            CreationPlan creationPlan = solver.solveFor(entityClass, dependenciesDefinition);
            
            Persister persister = PersisterImpl.newInstance(entityManager, dependenciesDefinition);
            return persister.persistValues(creationPlan);
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public RandomValuePopulator getRandomValuePopulator() {
        return randomValuePopulator;
    }

    public DependenciesDefinition getDependenciesDefinition() {
        return dependenciesDefinition;
    }
}
