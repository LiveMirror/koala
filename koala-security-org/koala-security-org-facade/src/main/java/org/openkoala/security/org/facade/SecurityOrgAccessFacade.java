package org.openkoala.security.org.facade;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;
import org.openkoala.security.org.facade.dto.OrganizationScopeDTO;

import java.util.List;

public interface SecurityOrgAccessFacade {
	
	List<OrganizationScopeDTO> findAllOrganizationScopesTree();

    InvokeResult pagingQueryEmployeeUsers(int pageIndex, int pageSize, EmployeeUserDTO queryEmployeeUserCondition);

}
