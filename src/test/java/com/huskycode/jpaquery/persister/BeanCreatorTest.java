package com.huskycode.jpaquery.persister;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.huskycode.jpaquery.persister.exception.EntityInstantiationException;
import com.huskycode.jpaquery.persister.util.BeanUtil;

/**
 * @author Varokas Panusuwan
 */
public class BeanCreatorTest {
	private BeanUtil beanCreator;
	
	@BeforeEach
	public void before() {
		beanCreator = new BeanUtil();
	}
	
    @Test
    public void shouldThrowExceptionIfCannotInstantiateClass() {
		assertThrows(EntityInstantiationException.class, () ->
			beanCreator.newInstance(NoPublicConstructor.class));
	}


    /** A class with no public constructor for test */
    private class NoPublicConstructor{ private NoPublicConstructor() {} }
}
