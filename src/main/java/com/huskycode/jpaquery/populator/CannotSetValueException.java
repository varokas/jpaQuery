package com.huskycode.jpaquery.populator;

/**
 * @author Varokas Panusuwan
 */
public class CannotSetValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CannotSetValueException(Exception e) {
		super(e);
	}

}
