package com.huskycode.acceptancetest.jpaquery;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.huskycode.jpaquery.AbstractEntityManagerWired;
import com.huskycode.jpaquery.JPAQueryContext;
import com.huskycode.jpaquery.testmodel.pizza.Address;
import com.huskycode.jpaquery.testmodel.pizza.Order;
import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;
import com.huskycode.jpaquery.types.tree.PersistedResult;

/**
 * @author Varokas Panusuwan
 */
@Transactional
public class JPAQueryIT extends AbstractEntityManagerWired {
    private PizzaDeps pizzaDeps;
    private JPAQueryContext context;
    
    @Before
    public void before() {
    	pizzaDeps = new PizzaDeps();
    }
	
	@Test
    public void testCreateClassWithNoDeps() {
        context = JPAQueryContext.newInstance(entityManager, pizzaDeps.getDeps());
        
        PersistedResult result = context.create(Address.class);
        assertThat(result, is(not(nullValue())));
    }
	
	@Test
    public void testCreateClassWithDeps() {
        context = JPAQueryContext.newInstance(entityManager, pizzaDeps.getDeps());
        
        PersistedResult result = context.create(Order.class);
        assertThat(result, is(not(nullValue())));
    }
}
