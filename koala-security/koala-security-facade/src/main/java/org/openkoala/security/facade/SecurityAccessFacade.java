package org.openkoala.security.facade;

import java.util.List;
import java.util.Set;

import org.dayatang.querychannel.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.facade.dto.*;

public interface SecurityAccessFacade {

    /**
     * 根据用户ID获取用户
     *
     * @param userId 用户ID
     * @return
     */
    UserDTO getUserById(Long userId);

    /**
     * 根据用户名查找所有的角色
     *
     * @param userAccount 用户名
     * @return
     */
    InvokeResult findRolesByUserAccount(String userAccount);

    /**
     * 根据用户名查找该
     *
     * @param userAccount
     * @return
     */
    List<MenuResourceDTO> findMenuResourceByUserAccount(String userAccount);

    /**
     * 分页查询用户信息
     *
     * @param pageIndex
     * @param pageSize
     * @param queryUserCondition
     * @return
     */
    Page<UserDTO> pagingQueryUsers(int pageIndex, int pageSize, UserDTO queryUserCondition);

    /**
     * 分页查询角色信息
     *
     * @param pageIndex
     * @param pageSize
     * @param queryRoleCondition
     * @return
     */
    Page<RoleDTO> pagingQueryRoles(int pageIndex, int pageSize, RoleDTO queryRoleCondition);

    /**
     * 分页查询权限信息
     *
     * @param pageIndex
     * @param pageSize
     * @param queryPermissionCondition
     * @return
     */
    Page<PermissionDTO> pagingQueryPermissions(int pageIndex, int pageSize, PermissionDTO queryPermissionCondition);

    /**
     * 查询某个角色下用户的菜单资源。
     *
     * @param userAccount
     * @param roleName
     * @return
     */
    InvokeResult findMenuResourceByUserAsRole(String userAccount, String roleName);

    InvokeResult findAllMenusTree();

    Page<RoleDTO> pagingQueryNotGrantRoles(int pageIndex, int pageSize, RoleDTO queryRoleCondition, Long userId);

    Page<PermissionDTO> pagingQueryGrantPermissionByUserId(int pageIndex, int pageSize, Long userId);

    Page<RoleDTO> pagingQueryGrantRolesByUserId(int pageIndex, int pageSize, Long userId);

    Page<PermissionDTO> pagingQueryNotGrantPermissionsByRoleId(int pageIndex, int pageSize, Long roleId);

    Page<PermissionDTO> pagingQueryGrantPermissionsByRoleId(int pageIndex, int pageSize, Long roleId);

    Page<UrlAccessResourceDTO> pagingQueryUrlAccessResources(int pageIndex, int pageSize, UrlAccessResourceDTO queryUrlAccessResourceCondition);

    /**
     * 根据URL或者菜单类型查找所有的权限
     *
     * @return
     */
    Set<PermissionDTO> findPermissionsByMenuOrUrl();

    /**
     * 查找所有的角色
     *
     * @return
     */
    Set<RoleDTO> findRolesByMenuOrUrl();

    List<UrlAuthorityDTO> findAllUrlAccessResources();

    /**
     * 根据角色ID查找所有的URL访问资源。
     *
     * @param pageIndex
     * @param pageSize
     * @param roleId
     * @return
     */
    Page<UrlAccessResourceDTO> pagingQueryGrantUrlAccessResourcesByRoleId(int pageIndex, int pageSize, Long roleId);

    /**
     * 根据角色ID查找所有没有授权的URL访问资源。
     *
     * @param pageIndex
     * @param pageSize
     * @param roleId
     * @return
     */
    Page<UrlAccessResourceDTO> pagingQueryNotGrantUrlAccessResourcesByRoleId(int pageIndex, int pageSize, Long roleId);

    /**
     * 根据URL访问资源分页查询已经授权的权限。
     *
     * @param pageIndex
     * @param pageSize
     * @param urlAccessResourceId
     * @return
     */
    Page<PermissionDTO> pagingQueryGrantPermissionsByUrlAccessResourceId(int pageIndex, int pageSize, Long urlAccessResourceId);

    /**
     * 根据URL访问资源分页查询没有授权的权限。
     *
     * @param pageIndex
     * @param pageSize
     * @param urlAccessResourceId
     * @return
     */
    Page<PermissionDTO> pagingQueryNotGrantPermissionsByUrlAccessResourceId(int pageIndex, int pageSize, Long urlAccessResourceId);

    /**
     * 根据角色ID查询菜单树（已经选择的有选择标识）。
     *
     * @param roleId 角色ID
     * @return
     */
    InvokeResult findMenuResourceTreeSelectItemByRoleId(Long roleId);

    Page<PermissionDTO> pagingQueryGrantPermissionsByMenuResourceId(int pageIndex, int pageSize, Long menuResourceId);

