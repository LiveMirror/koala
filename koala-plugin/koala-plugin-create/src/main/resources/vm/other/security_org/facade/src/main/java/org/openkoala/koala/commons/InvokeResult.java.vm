package org.openkoala.koala.commons;

/**
 * Created by yyang on 14-8-14. 
 * 去掉了泛型。
 */
public class InvokeResult {

	private Object data;

	private String errorMessage;

	private boolean hasErrors;

	public static InvokeResult success(Object data) {
		InvokeResult result = new InvokeResult();
		result.data = data;
		result.hasErrors = false;
		return result;
	}

	public static InvokeResult success() {
		InvokeResult result = new InvokeResult();
		result.hasErrors = false;
		return result;
	}

	public static InvokeResult failure(String message) {
		InvokeResult result = new InvokeResult();
		result.hasErrors = true;
		result.errorMessage = message;
		return result;
	}

	public Object getData() {
		return data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isHasErrors() {
		return hasErrors;
	}

	public boolean isSuccess() {
		return !hasErrors;
	}
}
