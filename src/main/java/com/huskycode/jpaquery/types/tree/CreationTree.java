package com.huskycode.jpaquery.types.tree;

import com.huskycode.jpaquery.solver.Solver;

/**
 * A tree of object to be created along with confined values. The missing 
 * link between entities should already been solved by the {@link Solver}
 * 
 * @author Varokas Panusuwan
 */
public class CreationTree {
	private Class root;
	
	private CreationTree() {
	}
	
	public static CreationTree newInstance(Class<?> root) {
		CreationTree tree = new CreationTree();
		tree.root = root;
		return tree;
	}

	public Class getRoot() {
		return root;
	}
}
