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
import com.huskycode.jpaquery.populator.ValuesPopulator;
import com.huskycode.jpaquery.populator.ValuesPopulatorImpl;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.EntityNode;
import com.huskycode.jpaquery.types.tree.PersistedResult;

/**
 * @author Varokas Panusuwan
 */
public class PersisterImpl implements Persister {
	private EntityManager em;
    private DependenciesDefinition deps;
    
    private CreationPlanTraverser creationPlanTraverser = new CreationPlanTraverser();
    private EntityPersisterFactory entityPersisterFactory = new EntityPersisterFactoryImpl();
	
    @VisibleForTesting
	PersisterImpl(EntityManager em, DependenciesDefinition deps) {
		this.em = em;
		this.deps = deps;
	}
	
	
	public static PersisterImpl newInstance(EntityManager em, DependenciesDefinition deps) {
		PersisterImpl persisterImpl = new PersisterImpl(em, deps);
		return persisterImpl;
	}

    @Override
    public PersistedResult persistValues(CreationPlan plan) {
        List<Object> objects = new ArrayList<Object>();

        PropogatedValueStore<EntityNode, Field, Object> valueStore = PropogatedValueStore.newInstance();
        
        for (EntityNode node : creationPlanTraverser.getEntityNodes(plan)) {
        	Map<Field, Object> overrideFields = getOverrideFields(node, valueStore);
        	Object obj = entityPersisterFactory
        			.createEntityPersister(node, deps, em)
        			.persistNode(node, overrideFields);
            
            objects.add(obj);
            storeFieldValueToPopulate(obj, node, valueStore);
        }

        return PersistedResult.newInstance(objects);
    }

    private Map<Field, Object> getOverrideFields(EntityNode node, 
    						PropogatedValueStore<EntityNode, Field, Object> valueStore) {
    	Map<Field, Object> overrideFields = new HashMap<Field, Object>();
    	if (node.getCommand() != null) {
    		overrideFields.putAll(node.getCommand().getFieldValues());
    	}
    	overrideFields.putAll(valueStore.get(node));
    	return overrideFields;
    }
    
    private void storeFieldValueToPopulate(Object obj, EntityNode parent,
    		PropogatedValueStore<EntityNode, Field, Object> valueStore) {
		for (EntityNode child : parent.getChilds()) {
			List<Link<?,?,?>> links = deps.getDependencyLinks(child.getEntityClass(), parent.getEntityClass());
			for (Link<?,?,?> link : links) {
				Field parentField = link.getTo().getField();
				valueStore.putValue(child, link.getFrom().getField(), BeanUtil.getValue(obj, parentField));
			}
		}
	}
}
