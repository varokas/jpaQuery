package com.huskycode.jpaquery;

import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.mockito.Mockito;

import javax.persistence.metamodel.SingularAttribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Varokas Panusuwan
 */
public class DependenciesDefinitionTest {
    @Test
    public void shouldBeAbleToDefineDependenciesByLinks() {
        SingularAttribute pointA = Mockito.mock(SingularAttribute.class);
        SingularAttribute pointB = Mockito.mock(SingularAttribute.class);
        Link anyLink = Link.from(Object.class, pointA).to(Object.class, pointB);

        DependenciesDefinition deps =
                DependenciesDefinition.fromLinks(new Link[] { anyLink });

        Assert.assertThat(deps.getLinks().length, CoreMatchers.is(1));
        Assert.assertThat(deps.getLinks()[0],
                CoreMatchers.is(CoreMatchers.sameInstance(anyLink)));
    }

    @Test
    public void shouldGetAllDirectDependency() {
        SingularAttribute pointA = Mockito.mock(SingularAttribute.class);
        SingularAttribute pointB = Mockito.mock(SingularAttribute.class);
        Link anyLink = Link.from(A.class, pointA).to(B.class, pointB);
        DependenciesDefinition dependenciesDefinition =
                DependenciesDefinition.fromLinks(new Link[] { anyLink });

        List<Link<?,?,?>> dependencies = dependenciesDefinition.getDirectDependency(A.class);

        Assert.assertEquals(1, dependencies.size());

    }
    
	@Test
	public void testGetAllDependentEntities() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		
		Set<Class<?>> customerAllDependencies = dependenciesDefinition.getAllDependencyEntity(Customer.class);
		Assert.assertEquals(1, customerAllDependencies.size());
		Assert.assertTrue("Shuold have Address as parent dependency", customerAllDependencies.contains(Address.class));
		
		
		Set<Class<?>> pizzaOrderAllDependencies = dependenciesDefinition.getAllDependencyEntity(PizzaOrder.class);
		Assert.assertEquals(4, pizzaOrderAllDependencies.size());
		Assert.assertTrue("Shuold have Customer as parent dependency", pizzaOrderAllDependencies.contains(Customer.class));
		Assert.assertTrue("Shuold have Employee as parent dependency", pizzaOrderAllDependencies.contains(Employee.class));
		Assert.assertTrue("Shuold have Address as parent dependency", pizzaOrderAllDependencies.contains(Address.class));
		Assert.assertTrue("Shuold have Vehicle as parent dependency", pizzaOrderAllDependencies.contains(Vehicle.class));
	}	

    static class A {

    }

    static class B {

    }
}
