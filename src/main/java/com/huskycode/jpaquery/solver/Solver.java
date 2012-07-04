package com.huskycode.jpaquery.solver;

import java.util.List;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.types.tree.CreationTree;

/**
 * Solve for list of entity with values, given {@link DependenciesDefinition}
 * 
 * @author varokaspanusuwan
 */
public interface Solver {
	<E> List<CreationTree> solveFor(Class<E> entityClass, DependenciesDefinition deps);
}
