package com.huskycode.jpaquery.persister.entitycreator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.huskycode.jpaquery.persister.store.PropogatedValueStore;
import com.huskycode.jpaquery.persister.util.BeanUtil;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.populator.ValuesPopulator;
import com.huskycode.jpaquery.populator.ValuesPopulatorImpl;
import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * This is a persister that works on a table that acts as enum. 
 * We are not going to persist anything to the database, but 
 * we will get this first row of that enum to use.
 * 
 * @author varokas
 */
public class EnumTableEntityPersister implements EntityPersister {
	private final EntityManager em;
    
    public EnumTableEntityPersister(final EntityManager em) {
    	this.em = em;
	}
    
	@Override
	public Object persistNode(EntityNode node, Map<Field, Object> overrideFields) {
		//Ignores the override for now
		
		return findFirstRowOf(node.getEntityClass());
	}
	
	private Object findFirstRowOf(Class<?> entityClass) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<?> criteriaQuery = cb.createQuery(entityClass);
		Root root = criteriaQuery.from(entityClass);
		CriteriaQuery<?> cq = criteriaQuery.select(root);
		TypedQuery<?> query = em.createQuery(cq);
		
		return query.getResultList().get(0);
	}
}
