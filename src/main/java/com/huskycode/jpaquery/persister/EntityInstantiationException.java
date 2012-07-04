package com.huskycode.jpaquery.persister;

/**
 * @author Varokas Panusuwan
 */
public class EntityInstantiationException extends RuntimeException {
    public EntityInstantiationException(String msg, Exception cause) {
        super(msg, cause);
    }
}
