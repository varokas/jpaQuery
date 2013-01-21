package com.huskycode.jpaquery;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.crud.CreationPlanFromDefinition;
import com.huskycode.jpaquery.persister.Persister;
import com.huskycode.jpaquery.persister.PersisterImpl;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.solver.CommandNodesIndexBuilder;
import com.huskycode.jpaquery.solver.CommandNodesIndexBuilderImpl;
import com.huskycode.jpaquery.solver.CommandNodesIndexResult;
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
    private CommandNodesIndexBuilder indexBuilder;
    private JPAQueryContext() {
    }

    /** Visible for Testing */
    static JPAQueryContext newInstance(final EntityManager entityManager, final DependenciesDefinition deps,
            final RandomValuePopulator randomValuePopulator,
            final CommandNodesIndexBuilder indexBuilder) {
        JPAQueryContext jpaContext = new JPAQueryContext();

        if (entityManager == null) {
            throw new IllegalArgumentException("Entity manager supplied cannot be null");
        }
        if (deps == null) {
            throw new IllegalArgumentException("Dependencies definition supplied cannot be null");
        }

        jpaContext.randomValuePopulator = randomValuePopulator;
        jpaContext.entityManager = entityManager;
        jpaContext.dependenciesDefinition = deps;
        jpaContext.indexBuilder = indexBuilder;
        return jpaContext;
    }

    public static JPAQueryContext newInstance(final EntityManager entityManager, final DependenciesDefinition deps) {
        return newInstance(entityManager, deps, new RandomValuePopulatorImpl());
    }

    public static JPAQueryContext newInstance(final EntityManager entityManager,
                                                final DependenciesDefinition deps,
                                                final RandomValuePopulator randomValuePopulator) {
        return newInstance(entityManager, deps, randomValuePopulator, new CommandNodesIndexBuilderImpl());
    }

    public <E> PersistedResult create(final Class<E> entityClass) {
        Solver solver = SolverImpl.newInstance(dependenciesDefinition);
        CreationPlan creationPlan = solver.solveFor(entityClass);

        Persister persister = createPersister();
        return persister.persistValues(creationPlan);
    }

    public <E> PersistedResult create(final CommandNodes commands) {
        Solver solver = SolverImpl.newInstance(dependenciesDefinition);
        CreationPlan creationPlan = solver.solveFor(commands);
        CommandNodesIndexResult indexes = indexBuilder.build(commands);
        Persister persister = createPersister();
        return persister.persistValues(creationPlan, indexes);
    }

    public <E> PersistedResult createFromDependencyDefinition() {
        CreationPlanFromDefinition creator = CreationPlanFromDefinition.getInstance();
        Persister persister = createPersister();
        return persister.persistValues(creator.from(dependenciesDefinition));
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    private Persister createPersister() {
        return PersisterImpl.newInstance(entityManager, dependenciesDefinition, randomValuePopulator);
    }

    public RandomValuePopulator getRandomValuePopulator() {
        return randomValuePopulator;
    }

    public DependenciesDefinition getDependenciesDefinition() {
        return dependenciesDefinition;
    }
}
