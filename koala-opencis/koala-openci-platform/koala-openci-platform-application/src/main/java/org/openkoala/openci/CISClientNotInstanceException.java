package org.openkoala.openci;

public class CISClientNotInstanceException extends RuntimeException {

	private static final long serialVersionUID = 8371517322768646485L;

	public CISClientNotInstanceException() {
		super();
	}

	public CISClientNotInstanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CISClientNotInstanceException(String message) {
		super(message);
	}

	public CISClientNotInstanceException(Throwable cause) {
		super(cause);
	}

}
