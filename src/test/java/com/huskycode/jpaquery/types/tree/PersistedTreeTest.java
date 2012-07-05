package com.huskycode.jpaquery.types.tree;

import static org.hamcrest.CoreMatchers.sameInstance;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class PersistedTreeTest {

	@Test
	public void shouldBeAbleToCreateAndRetrieve() {
		List<Object> persistedObjects = Arrays.asList(new Object());
		PersistedResult pt = PersistedResult.newInstance(persistedObjects);
		Assert.assertThat(pt.getPersistedObjects(), sameInstance(persistedObjects));
	}

}
