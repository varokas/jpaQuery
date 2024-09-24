package com.huskycode.jpaquery.testmodel.pizza.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

public class PizzaDepsTest {

	private PizzaDeps pizzaDeps;
	
	@BeforeEach
	public void before() {
		pizzaDeps = new PizzaDeps();
	}
	
	@Test
	public void testCreateDependenciesFromFieldSuccessfully() {
		DependenciesDefinition deps = pizzaDeps.getDepsUsingField();
		
		assertThat(deps.getLinks().length, is(14));
	}

}
