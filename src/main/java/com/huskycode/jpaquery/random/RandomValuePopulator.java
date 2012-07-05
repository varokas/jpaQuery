package com.huskycode.jpaquery.random;

/**
 * Populates Random value into an Entity
 *
 * @author Varokas Panusuwan
 */
public interface RandomValuePopulator {
    <E> void populateValue(E entity);
}
