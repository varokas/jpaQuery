package com.huskycode.jpaquery.command;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;

import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.util.Maps;

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
		assertEquals(commandNode.getChildren().get(0).getEntity(), Customer.class);
	}

	@Test
	public void testSetFieldValues() throws NoSuchFieldException, SecurityException {
		Field field =Address.class.getDeclaredField("city");
		Object value = "anyCity";
		commandNode = n(Address.class)
						.with(n(Customer.class))
						.withValues(
								Maps.of(field, value)
						);
		
		assertEquals(1, commandNode.getFieldValues().size());
		assertEquals(value, commandNode.getFieldValues().get(field));
	}
}
