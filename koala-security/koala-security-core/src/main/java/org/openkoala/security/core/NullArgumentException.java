package org.openkoala.security.core;

public class NullArgumentException extends SecurityException {

	private static final long serialVersionUID = -8533244469539459618L;

	public NullArgumentException(String argName) {
		super((argName == null ? "Argment" : argName) + "must not be null.");
	}

}
