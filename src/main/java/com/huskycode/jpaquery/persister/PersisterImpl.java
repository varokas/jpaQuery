package com.huskycode.jpaquery.persister;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.persister.store.PropogatedValueStore;
import com.huskycode.jpaquery.persister.util.BeanUtil;
import com.huskycode.jpaquery.populator.CannotSetValueException;
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
    private RandomValuePopulator randomValuePopulator;
    private DependenciesDefinition deps;
    
    private ValuesPopulator valuesPopulator = ValuesPopulatorImpl.getInstance();
    private CreationPlanTraverser creationPlanTraverser = new CreationPlanTraverser();
	
	private PersisterImpl(EntityManager em, DependenciesDefinition deps) {
		this.em = em;
		this.deps = deps;
	}
	
	//VisibleForTesting
	PersisterImpl(EntityManager em,
			RandomValuePopulator randomValuePopulator, 
			DependenciesDefinition deps) {
		this(em, deps);
		this.randomValuePopulator = randomValuePopulator;
	}
	
	public static PersisterImpl newInstance(EntityManager em, DependenciesDefinition deps) {
		PersisterImpl persisterImpl = new PersisterImpl(em, deps);
		persisterImpl.randomValuePopulator = new RandomValuePopulatorImpl();
		return persisterImpl;
	}

    @Override
    public PersistedResult persistValues(CreationPlan plan) {
        List<Object> objects = new ArrayList<Object>();

        PropogatedValueStore valueStore = new PropogatedValueStore();
        
        for (EntityNode node : creationPlanTraverser.getEntityNodes(plan)) {
        	Class<?> c = node.getEntityClass();
            
        	Object obj = createNodeInDatabase(valueStore, node, c);
            
            objects.add(obj);
            storeFieldValueToPopulate(obj, node, valueStore);
        }

        return PersistedResult.newInstance(objects);
    }

	private Object createNodeInDatabase(PropogatedValueStore valueStore,
			EntityNode node, Class<?> c) {
		Object obj = BeanUtil.newInstance(c);
		
		Map<Field, Object> valuesToPopulate = getValuesToOverride(
				valueStore, node, c);
		
		randomValuePopulator.populateValue(obj);
		valuesPopulator.populateValue(obj, valuesToPopulate);
		
		em.persist(obj);
		return obj;
	}

	private Map<Field, Object> getValuesToOverride(
			PropogatedValueStore valueStore, EntityNode node, Class<?> c) {
		Field idField = BeanUtil.findIdField(c);

		Map<Field, Object> valuesToPopulate = valueStore.get(node);
		if(idField != null) {
			valuesToPopulate.put(idField, null); //Reset id field to null for JPA to autogen id.
		}
		return valuesToPopulate;
	}
    
    private void storeFieldValueToPopulate(Object obj, EntityNode parent,
			PropogatedValueStore valueStore) {
		for (EntityNode child : parent.getChilds()) {
			List<Link<?,?,?>> links = deps.getDependencyLinks(child.getEntityClass(), parent.getEntityClass());
			for (Link<?,?,?> link : links) {
				Field parentField = link.getTo().getField();
				valueStore.putValue(child, link.getFrom().getField(), BeanUtil.getValue(obj, parentField));
			}
		}
	}
}
