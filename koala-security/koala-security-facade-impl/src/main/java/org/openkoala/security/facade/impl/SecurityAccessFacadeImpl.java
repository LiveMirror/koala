package org.openkoala.security.facade.impl;

import java.text.MessageFormat;
import java.util.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.*;

import com.google.common.collect.Sets;

import org.openkoala.security.facade.impl.assembler.MenuResourceAssembler;
import org.openkoala.security.facade.impl.assembler.PermissionAssembler;
import org.openkoala.security.facade.impl.assembler.RoleAssembler;
import org.openkoala.security.facade.impl.assembler.UserAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class SecurityAccessFacadeImpl implements SecurityAccessFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityAccessFacadeImpl.class);

	@Inject
	private SecurityAccessApplication securityAccessApplication;

	private QueryChannelService queryChannelService;

	public QueryChannelService getQueryChannelService() {
		if (queryChannelService == null) {
			queryChannelService = InstanceFactory.getInstance(QueryChannelService.class,"queryChannel_security");
		}
		return queryChannelService;
	}

	public UserDTO getUserById(Long userId) {
		User user = securityAccessApplication.getUserById(userId);
		return UserAssembler.toUserDTO(user);
	}

	@Override
	public UserDTO getUserByUserAccount(String userAccount) {
		User user = securityAccessApplication.getUserByUserAccount(userAccount);
		return user != null ? UserAssembler.toUserDTO(user) : null;
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		User user = securityAccessApplication.getUserByEmail(email);
        return user != null ? UserAssembler.toUserDTO(user) : null;
	}

	@Override
	public UserDTO getUserByTelePhone(String telePhone) {
		User user = securityAccessApplication.getUserByTelePhone(telePhone);
        return user != null ? UserAssembler.toUserDTO(user) : null;
	}

	public InvokeResult findRolesByUserAccount(String userAccount) {
		try {
			List<RoleDTO> results = new ArrayList<RoleDTO>();
			List<Role> roles = securityAccessApplication.findAllRolesByUserAccount(userAccount);
			for (Role role : roles) {
                RoleDTO result = RoleAssembler.toRoleDTO(role);
				results.add(result);
			}
			return InvokeResult.success(results);
		} catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
			return	InvokeResult.failure("根据用户名查找所有的角色失败。");
		}

	}

	public List<MenuResourceDTO> findMenuResourceByUserAccount(String userAccount) {

		List<MenuResourceDTO> results = new ArrayList<MenuResourceDTO>();

		List<MenuResource> menuResources = securityAccessApplication.findMenuResourceByUserAccount(userAccount);

		for (MenuResource menuResource : menuResources) {
			MenuResourceDTO result = MenuResourceAssembler.toMenuResourceDTO(menuResource);
			results.add(result);
		}

		return results;
	}

	/**TODO
	 *
	 * @param userAccount
	 * @param roleName
	 * @return
	 */
	@Override
	public InvokeResult findMenuResourceByUserAsRole(String userAccount, String roleName) {

		Set<Authority> authorities = new HashSet<Authority>();
		Role role = securityAccessApplication.getRoleBy(roleName);
		// securityAccessApplication.checkAuthorization(userAccount, role); //TODO 检查
		if (role != null) {
			authorities.add(role);
			authorities.addAll(role.getPermissions());
		}
		authorities.addAll(User.findAllPermissionsBy(userAccount));
		// 1、User 的角色、2、User本身的Permission 3、角色所关联的Permission。
		List<MenuResourceDTO> results = findTopMenuResourceDTOByUserAccountAsRole(authorities);
		List<MenuResourceDTO> childrenMenuResources = findAllMenuResourceDTOByUserAccountAsRole(authorities);

		List<MenuResourceDTO> all = new ArrayList<MenuResourceDTO>();
		all.addAll(results);
		all.addAll(childrenMenuResources);

		addMenuChildrenToParent(all);

		return InvokeResult.success(results);

	}

	@Override
	public InvokeResult findAllMenusTree() {
		List<MenuResourceDTO> results = findTopMenuResource();
		List<MenuResourceDTO> childrenMenuResources = findChidrenMenuResource();
		List<MenuResourceDTO> all = new ArrayList<MenuResourceDTO>();
		all.addAll(results);
		all.addAll(childrenMenuResources);
		addMenuChildrenToParent(all);
		return InvokeResult.success(results);
	}

    @Override
    public InvokeResult findMenuResourceTreeSelectItemByRoleId(Long roleId) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_resource.id,_resource.name) FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN  _resourceAssignment.resource _resource WHERE TYPE(_resource) = :resourceType AND _authority.id = :authorityId");

            List<MenuResourceDTO> allMenResourcesAsRole = getQueryChannelService()//
                    .createJpqlQuery(jpql.toString())//
                    .addParameter("resourceType", MenuResource.class)//
                    .addParameter("authorityId", roleId)//
                    .list();

            InvokeResult menuResult = findAllMenusTree();
            List<MenuResourceDTO> allMenuResources = (List<MenuResourceDTO>) menuResult.getData();

            for (MenuResourceDTO menuResourceDTO : allMenuResources) {
                if (!menuResourceDTO.getChildren().isEmpty()) {
                    for (MenuResourceDTO childMenuResourceDTO : menuResourceDTO
                            .getChildren()) {
                        childMenuResourceDTO.setChecked(allMenResourcesAsRole
                                .contains(childMenuResourceDTO));
                    }
                }
                menuResourceDTO.setChecked(allMenResourcesAsRole.contains(menuResourceDTO));
            }
            return InvokeResult.success(allMenuResources);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return InvokeResult.failure("根据角色ID查询菜单权限资源树带有已经选中项失败");
        }
    }

    // TODO 待测试，感觉有问题。
	@Override
	public Set<PermissionDTO> findPermissionsByMenuOrUrl() {

		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier,_authority.description,_resource.url) FROM ResourceAssignment _resourceAssignment  JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE Type(_authority) = Permission AND TYPE(_resource) = MenuResource OR TYPE(_resource) = UrlAccessResource");

		List<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.list();

		return Sets.newHashSet(results);
	}

    // TODO 待测试，感觉有问题。
	@Override
	public Set<RoleDTO> findRolesByMenuOrUrl() {

		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_authority.id, _authority.name,_authority.description, _resource.url) FROM ResourceAssignment _resourceAssignment  JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE TYPE(_authority) = Role AND (TYPE(_resource) = MenuResource OR TYPE(_resource) = UrlAccessResource)");

		List<RoleDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.list();

		return Sets.newHashSet(results);
	}

	/**
	 * 查询出所有的Url访问资源，并且有Role 和Permission
	 */
	@Override
	public List<UrlAuthorityDTO> findAllUrlAccessResources() {

		List<UrlAuthorityDTO> results = findAllUrls();
        List<UrlRoleDTO> urlRoles = findAllUrlRoles();
        List<UrlPermissionDTO> urlPermissions = findAllUrlPermissions();
        // TODO 等待优化
        for(UrlAuthorityDTO result : results){
            for(UrlRoleDTO urlRole :urlRoles){
               if(result.getUrl().equals(urlRole.getUrl())){
                    result.addRole(urlRole.getRole());
                }
            }
            for(UrlPermissionDTO urlPermission : urlPermissions){
                if(result.getUrl().equals(urlPermission.getUrl())){
                    result.addPermission(urlPermission.getPermission());
                }
            }
        }

		return results;
	}

    /**
     * 去除重复的URL
     * @return
     */
    private List<UrlAuthorityDTO> findAllUrls() {
        StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.UrlAuthorityDTO(_resource.url)");
        jpql = fromResourceAssigment(jpql);
        jpql.append(" GROUP BY _resource.url");
        return getQueryChannelService().createJpqlQuery(jpql.toString())//
                .addParameter("resourceType", UrlAccessResource.class)//
                .list();

    }

    /**
     * Url-Role
     * @return
     */
    private List<UrlRoleDTO> findAllUrlRoles() {

        StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.UrlRoleDTO(_resource.url, _authority.name)");
        jpql = fromResourceAssigment(jpql);
        jpql.append(" AND TYPE(_authority) = :authorityType");

        return getQueryChannelService()//
                .createJpqlQuery(jpql.toString())//
                .addParameter("authorityType", Role.class)//
                .addParameter("resourceType", UrlAccessResource.class)//
                .list();

    }

    /**
     * 查询findAllUrlRoles和findAllUrlPermissions方法中都有一样的查询条件，抽取出来。
     *
     * @param jpql
     * @return
     */
    private StringBuilder fromResourceAssigment(StringBuilder jpql) {
        jpql.append(" FROM ResourceAssignment _resourceAssignment  JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource");
        jpql.append(" WHERE TYPE(_resource) = :resourceType");
        return jpql;
    }

    /**
     * Url-Permission
     * @return
     */
    private List<UrlPermissionDTO> findAllUrlPermissions() {

        StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.UrlPermissionDTO(_resource.url, _authority.identifier)");
        jpql = fromResourceAssigment(jpql);
        jpql.append(" AND TYPE(_authority) = :authorityType");

        return getQueryChannelService()//
                .createJpqlQuery(jpql.toString())//
                .addParameter("authorityType", Permission.class)//
                .addParameter("resourceType", UrlAccessResource.class)//
                .list();

    }

	@Override
	public InvokeResult pagingQueryUsers(int pageIndex, int pageSize, UserDTO queryUserCondition) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.UserDTO(_user.id, _user.version, _user.name, _user.userAccount, _user.createDate, _user.description, _user.lastLoginTime, _user.createOwner, _user.lastModifyTime, _user.disabled) FROM User _user where 1=1");

		assembleUserJpqlAndConditionValues(queryUserCondition, jpql, "_user", conditionVals);

		Page<UserDTO> results = getQueryChannelService().createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(pageIndex, pageSize)//
				.pagedList();

		return InvokeResult.success(results);
	}

    @Override
    public InvokeResult pagingQueryRoles(int pageIndex, int pageSize, RoleDTO queryRoleCondition) {
        Map<String, Object> conditionVals = new HashMap<String, Object>();
        StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_role.id, _role.name, _role.description) FROM Role _role where 1 = 1");

        assembleRoleJpqlAndConditionValues(queryRoleCondition, jpql, "_role", conditionVals);

        Page<RoleDTO> results = getQueryChannelService().createJpqlQuery(jpql.toString())//
                .setParameters(conditionVals)//
                .setPage(pageIndex, pageSize)//
                .pagedList();

        return InvokeResult.success(results);
    }

    @Override
    public InvokeResult pagingQueryPermissions(int pageIndex, int pageSize,
                                                      PermissionDTO queryPermissionCondition) {
        Map<String, Object> conditionVals = new HashMap<String, Object>();
        StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id, _permission.name, _permission.identifier, _permission.description) FROM Permission _permission where 1 = 1");

        assemblePermissionJpqlAndConditionValues(queryPermissionCondition, jpql, "_permission", conditionVals);

        Page<Permission> results = getQueryChannelService().createJpqlQuery(jpql.toString())//
                .setParameters(conditionVals)//
                .setPage(pageIndex, pageSize)//
                .pagedList();

        return InvokeResult.success(results);
         
    }

	@Override
	public InvokeResult pagingQueryNotGrantRoles(int pageIndex, int pageSize, RoleDTO queryRoleCondition, Long userId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_role.id, _role.name, _role.description)  FROM Role _role");
		Map<String, Object> conditionVals = new HashMap<String, Object>();

		assembleRoleJpqlAndConditionValues(queryRoleCondition, jpql, "_role", conditionVals);

		jpqlHasWhereCondition(jpql);

		jpql.append(" _role.id NOT IN(SELECT _authority.id FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority WHERE _actor.id= :userId)");

		conditionVals.put("userId", userId);
		 Page<RoleDTO> results  = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(pageIndex, pageSize)//
				.pagedList();

		 return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryGrantPermissionByUserId(int pageIndex, int pageSize, Long userId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name, _authority.identifier ,_authority.description)");
		jpql.append(" FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority");
		jpql.append(" WHERE TYPE(_authority) = :authorityType");
		jpql.append(" AND _actor.id = :userId");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userId", userId);
		parameters.put("authorityType", Permission.class);
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryGrantRolesByUserId(int pageIndex, int pageSize, Long userId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_authority.id, _authority.name, _authority.description)");
		jpql.append(" FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority");
		jpql.append(" WHERE TYPE(_authority) = :authorityType");
		jpql.append(" AND _actor.id = :userId");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("authorityType", Role.class);
		parameters.put("userId", userId);
		Page<RoleDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryNotGrantPermissionsByRoleId(int currentPage, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id, _permission.name,_permission.identifier, _permission.description)");
		jpql.append(" FROM Permission _permission WHERE _permission.id NOT IN(SELECT _permission.id FROM Permission _permission JOIN _permission.roles _role WHERE _role.id = :roleId)");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roleId", roleId);
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(currentPage, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryGrantPermissionsByRoleId(int pageIndex, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id, _permission.name,_permission.identifier, _permission.description) FROM Permission _permission JOIN _permission.roles _role WHERE _role.id = :roleId");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roleId", roleId);
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryUrlAccessResources(int pageIndex, int pageSize,UrlAccessResourceDTO queryUrlAccessResourceCondition) {

		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.UrlAccessResourceDTO(_urlAccessResource.id, _urlAccessResource.name, _urlAccessResource.url,_urlAccessResource.description) FROM UrlAccessResource _urlAccessResource where 1 = 1");
		Map<String, Object> conditionVals = new HashMap<String, Object>();

		assembleUrlAccessResourceJpqlAndConditionValues(queryUrlAccessResourceCondition, jpql, "_urlAccessResource",conditionVals);

		Page<UrlAccessResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(pageIndex, pageSize)//
				.pagedList();

		return InvokeResult.success(results);
	}

    /**
     * 不需要TYPE(_authority)的类型 因为主键是唯一的,能够确定是什么具体维度。
     * @param pageIndex
     * @param pageSize
     * @param roleId
     * @return
     */
	@Override
	public InvokeResult pagingQueryGrantUrlAccessResourcesByRoleId(int pageIndex, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.UrlAccessResourceDTO(_resource.id, _resource.name, _resource.url,_resource.description) FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE TYPE(_resource) = :resourceType AND _authority.id = :authorityId");
		Page<UrlAccessResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("resourceType", UrlAccessResource.class)//
				.addParameter("authorityId", roleId)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryNotGrantUrlAccessResourcesByRoleId(int pageIndex, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.UrlAccessResourceDTO(_securityResource.id, _securityResource.name, _securityResource.url,_securityResource.description) FROM SecurityResource _securityResource WHERE TYPE(_securityResource) = :resourceType AND _securityResource.id NOT IN (SELECT _resource.id FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE TYPE(_resource) = :resourceType AND _authority.id = :authorityId)");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("resourceType", UrlAccessResource.class);
		parameters.put("authorityId", roleId);

		Page<UrlAccessResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryGrantPermissionsByUrlAccessResourceId(int pageIndex, int pageSize,
			Long urlAccessResourceId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier, _authority.description) FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE TYPE(_authority) = :authorityType AND _resource.id = :resourceId");
		Page<PermissionDTO> results =getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("authorityType", Permission.class)//
				.addParameter("resourceId", urlAccessResourceId)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryNotGrantPermissionsByUrlAccessResourceId(int pageIndex, int pageSize,Long urlAccessResourceId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name, _authority.identifier,_authority.description) FROM Authority _authority WHERE _authority.id NOT IN(SELECT _authority.id FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE _resource.id = :resourceId AND TYPE(_authority) = :authorityType) AND TYPE(_authority) = :authorityType");
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("resourceId", urlAccessResourceId)//
				.addParameter("authorityType", Permission.class)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryGrantPermissionsByMenuResourceId(int pageIndex, int pageSize, Long menuResourceId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name, _authority.identifier,_authority.description) FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE _resource.id = :resourceId AND TYPE(_authority) = :authorityType");
		 Page<PermissionDTO> results=  getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("resourceId", menuResourceId)//
				.addParameter("authorityType", Permission.class)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
	return	InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryNotGrantPermissionsByMenuResourceId(int pageIndex, int pageSize, Long menuResourceId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier, _authority.description) FROM Authority _authority WHERE _authority.id NOT IN(SELECT _authority.id FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE _resource.id = :resourceId AND TYPE(_authority) = :authorityType)  AND TYPE(_authority) = :authorityType");
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("authorityType", Permission.class)//
				.addParameter("resourceId", menuResourceId)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryNotGrantPermissionsByUserId(int pageIndex, int pageSize,
			PermissionDTO queryPermissionCondition, Long userId) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();

		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id,_permission.name, _permission.identifier,_permission.description)  FROM Permission _permission");

		assemblePermissionJpqlAndConditionValues(queryPermissionCondition, jpql, "_permission", conditionVals);

		jpqlHasWhereCondition(jpql);

		jpql.append("_permission.id NOT IN(SELECT _authority.id FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority WHERE TYPE(_authority) = :authorityType AND _actor.id= :userId)");

		conditionVals.put("authorityType", Permission.class);
		conditionVals.put("userId", userId);

		return InvokeResult.success(getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(pageIndex, pageSize)//
				.pagedList());
	}

	@Override
	public InvokeResult pagingQueryPageElementResources(int page, int pageSize, PageElementResourceDTO queryPageElementCondition) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PageElementResourceDTO(_resource.id,_resource.version, _resource.name,_resource.identifier, _resource.description) FROM PageElementResource _resource WHERE 1 = 1");

        Map<String, Object> conditionVals = new HashMap<String, Object>();
		assemblePageElementResourceJpqlAndConditionValues(queryPageElementCondition, jpql, "_resource",conditionVals);

		Page<PageElementResourceDTO> results =  getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(page, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
		
	}

	@Override
	public InvokeResult pagingQueryGrantPageElementResourcesByRoleId(int pageIndex, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PageElementResourceDTO(_resource.id,_resource.version, _resource.name,_resource.identifier, _resource.description) FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE TYPE(_resource) = :resourceType AND TYPE(_authority) = :authorityType AND _authority.id = :authorityId");
		Page<PageElementResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
                .addParameter("resourceType",PageElementResource.class)//
				.addParameter("authorityType", Role.class)//
				.addParameter("authorityId", roleId)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryNotGrantPageElementResourcesByRoleId(int pageIndex, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PageElementResourceDTO(_pageElementResource.id,_pageElementResource.version, _pageElementResource.name, _pageElementResource.identifier, _pageElementResource.description) FROM PageElementResource _pageElementResource WHERE _pageElementResource.id NOT IN(SELECT _resource.id FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE TYPE(_resource) = :resourceType AND _authority.id = :authorityId ) ");
		Page<PageElementResourceDTO> results =  getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("resourceType", PageElementResource.class)//
				.addParameter("authorityId", roleId)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryGrantPermissionsByPageElementResourceId(int pageIndex, int pageSize, Long pageElementResourceId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier, _authority.description) FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE _resource.id = :resourceId AND TYPE(_authority) = :authorityType");
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("resourceId", pageElementResourceId)//
				.addParameter("authorityType", Permission.class)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

	@Override
	public InvokeResult pagingQueryNotGrantPermissionsByPageElementResourceId(int pageIndex, int pageSize,
			Long pageElementResourceId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id, _permission.name,_permission.identifier, _permission.description) FROM Permission _permission WHERE _permission.id NOT IN(SELECT _authority.id FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource WHERE _resource.id = :resourceId AND TYPE(_authority) = :authorityType)");
		Page<PermissionDTO> results =  getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("resourceId", pageElementResourceId)//
				.addParameter("authorityType", Permission.class)//
				.setPage(pageIndex, pageSize)//
				.pagedList();
		return InvokeResult.success(results);
	}

    @Override
    public Set<PermissionDTO> findPermissionsByUserAccountAndRoleName(String userAccount, String roleName) {
        Role role = Role.getRoleBy(roleName);
        Set<Permission> rolePermissions = role.getPermissions();// TODO 待检测性能。
        List<Permission> userPermissions = User.findAllPermissionsBy(userAccount);
        Set<Permission> permissions = new HashSet<Permission>();
        permissions.addAll(userPermissions);
        permissions.addAll(rolePermissions);

        Set<PermissionDTO> results = new HashSet<PermissionDTO>();
        for (Permission permission : permissions) {
            PermissionDTO result = PermissionAssembler.toPermissionDTO(permission);
            results.add(result);
        }
        return results;
    }

    @Override
    public InvokeResult pagingQueryRolesOfUser(int pageIndex, int pageSize, String userAccount) {
        Page<RoleDTO> results =  getQueryChannelService()//
                .createJpqlQuery("SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_authority.id, _authority.name, _authority.description) FROM Authorization _authorization JOIN  _authorization.actor _actor JOIN _authorization.authority _authority WHERE _actor.userAccount = :userAccount AND TYPE(_authority) = :authorityType GROUP BY _authority.id")
                .addParameter("authorityType", Role.class)//
                .addParameter("userAccount", userAccount)//
                .setPage(pageIndex, pageSize)//
                .pagedList();
        return InvokeResult.success(results);
    }

    @Override
    public InvokeResult getuserDetail(String userAccount) {
        User user = securityAccessApplication.getUserByUserAccount(userAccount);
        UserDTO result = UserAssembler.toUserDTONotPassword(user);
        return InvokeResult.success(result);
    }

	/*------------- Private helper methods  -----------------*/

	private void assembleUserJpqlAndConditionValues(UserDTO queryUserCondition, StringBuilder jpql,
			String conditionPrefix, Map<String, Object> conditionVals) {

		String andCondition = " AND " + conditionPrefix;

		if (null != queryUserCondition.getDisabled() && !"".equals(queryUserCondition.getDisabled())) {
			jpql.append(andCondition);
			jpql.append(".disabled = :disabled");
			conditionVals.put("disabled", queryUserCondition.getDisabled());
		}

		if (!StringUtils.isBlank(queryUserCondition.getName())) {
			jpql.append(andCondition);
			jpql.append(".name LIKE :name");
			conditionVals.put("name", MessageFormat.format("%{0}%", queryUserCondition.getName()));
		}
		if (!StringUtils.isBlank(queryUserCondition.getUserAccount())) {
			jpql.append(andCondition);
			jpql.append(".userAccount LIKE :userAccount");
			conditionVals.put("userAccount", MessageFormat.format("%{0}%", queryUserCondition.getUserAccount()));
		}
		if (!StringUtils.isBlank(queryUserCondition.getEmail())) {
			jpql.append(andCondition);
			jpql.append(".email LIKE :email");
			conditionVals.put("email", MessageFormat.format("%{0}%", queryUserCondition.getEmail()));
		}
		if (!StringUtils.isBlank(queryUserCondition.getTelePhone())) {
			jpql.append(andCondition);
			jpql.append(".telePhone LIKE :telePhone");
			conditionVals.put("telePhone", MessageFormat.format("%{0}%", queryUserCondition.getTelePhone()));
		}
		if (!StringUtils.isBlank(queryUserCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description LIKE :description");
			conditionVals.put("description", MessageFormat.format("%{0}%", queryUserCondition.getDescription()));
		}
	}

	private void assembleRoleJpqlAndConditionValues(RoleDTO queryRoleCondition, StringBuilder jpql,
			String conditionPrefix, Map<String, Object> conditionVals) {

		String andCondition = " AND " + conditionPrefix;

		if (!StringUtils.isBlank(queryRoleCondition.getName())) {
			jpql.append(andCondition);
			jpql.append(".name LIKE :name");
			conditionVals.put("name", MessageFormat.format("%{0}%", queryRoleCondition.getName()));
		}

		if (!StringUtils.isBlank(queryRoleCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description LIKE :description");
			conditionVals.put("description", MessageFormat.format("%{0}%", queryRoleCondition.getDescription()));
		}
	}

	private void assemblePermissionJpqlAndConditionValues(PermissionDTO queryPermissionCondition, StringBuilder jpql,
			String conditionPrefix, Map<String, Object> conditionVals) {
		String andCondition = " AND " + conditionPrefix;

		if (!StringUtils.isBlank(queryPermissionCondition.getName())) {
			jpql.append(andCondition);
			jpql.append(".name LIKE :name");
			conditionVals.put("name", MessageFormat.format("%{0}%", queryPermissionCondition.getName()));
		}
		if (!StringUtils.isBlank(queryPermissionCondition.getIdentifier())) {
			jpql.append(andCondition);
			jpql.append(".identifier LIKE :identifier");
			conditionVals.put("identifier", MessageFormat.format("%{0}%", queryPermissionCondition.getIdentifier()));
		}
		if (!StringUtils.isBlank(queryPermissionCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description LIKE :description");
			conditionVals.put("description", MessageFormat.format("%{0}%", queryPermissionCondition.getDescription()));
		}
	}

	private void assemblePageElementResourceJpqlAndConditionValues( PageElementResourceDTO queryPageElementResourceCondition, StringBuilder jpql, String conditionPrefix, Map<String, Object> conditionVals) {
		String andCondition = " AND " + conditionPrefix;

		if (!StringUtils.isBlank(queryPageElementResourceCondition.getName())) {
			jpql.append(andCondition);
			jpql.append(".name LIKE :name");
			conditionVals.put("name", MessageFormat.format("%{0}%", queryPageElementResourceCondition.getName()));
		}
		if (!StringUtils.isBlank(queryPageElementResourceCondition.getDescription())) {
			jpql.append(andCondition);
			conditionVals.put("description", MessageFormat.format("%{0}%", queryPageElementResourceCondition.getDescription()));			
		}
		if (!StringUtils.isBlank(queryPageElementResourceCondition.getIdentifier())) {
			jpql.append(andCondition);
			jpql.append(".identifier LIKE :identifier");
			conditionVals.put("identifier", MessageFormat.format("%{0}%", queryPageElementResourceCondition.getIdentifier()));			
		}
	}

	private void assembleUrlAccessResourceJpqlAndConditionValues(UrlAccessResourceDTO queryUrlAccessResourceCondition,
			StringBuilder jpql, String conditionPrefix, Map<String, Object> conditionVals) {
		String andCondition = " AND " + conditionPrefix;

		if (!StringUtils.isBlank(queryUrlAccessResourceCondition.getName())) {
			jpql.append(andCondition);
			jpql.append(".name LIKE :name");
			conditionVals.put("name", MessageFormat.format("%{0}%", queryUrlAccessResourceCondition.getName()));
		}
		if (!StringUtils.isBlank(queryUrlAccessResourceCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description LIKE :description");
			conditionVals.put("description", MessageFormat.format("%{0}%", queryUrlAccessResourceCondition.getDescription()));			
		}
		if (!StringUtils.isBlank(queryUrlAccessResourceCondition.getUrl())) {
			jpql.append(andCondition);
			jpql.append(".url LIKE :url");
			conditionVals.put("url", MessageFormat.format("%{0}%", queryUrlAccessResourceCondition.getUrl()));
		}
	}

	/**
	 * 顶级菜单
	 */
	private List<MenuResourceDTO> findTopMenuResourceDTOByUserAccountAsRole(Set<Authority> authorities) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_resource.id,_resource.name, _resource.url, _resource.menuIcon, _resource.description, _resource.parent.id,_resource.level)");
		jpql.append(" FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource");
        jpql.append(" WHERE TYPE(_resource) = MenuResource");
		jpql.append(" AND _authority IN (:authorities)");// 用户拥有的Authority
		jpql.append(" AND _resource.parent IS NULL");// 顶级
		jpql.append(" AND _resource.level = :level");// 顶级
		jpql.append(" GROUP BY _resource.id");

		List<MenuResourceDTO> result = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
                .addParameter("authorities",authorities)//
                .addParameter("level", 0)//
				.list();
		return result;
	}

	/**
	 * 所有菜单不包含顶级菜单
	 * 
	 * @param authorities
	 * @return
	 */
	private List<MenuResourceDTO> findAllMenuResourceDTOByUserAccountAsRole(Set<Authority> authorities) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_resource.id,_resource.name, _resource.url, _resource.menuIcon, _resource.description, _resource.parent.id,_resource.level)");
        jpql.append(" FROM ResourceAssignment _resourceAssignment JOIN _resourceAssignment.authority _authority JOIN _resourceAssignment.resource _resource");
        jpql.append(" WHERE TYPE(_resource) = MenuResource");
		jpql.append(" AND _authority IN (:authorities)");// 用户拥有的Authority
		jpql.append(" AND _resource.level > :level");//
		jpql.append(" GROUP BY _resource.id");

		List<MenuResourceDTO> result = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
                .addParameter("authorities", authorities)//
                .addParameter("level", 0)//
				.list();

		return result;
	}

	private void addMenuChildrenToParent(List<MenuResourceDTO> all) {
		LinkedHashMap<Long, MenuResourceDTO> map = new LinkedHashMap<Long, MenuResourceDTO>();
		for (MenuResourceDTO menuResourceDTO : all) {
			map.put(menuResourceDTO.getId(), menuResourceDTO);
		}
		for (MenuResourceDTO menuResourceDTO : map.values()) {
			Long parentId = menuResourceDTO.getParentId();
			if (!StringUtils.isBlank(parentId + "") && map.get(parentId) != null) {
				map.get(parentId).getChildren().add(menuResourceDTO);
			}
		}
	}

	private List<MenuResourceDTO> findChidrenMenuResource() {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_resource.id,_resource.name, _resource.url, _resource.menuIcon, _resource.description,"
						+ "_resource.parent.id,_resource.level,_resource.parent.name) FROM MenuResource _resource");
		jpql.append(" WHERE _resource.level > :level");//
		jpql.append(" GROUP BY _resource.id");//

		List<MenuResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
                .addParameter("level",0)//
				.list();

		return results;
	}

	private List<MenuResourceDTO> findTopMenuResource() {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_resource.id, _resource.name, _resource.url, _resource.menuIcon, _resource.description, _resource.parent.id,_resource.level) FROM MenuResource _resource");
		jpql.append(" WHERE _resource.parent IS NULL");// 顶级
		jpql.append(" AND _resource.level = :level");//
		jpql.append(" GROUP BY _resource.id");

		List<MenuResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
                .addParameter("level",0)//
				.list();
		return results;
	}

	/**
	 * 检查JPQL里面是否包含WHERE 关键字，如果没有就加上。
	 * 
	 * @param jpql
	 */
	private void jpqlHasWhereCondition(StringBuilder jpql) {
		if (jpql.indexOf("WHERE") != -1) {
			jpql.append(" AND ");
		} else {
			jpql.append(" WHERE ");
		}
	}

	@Override
	public boolean checkRoleByName(String roleName) {
		return securityAccessApplication.checkRoleByName(roleName);
	}

	@Override
	public boolean checkUserIsHaveRole(String userAccount, String roleName) {
		List<Role> roles = securityAccessApplication.findAllRolesByUserAccount(userAccount);
		if (roles.isEmpty()) {
			return false;
		}
		for (Role each : roles) {
			if (each.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}

}