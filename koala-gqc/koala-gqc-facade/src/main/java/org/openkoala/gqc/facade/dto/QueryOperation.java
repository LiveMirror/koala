package org.openkoala.gqc.facade.dto;

public enum QueryOperationDTO {
	
	EQ("="),
	GE(">="),
	GT(">"),
	NE("!="),
	LE("<="),
	LT("<"),
	LIKE("like"),
	IN("in"),
	BETWEEN("between");
	
	/**
	 * 操作
	 */
	private String operator;
	
	private QueryOperationDTO(String operator) {
		this.operator = operator;
	}
	
	public String getOperator() {
		return operator;
	}

}
