package org.openkoala.openci;

public class ToolTypeNotExistException extends RuntimeException {

	private static final long serialVersionUID = 2118447824908814533L;

	public ToolTypeNotExistException() {
		super();
	}

	public ToolTypeNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ToolTypeNotExistException(String message) {
		super(message);
	}

	public ToolTypeNotExistException(Throwable cause) {
		super(cause);
	}
	
}
