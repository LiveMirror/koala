package org.openkoala.security.org.facade;

import org.dayatang.utils.Page;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;
import org.openkoala.security.org.facade.dto.OrgPermissionDTO;
import org.openkoala.security.org.facade.dto.OrgRoleDTO;

public interface SecurityOrgAccessFacade {
	
    Page<EmployeeUserDTO> pagingQueryEmployeeUsers(int pageIndex, int pageSize, EmployeeUserDTO queryEmployeeUserCondition);

    /**
     * 分页查询用户已经拥有的角色
     *
     * @param pageIndex
     * @param pageSize
     * @param userId
     * @return
     */
    Page<OrgRoleDTO> pagingQueryGrantRolesByUserId(int pageIndex, int pageSize, Long userId);

    Page<OrgPermissionDTO> pagingQueryGrantPermissionsByUserId(int pageIndex, int pageSize, Long userId);
}
