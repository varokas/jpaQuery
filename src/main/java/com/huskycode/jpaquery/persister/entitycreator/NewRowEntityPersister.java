package com.huskycode.jpaquery.persister.entitycreator;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.persister.store.PropogatedValueStore;
import com.huskycode.jpaquery.persister.util.BeanUtil;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.populator.ValuesPopulator;
import com.huskycode.jpaquery.populator.ValuesPopulatorImpl;
import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * This is a persister that will persist 
 * an entity as a normal database row.
 * 
 * @author varokas
 */
public class NewRowEntityPersister implements EntityPersister {

    private RandomValuePopulator randomValuePopulator = new RandomValuePopulatorImpl();
    private ValuesPopulator valuesPopulator = ValuesPopulatorImpl.getInstance();
	private final EntityManager em;
    
    NewRowEntityPersister(final EntityManager em) {
    	this.em = em;
	}
    
	@Override
	public Object persistNode(EntityNode node, Map<Field, Object> overrideFields) {
		return createNodeInDatabase(overrideFields, node);
	}

	private Object createNodeInDatabase(Map<Field, Object> overrideFields,
			EntityNode node) {
		Class<?> c = node.getEntityClass();
		Object obj = BeanUtil.newInstance(c);
		
		Map<Field, Object> valuesToPopulate = getValuesToOverride(
				overrideFields, node, c);
		
		randomValuePopulator.populateValue(obj);
		valuesPopulator.populateValue(obj, valuesToPopulate);
		
		em.persist(obj);
		return obj;
	}

	private Map<Field, Object> getValuesToOverride(
			Map<Field, Object> valuesToPopulate, EntityNode node, Class<?> c) {
		Field idField = BeanUtil.findIdField(c);

		if(idField != null) {
			valuesToPopulate.put(idField, null); //Reset id field to null for JPA to autogen id.
		}
		return valuesToPopulate;
	}
}
