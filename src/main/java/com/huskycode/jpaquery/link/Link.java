package com.huskycode.jpaquery.link;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Use to define dependency on one class to another.
 *
 * @author Varokas Panusuwan
 */
public class Link<E1,E2,T> {
   private final SingularAttribute<E1,T> from;
   private final SingularAttribute<E2,T> to;

    private Link(SingularAttribute<E1, T> from, SingularAttribute<E2, T> to) {
        this.from = from;
        this.to = to;
    }

    public SingularAttribute<E1,T> getFrom() {
        return from;
    }

    public SingularAttribute<E2,T> getTo() {
        return to;
    }

    public static <E,T> From from(SingularAttribute<E,T> from) {
        return new From(from);
    }

    public Class<E1> getFromEntityJavaType() {
        return from.getDeclaringType().getJavaType();
    }

    public static class From<E1,E2,T> {
        private final SingularAttribute<E1,T> from;

        private From(SingularAttribute<E1,T> from) {
            this.from = from;
        }

        public Link to(SingularAttribute<E2,T> to) {
            return new Link(this.from,  to);
        }
    }
}
