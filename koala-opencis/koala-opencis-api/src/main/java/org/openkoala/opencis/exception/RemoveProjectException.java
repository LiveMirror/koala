package org.openkoala.opencis.exception;

import org.openkoala.opencis.CISClientBaseRuntimeException;

public class RemoveProjectException extends CISClientBaseRuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public RemoveProjectException(){
		super();
	}
	
	public RemoveProjectException(String message){
		super(message);
	}
	
	public RemoveProjectException(String message, Throwable cause){
		super(message, cause);
	}

}
