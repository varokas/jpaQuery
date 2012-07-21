package com.huskycode.jpaquery.solver;

import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.types.tree.CreationPlan;

/**
 * Solve for list of entity with values, given {@link DependenciesDefinition}
 * 
 * @author varokaspanusuwan
 */
public interface Solver {
	@Deprecated
	<E> CreationPlan solveFor(Class<E> entityClass);
	<E> CreationPlan solveFor(CommandNodes commands);
}
