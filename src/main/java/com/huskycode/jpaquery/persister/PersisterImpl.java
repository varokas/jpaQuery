package com.huskycode.jpaquery.persister;

import com.huskycode.jpaquery.random.RandomValuePopulator;
import com.huskycode.jpaquery.random.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.PersistedTree;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Varokas Panusuwan
 */
public class PersisterImpl implements Persister {
	private EntityManager em;
	private BeanCreator beanCreator;
    private RandomValuePopulator randomValuePopulator;
	
	private PersisterImpl(EntityManager em) {
		this.em = em;
	}
	
	//VisibleForTesting
	PersisterImpl(EntityManager em, BeanCreator beanCreator,
			RandomValuePopulator randomValuePopulator) {
		this.em = em;
		this.beanCreator = beanCreator;
		this.randomValuePopulator = randomValuePopulator;
	}
	
	public static PersisterImpl newInstance(EntityManager em) {
		PersisterImpl persisterImpl = new PersisterImpl(em);
		persisterImpl.beanCreator = new BeanCreator();
		persisterImpl.randomValuePopulator = new RandomValuePopulatorImpl();
		return persisterImpl;
	}

    @Override
    public List<PersistedTree> persistValues(CreationPlan plan) {
        List<Object> objects = new ArrayList<Object>();
        for(Class<?> c: plan.getClasses()) {
            Object obj = beanCreator.newInstance(c);

            try {
                randomValuePopulator.populateValue(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            objects.add(obj);
            em.persist(obj);
        }

        List<PersistedTree> persistedTree = Arrays.asList(PersistedTree.newInstance(objects));

        return persistedTree;
    }
}
