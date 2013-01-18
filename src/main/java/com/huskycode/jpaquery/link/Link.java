package com.huskycode.jpaquery.link;

import java.lang.reflect.Field;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Use to define dependency on one class to another.
 * 
 * @author Varokas Panusuwan
 */
public class Link<E1, E2, T> {
    private final Attribute<E1, T> from;
    private final Attribute<E2, T> to;

    private Link(final Attribute<E1, T> from, final Attribute<E2, T> to) {
        this.from = from;
        this.to = to;
    }

    public Attribute<E1, T> getFrom() {
        return from;
    }

    public Attribute<E2, T> getTo() {
        return to;
    }

    public static <E, T> From from(final Class<E> fromEntity, final SingularAttribute<?, T> from) {
        return new From(fromEntity, from);
    }

    public static <E, T> From from(final Class<E> fromEntity, final Field field) {
        return new From(fromEntity, field);
    }

    public static class From<E1, E2, T> {
        private final Attribute<E1, T> from;

        private From(final Class<E1> fromEntity, final SingularAttribute<?, T> from) {
            this(fromEntity, getJavaMember(from));
        }

        public From(final Class<E1> fromEntity, final Field field) {
            this.from = AttributeImpl.newInstance(fromEntity, field);
        }

        public Link to(final Class<E2> toEntity, final Field field) {
            return new Link(this.from, AttributeImpl.newInstance(toEntity, field));
        }

        public Link to(final Class<E2> toEntity, final SingularAttribute<?, T> to) {
            return to(toEntity, getJavaMember(to));
        }
    }

    private static <T> Field getJavaMember(final SingularAttribute<?, T> to) {
        // A Hack for Composite Key
        Class<?> declaredJavaType = to.getDeclaringType().getJavaType();

        Field originalField = (Field)to.getJavaMember();
        if (originalField.getDeclaringClass().equals(declaredJavaType)) {
            return originalField;
        } else {
            try {
                return declaredJavaType.getDeclaredField(originalField.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
