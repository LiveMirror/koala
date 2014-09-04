package org.openkoala.organisation.facade.dto;

/**
 * Created by yyang on 14-8-14. 
 * 去掉了泛型。
 */
public class InvokeResult {

	private static final String SUCCESS = "success";
	
	private Object data;

	private String message;

	private boolean hasErrors;

	public static InvokeResult success(Object data) {
		InvokeResult result = new InvokeResult();
		result.data = data;
		result.message = SUCCESS;
		result.hasErrors = false;
		return result;
	}

	public static InvokeResult success() {
		InvokeResult result = new InvokeResult();
		result.message = SUCCESS;
		result.hasErrors = false;
		return result;
	}

	public static InvokeResult failure(String message) {
		InvokeResult result = new InvokeResult();
		result.hasErrors = true;
		result.message = message;
		return result;
	}

	public Object getData() {
		return data;
	}

	public String getMessage() {
		return message;
	}

	public boolean isHasErrors() {
		return hasErrors;
	}

	public boolean isSuccess() {
		return !hasErrors;
	}
}
