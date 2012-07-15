package com.huskycode.jpaquery.persister.entitycreator;

import java.lang.reflect.Field;
import java.util.Map;

import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * Create entity and persist to database.
 *
 * @author varokas
 */
public interface EntityPersister {
	/**
	 * Persist the node type, given the fields to override.
	 * @param node
	 * @param overrideFields
	 * @return 
	 */
	public Object persistNode(EntityNode node, Map<Field, Object> overrideFields);
}
