package com.huskycode.jpaquery.types.tree;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;

import org.junit.Assert;
import org.junit.Test;

public class CreationTreeTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testCreateWithRoot() {
		Class<?> anyClass = Object.class;
		CreationTree tree = CreationTree.newInstance(anyClass);
		Assert.assertThat(tree.getRoot(), is(sameInstance((Class)anyClass)));
		
	}

}
