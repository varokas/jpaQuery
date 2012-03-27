package com.huskycode.jpaquery.link;

import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;

/**
 * Use to define dependency on one class to another.
 *
 * @author Varokas Panusuwan
 */
public class Link<E1,E2,T> {
   private final Attribute<E1, T> from;
   private final Attribute<E2, T> to;

    private Link(Attribute<E1, T> from, Attribute<E2, T> to) {
        this.from = from;
        this.to = to;
    }

    public Attribute<E1, T> getFrom() {
        return from;
    }

    public Attribute<E2, T> getTo() {
        return to;
    }

    public static <E, T> From from(Class<E> fromEntity, SingularAttribute<?,T> from) {
        return new From(fromEntity, from);
    }

    public static class From<E1,E2,T> {
        private final Attribute<E1, T> from;

        private From(Class<E1> fromEntity, SingularAttribute<?,T> from) {
            this.from = AttributeImpl.newInstance(fromEntity, (Field)from.getJavaMember());
        }

        public Link to(Class<E2> toEntity, SingularAttribute<?, T> to) {
            return new Link(this.from,  AttributeImpl.newInstance(toEntity, (Field)to.getJavaMember()));
        }
    }
}
