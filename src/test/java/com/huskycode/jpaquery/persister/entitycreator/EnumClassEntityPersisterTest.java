package com.huskycode.jpaquery.persister.entitycreator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.huskycode.jpaquery.types.tree.EntityNodeImpl;
import org.junit.Test;

import com.huskycode.jpaquery.types.tree.EntityNode;

public class EnumClassEntityPersisterTest {
	private EnumClassEntityPersister persister;
	
	static enum AnEnum {
		FirstValue,
		SecondValue
	}

	@Test
	public void testChooseTheFirstValueOfEnum() {
		persister = new EnumClassEntityPersister();
		
		EntityNode n = EntityNodeImpl.newInstance(AnEnum.class);
		Object obj = persister.persistNode(n, new HashMap<Field, Object>());
		
		assertThat(obj, is(not(nullValue())));
		assertEquals(AnEnum.FirstValue, obj);
	}

}
