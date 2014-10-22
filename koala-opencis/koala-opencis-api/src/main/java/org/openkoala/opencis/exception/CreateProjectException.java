package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class CreateProjectException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public CreateProjectException(){
		super();
	}
	
	public CreateProjectException(String message){
		super(message);
	}
	
	public CreateProjectException(String message, Throwable cause){
		super(message, cause);
	}

}
