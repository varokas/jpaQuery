package com.huskycode.jpaquery.command;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;

public class CommandNodeFactoryTest {
	private CommandNode commandNode;
	
	@Test
	public void testCreateOneNode() {
		commandNode = n(Address.class);
		assertEquals(Address.class, commandNode.getEntity());
		assertThat(commandNode.getChildren().size(), is(0));
	}
	
	@Test
	public void testCreateNodeWithChildren() {
		commandNode = n(Address.class,
			n(Customer.class)
		);

		assertEquals(Address.class, commandNode.getEntity());
		assertThat(commandNode.getChildren().size(), is(1));
		assertThat(commandNode.getChildren().get(0), is(n(Customer.class)));
	}


}
