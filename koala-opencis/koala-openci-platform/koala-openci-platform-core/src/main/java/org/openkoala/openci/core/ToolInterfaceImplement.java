package org.openkoala.openci.core;

import java.lang.String;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dayatang.domain.AbstractEntity;

@Entity
@Table(name = "tool_interface_implements")
public class ToolInterfaceImplement extends AbstractEntity {

	private static final long serialVersionUID = -8821892076942681689L;

	
	@ManyToOne
	@JoinColumn(name = "tool_id")
	private Tool tool;

	
	@Enumerated(EnumType.STRING)
	@Column(name = "tool_interface")
	private ToolInterface toolInterface;

	
	@Column(name = "is_success")
	private boolean success;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "execute_date")
	private Date executeDate;

	
	@Lob
	private String record;

	public ToolInterfaceImplement(Tool tool, ToolInterface toolInterface, boolean isSuccess, String record) {
		this.tool = tool;
		this.toolInterface = toolInterface;
		this.success = isSuccess;
		this.record = record;
		this.executeDate = new Date();
	}

	public ToolInterfaceImplement() {
	}

	protected Tool getTool() {
		return tool;
	}

	public ToolInterface getToolInterface() {
		return toolInterface;
	}

	public boolean isSuccess() {
		return success;
	}

	public Date getExecuteDate() {
		return executeDate;
	}

	public String getRecord() {
		return record;
	}

	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	protected void setTool(Tool tool) {
		this.tool = tool;
	}

	protected void setToolInterface(ToolInterface toolInterface) {
		this.toolInterface = toolInterface;
	}

	protected void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}

	@Override
	public String toString() {
		return getToolInterface().toString();
	}

	@Override
	public String[] businessKeys() {
		return new String[] {"toolInterface", "executeDate"};
	}

}
