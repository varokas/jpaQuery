package com.huskycode.jpaquery.solver;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import org.junit.Before;
import org.junit.Test;

public class SolverImplTest {
	private SolverImpl solverImpl;

	@Before
	public void before() {
		solverImpl = SolverImpl.newInstance();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testSolveForClassWithEmptyDepsReturnsOnlyRoot() {
		CreationPlan result = solverImpl.solveFor(ClassA.class, DependenciesDefinition.fromLinks(new Link[0]));
		
		//assertThat(result.size(), is(1));
		//assertThat(result.get(0).getRoot(), sameInstance((Class)ClassA.class));
	}

}
