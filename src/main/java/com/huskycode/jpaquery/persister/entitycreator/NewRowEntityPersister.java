package com.huskycode.jpaquery.persister.entitycreator;

import java.lang.reflect.Field;
import java.util.Map;

import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * This is a persister that will persist 
 * an entity as a normal database row.
 * 
 * @author varokas
 */
public class NewRowEntityPersister implements EntityPersister {

	@Override
	public void persistNode(EntityNode node, Map<Field, Object> overrideFields) {
		// TODO Auto-generated method stub
		
	}

}
