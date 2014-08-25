package com.huskycode.jpaquery.populator;

import com.huskycode.jpaquery.types.db.Row;
import com.huskycode.jpaquery.types.db.Table;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Populates Random value into an Entity
 * 
 * @author Varokas Panusuwan
 */
public interface RandomValuePopulator {
    <E> void populateValue(E entity);

    void addFieldRandomizer(Field field, FieldValueRandomizer<?> randomizer);

    <E, T> void addFieldRandomizer(SingularAttribute<E, T> attr, FieldValueRandomizer<T> randomizer);

    <E, T> void addFieldRandomizers(Map<SingularAttribute, FieldValueRandomizer> map);

    Row random(Table table);
}
