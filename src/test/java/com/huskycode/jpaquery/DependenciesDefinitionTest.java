package com.huskycode.jpaquery;

import com.huskycode.jpaquery.link.Link;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

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

    static class A {

    }

    static class B {

    }
}
