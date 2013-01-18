package com.huskycode.jpaquery.persister.entitycreator;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * @author varokas
 */
public class EntityPersisterFactoryImpl implements EntityPersisterFactory {

    private final RandomValuePopulator randomValuePopulator;

    public EntityPersisterFactoryImpl(final RandomValuePopulator randomValuePopulator) {
        this.randomValuePopulator = randomValuePopulator;
    }

    public EntityPersisterFactoryImpl() {
        this(new RandomValuePopulatorImpl());
    }

    @Override
    public EntityPersister createEntityPersister(final EntityNode entityNode, final DependenciesDefinition deps,
            final EntityManager em) {
        Class<?> entityClass = entityNode.getEntityClass();

        if (entityClass.isEnum()) {
            return new EnumClassEntityPersister();
        } else if (deps.getEnumTables().contains(entityClass)) {
            return new EnumTableEntityPersister(em);
        } else {
            return new NewRowEntityPersister(em, randomValuePopulator);
        }
    }
}
