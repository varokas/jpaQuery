package com.huskycode.jpaquery.persister.entitycreator;

import java.lang.reflect.Field;
import java.util.Map;

import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * This is a persister that works on a java enum that 
 * would be a provider of the value.
 * 
 * @author varokas
 */
public class EnumClassEntityPersister implements EntityPersister {

	@Override
	public Object persistNode(EntityNode node, Map<Field, Object> overrideFields) {
		// Nobody should try to override enum fields, so we will ignore it.
		
		//will not be null because the factory should choose this
		//only when entityClass is an enum
		return node.getEntityClass().getEnumConstants()[0];
	}

}
