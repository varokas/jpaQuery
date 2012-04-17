package com.huskycode.jpaquery;

import java.util.List;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.persister.EntityInstantiationException;
import com.huskycode.jpaquery.persister.Persister;
import com.huskycode.jpaquery.persister.PersisterImpl;
import com.huskycode.jpaquery.random.RandomValuePopulator;
import com.huskycode.jpaquery.random.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.solver.Solver;
import com.huskycode.jpaquery.solver.SolverImpl;
import com.huskycode.jpaquery.types.tree.CreationTree;
import com.huskycode.jpaquery.types.tree.PersistedTree;

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

    public <E> E create(Class<E> entityClass) {
            Solver solver = SolverImpl.newInstance();
            List<CreationTree> creationTrees = solver.solveFor(entityClass, DependenciesDefinition.fromLinks(new Link[0]));
            
            Persister persister = PersisterImpl.newInstance(entityManager);
            List<PersistedTree> persistedTrees = persister.persistValues(creationTrees);

            return (E)persistedTrees.get(0);
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
