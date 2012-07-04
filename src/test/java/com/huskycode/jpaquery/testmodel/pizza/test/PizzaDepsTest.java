package com.huskycode.jpaquery.testmodel.pizza.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.huskycode.jpaquery.DependenciesDefinition;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

public class PizzaDepsTest {

	private PizzaDeps pizzaDeps;
	
	@Before
	public void before() {
		pizzaDeps = new PizzaDeps();
	}
	
	@Test
	public void testCreateDependenciesFromFieldSuccessfully() {
		DependenciesDefinition deps = pizzaDeps.getDepsUsingField();
		
		assertThat(deps.getLinks().length, is(6));
	}

}
