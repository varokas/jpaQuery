package com.huskycode.jpaquery.types.tree;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static com.huskycode.jpaquery.command.CommandNodesFactory.ns;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.huskycode.jpaquery.command.CommandNode;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.solver.CommandNodesIndexResult;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;

public class PersistedResultTest {

	@Test
	public void shouldBeAbleToCreateAndRetrieve() {
		List<Object> persistedObjects = Arrays.asList(new Object());
		PersistedResult pt = PersistedResult.newInstance(persistedObjects);
		assertThat(pt.getPersistedObjects(), sameInstance(persistedObjects));
	}

	@Test
	public void shouldReturnAllObjectOfClass() {
		List<Object> persistedObjects = Arrays.asList(new Address(),
													  new Address(),
													  new Customer(),
													  new PizzaOrder(),
													  new PizzaOrder(),
													  new PizzaOrder());
		PersistedResult pt = PersistedResult.newInstance(persistedObjects);
		Assertions.assertEquals(2, pt.getForClass(Address.class).size());
		Assertions.assertEquals(persistedObjects.subList(0, 2), pt.getForClass(Address.class));
		Assertions.assertEquals(1, pt.getForClass(Customer.class).size());
		Assertions.assertEquals(persistedObjects.subList(2, 3), pt.getForClass(Customer.class));
		Assertions.assertEquals(3, pt.getForClass(PizzaOrder.class).size());
		Assertions.assertEquals(persistedObjects.subList(3, 6), pt.getForClass(PizzaOrder.class));
	}

	@Test
	public void tesGetforClassByCommandIndexReturnCorrectly() {
	    //create commands
	    CommandNodes commands = ns(n(Address.class,
                                        n(PizzaOrder.class,
                                                n(Employee.class),
                                                n(Employee.class)),
                                        n(PizzaOrder.class,
                                                n(Employee.class)),
                                        n(Employee.class)),
                                   n(Address.class));
	    //create index
	    CommandNodesIndexResult index = CommandNodesIndexResult.newInstance();
	    index.put(commands.get().get(0), 0);
	    index.put(commands.get().get(1), 1);
	    index.put(commands.get().get(0).getChildren().get(0), 0);
	    index.put(commands.get().get(0).getChildren().get(1), 1);
	    index.put(commands.get().get(0).getChildren().get(2), 3);
	    index.put(commands.get().get(0).getChildren().get(0).getChildren().get(0), 0);
	    index.put(commands.get().get(0).getChildren().get(0).getChildren().get(1), 1);
	    index.put(commands.get().get(0).getChildren().get(1).getChildren().get(0), 2);
	    //create objects
	    List<Object> persistedObjects = Arrays.asList(new Address(),
                                                        new Address(),
                                                        new PizzaOrder(),
                                                        new PizzaOrder(),
                                                        new Employee(),
                                                        new Employee(),
                                                        new Employee(),
                                                        new Employee());
	    //create commandObjectMap
	    Map<CommandNode, Object> commandObjectMap = new HashMap<CommandNode, Object>();
	    commandObjectMap.put(commands.get().get(0), persistedObjects.get(0));
	    commandObjectMap.put(commands.get().get(1), persistedObjects.get(1));
	    commandObjectMap.put(commands.get().get(0).getChildren().get(0), persistedObjects.get(2));
	    commandObjectMap.put(commands.get().get(0).getChildren().get(1), persistedObjects.get(3));
	    commandObjectMap.put(commands.get().get(0).getChildren().get(0).getChildren().get(0), persistedObjects.get(4));
	    commandObjectMap.put(commands.get().get(0).getChildren().get(0).getChildren().get(1), persistedObjects.get(5));
	    commandObjectMap.put(commands.get().get(0).getChildren().get(1).getChildren().get(0), persistedObjects.get(6));
	    commandObjectMap.put(commands.get().get(0).getChildren().get(2), persistedObjects.get(7));

	    PersistedResult pt = PersistedResult.newInstance(persistedObjects, index, commandObjectMap);

	    //verify
	    Assertions.assertSame(persistedObjects.get(0), pt.getForClassByCommandIndex(Address.class, 0));
	    Assertions.assertSame(persistedObjects.get(1), pt.getForClassByCommandIndex(Address.class, 1));
	    Assertions.assertSame(persistedObjects.get(2), pt.getForClassByCommandIndex(PizzaOrder.class, 0));
	    Assertions.assertSame(persistedObjects.get(3), pt.getForClassByCommandIndex(PizzaOrder.class, 1));
	    Assertions.assertSame(persistedObjects.get(4), pt.getForClassByCommandIndex(Employee.class, 0));
	    Assertions.assertSame(persistedObjects.get(5), pt.getForClassByCommandIndex(Employee.class, 1));
	    Assertions.assertSame(persistedObjects.get(6), pt.getForClassByCommandIndex(Employee.class, 2));
	    Assertions.assertSame(persistedObjects.get(7), pt.getForClassByCommandIndex(Employee.class, 3));
	}
}
