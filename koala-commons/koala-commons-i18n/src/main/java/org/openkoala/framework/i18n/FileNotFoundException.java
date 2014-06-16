package org.openkoala.framework.i18n;

public class FileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4141797733973789585L;

	public FileNotFoundException() {
		super();
	}

	public FileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileNotFoundException(String message) {
		super(message);
	}

	public FileNotFoundException(Throwable cause) {
		super(cause);
	}

}
