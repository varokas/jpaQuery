package com.huskycode.jpaquery.persister;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.annotation.VisibleForTesting;
import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.persister.entitycreator.EntityPersisterFactory;
import com.huskycode.jpaquery.persister.entitycreator.EntityPersisterFactoryImpl;
import com.huskycode.jpaquery.persister.store.PropogatedValueStore;
import com.huskycode.jpaquery.persister.util.BeanUtil;
import com.huskycode.jpaquery.populator.CreationPlanTraverser;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.solver.CommandNodesIndexResult;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;
import com.huskycode.jpaquery.types.tree.PersistedResult;

/**
 * @author Varokas Panusuwan
 */
public class PersisterImpl implements Persister {
    private final EntityManager em;
    private final DependenciesDefinition deps;

    private final CreationPlanTraverser creationPlanTraverser = new CreationPlanTraverser();
    private final EntityPersisterFactory entityPersisterFactory;

    @VisibleForTesting
    PersisterImpl(final EntityManager em, final DependenciesDefinition deps) {
        this(em, deps, new RandomValuePopulatorImpl());
    }

    PersisterImpl(final EntityManager em, final DependenciesDefinition deps,
            final RandomValuePopulator randomValuePopulator) {
        this.em = em;
        this.deps = deps;
        this.entityPersisterFactory = new EntityPersisterFactoryImpl(randomValuePopulator);
    }

    public static PersisterImpl newInstance(final EntityManager em, final DependenciesDefinition deps) {
        PersisterImpl persisterImpl = new PersisterImpl(em, deps);
        return persisterImpl;
    }

    public static PersisterImpl newInstance(final EntityManager em, final DependenciesDefinition deps,
            final RandomValuePopulator randomValuePopulator) {
        PersisterImpl persisterImpl = new PersisterImpl(em, deps, randomValuePopulator);
        return persisterImpl;
    }

    @Override
    public PersistedResult persistValues(final CreationPlan plan) {
        return persistValues(plan, null);
    }


    @Override
    public PersistedResult persistValues(final CreationPlan plan, final CommandNodesIndexResult commandIndexes) {
        List<Object> objects = new ArrayList<Object>();

        PropogatedValueStore<EntityNode, Field, Object> valueStore = PropogatedValueStore.newInstance();
        Map<CommandNode, Object> commandObjectMap = new HashMap<CommandNode, Object>();
        for (EntityNode node : creationPlanTraverser.getEntityNodes(plan)) {
            Map<Field, Object> overrideFields = getOverrideFields(node, valueStore);
            Object obj = entityPersisterFactory.createEntityPersister(node, deps, em).persistNode(node, overrideFields);

            objects.add(obj);
            storeFieldValueToPopulate(obj, node, valueStore);

            if (node.getCommand() != null) {
                commandObjectMap.put(node.getCommand(), obj);
            }
        }

        if (commandIndexes != null) {
            return PersistedResult.newInstance(objects, commandIndexes, commandObjectMap);
        } else {
            return PersistedResult.newInstance(objects);
        }
    }

    private Map<Field, Object> getOverrideFields(final EntityNode node,
            final PropogatedValueStore<EntityNode, Field, Object> valueStore) {
        Map<Field, Object> overrideFields = new HashMap<Field, Object>();
        if (node.getCommand() != null) {
            overrideFields.putAll(node.getCommand().getFieldValues());
        }
        overrideFields.putAll(valueStore.get(node));
        return overrideFields;
    }

    private void storeFieldValueToPopulate(final Object obj, final EntityNode parent,
            final PropogatedValueStore<EntityNode, Field, Object> valueStore) {
        for (EntityNode child : parent.getChilds()) {
            List<Link<?, ?, ?>> links = deps.getDependencyLinks(child.getEntityClass(), parent.getEntityClass());
            for (Link<?, ?, ?> link : links) {
                Field parentField = link.getTo().getField();
                valueStore.putValue(child, link.getFrom().getField(), BeanUtil.getValue(obj, parentField));
            }
        }
    }
}
