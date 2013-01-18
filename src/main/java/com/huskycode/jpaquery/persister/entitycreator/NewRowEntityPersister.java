package com.huskycode.jpaquery.persister.entitycreator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import com.huskycode.jpaquery.persister.util.BeanUtil;
import com.huskycode.jpaquery.populator.RandomValuePopulator;
import com.huskycode.jpaquery.populator.ValuesPopulator;
import com.huskycode.jpaquery.populator.ValuesPopulatorImpl;
import com.huskycode.jpaquery.types.tree.EntityNode;

/**
 * This is a persister that will persist an entity as a normal database row.
 * 
 * @author varokas
 */
public class NewRowEntityPersister implements EntityPersister {

    private final RandomValuePopulator randomValuePopulator;
    private final ValuesPopulator valuesPopulator = ValuesPopulatorImpl.getInstance();
    private final EntityManager em;

    NewRowEntityPersister(final EntityManager em, final RandomValuePopulator randomValuePopulator) {
        this.em = em;
        this.randomValuePopulator = randomValuePopulator;
    }

    @Override
    public Object persistNode(final EntityNode node, final Map<Field, Object> overrideFields) {
        return createNodeInDatabase(overrideFields, node);
    }

    private Object createNodeInDatabase(final Map<Field, Object> overrideFields, final EntityNode node) {
        Class<?> c = node.getEntityClass();
        Object obj = BeanUtil.newInstance(c);

        Map<Field, Object> valuesToPopulate = getValuesToOverride(overrideFields, node, c);

        randomValuePopulator.populateValue(obj);
        valuesPopulator.populateValue(obj, valuesToPopulate);

        em.persist(obj);
        return obj;
    }

    private Map<Field, Object> getValuesToOverride(final Map<Field, Object> valuesToPopulate, final EntityNode node,
            final Class<?> c) {
        Field idField = BeanUtil.findIdField(c);

        if (idField != null) {
            if (BeanUtil.isAnnotatedWithGenerated(idField)) {
                valuesToPopulate.put(idField, null); // Reset id field to null
                                                     // for
                                                     // JPA to autogen id.
            } else {
                if (valuesToPopulate.get(idField) == null) {// when PK is also
                                                            // foriegn key
                    Object maxRecordId = getMaxRecordId(c, idField);
                    idField.setAccessible(true);
                    if (maxRecordId != null) {
                        try {
                            if (idField.getType().equals(Integer.class)) {
                                valuesToPopulate.put(idField, (Integer.valueOf(maxRecordId.toString())) + 1);
                            } else {
                                valuesToPopulate.put(idField, (Long.valueOf(maxRecordId.toString())) + 1);
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        return valuesToPopulate;
    }

    private <T> Object getMaxRecordId(final Class<T> c, final Field idField) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
        Root<T> root = criteriaQuery.from(c);
        EntityType<T> model = root.getModel();
        SingularAttribute attr = model.getId(idField.getType());
        CriteriaQuery<Tuple> cq = criteriaQuery.multiselect(cb.max(root.get(attr)));
        TypedQuery<Tuple> query = em.createQuery(cq);
        List<Tuple> result = query.getResultList();
        return result.size() > 0 ? result.get(0).get(0) : null;
    }
}
