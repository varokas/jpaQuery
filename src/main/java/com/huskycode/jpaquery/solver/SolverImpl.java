package com.huskycode.jpaquery.solver;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.types.tree.CreationPlan;

public class SolverImpl implements Solver {
	private SolverImpl() {
		
	}
	
	public static SolverImpl newInstance() { 
		return new SolverImpl();
	}

	@Override
	public <E> CreationPlan solveFor(Class<E> entityClass,
			DependenciesDefinition deps) {
        //CreationPlan plan = CreationPlan.newInstance(new ArrayList<Class<?>>(entityClass));
		
		return CreationPlan.newInstance(null);
	}

}
