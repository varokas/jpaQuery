package com.huskycode.integration;

import com.huskycode.jpaquery.testmodel.ClassA;
import com.huskycode.jpaquery.testmodel.ClassA_;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Varokas Panusuwan
 */
public class EntityManagerTest {
    private EntityManager entityManager = TestEntityManager.INSTANCE.getEntityManager();

    @Test
    public void testEntityManagerAbleToSaveAnEntityClass() {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        ClassA a = new ClassA();
        entityManager.persist(a);
        ClassA queried = entityManager.find(ClassA.class, a.getId());
                
        assertThat(queried.getId(), is(equalTo(a.getId())));

        tx.rollback();
    }

    @Test
    public void testMetamodelNotNull() {
        assertThat(ClassA_.id, is(not(nullValue())));
    }
}
