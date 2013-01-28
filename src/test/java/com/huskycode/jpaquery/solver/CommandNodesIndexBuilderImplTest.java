package com.huskycode.jpaquery.solver;

import static com.huskycode.jpaquery.command.CommandNodeFactory.n;
import static com.huskycode.jpaquery.command.CommandNodesFactory.ns;
import junit.framework.Assert;

import org.junit.Test;

import com.huskycode.jpaquery.command.CommandNodeFactory.CommandNodeImpl;
import com.huskycode.jpaquery.command.CommandNodes;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;

public class CommandNodesIndexBuilderImplTest {
    @Test
    public void testBuildIndexCorrectly() {
        CommandNodes commands = ns(n(Address.class,
                                        n(PizzaOrder.class,
                                                n(Employee.class),
                                                n(Employee.class)),
                                        n(PizzaOrder.class,
                                                n(Employee.class)),
                                        n(Employee.class)),
                                   n(Address.class));

        CommandNodesIndexBuilderImpl indexBuilder = new CommandNodesIndexBuilderImpl();
        CommandNodesIndexResult result = indexBuilder.build(commands);

        Assert.assertEquals(0, result.getIndexOf(commands.get().get(0)).intValue());
        Assert.assertEquals(1, result.getIndexOf(commands.get().get(1)).intValue());

        Assert.assertEquals(0, result.getIndexOf(commands.get().get(0).getChildren().get(0)).intValue());
        Assert.assertEquals(1, result.getIndexOf(commands.get().get(0).getChildren().get(1)).intValue());
        Assert.assertEquals(3, result.getIndexOf(commands.get().get(0).getChildren().get(2)).intValue());

        Assert.assertEquals(0, result.getIndexOf(commands.get().get(0).getChildren().get(0).getChildren().get(0)).intValue());
        Assert.assertEquals(1, result.getIndexOf(commands.get().get(0).getChildren().get(0).getChildren().get(1)).intValue());
        Assert.assertEquals(2, result.getIndexOf(commands.get().get(0).getChildren().get(1).getChildren().get(0)).intValue());
    }

    @Test
    public void testBuildIndexDoNotResignIndexForReusingCommands() {
        CommandNodeImpl order1 = n(PizzaOrder.class);
        CommandNodeImpl order2 = n(PizzaOrder.class);
        CommandNodes commands = ns(n(Customer.class, order1),
                                    n(Customer.class, order2),
                                    n(Employee.class, order1, order2));

        CommandNodesIndexBuilderImpl indexBuilder = new CommandNodesIndexBuilderImpl();
        CommandNodesIndexResult result = indexBuilder.build(commands);

        Assert.assertEquals(0, result.getIndexOf(order1).intValue());
        Assert.assertEquals(1, result.getIndexOf(order2).intValue());
    }
}
