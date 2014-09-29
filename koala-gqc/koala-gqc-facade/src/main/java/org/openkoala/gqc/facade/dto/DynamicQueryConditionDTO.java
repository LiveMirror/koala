package org.openkoala.gqc.facade.dto;

public class DynamicQueryConditionDTO extends QueryConditionDTO {

	private static final long serialVersionUID = 6246789259896062885L;
	
	private String label;
	
	private WidgetTypeDTO widgetTypeDTO;
	
	private String value;
	
	private String startValue;
	
	private String endValue;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public WidgetTypeDTO getWidgetTypeDTO() {
		return widgetTypeDTO;
	}

	public void setWidgetTypeDTO(WidgetTypeDTO widgetTypeDTO) {
		this.widgetTypeDTO = widgetTypeDTO;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStartValue() {
		return startValue;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

	public String getEndValue() {
		return endValue;
	}

	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}
	
}
