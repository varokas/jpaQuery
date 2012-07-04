package com.huskycode.jpaquery.solver;

import java.util.Arrays;
import java.util.List;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.types.tree.CreationTree;
public class SolverImpl implements Solver {
	private SolverImpl() {
		
	}
	
	public static SolverImpl newInstance() { 
		return new SolverImpl();
	}

	@Override
	public <E> List<CreationTree> solveFor(Class<E> entityClass,
			DependenciesDefinition deps) {
		CreationTree creationTree = CreationTree.newInstance(entityClass);
		
		return Arrays.asList(creationTree);
	}

}
