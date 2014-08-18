package com.huskycode.jpaquery.jpa;

import com.huskycode.jpaquery.GenericDependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.pizza.*;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author varokas
 */
public class JPADepsBuilderTest {
    private JPADepsBuilder jpaDepsBuilder = new JPADepsBuilder();
    private Link<?, ?, ?> aLink;

    @Mock
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    public JPADepsBuilderTest() throws NoSuchFieldException {
        aLink = Link.from(Customer.class, Customer.class.getDeclaredField("customerAddressId"))
                .to(Address.class, Address.class.getDeclaredField("addressId"));
    }

    @Test
    public void testDepsBuilderBuildDepsContext() {
        DependenciesContext dependenciesContext = jpaDepsBuilder.build(entityManager);

        assertThat(dependenciesContext.getDependenciesDefinition(), notNullValue());
        assertThat(dependenciesContext.getRowPersister(), notNullValue());
    }

    @Test
    public void testDepsBuilderBuildsCorrectLink() {
        GenericDependenciesDefinition dependenciesDefinition =
                jpaDepsBuilder.withLink(aLink).build(entityManager).getDependenciesDefinition();

        assertThat(dependenciesDefinition.getLinks(), Matchers.hasSize(1));
        com.huskycode.jpaquery.types.db.Link link = dependenciesDefinition.getLinks().get(0);

        assertThat(link.getFrom().getName(), equalTo("customerAddressId"));
        assertThat(link.getFrom().getType(), equalTo((Class) long.class));
        assertThat(link.getTo().getName(), equalTo("addressId"));
        assertThat(link.getTo().getType(), equalTo((Class) Long.class));
    }

    @Test
    public void testDepsBuilderReuseTableInLink() throws NoSuchFieldException {
        Link[] links = new Link[]{
                Link.from(PizzaOrder.class, PizzaOrder.class.getDeclaredField("takenByEmployeeId"))
                        .to(Employee.class, Employee.class.getDeclaredField("employeeId")),
                Link.from(PizzaOrder.class, PizzaOrder.class.getDeclaredField("deliveredByEmployeeId"))
                        .to(Employee.class, Employee_.class.getDeclaredField("employeeId"))
        };

        GenericDependenciesDefinition dependenciesDefinition =
                jpaDepsBuilder.withLinks(links).build(entityManager).getDependenciesDefinition();

        assertThat(dependenciesDefinition.getLinks(), Matchers.hasSize(2));

        com.huskycode.jpaquery.types.db.Link link1 = dependenciesDefinition.getLinks().get(0);
        com.huskycode.jpaquery.types.db.Link link2 = dependenciesDefinition.getLinks().get(1);

        assertThat(link1.getFrom().getTable(), sameInstance(link2.getFrom().getTable()));
        assertThat(link1.getTo().getTable(), sameInstance(link2.getTo().getTable()));
    }

    @Test
    public void testDepsBuilderPopulatesEnumAndTriggerTables() {
        GenericDependenciesDefinition dependenciesDefinition =
                jpaDepsBuilder.withEnumTable(Customer.class)
                        .withTriggeredTable(Address.class)
                        .build(entityManager)
                        .getDependenciesDefinition();

        assertThat(dependenciesDefinition.getEnumTables(), hasSize(1));
        assertThat(dependenciesDefinition.getTriggeredTables(), hasSize(1));

        assertThat(dependenciesDefinition.getEnumTables().iterator().next().getName(), equalTo("Customer"));
        assertThat(dependenciesDefinition.getTriggeredTables().iterator().next().getName(), equalTo("Address"));

    }
}
