package org.openkoala.security.org.facade.dto;

import org.openkoala.security.facade.dto.UserDTO;

import java.util.Date;

/**
 * Created by luzhao on 14-8-28.
 */
public class EmployeeUserDTO extends UserDTO {

	private static final long serialVersionUID = -6799621455814324698L;

	/**
	 * 员工名称，用于页面显示
	 */
	private String employeeName;

	private String employeeOrgName;

    private Long employeeOrgId;

	public EmployeeUserDTO() {
	}

	public EmployeeUserDTO(Long id, int version, String name, String userAccount, Date createDate, String description, String createOwner, Date lastModifyTime, Boolean disabled) {
		super(id, version, name, userAccount, createDate, description, createOwner, lastModifyTime, disabled);
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeOrgName() {
		return employeeOrgName;
	}

	public void setEmployeeOrgName(String employeeOrgName) {
		this.employeeOrgName = employeeOrgName;
	}

    public Long getEmployeeOrgId() {
        return employeeOrgId;
    }

    public void setEmployeeOrgId(Long employeeOrgId) {
        this.employeeOrgId = employeeOrgId;
    }
}
