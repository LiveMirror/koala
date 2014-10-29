package org.openkoala.gqc.facade.dto;

import java.io.Serializable;

public class FieldDetailDTO implements Serializable {

	private static final long serialVersionUID = -6712912171901340171L;
	
	private String fieldName;
	
	private String label;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
