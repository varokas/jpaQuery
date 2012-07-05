package com.huskycode.jpaquery.persister;

import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.PersistedResult;

import java.util.List;

/**
 * Persist an object hierarchy to database, propergating the 
 * right value down the creation tree.
 * @author varokaspanusuwan
 */
public interface Persister {
	PersistedResult persistValues(CreationPlan plan);
}
