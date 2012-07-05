package com.huskycode.jpaquery.persister;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.random.RandomValuePopulator;
import com.huskycode.jpaquery.random.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.types.tree.CreationPlan;
import com.huskycode.jpaquery.types.tree.PersistedResult;

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
    public PersistedResult persistValues(CreationPlan plan) {
        List<Object> objects = new ArrayList<Object>();
        for(Class<?> c: plan.getClasses()) {
            Object obj = beanCreator.newInstance(c);

            randomValuePopulator.populateValue(obj);

            objects.add(obj);
            em.persist(obj);
        }

        return PersistedResult.newInstance(objects);
    }
}
