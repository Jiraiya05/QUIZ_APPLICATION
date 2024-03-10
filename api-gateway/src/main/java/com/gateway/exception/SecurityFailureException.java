package com.gateway.exception;

public class SecurityFailureException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	public SecurityFailureException(String message) {
		super();
		this.message = message;
	}
	
	

}
