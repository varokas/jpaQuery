package com.huskycode.jpaquery.solver;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.types.tree.CreationPlan;

/**
 * Solve for list of entity with values, given {@link DependenciesDefinition}
 * 
 * @author varokaspanusuwan
 */
public interface Solver {
	<E> CreationPlan solveFor(Class<E> entityClass, DependenciesDefinition deps);
}
