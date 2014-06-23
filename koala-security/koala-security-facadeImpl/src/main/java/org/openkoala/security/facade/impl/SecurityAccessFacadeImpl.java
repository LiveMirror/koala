package org.openkoala.security.facade.impl;

import static org.openkoala.security.facade.util.GenerateDTOUtils.*;
import static org.openkoala.security.facade.util.TransFromDomainUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.dayatang.domain.InstanceFactory;
import org.dayatang.querychannel.Page;
import org.dayatang.querychannel.QueryChannelService;
import org.openkoala.security.application.SecurityAccessApplication;
import org.openkoala.security.core.domain.Authority;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.OrganizationScopeDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UserDTO;
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
			queryChannelService = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel");
		}
		return queryChannelService;
	}

	public UserDTO getUserDtoBy(Long userId) {
		User user = securityAccessApplication.getUserBy(userId);
		return generateUserDtoBy(user);
	}

	public UserDTO getUserDtoBy(String username) {
		User user = securityAccessApplication.getUserBy(username);
		return generateUserDtoBy(user);
	}

	public List<RoleDTO> findRoleDtosBy(String username) {
		List<RoleDTO> results = new ArrayList<RoleDTO>();
		Set<Role> roles = securityAccessApplication.findAllRolesByUserAccount(username);
		for (Role role : roles) {
			results.add(generateRoleDtoBy(role));
		}
		return results;
	}

	public Set<PermissionDTO> findPermissionDtosBy(String username) {
		Set<PermissionDTO> results = new HashSet<PermissionDTO>();
		Set<Permission> permissions = securityAccessApplication.findAllPermissionsByUserAccount(username);
		for (Permission permission : permissions) {
			PermissionDTO permissionDto = generatePermissionDtoBy(permission);
			permissionDto.setUserName(username);
			results.add(permissionDto);
		}
		return results;
	}

	public List<MenuResourceDTO> findMenuResourceDtoByUsername(String username) {

		List<MenuResourceDTO> results = new ArrayList<MenuResourceDTO>();

		Set<MenuResource> menuResources = securityAccessApplication.findMenuResourceByUserAccount(username);

		for (MenuResource menuResource : menuResources) {
			MenuResourceDTO menuResourceDto = generateMenuResourceDtoBy(menuResource);
			results.add(menuResourceDto);
		}

		return results;
	}

	@Override
	public boolean updatePassword(UserDTO userDto, String oldUserPassword) {
		User user = transFromUserBy(userDto);
		return securityAccessApplication.updatePassword(user, oldUserPassword);
	}

	@Override
	public void updateUserDTO(UserDTO userDTO) {
		User user = transFromUserBy(userDTO);
		securityAccessApplication.updateActor(user);
	}

	@Override
	public Page<UserDTO> pagingQueryUsers(int currentPage, int pageSize, UserDTO userDTO) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("FROM User _user WHERE _user.enabled = true");

		assembleJpqlAndConditionValues(userDTO, jpql, "_user", conditionVals);

		Page<User> userPage = getQueryChannelService().createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

		return new Page<UserDTO>(userPage.getStart(), userPage.getResultCount(), pageSize,
				generateUserDtosBy(userPage.getData()));
	}

	private void assembleJpqlAndConditionValues(UserDTO userDTO, StringBuilder jpql, String conditionPrefix,
			Map<String, Object> conditionVals) {

		String andCondition = " AND " + conditionPrefix;
		if (!StringUtils.isBlank(userDTO.getName())) {
			jpql.append(andCondition);
			jpql.append(".name LIKE :name");
			conditionVals.put("name", userDTO.getName());
		}
		if (!StringUtils.isBlank(userDTO.getUserAccount())) {
			jpql.append(andCondition);
			jpql.append(".userAccount LIKE :userAccount");
			conditionVals.put("userAccount", userDTO.getUserAccount());
		}
		if (!StringUtils.isBlank(userDTO.getEmail())) {
			jpql.append(andCondition);
			jpql.append(".email LIKE :email");
			conditionVals.put("email", userDTO.getEmail());
		}
		if (!StringUtils.isBlank(userDTO.getTelePhone())) {
			jpql.append(andCondition);
			jpql.append(".telePhone LIKE :telePhone");
			conditionVals.put("telePhone", userDTO.getTelePhone());
		}
	}

	@Override
	public Page<RoleDTO> pagingQueryRoles(int currentPage, int pageSize, RoleDTO roleDTO) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("FROM Role _role");

		assembleJpqlAndConditionValues(roleDTO, jpql, "_role", conditionVals);

		Page<Role> rolePage = getQueryChannelService().createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

		return new Page<RoleDTO>(rolePage.getStart(), rolePage.getResultCount(), pageSize,
				generateRoleDtosBy(rolePage.getData()));
	}

	private void assembleJpqlAndConditionValues(RoleDTO roleDTO, StringBuilder jpql, String conditionPrefix,
			Map<String, Object> conditionVals) {

		String andCondition = " AND " + conditionPrefix;
		String whereCondition = " WHERE " + conditionPrefix;
		if (!StringUtils.isBlank(roleDTO.getRoleName())) {
			jpql.append(whereCondition);
			jpql.append(".name =:name");
			conditionVals.put("name", roleDTO.getRoleName());
		}
		if (!StringUtils.isBlank(roleDTO.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description =:description");
			conditionVals.put("description", roleDTO.getDescription());
		}
	}

	@Override
	public Page<PermissionDTO> pagingQueryPermissions(int currentPage, int pageSize, PermissionDTO permissionDTO) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("FROM Permission _permission");

		assembleJpqlAndConditionValues(permissionDTO, jpql, "_permission", conditionVals);

		Page<Permission> permissionPage = getQueryChannelService().createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

		return new Page<PermissionDTO>(permissionPage.getStart(), permissionPage.getResultCount(), pageSize,
				generatePermissionDtosBy(permissionPage.getData()));
	}

	private void assembleJpqlAndConditionValues(PermissionDTO permissionDTO, StringBuilder jpql,
			String conditionPrefix, Map<String, Object> conditionVals) {
		String andCondition = " AND " + conditionPrefix;
		String whereCondition = " WHERE " + conditionPrefix;
		if (!StringUtils.isBlank(permissionDTO.getPermissionName())) {
			jpql.append(whereCondition);
			jpql.append(".name =:name");
			conditionVals.put("name", permissionDTO.getPermissionName());
		}
		if (!StringUtils.isBlank(permissionDTO.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description =:description");
			conditionVals.put("description", permissionDTO.getDescription());
		}
	}

	/**
	 * 顶级菜单
	 */
	private List<MenuResourceDTO> findTopMenuResourceDTOByUserAccountAsRole(Set<Authority> authorities) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id, _securityResource.identifier, _securityResource.name, _securityResource.url, _securityResource.menuIcon, _securityResource.description,"
						+ "_securityResource.parent.id, _securityResource.disabled,_securityResource.level) FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority");
		jpql.append(" WHERE TYPE(_securityResource) = MenuResource");
		jpql.append(" AND _authority IN (:_authority)");// 用户拥有的Authority
		jpql.append(" AND _securityResource.parent IS NULL");// 顶级
		jpql.append(" AND _securityResource.disabled = :disabled");// 可用的
		jpql.append(" AND _securityResource.level = :level");// 顶级

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_authority", authorities);
		map.put("disabled", false);
		map.put("level", 0);

		List<MenuResourceDTO> result = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(map)//
				.list();
		LOGGER.info("顶级：findTopMenuResourceDTOByUserAccountAsRole:{}", new Object[] { result });
		return result;
	}

	/**
	 * 所有菜单不包含顶级菜单
	 * 
	 * @param userAccount
	 * @param roleId
	 * @return
	 */
	private List<MenuResourceDTO> findAllMenuResourceDTOByUserAccountAsRole(Set<Authority> authorities) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id, _securityResource.identifier, _securityResource.name, _securityResource.url, _securityResource.menuIcon, _securityResource.description,"
						+ "_securityResource.parent.id, _securityResource.disabled,_securityResource.level) FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority");
		jpql.append(" WHERE TYPE(_securityResource) = MenuResource");
		jpql.append(" AND _authority IN (:_authority)");// 用户拥有的Authority
		jpql.append(" AND _securityResource.disabled = :disabled");//
		jpql.append(" AND _securityResource.level > :level");//

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_authority", authorities);
		map.put("disabled", false);
		map.put("level", 0);

		List<MenuResourceDTO> result = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(map)//
				.list();

		LOGGER.info("所有：findAllMenuResourceDTOByUserAccountAsRole:{}", new Object[] { result });
		return result;
	}

	/**
	 * 所有菜单不包含顶级菜单
	 * 
	 * @param userAccount
	 * @param roleId
	 * @return
	 */
	@Override
	public List<MenuResourceDTO> findMenuResourceDTOByUserAccountAsRole(String userAccount, Long roleId) {

		User user = securityAccessApplication.getUserBy(userAccount);
		Set<Authority> authorities = new HashSet<Authority>();
		List<MenuResourceDTO> results;
		List<MenuResourceDTO> childrenMenuResources;

		if (!user.isSuper()) {
			Role role = securityAccessApplication.getRoleBy(roleId);
			// securityAccessApplication.checkAuthorization(userAccount, role);
			authorities.add(role);
			authorities.addAll(role.getPermissions());
			authorities.addAll(User.findAllPermissionsBy(userAccount));
			// 1、User 的角色、2、User本身的Permission 3、角色所关联的Permission。
			results = findTopMenuResourceDTOByUserAccountAsRole(authorities);
			childrenMenuResources = findAllMenuResourceDTOByUserAccountAsRole(authorities);
		} else {
			results = findTopMenuResource();
			childrenMenuResources = findChidrenMenuResource();
		}

		Set<MenuResourceDTO> all = new HashSet<MenuResourceDTO>();
		all.addAll(results);
		all.addAll(childrenMenuResources);

		addMenuChildrenToParent(all);

		return results;

	}

	private void addMenuChildrenToParent(Set<MenuResourceDTO> all) {
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

	private void addOrganizationScopeChildrenToParent(Set<OrganizationScopeDTO> all) {
		LinkedHashMap<Long, OrganizationScopeDTO> map = new LinkedHashMap<Long, OrganizationScopeDTO>();
		for (OrganizationScopeDTO organizationScopeDTO : all) {
			map.put(organizationScopeDTO.getId(), organizationScopeDTO);
		}
		for (OrganizationScopeDTO organizationScopeDTO : map.values()) {
			Long parentId = organizationScopeDTO.getParentId();
			if (!StringUtils.isBlank(parentId + "") && map.get(parentId) != null) {
				map.get(parentId).getChildren().add(organizationScopeDTO);
			}
		}
	}

	private List<MenuResourceDTO> findChidrenMenuResource() {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id, _securityResource.identifier, _securityResource.name, _securityResource.url, _securityResource.menuIcon, _securityResource.description,"
						+ "_securityResource.parent.id, _securityResource.disabled,_securityResource.level) FROM SecurityResource _securityResource");
		jpql.append(" WHERE TYPE(_securityResource) = MenuResource");
		jpql.append(" AND _securityResource.disabled = :disabled");//
		jpql.append(" AND _securityResource.level > :level");//

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("disabled", false);
		map.put("level", 0);

		List<MenuResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(map)//
				.list();

		LOGGER.info("所有：findAllMenuResourceDTOByUserAccountAsRole:{}", new Object[] { results });
		return results;
	}

	private List<MenuResourceDTO> findTopMenuResource() {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id, _securityResource.identifier, _securityResource.name, _securityResource.url, _securityResource.menuIcon, _securityResource.description,"
						+ "_securityResource.parent.id, _securityResource.disabled,_securityResource.level) FROM SecurityResource _securityResource");
		jpql.append(" WHERE TYPE(_securityResource) = MenuResource");
		jpql.append(" AND _securityResource.parent IS NULL");// 顶级
		jpql.append(" AND _securityResource.disabled = :disabled");//
		jpql.append(" AND _securityResource.level = :level");//

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("disabled", false);
		map.put("level", 0);

		List<MenuResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(map)//
				.list();

		return results;
	}

	@Override
	public List<MenuResourceDTO> findAllMenusTree() {
		List<MenuResourceDTO> results = findTopMenuResource();
		List<MenuResourceDTO> childrenMenuResources = findChidrenMenuResource();
		Set<MenuResourceDTO> all = new HashSet<MenuResourceDTO>();
		all.addAll(results);
		all.addAll(childrenMenuResources);
		addMenuChildrenToParent(all);
		return results;
	}

	@Override
	public List<OrganizationScopeDTO> findAllOrganizationScopesTree() {
		List<OrganizationScopeDTO> results = findTopOrganizationScopes();
		List<OrganizationScopeDTO> childrenOrganizationScopeDTOs = findChidrenOrganizationScopes();
		Set<OrganizationScopeDTO> all = new HashSet<OrganizationScopeDTO>();
		all.addAll(results);
		all.addAll(childrenOrganizationScopeDTOs);
		addOrganizationScopeChildrenToParent(all);
		return results;
	}

	@SuppressWarnings("unchecked")
	private List<OrganizationScopeDTO> findChidrenOrganizationScopes() {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.OrganizationScopeDTO(_organizationScope.id, _organizationScope.name, _organizationScope.description, _organizationScope.parent.id) FROM OrganizationScope _organizationScope");
		jpql.append(" WHERE _organizationScope.level > :level");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("level", 0);

		List<OrganizationScopeDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.list();

		return results;
	}

	private List<OrganizationScopeDTO> findTopOrganizationScopes() {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.OrganizationScopeDTO(_organizationScope.id, _organizationScope.name, _organizationScope.description, _organizationScope.parent.id) FROM OrganizationScope _organizationScope");
		jpql.append(" WHERE _organizationScope.parent IS NULL");
		jpql.append(" AND _organizationScope.level = :level");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("level", 0);

		List<OrganizationScopeDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.list();

		return results;
	}

	@Override
	public Page<RoleDTO> pagingQueryNotGrantRoles(int currentPage, int pageSize, RoleDTO queryRoleCondition,
			Long userId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_role.id, _role.name, _role.description)");
		jpql.append(" FROM Role _role WHERE _role.id");
		jpql.append(" NOT IN(SELECT _authority.id FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority WHERE _actor.id= :userId)");
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		conditionVals.put("userId", userId);
		assembleRoleJpqlAndConditionValues(queryRoleCondition, jpql, "_role", conditionVals);

		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

	}

	@Override
	public Page<PermissionDTO> pagingQueryNotGrantRoles(int currentPage, int pageSize,
			PermissionDTO queryPermissionCondition,Long userId) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id, _permission.name, _permission.description)");
		jpql.append(" FROM Permission _permission WHERE _permission.id");
		jpql.append(" NOT IN(SELECT _authority.id FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority WHERE _actor.id= :userId)");
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		conditionVals.put("userId", userId);

		assemblePermissionJpqlAndConditionValues(queryPermissionCondition, jpql, "_permission", conditionVals);

		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

	}

	private void assembleRoleJpqlAndConditionValues(RoleDTO queryRoleCondition, StringBuilder jpql,
			String conditionPrefix, Map<String, Object> conditionVals) {
		String andCondition = " AND " + conditionPrefix;
		String whereCondition = " WHERE " + conditionPrefix;
		if (!StringUtils.isBlank(queryRoleCondition.getRoleName())) {
			jpql.append(whereCondition);
			jpql.append(".name =:name");
			conditionVals.put("name", queryRoleCondition.getRoleName());
		}
		if (!StringUtils.isBlank(queryRoleCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description =:description");
			conditionVals.put("description", queryRoleCondition.getDescription());
		}
	}

	private void assemblePermissionJpqlAndConditionValues(PermissionDTO queryPermissionCondition, StringBuilder jpql,
			String conditionPrefix, Map<String, Object> conditionVals) {
		String andCondition = " AND " + conditionPrefix;
		String whereCondition = " WHERE " + conditionPrefix;
		if (!StringUtils.isBlank(queryPermissionCondition.getPermissionName())) {
			jpql.append(whereCondition);
			jpql.append(".name =:name");
			conditionVals.put("name", queryPermissionCondition.getPermissionName());
		}
		if (!StringUtils.isBlank(queryPermissionCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description =:description");
			conditionVals.put("description", queryPermissionCondition.getDescription());
		}
	}

	@Override
	public Page<PermissionDTO> pagingQueryPermissionsByUserAccount(int currentPage, int pageSize, Long userId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name, _authority.description)");
		jpql.append(" FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority");
		jpql.append(" WHERE TYPE(_authority) = Permission");
		jpql.append(" AND _actor.id = :userId");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userId", userId);
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(currentPage, pageSize)//
				.pagedList();
		return results;
	}

	@Override
	public Page<RoleDTO> pagingQueryRolesByUserAccount(int currentPage, int pageSize, Long userId) {
//		Set<Role> roles = securityAccessApplication.findAllRolesByUserAccount(userAccount);
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_authority.id, _authority.name, _authority.description)");
		jpql.append(" FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority");
		jpql.append(" WHERE TYPE(_authority) = Role");
		jpql.append(" AND _actor.id = :userId");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userId", userId);
		Page<RoleDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(currentPage, pageSize)//
				.pagedList();
		return results;
	}

	@Override
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByRole(int currentPage, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id, _permission.name, _permission.description)");
		jpql.append("FROM Permission _permission WHERE _permission.id NOT IN(SELECT _permission.id FROM Permission _permission JOIN _permission.roles _role WHERE _role.id = :roleId)");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roleId", roleId);
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(currentPage, pageSize)//
				.pagedList();
		return results;
	}

	@Override
	public Page<PermissionDTO> pagingQueryPermissionsByRole(int currentPage, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder("SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id, _permission.name, _permission.description) FROM Permission _permission JOIN _permission.roles _role WHERE _role.id = :roleId");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roleId", roleId);
		Page<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(currentPage, pageSize)//
				.pagedList();
		return results;
	}
}
