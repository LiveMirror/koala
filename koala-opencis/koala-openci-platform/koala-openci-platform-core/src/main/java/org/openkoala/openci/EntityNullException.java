package org.openkoala.openci;

public class EntityNullException extends RuntimeException {

	private static final long serialVersionUID = 5155042229072278580L;

	public EntityNullException() {
		super();
	}

	public EntityNullException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNullException(String message) {
		super(message);
	}

	public EntityNullException(Throwable cause) {
		super(cause);
	}
	
}
