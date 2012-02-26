package com.huskycode.jpaquery;

/**
 * @author Varokas Panusuwan
 */
public class EntityInstantiationException extends RuntimeException {
    public EntityInstantiationException(String msg, Exception cause) {
        super(msg, cause);
    }
}
