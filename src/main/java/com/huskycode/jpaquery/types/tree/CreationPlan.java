package com.huskycode.jpaquery.types.tree;

import com.huskycode.jpaquery.solver.Solver;

import java.util.List;

/**
 * A tree of object to be created along with confined values. The missing 
 * link between entities should already been solved by the {@link Solver}
 * 
 * @author Varokas Panusuwan
 */
public class CreationPlan {
	private List<Class<?>> classes;
	
	private CreationPlan(List<Class<?>> classes) {
        this.classes = classes;
	}
	
	public static CreationPlan newInstance(List<Class<?>> classes) {
        CreationPlan tree = new CreationPlan(classes);
		return tree;
	}

	public List<Class<?>> getClasses() {
		return this.classes;
	}
}
