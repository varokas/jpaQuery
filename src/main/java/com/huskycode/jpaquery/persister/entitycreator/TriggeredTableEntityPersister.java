package com.huskycode.jpaquery.persister.entitycreator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.populator.ValuesPopulator;
import com.huskycode.jpaquery.populator.ValuesPopulatorImpl;
import com.huskycode.jpaquery.types.tree.EntityNode;

public class TriggeredTableEntityPersister implements EntityPersister {

    private final ValuesPopulator valuesPopulator = ValuesPopulatorImpl.getInstance();
    private final EntityManager em;
    private final List<Link<?,?,?>> allDirectLinks;

    TriggeredTableEntityPersister(final EntityManager em, final List<Link<?,?,?>> allDirectLinks) {
        this.em = em;
        this.allDirectLinks = allDirectLinks;
    }

    @Override
    public Object persistNode(final EntityNode node, final Map<Field, Object> overrideFields) {
        Object obj = getRecord(node.getEntityClass(), overrideFields);
        if (obj != null) {
            valuesPopulator.populateValue(obj, overrideFields);
            this.em.merge(obj);
            return obj;
        } else {
            throw new RuntimeException(node.getEntityClass()
                    + " cannot be found in database. It should be created via trigger by parent tables given the link definition");
        }
    }

    private <T> T getRecord(final Class<T> c, final Map<Field, Object> overrideFields) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(c);
        Root<T> root = criteriaQuery.from(c);
        EntityType<T> model = root.getModel();
        CriteriaQuery<T> cq = criteriaQuery.select(root);
        for (Link<?,?,?> link : this.allDirectLinks) {
            Field field = link.getFrom().getField();
            SingularAttribute attr = model.getSingularAttribute(link.getFrom().getField().getName());
            cq.where(cb.equal(root.get(attr), overrideFields.get(field)));
        }
        TypedQuery<T> query = em.createQuery(cq);
        List<T> result = query.getResultList();
        return result.size() > 0 ? result.get(0) : null;
    }

}
