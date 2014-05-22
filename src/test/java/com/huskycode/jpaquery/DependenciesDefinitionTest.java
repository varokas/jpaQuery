package com.huskycode.jpaquery;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import javax.persistence.metamodel.SingularAttribute;

import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.types.db.factory.TableFactory;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.huskycode.jpaquery.link.AttributeImpl;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import com.huskycode.jpaquery.testmodel.pizza.Employee;
import com.huskycode.jpaquery.testmodel.pizza.PizzaOrder;
import com.huskycode.jpaquery.testmodel.pizza.RefDeliveryStatus;
import com.huskycode.jpaquery.testmodel.pizza.RefPaymentMethod;
import com.huskycode.jpaquery.testmodel.pizza.RefVehicleTypeEnum;
import com.huskycode.jpaquery.testmodel.pizza.Vehicle;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

/**
 * @author Varokas Panusuwan
 */
public class DependenciesDefinitionTest {
    private final TableFactory tableFactory = new TableFactory();

    @Test
    public void shouldBeAbleToDefineDependenciesByLinks() {
        Link anyLink = createMockLink(A.class.getDeclaredFields()[0], B.class.getDeclaredFields()[0]);

        DependenciesDefinition deps = new DepsBuilder().withLink(anyLink).build();

        Assert.assertThat(deps.getLinks().length, CoreMatchers.is(1));
        Assert.assertThat(deps.getLinks()[0],
                CoreMatchers.is(CoreMatchers.sameInstance(anyLink)));
    }

    @Test
    public void shouldGetAllDirectDependency() {
        Link anyLink = createMockLink(A.class.getDeclaredFields()[0], B.class.getDeclaredFields()[0]);

        DependenciesDefinition deps = new DepsBuilder().withLink(anyLink).build();

        List<Link<?,?,?>> dependencies = deps.getDirectDependency(A.class);

        Assert.assertEquals(1, dependencies.size());

    }

    private Link createMockLink(final Field fieldFrom, final Field fieldTo) {
    	Link link = Mockito.mock(Link.class);
    	Mockito.when(link.getFrom()).thenReturn(AttributeImpl.newInstance(fieldFrom.getDeclaringClass(), fieldFrom));
    	Mockito.when(link.getTo()).thenReturn(AttributeImpl.newInstance(fieldTo.getDeclaringClass(), fieldTo));
    	return link;
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
		Assert.assertTrue("Shuold have RefVehicleType as parent dependency", pizzaOrderAllDependencies.contains(RefVehicleTypeEnum.class));
	}

	@Test
	public void testGetChildParentLinkDependency() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();

		List<Link<?,?,?>> links = dependenciesDefinition.getDependencyLinks(PizzaOrder.class, Customer.class);

		Assert.assertEquals(1, links.size());
		Assert.assertEquals(PizzaOrder.class, links.get(0).getFrom().getEntityClass());
		Assert.assertEquals(Customer.class, links.get(0).getTo().getEntityClass());
	}

	@Test
	public void testTriggeredTables() {
	    DependenciesDefinition deps = new DepsBuilder().withTriggeredTable(ClassA.class).build();

	    Assert.assertTrue("Fail to contain Triggered Table correctly", deps.getTriggeredTables().contains(tableFactory.createFromJPAEntity(ClassA.class)));
	}
	
	@Test
	public void testIsForeignKeyReturnTrueWhenFieldDependsOnParentField() throws NoSuchFieldException, SecurityException {
		Link anyLink = createMockLink(A.class.getDeclaredFields()[0], B.class.getDeclaredFields()[0]);
		SingularAttribute<A, Integer> aFieldAttr = Mockito.mock(SingularAttribute.class);
		Mockito.when(aFieldAttr.getJavaMember()).thenReturn(A.class.getDeclaredField("aField"));
        DependenciesDefinition deps = new DepsBuilder().withLink(anyLink).build();

	    Assert.assertTrue("Fail return true when field is a foreign key", deps.isForeignKey(A.class, A.class.getDeclaredField("aField")));
	    Assert.assertTrue("Fail return true when field is a foreign key", deps.isForeignKey(A.class, aFieldAttr));
	}
	
	@Test
	public void testIsForeignKeyReturnFalseWhenFieldDependsOnParentField() throws NoSuchFieldException, SecurityException {
		SingularAttribute<A, Integer> aFieldAttr = Mockito.mock(SingularAttribute.class);
		Mockito.when(aFieldAttr.getJavaMember()).thenReturn(A.class.getDeclaredField("aField"));
        DependenciesDefinition deps = new DepsBuilder().build();

	    Assert.assertFalse("Fail return false when field is NOT a foreign key", deps.isForeignKey(A.class, A.class.getDeclaredField("aField")));
	    Assert.assertFalse("Fail return false when field is NOT a foreign key", deps.isForeignKey(A.class, aFieldAttr));
	}

    static class A {
    	int aField;
    }

    static class B {
    	int bField;
    }
}
