package org.openkoala.security.org.facade.dto;

import org.openkoala.security.facade.dto.RoleDTO;

/**
 * Created by luzhao on 14-9-22.
 */
public class OrgRoleDTO extends RoleDTO {

	private static final long serialVersionUID = -14081653051740223L;
	
	//与组织机构集成需要
    private String employeeUserOrgName;

    public OrgRoleDTO() {
    }

    public OrgRoleDTO(Long id, String name, String description) {
        super(id, name, description);
    }

    public String getEmployeeUserOrgName() {
        return employeeUserOrgName;
    }

    public void setEmployeeUserOrgName(String employeeUserOrgName) {
        this.employeeUserOrgName = employeeUserOrgName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
