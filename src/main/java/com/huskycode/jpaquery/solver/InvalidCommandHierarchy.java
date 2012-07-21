package com.huskycode.jpaquery.solver;

public class InvalidCommandHierarchy extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidCommandHierarchy() {}
	
	public InvalidCommandHierarchy(String msg) {
		super(msg);
	}
}
