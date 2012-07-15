package com.huskycode.jpaquery.persister.entitycreator;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * @author varokas
 */
public class EntityPersisterFactoryImpl implements EntityPersisterFactory {
	@Override
	public EntityPersister createEntityPersister(EntityNode entityNode,
			DependenciesDefinition deps) {
		return new NewRowEntityPersister();
	}
}
