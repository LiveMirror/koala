package org.openkoala.security.org.facade;

import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;
import org.openkoala.security.org.facade.dto.OrganizationScopeDTO;

import java.util.List;

public interface SecurityOrgAccessFacade {
	
    InvokeResult pagingQueryEmployeeUsers(int pageIndex, int pageSize, EmployeeUserDTO queryEmployeeUserCondition);

    /**
     * 分页查询用户已经拥有的角色
     *
     * @param pageIndex
     * @param pageSize
     * @param userId
     * @return
     */
    InvokeResult pagingQueryGrantRolesByUserId(int pageIndex, int pageSize, Long userId);

    InvokeResult pagingQueryGrantPermissionsByUserId(int pageIndex, int pageSize, Long userId);
}
