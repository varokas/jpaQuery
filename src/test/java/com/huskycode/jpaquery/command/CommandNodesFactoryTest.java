package com.huskycode.jpaquery.command;

import static com.huskycode.jpaquery.command.CommandNodesFactory.ns;
import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;

public class CommandNodesFactoryTest {
	private CommandNodes commandNodes;
	
	@Test
	public void testCreateOneNode() {
		commandNodes = ns(n(Address.class));
		assertEquals(1, commandNodes.get().size());
		assertEquals(Address.class, commandNodes.get().get(0).getEntity());
		assertThat(commandNodes.get().get(0).getChildren().size(), is(0));
	}
	
	@Test
	public void testCreateNodeWithChildren() {
		commandNodes =ns(n(Address.class,
						n(Customer.class)
						));

		assertEquals(1, commandNodes.get().size());
		assertEquals(Address.class, commandNodes.get().get(0).getEntity());
		assertThat(commandNodes.get().get(0).getChildren().size(), is(1));
		assertThat(commandNodes.get().get(0).getChildren().get(0), is(n(Customer.class)));
	}


}
