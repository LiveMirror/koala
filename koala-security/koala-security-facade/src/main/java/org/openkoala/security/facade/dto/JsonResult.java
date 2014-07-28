package org.openkoala.security.facade.dto;

import java.io.Serializable;

/**
 * 所有的控制器都是通过该对象返回JSON到页面。
 * 
 * @author luzhao
 * 
 */
public class JsonResult implements Serializable{

	private static final long serialVersionUID = 2865490206901460730L;

	private Object data = null;
	
	private boolean success = true;
	
	private String message = "";

	public Object getData() {
		return data;
	}

	public void setData(Object object) {
		this.data = object;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
