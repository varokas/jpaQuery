package com.huskycode.jpaquery;

import com.huskycode.jpaquery.testmodel.ClassA;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Varokas Panusuwan
 */
public class EntityManagerTest extends AbstractEntityManagerWiredTest {
    @Test
    @Rollback
    public void testEntityManagerWiredBySpringCorrectly() {
        assertThat(entityManager, is(not(nullValue())));
    }

    @Test
    @Rollback
    public void testEntityManagerAbleToSaveAnEntityClass() {
        ClassA a = new ClassA();
        entityManager.persist(a);
        ClassA queried = entityManager.find(ClassA.class, a.getId());
                
        assertThat(queried.getId(), is(equalTo(a.getId())));
    }
}
