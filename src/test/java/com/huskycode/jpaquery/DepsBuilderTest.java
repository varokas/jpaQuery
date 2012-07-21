package com.huskycode.jpaquery;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;

/**
 * @author varokas
 *
 */
public class DepsBuilderTest {
	private DepsBuilder depBuilder;
	
	private Link<?,?,?> aLink;
	private Class<?> anEnumTable;

	@SuppressWarnings("unchecked")
	@Before
	public void before() throws SecurityException, NoSuchFieldException { 
		depBuilder = new DepsBuilder();
		aLink = Link.from(Customer.class, Customer.class.getDeclaredField("customerAddressId"))
				.to(Address.class, Address.class.getDeclaredField("addressId"));
		anEnumTable = Address.class;
	}
	
	@Test
	public void testDepBuilderConstructDepsWithCorrectLink() {
		depBuilder.withLink(aLink);
		
		DependenciesDefinition deps = depBuilder.build();
		assertThat(deps.getLinks().length, is(1));
		assertEquals(aLink, deps.getLinks()[0]);
	}
	
	@Test
	public void testDepBuilderConstructDepsWithCorrectLinkArray() {
		Link<?, ?, ?>[] linkArray = new Link<?,?,?>[] {aLink};
		depBuilder.withLinks(linkArray);
		
		DependenciesDefinition deps = depBuilder.build();
		assertArrayEquals(linkArray, deps.getLinks());
	}
	
	@Test
	public void testDepBuilderConstructDepsWithCorrectEnum() {
		depBuilder.withEnumTable(anEnumTable);
		
		DependenciesDefinition deps = depBuilder.build();
		assertThat(deps.getEnumTables().size(), is(1));
		assertEquals(anEnumTable, deps.getEnumTables().iterator().next());
	}
	
	@Test
	public void testDepBuilderConstructDepsWithCorrectEnumFromArray() {
		Class<?>[] enumTableArray = new Class<?>[] {anEnumTable};
		depBuilder.withEnumTables(enumTableArray);
		
		DependenciesDefinition deps = depBuilder.build();
		assertEquals(new HashSet<Class<?>>(Arrays.asList(enumTableArray)),
				deps.getEnumTables());
	}


}
