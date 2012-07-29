package com.huskycode.jpaquery;

import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.RefDeliveryStatus;
import com.huskycode.jpaquery.testmodel.pizza.RefPaymentMethod;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleType;
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

        DependenciesDefinition deps = new DepsBuilder().withLink(anyLink).build();

        Assert.assertThat(deps.getLinks().length, CoreMatchers.is(1));
        Assert.assertThat(deps.getLinks()[0],
                CoreMatchers.is(CoreMatchers.sameInstance(anyLink)));
    }

    @Test
    public void shouldGetAllDirectDependency() {
        SingularAttribute pointA = Mockito.mock(SingularAttribute.class);
        SingularAttribute pointB = Mockito.mock(SingularAttribute.class);
        Link anyLink = Link.from(A.class, pointA).to(B.class, pointB);
        DependenciesDefinition deps = new DepsBuilder().withLink(anyLink).build();

        List<Link<?,?,?>> dependencies = deps.getDirectDependency(A.class);

        Assert.assertEquals(1, dependencies.size());

    }
    
	@Test
	public void testGetAllDependentEntities() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		
		Set<Class<?>> customerAllDependencies = dependenciesDefinition.getAllParentDependencyEntity(Customer.class);
		Assert.assertEquals(2, customerAllDependencies.size());
		Assert.assertTrue("Shuold have Address as parent dependency", customerAllDependencies.contains(Address.class));
		Assert.assertTrue("Shuold have RefPaymentType as parent dependency", customerAllDependencies.contains(RefPaymentMethod.class));
		
		
		Set<Class<?>> pizzaOrderAllDependencies = dependenciesDefinition.getAllParentDependencyEntity(PizzaOrder.class);
		Assert.assertEquals(7, pizzaOrderAllDependencies.size());
		Assert.assertTrue("Shuold have Customer as parent dependency", pizzaOrderAllDependencies.contains(Customer.class));
		Assert.assertTrue("Shuold have Employee as parent dependency", pizzaOrderAllDependencies.contains(Employee.class));
		Assert.assertTrue("Shuold have Address as parent dependency", pizzaOrderAllDependencies.contains(Address.class));
		Assert.assertTrue("Shuold have Vehicle as parent dependency", pizzaOrderAllDependencies.contains(Vehicle.class));
		
		Assert.assertTrue("Shuold have RefPaymentMethod as parent dependency", pizzaOrderAllDependencies.contains(RefPaymentMethod.class));
		Assert.assertTrue("Shuold have RefDeliveryStatus as parent dependency", pizzaOrderAllDependencies.contains(RefDeliveryStatus.class));
		Assert.assertTrue("Shuold have RefVehicleType as parent dependency", pizzaOrderAllDependencies.contains(RefVehicleType.class));
	}	
	
	@Test
	public void testGetChildParentLinkDependency() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();
		
		List<Link<?,?,?>> links = dependenciesDefinition.getDependencyLinks(PizzaOrder.class, Customer.class);
		
		Assert.assertEquals(1, links.size());
		Assert.assertEquals(PizzaOrder.class, links.get(0).getFrom().getEntityClass());
		Assert.assertEquals(Customer.class, links.get(0).getTo().getEntityClass());
	}

    static class A {

    }

    static class B {

    }
}
