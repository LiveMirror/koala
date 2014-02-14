package org.openkoala.openci.web.dto;

public class ResultMsgDto extends ResultDto {

	private String msg;
	
	public static ResultMsgDto createSuccess(String msg) {
		return new ResultMsgDto(true, msg);
	}
	
	public static ResultDto createFailure(String msg) {
		return new ResultMsgDto(false, msg);
	} 
	
	public ResultMsgDto(boolean result, String msg) {
		super(result);
		this.msg = msg;
	}

	public ResultMsgDto() {
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
