package com.huskycode.jpaquery.persister;

import java.util.List;

import com.huskycode.jpaquery.types.tree.CreationTree;
import com.huskycode.jpaquery.types.tree.PersistedTree;

/**
 * Persist an object hierarchy to database, propergating the 
 * right value down the creation tree.
 * @author varokaspanusuwan
 */
public interface Persister {
	List<PersistedTree> persistValues(List<CreationTree> creationTree);
}
