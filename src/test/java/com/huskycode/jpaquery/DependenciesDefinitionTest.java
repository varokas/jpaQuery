package com.huskycode.jpaquery;

import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import jakarta.persistence.metamodel.SingularAttribute;

import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.types.db.factory.TableFactory;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

        assertThat(deps.getLinks().length, CoreMatchers.is(1));
        assertThat(deps.getLinks()[0],
                CoreMatchers.is(CoreMatchers.sameInstance(anyLink)));
    }

    @Test
    public void shouldGetAllDirectDependency() {
        Link anyLink = createMockLink(A.class.getDeclaredFields()[0], B.class.getDeclaredFields()[0]);

        DependenciesDefinition deps = new DepsBuilder().withLink(anyLink).build();

        List<Link<?,?,?>> dependencies = deps.getDirectDependency(A.class);

        Assertions.assertEquals(1, dependencies.size());

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
		Assertions.assertEquals(2, customerAllDependencies.size());
		Assertions.assertTrue(customerAllDependencies.contains(Address.class), "Shuold have Address as parent dependency");
		Assertions.assertTrue(customerAllDependencies.contains(RefPaymentMethod.class), "Shuold have RefPaymentType as parent dependency");


		Set<Class<?>> pizzaOrderAllDependencies = dependenciesDefinition.getAllParentDependencyEntity(PizzaOrder.class);
		Assertions.assertEquals(7, pizzaOrderAllDependencies.size());
		Assertions.assertTrue(pizzaOrderAllDependencies.contains(Customer.class), "Shuold have Customer as parent dependency");
		Assertions.assertTrue(pizzaOrderAllDependencies.contains(Employee.class), "Shuold have Employee as parent dependency");
		Assertions.assertTrue(pizzaOrderAllDependencies.contains(Address.class), "Shuold have Address as parent dependency");
		Assertions.assertTrue(pizzaOrderAllDependencies.contains(Vehicle.class), "Shuold have Vehicle as parent dependency");

		Assertions.assertTrue(pizzaOrderAllDependencies.contains(RefPaymentMethod.class), "Shuold have RefPaymentMethod as parent dependency");
		Assertions.assertTrue(pizzaOrderAllDependencies.contains(RefDeliveryStatus.class), "Shuold have RefDeliveryStatus as parent dependency");
		Assertions.assertTrue(pizzaOrderAllDependencies.contains(RefVehicleTypeEnum.class), "Shuold have RefVehicleType as parent dependency");
	}

	@Test
	public void testGetChildParentLinkDependency() {
		DependenciesDefinition dependenciesDefinition = new PizzaDeps().getDepsUsingField();

		List<Link<?,?,?>> links = dependenciesDefinition.getDependencyLinks(PizzaOrder.class, Customer.class);

		Assertions.assertEquals(1, links.size());
		Assertions.assertEquals(PizzaOrder.class, links.get(0).getFrom().getEntityClass());
		Assertions.assertEquals(Customer.class, links.get(0).getTo().getEntityClass());
	}

	@Test
	public void testTriggeredTables() {
	    DependenciesDefinition deps = new DepsBuilder().withTriggeredTable(ClassA.class).build();

	    Assertions.assertTrue(deps.getTriggeredTables().contains(tableFactory.createFromJPAEntity(ClassA.class)), "Fail to contain Triggered Table correctly");
	}
	
	@Test
	public void testIsForeignKeyReturnTrueWhenFieldDependsOnParentField() throws NoSuchFieldException, SecurityException {
		Link anyLink = createMockLink(A.class.getDeclaredFields()[0], B.class.getDeclaredFields()[0]);
		SingularAttribute<A, Integer> aFieldAttr = Mockito.mock(SingularAttribute.class);
		Mockito.when(aFieldAttr.getJavaMember()).thenReturn(A.class.getDeclaredField("aField"));
        DependenciesDefinition deps = new DepsBuilder().withLink(anyLink).build();

	    Assertions.assertTrue(deps.isForeignKey(A.class, A.class.getDeclaredField("aField")), "Fail return true when field is a foreign key");
	    Assertions.assertTrue(deps.isForeignKey(A.class, aFieldAttr), "Fail return true when field is a foreign key");
	}
	
	@Test
	public void testIsForeignKeyReturnFalseWhenFieldDependsOnParentField() throws NoSuchFieldException, SecurityException {
		SingularAttribute<A, Integer> aFieldAttr = Mockito.mock(SingularAttribute.class);
		Mockito.when(aFieldAttr.getJavaMember()).thenReturn(A.class.getDeclaredField("aField"));
        DependenciesDefinition deps = new DepsBuilder().build();

	    Assertions.assertFalse(deps.isForeignKey(A.class, A.class.getDeclaredField("aField")), "Fail return false when field is NOT a foreign key");
	    Assertions.assertFalse(deps.isForeignKey(A.class, aFieldAttr), "Fail return false when field is NOT a foreign key");
	}

    static class A {
    	int aField;
    }

    static class B {
    	int bField;
    }
}
