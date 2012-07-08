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
	private ActionGraph actionGraph;
	
	private CreationPlan() {}
	
	public static CreationPlan newInstance(List<Class<?>> classes) {
        CreationPlan plan = new CreationPlan();
        plan.classes = classes;
		return plan;
	}
	
	public static CreationPlan newInstance(ActionGraph actionGraph) {
		CreationPlan plan = new CreationPlan();
		plan.actionGraph = actionGraph;
		return plan;
	}

	public List<Class<?>> getClasses() {
		return this.classes;
	}
	
	public ActionGraph getActionGraph() {
		return this.actionGraph;
	}
}
