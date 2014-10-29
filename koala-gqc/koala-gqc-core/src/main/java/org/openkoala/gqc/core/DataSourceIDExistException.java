package org.openkoala.gqc.core;

public class DataSourceIDExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1121011310325737269L;

	public DataSourceIDExistException() {
	}

	public DataSourceIDExistException(String message) {
		super(message);
	}

	public DataSourceIDExistException(Throwable cause) {
		super(cause);
	}

	public DataSourceIDExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
