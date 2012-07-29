package com.huskycode.jpaquery.persister.entitycreator;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * @author varokas
 */
public class EntityPersisterFactoryImpl implements EntityPersisterFactory {
	@Override
	public EntityPersister createEntityPersister(EntityNode entityNode,
			DependenciesDefinition deps, EntityManager em) {
		Class<?> entityClass = entityNode.getEntityClass();
		if(deps.getEnumTables().contains(entityClass)) {
			if(entityClass.isEnum()) {
				return new EnumClassEntityPersister();
			}
			else {
				return new EnumTableEntityPersister(em);
			}
		}
		else {	
			return new NewRowEntityPersister(em);
		}
	}
}
