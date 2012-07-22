package com.huskycode.jpaquery;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.huskycode.jpaquery.testmodel.pizza.deps.PizzaDeps;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
