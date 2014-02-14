package org.openkoala.openci.web.dto;

public class ResultDto {

	private boolean result;

	public static ResultDto createSuccess() {
		return new ResultDto(true);
	}
	
	public static ResultDto createFailure() {
		return new ResultDto(false);
	} 
	
	public ResultDto(boolean result) {
		this.result = result;
	}
	
	public ResultDto() {
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	
	
	
}
