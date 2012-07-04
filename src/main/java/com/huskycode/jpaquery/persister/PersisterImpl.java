package com.huskycode.jpaquery.persister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import com.huskycode.jpaquery.random.RandomValuePopulator;
import com.huskycode.jpaquery.random.RandomValuePopulatorImpl;
import com.huskycode.jpaquery.types.tree.CreationTree;
import com.huskycode.jpaquery.types.tree.PersistedTree;

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
	public List<PersistedTree> persistValues(List<CreationTree> creationTrees) {
		List<Object> objects = new ArrayList<Object>();
		for(CreationTree creationTree: creationTrees) {
			Object obj = beanCreator.newInstance(creationTree.getRoot());
			
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
