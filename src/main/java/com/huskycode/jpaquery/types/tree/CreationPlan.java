package com.huskycode.jpaquery.types.tree;

import com.huskycode.jpaquery.solver.Solver;

/**
 * A tree of object to be created along with confined values. The missing
 * link between entities should already been solved by the {@link Solver}
 *
 * @author Varokas Panusuwan
 */
public class CreationPlan {
	private ActionGraph actionGraph;

	public static CreationPlan newInstance(final ActionGraph actionGraph) {
		CreationPlan plan = new CreationPlan();
		plan.actionGraph = actionGraph;
		return plan;
	}

	public ActionGraph getActionGraph() {
		return this.actionGraph;
	}
}
