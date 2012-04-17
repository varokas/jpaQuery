package com.huskycode.jpaquery.persister;

/**
 * @author Varokas Panusuwan
 */
public class BeanCreator {
	public <E> E newInstance(Class<E> beanClass) {
        try {
            return beanClass.newInstance();
        } catch (InstantiationException e) {
            throw new EntityInstantiationException("Cannot create class: " + beanClass.getName(), e);
        } catch (IllegalAccessException e) {
            throw new EntityInstantiationException("Cannot create class: " + beanClass.getName(), e);
        }
	}
}
