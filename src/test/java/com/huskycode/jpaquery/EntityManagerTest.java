package com.huskycode.jpaquery;

import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.testmodel.ClassA_;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Varokas Panusuwan
 */
public class EntityManagerTest extends AbstractEntityManagerWired {
    @Test
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

    @Test
    public void testMetamodelNotNull() {
        assertThat(ClassA_.id, is(not(nullValue())));
    }
}
