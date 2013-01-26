package com.huskycode.jpaquery.persister.entitycreator;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
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
        } else if (deps.getTriggeredTables().contains(entityClass)) {
            List<Link<?,?,?>> allDirectLinks = getAllDirectLinksFrom(entityClass, deps);
            return new TriggeredTableEntityPersister(em, allDirectLinks);
        } else {
            return new NewRowEntityPersister(em, randomValuePopulator);
        }
    }

    private List<Link<?,?,?>> getAllDirectLinksFrom(final Class<?> from, final DependenciesDefinition deps) {
        List<Link<?,?,?>> allLinks = new ArrayList<Link<?,?,?>>();
        for (Class<?> parent : deps.getDirectParentDependencyEntity(from)) {
            allLinks.addAll(deps.getDependencyLinks(from, parent));
        }
        return allLinks;
    }
}
