package com.huskycode.jpaquery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

/**
 * @author Varokas Panusuwan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-test.xml"})
@Transactional
public abstract class AbstractEntityManagerWired {
    @PersistenceContext
    protected EntityManager entityManager;
    
    @Before
    public void before() {
    	new PizzaDeps().populateInitialData(entityManager);
    }

}
