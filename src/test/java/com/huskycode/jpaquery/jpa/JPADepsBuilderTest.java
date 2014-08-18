package com.huskycode.jpaquery.jpa;

import com.huskycode.jpaquery.GenericDependenciesDefinition;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Customer;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author varokas
 */
public class JPADepsBuilderTest {
    private JPADepsBuilder jpaDepsBuilder = new JPADepsBuilder();
    private Link<?,?,?> aLink;

    public JPADepsBuilderTest() throws NoSuchFieldException {
        aLink = Link.from(Customer.class, Customer.class.getDeclaredField("customerAddressId"))
                .to(Address.class, Address.class.getDeclaredField("addressId"));
    }

    @Test
    public void testDepsBuilderBuildDepsContext() {
        DependenciesContext dependenciesContext = jpaDepsBuilder.build();

        assertThat(dependenciesContext.getDependenciesDefinition(), notNullValue());
        assertThat(dependenciesContext.getRowPersister(), notNullValue());
    }

    @Test
    public void testDepsBuilderBuildsCorrectLink() {
        GenericDependenciesDefinition dependenciesDefinition =
                jpaDepsBuilder.withLink(aLink).build().getDependenciesDefinition();

        assertThat(dependenciesDefinition.getLinks(), Matchers.hasSize(1));
        com.huskycode.jpaquery.types.db.Link link = dependenciesDefinition.getLinks().get(0);

        assertThat(link.getFrom().getName(), equalTo("customerAddressId"));
        assertThat(link.getFrom().getType(), equalTo((Class)long.class));
        assertThat(link.getTo().getName(), equalTo("addressId"));
        assertThat(link.getTo().getType(), equalTo((Class)Long.class));
    }
}
