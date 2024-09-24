package com.huskycode.jpaquery.persister.entitycreator;

import java.lang.reflect.Field;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
