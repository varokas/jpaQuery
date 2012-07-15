package com.huskycode.jpaquery.persister;

import org.junit.Before;
import org.junit.Test;

import com.huskycode.jpaquery.persister.exception.EntityInstantiationException;
import com.huskycode.jpaquery.persister.util.BeanUtil;

/**
 * @author Varokas Panusuwan
 */
public class BeanCreatorTest {
	private BeanUtil beanCreator;
	
	@Before
	public void before() {
		beanCreator = new BeanUtil();
	}
	
    @Test(expected = EntityInstantiationException.class)
    public void shouldThrowExceptionIfCannotInstantiateClass() {
        beanCreator.newInstance(NoPublicConstructor.class);
    }


    /** A class with no public constructor for test */
    private class NoPublicConstructor{ private NoPublicConstructor() {} }
}
