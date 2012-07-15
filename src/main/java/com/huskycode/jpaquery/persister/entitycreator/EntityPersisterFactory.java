package com.huskycode.jpaquery.persister.entitycreator;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * @author Varokas Panusuwan
 */
public interface EntityPersisterFactory {
	public EntityPersister createEntityPersister(EntityNode entityNode, DependenciesDefinition deps, EntityManager em);
}