    Page<PermissionDTO> pagingQueryNotGrantPermissionsByMenuResourceId(int pageIndex, int pageSize, Long menuResourceId);

    Page<PermissionDTO> pagingQueryNotGrantPermissionsByUserId(int pageIndex, int pageSize, PermissionDTO queryPermissionCondition, Long userId);

    Page<PageElementResourceDTO> pagingQueryPageElementResources(int pageIndex, int pageSize, PageElementResourceDTO queryPageElementResourceCondition);

    Page<PageElementResourceDTO> pagingQueryGrantPageElementResourcesByRoleId(int pageIndex, int pageSize, Long roleId);

    Page<PageElementResourceDTO> pagingQueryNotGrantPageElementResourcesByRoleId(int pageIndex, int pageSize, Long roleId);

    Page<PermissionDTO> pagingQueryGrantPermissionsByPageElementResourceId(int pageIndex, int pageSize, Long pageElementResourceId);

    Page<PermissionDTO> pagingQueryNotGrantPermissionsByPageElementResourceId(int pageIndex, int pageSize, Long pageElementResourceId);

    /**
     * 通过账户查找用户
     *
     * @param userAccount 账户
     * @return
     */
    UserDTO getUserByUserAccount(String userAccount);

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return
     */
    UserDTO getUserByEmail(String email);

    /**
     * 通过联系电话查询用户
     *
     * @param telePhone 联系电话
     * @return
     */
    UserDTO getUserByTelePhone(String telePhone);

    /**
     * 根据账户和角色名称查找所有权限
     *
     * @param userAccount
     * @param roleName
     * @return
     */
    Set<PermissionDTO> findPermissionsByUserAccountAndRoleName(String userAccount, String roleName);

    Page<RoleDTO> pagingQueryRolesOfUser(int pageIndex, int pageSize, String userAccount);

    /**
     * 根据账户查询用户详细
     *
     * @param userAccount 账户
     * @return
     */
    InvokeResult getuserDetail(String userAccount);

    /**
     * 检测角色名是否存在
     *
     * @param roleName
     * @return
     */
    boolean checkRoleByName(String roleName);

    /**
     * 检测用户是否有某个角色
     *
     * @param userAccount
     * @param roleName
     * @return
     */
    boolean checkUserIsHaveRole(String userAccount, String roleName);

    /**
     * 根据用户ID查找用户信息：包含用户本身信息，用户授权信息（即：拥有的角色、拥有的权限）。
     *
     * @param userId
     * @return
     */
    InvokeResult findInfoOfUser(Long userId);

    /**
     * 根据角色ID查找角色信息：包含角色本身信息，权限信息，权限资源信息（菜单资源、URL访问资源、页面元素资源）
     *
     * @param roleId
     * @return
     */
    InvokeResult findInfOfRole(Long roleId);

    InvokeResult findInfOfPermission(Long permissionId);

    /**
     * @param pageIndex
     * @param pageSize
     * @param roleId
     * @param queryUrlAccessResourceCondition
     * @return
     */
    Page<UrlAccessResourceDTO> pagingQueryGrantUrlAccessResourcesByRoleId(int pageIndex, int pageSize, Long roleId, UrlAccessResourceDTO queryUrlAccessResourceCondition);

    Page<UrlAccessResourceDTO> pagingQueryNotGrantUrlAccessResourcesByRoleId(int pageIndex, int pageSize, Long roleId, UrlAccessResourceDTO queryUrlAccessResourceCondition);

    Page<PageElementResourceDTO> pagingQueryGrantPageElementResourcesByRoleId(int pageIndex, int pageSize, Long roleId, PageElementResourceDTO queryPageElementResourceCondition);

    Page<PermissionDTO> pagingQueryGrantPermissionsByRoleId(int pageIndex, int pageSize, Long roleId, PermissionDTO queryPermissionCondition);

    Page<PageElementResourceDTO> pagingQueryNotGrantPageElementResourcesByRoleId(int pageIndex, int pageSize, Long roleId, PageElementResourceDTO queryPageElementResourceCondition);

    Page<PermissionDTO> pagingQueryNotGrantPermissionsByRoleId(int pageIndex, int pageSize, Long roleId, PermissionDTO queryPermissionCondition);

    Page<PermissionDTO> pagingQueryNotGrantPermissionsByMenuResourceId(int pageIndex, int pageSize, Long menuResourceId, PermissionDTO queryPermissionCondition);

    Page<PermissionDTO> pagingQueryNotGrantPermissionsByPageElementResourceId(int pageIndex, int pageSize, Long pageElementResourceId, PermissionDTO queryPermissionCondition);

    Page<PermissionDTO> pagingQueryNotGrantPermissionsByUrlAccessResourceId(int pageIndex, int pageSize, Long urlAccessResourceId, PermissionDTO queryPermissionCondition);
}
