package com.huskycode.jpaquery.types.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;

public class CreationPlanTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testCreateWithRoot() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(Object.class);
		CreationPlan plan = CreationPlan.newInstance(classes);
        Assert.assertThat(plan.getClasses(), is(sameInstance(classes)));

    }

}
