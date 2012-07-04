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
    
    public static <E, T> From from(Class<E> fromEntity, Field field) {
    	return new From(fromEntity, field);
    }

    public static class From<E1,E2,T> {
        private final Attribute<E1, T> from;

        private From(Class<E1> fromEntity, SingularAttribute<?,T> from) {
            this(fromEntity, (Field)from.getJavaMember());
        }

        public From(Class<E1> fromEntity, Field field) {
			this.from = AttributeImpl.newInstance(fromEntity, field);
		}
        
        public Link to(Class<E2> toEntity, Field field) {
            return new Link(this.from,  AttributeImpl.newInstance(toEntity, field));
        }

		public Link to(Class<E2> toEntity, SingularAttribute<?, T> to) {
            return to(toEntity, (Field)to.getJavaMember());
        }
    }
}
