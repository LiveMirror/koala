package org.openkoala.auth.application;

import java.util.List;

import org.dayatang.querychannel.Page;
import org.openkoala.auth.application.vo.QueryConditionVO;
import org.openkoala.auth.application.vo.ResourceVO;
import org.openkoala.auth.application.vo.RoleVO;
import org.openkoala.auth.application.vo.UserVO;

public interface RoleApplication {

    /**
     * 根据角色ID获取角色信息
     * @param roleId
     * @return
     */
    RoleVO getRole(Long roleId);

    /**
     * 保存角色 
     * @param roleVO
     * @return
     */
    RoleVO saveRole(RoleVO roleVO);

    /**
     * 修改角色 
     * @param roleVO
     */
    void updateRole(RoleVO roleVO);

    /**
     * 根据ID删除角色 
     * @param roleId
     */
    void removeRole(Long roleId);

    /**
     * 查询所有角色 
     * @return
     */
    List<RoleVO> findAllRole();

    /**
     * 分页查询角色
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<RoleVO> pageQueryRole(int currentPage, int pageSize);

    /**
     * 为角色分配用户
     * @param roleVO
     * @param userVO
     */
    void assignUser(RoleVO roleVO, UserVO userVO);

    /**
     * 为角色分配一个菜单资源
     * @param roleVO
     * @param menuResVO
     */
    void assignMenuResource(RoleVO roleVO, ResourceVO menuResVO);

    /**
     * 为角色分配多个菜单资源
     * @param roleVO
     * @param menuResVOs
     */
    void assignMenuResource(RoleVO roleVO, List<ResourceVO> menuResVOs);

    /**
     * 为角色分配一个资源
     * @param roleVO
     * @param resourceVO
     */
    void assignResource(RoleVO roleVO, ResourceVO resourceVO);

    /**
     * 为角色分配多个资源
     * @param roleVO
     * @param resourceVOs
     */
    void assignURLResource(RoleVO roleVO, List<ResourceVO> resourceVOs);

    /**
     * 根据用户账号查找角色
     * @param userAccount
     * @return
     */
    List<RoleVO> findRoleByUserAccount(String userAccount);
    
    
    /**
     * 根据角色名查询其所拥有的资源
     * @param roleName
     * @return
     */
    List<ResourceVO> findResourceByRoleName(String roleName);

    /**
     * 根据角色查找用户
     * @param roleVO
     * @return
     */
    List<UserVO> findUserByRole(RoleVO roleVO);

    /**
     * 根据角色查找菜单
     * @param roleVO
     * @return
     */
    List<ResourceVO> findMenuByRole(RoleVO roleVO);

    /**
     * 根据角色查找所有资源
     * @param roleVO
     * @return
     */
    List<ResourceVO> findAllResourceByRole(RoleVO roleVO);

    /**
     * 根据角色查找资源
     * @param roleVO
     * @return
     */
    List<ResourceVO> findResourceByRole(RoleVO roleVO);

    /**
     * 废除菜单
     * @param roleVO
     * @param menus
     */
    void abolishMenu(RoleVO roleVO, List<ResourceVO> menus);

    /**
     * 废除资源
     * @param roleVO
     * @param urls
     */
    void abolishResource(RoleVO roleVO, List<ResourceVO> urls);

    /**
     * 废除用户
     * @param roleVO
     * @param users
     */
    void abolishUser(RoleVO roleVO, List<UserVO> users);

    /**
     * 根据角色查找没有被分配的用户
     * @param currentPage
     * @param pageSize
     * @param userVO
     * @param roleVO
     * @return
     */
    Page<UserVO> pageQueryNotAssignUserByRole(int currentPage, int pageSize, UserVO userVO, RoleVO roleVO);

    /**
     * 根据查询条件分页查找角色
     * @param currentPage
     * @param pageSize
     * @param query
     * @return
     */
    Page<RoleVO> pageQueryByRoleCustom(int currentPage, int pageSize, QueryConditionVO query);
    
    /**
     * 分页查询用户所拥有的角色
     * @param currentPage
     * @param pageSize
     * @param useraccount
     * @return
     */
    Page<RoleVO> pageQueryRoleByUseraccount(int currentPage, int pageSize, String useraccount);
    
    /**
     * 分页查询角色所拥有的用户
     * @param roleVO
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<UserVO> pageQueryUserByRole(RoleVO roleVO, int currentPage, int pageSize);
    
    void removeRoles(Long[] roleIds);
    
    Page<RoleVO> pageQueryNotAssignRoleByUseraccount(int currentPage, int pageSize, 
			String useraccount, RoleVO roleVO);
    
}
