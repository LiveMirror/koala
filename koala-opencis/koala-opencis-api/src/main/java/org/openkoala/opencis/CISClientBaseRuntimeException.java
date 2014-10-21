package org.openkoala.opencis;


public class CISClientBaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 2389484663291825626L;
	
	public CISClientBaseRuntimeException(){
		super();
	}

    public CISClientBaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public CISClientBaseRuntimeException(String message){
		super(message);
	}
	
	public CISClientBaseRuntimeException(String message, Throwable cause){
		super(message, cause);
	}

}
