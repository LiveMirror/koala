package org.openkoala.security.facade.impl;

import static org.openkoala.security.facade.impl.assembler.GenerateDTOUtils.*;

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
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.core.domain.User;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.openkoala.security.facade.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

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

	public UserDTO getUserBy(Long userId) {
		User user = securityAccessApplication.getUserById(userId);
		return generateUserDTOBy(user);
	}

	@Override
	public UserDTO getUserByUserAccount(String userAccount) {
		User user = securityAccessApplication.getUserByUserAccount(userAccount);
		if (user != null) {
			return generateUserDTOBy(user);
		}
		return null;
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		User user = securityAccessApplication.getUserByEmail(email);
		if (user != null) {
			return generateUserDTOBy(user);
		}
		return null;
	}

	@Override
	public UserDTO getUserByTelePhone(String telePhone) {
		User user = securityAccessApplication.getUserByTelePhone(telePhone);
		if (user != null) {
			return generateUserDTOBy(user);
		}
		return null;
	}

	public List<RoleDTO> findRolesBy(String userAccount) {
		List<RoleDTO> results = new ArrayList<RoleDTO>();
		List<Role> roles = securityAccessApplication.findAllRolesByUserAccount(userAccount);
		for (Role role : roles) {
			results.add(generateRoleDTOBy(role));
		}
		return results;
	}

	public Set<PermissionDTO> findPermissionsBy(String userAccount) {
		Set<PermissionDTO> results = new HashSet<PermissionDTO>();
		List<Permission> permissions = securityAccessApplication.findAllPermissionsByUserAccount(userAccount);
		for (Permission permission : permissions) {
			PermissionDTO permissionDto = generatePermissionDTOBy(permission);
			permissionDto.setUserAccount(userAccount);
			results.add(permissionDto);
		}
		return results;
	}

	public List<MenuResourceDTO> findMenuResourceByUserAccount(String userAccount) {

		List<MenuResourceDTO> results = new ArrayList<MenuResourceDTO>();

		Set<MenuResource> menuResources = securityAccessApplication.findMenuResourceByUserAccount(userAccount);

		for (MenuResource menuResource : menuResources) {
			MenuResourceDTO menuResourceDto = generateMenuResourceDTOBy(menuResource);
			results.add(menuResourceDto);
		}

		return results;
	}

	/**
	 * 所有菜单不包含顶级菜单
	 * 
	 * @param userAccount
	 * @param roleId
	 * @return
	 */
	@Override
	public List<MenuResourceDTO> findMenuResourceByUserAsRole(String userAccount, Long roleId) {

		Set<Authority> authorities = new HashSet<Authority>();
		Role role = securityAccessApplication.getRoleBy(roleId);
		// securityAccessApplication.checkAuthorization(userAccount, role); //TODO 检查
		authorities.add(role);
		authorities.addAll(role.getPermissions());
		authorities.addAll(User.findAllPermissionsBy(userAccount));
		// 1、User 的角色、2、User本身的Permission 3、角色所关联的Permission。
		List<MenuResourceDTO> results = findTopMenuResourceDTOByUserAccountAsRole(authorities);
		List<MenuResourceDTO> childrenMenuResources = findAllMenuResourceDTOByUserAccountAsRole(authorities);

		List<MenuResourceDTO> all = new ArrayList<MenuResourceDTO>();
		all.addAll(results);
		all.addAll(childrenMenuResources);

		addMenuChildrenToParent(all);

		return results;

	}

	@Override
	public List<MenuResourceDTO> findAllMenusTree() {
		List<MenuResourceDTO> results = findTopMenuResource();
		List<MenuResourceDTO> childrenMenuResources = findChidrenMenuResource();
		List<MenuResourceDTO> all = new ArrayList<MenuResourceDTO>();
		all.addAll(results);
		all.addAll(childrenMenuResources);
		addMenuChildrenToParent(all);
		return results;
	}

	@Override
	public List<MenuResourceDTO> findMenuResourceTreeSelectItemByRoleId(Long roleId) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id,_securityResource.name) FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority WHERE TYPE(_securityResource) = :securityResourceType AND _authority.id = :authorityId");

		List<MenuResourceDTO> allMenResourcesAsRole = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("securityResourceType", MenuResource.class)//
				.addParameter("authorityId", roleId)//
				.list();

		List<MenuResourceDTO> allMenuResources = findAllMenusTree();

		for (MenuResourceDTO menuResourceDTO : allMenuResources) {
			if (!menuResourceDTO.getChildren().isEmpty()) {
				for (MenuResourceDTO childMenuResourceDTO : menuResourceDTO.getChildren()) {
					childMenuResourceDTO.setChecked(allMenResourcesAsRole.contains(childMenuResourceDTO));
				}
			}
			menuResourceDTO.setChecked(allMenResourcesAsRole.contains(menuResourceDTO));
		}

		return allMenuResources;
	}

	@Override
	public Set<PermissionDTO> findPermissionsByMenuOrUrl() {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier,_authority.description,_securityResource.url) FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE Type(_authority) = Permission AND TYPE(_securityResource) = MenuResource OR TYPE(_securityResource) = UrlAccessResource");

		List<PermissionDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.list();

		return Sets.newHashSet(results);
	}

	@Override
	public Set<RoleDTO> findRolesByMenuOrUrl() {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_authority.id, _authority.name,_authority.description, _securityResource.url) FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE TYPE(_authority) = Role AND (TYPE(_securityResource) = MenuResource OR TYPE(_securityResource) = UrlAccessResource)");

		List<RoleDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.list();

		return Sets.newHashSet(results);
	}

	/**
	 * 查询出所有的Url访问资源，并且有Role 和Permission
	 */
	@Override
	public List<UrlAccessResourceDTO> findAllUrlAccessResources() {

		List<UrlAccessResource> urlAccessResources = securityAccessApplication.findAllUrlAccessResources();

		List<UrlAccessResourceDTO> results = new ArrayList<UrlAccessResourceDTO>();

		for (UrlAccessResource urlAccessResource : urlAccessResources) {
			Set<Authority> authorities = urlAccessResource.getAuthorities();

			List<String> roles = UrlAccessResource.getRoleNames(authorities);
			List<String> permissions = UrlAccessResource.getPermissionIdentifiers(authorities);

			UrlAccessResourceDTO urlAccessResourceDTO = generateUrlAccessResourceDTOBy(urlAccessResource);
			if (!roles.isEmpty()) {
				urlAccessResourceDTO.setRoles(roles.toString());
			}

			if (!permissions.isEmpty()) {
				urlAccessResourceDTO.setPermissions(permissions.toString());
			}

			if (!roles.isEmpty() || !permissions.isEmpty()) {
				results.add(urlAccessResourceDTO);
			}
		}

		return results;
	}

	@Override
	public UserDTO login(String principal, String password) {
		User user = securityAccessApplication.login(principal, password);
		return generateUserDTOBy(user);
	}

	@Override
	public Page<UserDTO> pagingQueryUsers(int currentPage, int pageSize, UserDTO queryUserCondition) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("SELECT _user FROM User _user");

		assembleUserJpqlAndConditionValues(queryUserCondition, jpql, "_user", conditionVals);

		Page<User> userPage = getQueryChannelService().createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

		return new Page<UserDTO>(userPage.getStart(), userPage.getResultCount(), pageSize,
				generateUserDTOsBy(userPage.getData()));
	}

	@Override
	public Page<RoleDTO> pagingQueryRoles(int currentPage, int pageSize, RoleDTO queryRoleCondition) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("SELECT _role FROM Role _role");

		assembleRoleJpqlAndConditionValues(queryRoleCondition, jpql, "_role", conditionVals);

		Page<Role> rolePage = getQueryChannelService().createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

		return new Page<RoleDTO>(rolePage.getStart(), rolePage.getResultCount(), pageSize,
				generateRoleDTOsBy(rolePage.getData()));
	}

	@Override
	public Page<PermissionDTO> pagingQueryPermissions(int currentPage, int pageSize,
			PermissionDTO queryPermissionCondition) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("SELECT _permission FROM Permission _permission");

		assemblePermissionJpqlAndConditionValues(queryPermissionCondition, jpql, "_permission", conditionVals);

		Page<Permission> permissionPage = getQueryChannelService().createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

		return new Page<PermissionDTO>(permissionPage.getStart(), permissionPage.getResultCount(), pageSize,
				generatePermissionDTOsBy(permissionPage.getData()));
	}

	@Override
	public Page<RoleDTO> pagingQueryNotGrantRoles(int currentPage, int pageSize, RoleDTO queryRoleCondition, Long userId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.RoleDTO(_role.id, _role.name, _role.description)  FROM Role _role");
		Map<String, Object> conditionVals = new HashMap<String, Object>();

		assembleRoleJpqlAndConditionValues(queryRoleCondition, jpql, "_role", conditionVals);

		jpqlHasWhereCondition(jpql);

		jpql.append(" _role.id NOT IN(SELECT _authority.id FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority WHERE _actor.id= :userId)");

		conditionVals.put("userId", userId);
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

	}

	@Override
	public Page<PermissionDTO> pagingQueryGrantPermissionByUserId(int currentPage, int pageSize, Long userId) {
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
				.setPage(currentPage, pageSize)//
				.pagedList();
		return results;
	}

	@Override
	public Page<RoleDTO> pagingQueryGrantRolesByUserId(int currentPage, int pageSize, Long userId) {
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
				.setPage(currentPage, pageSize)//
				.pagedList();
		return results;
	}

	@Override
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByRoleId(int currentPage, int pageSize, Long roleId) {
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
		return results;
	}

	@Override
	public Page<PermissionDTO> pagingQueryGrantPermissionsByRoleId(int currentPage, int pageSize, Long roleId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id, _permission.name,_permission.identifier, _permission.description) FROM Permission _permission JOIN _permission.roles _role WHERE _role.id = :roleId");
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
	public Page<UrlAccessResourceDTO> pagingQueryUrlAccessResources(int currentPage, int pageSize,
			UrlAccessResourceDTO urlAccessResourceDTO) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.UrlAccessResourceDTO(_urlAccessResource.id, _urlAccessResource.name, _urlAccessResource.url,_urlAccessResource.description) FROM UrlAccessResource _urlAccessResource");
		Map<String, Object> conditionVals = new HashMap<String, Object>();

		assembleUrlAccessResourceJpqlAndConditionValues(urlAccessResourceDTO, jpql, "_urlAccessResource", conditionVals);

		Page<UrlAccessResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(currentPage, pageSize)//
				.pagedList();

		return results;
	}

	@Override
	public Page<UrlAccessResourceDTO> pagingQueryGrantUrlAccessResourcesByRoleId(int page, int pagesize, Long roleId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.UrlAccessResourceDTO(_securityResource.id, _securityResource.name, _securityResource.url,_securityResource.description) FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority WHERE TYPE(_securityResource) = :securityResourceType AND TYPE(_authority) = :authorityType AND _authority.id = :authorityId");
		Page<UrlAccessResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("securityResourceType", UrlAccessResource.class)//
				.addParameter("authorityType", Role.class)//
				.addParameter("authorityId", roleId)//
				.setPage(page, pagesize)//
				.pagedList();
		return results;
	}

	@Override
	public Page<UrlAccessResourceDTO> pagingQueryNotGrantUrlAccessResourcesByRoleId(int page, int pagesize, Long roleId) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.UrlAccessResourceDTO(_securityResource.id, _securityResource.name, _securityResource.url,_securityResource.description) FROM SecurityResource _securityResource WHERE TYPE(_securityResource) =:_securityResourceType AND _securityResource.id NOT IN (SELECT _securityResource.id FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority WHERE TYPE(_securityResource) =:_securityResourceType AND _authority.id = :authorityId)");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("_securityResourceType", UrlAccessResource.class);
		parameters.put("authorityId", roleId);

		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(parameters)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PermissionDTO> pagingQueryGrantPermissionsByUrlAccessResourceId(int page, int pagesize,
			Long urlAccessResourceId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier, _authority.description) FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE TYPE(_authority) = :authorityType AND _securityResource.id = :securityResourceId");
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("authorityType", Permission.class)//
				.addParameter("securityResourceId", urlAccessResourceId)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByUrlAccessResourceId(int page, int pagesize,
			Long urlAccessResourceId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name, _authority.identifier,_authority.description) FROM Authority _authority WHERE _authority.id NOT IN(SELECT _authority.id FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE _securityResource.id = :securityResourceId AND TYPE(_authority) = :authorityType) AND TYPE(_authority) = :authorityType");
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("securityResourceId", urlAccessResourceId)//
				.addParameter("authorityType", Permission.class)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PermissionDTO> pagingQueryGrantPermissionsByMenuResourceId(int page, int pagesize, Long menuResourceId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name, _authority.identifier,_authority.description) FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE _securityResource.id = :securityResourceId AND TYPE(_authority) = :authorityType");
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("securityResourceId", menuResourceId)//
				.addParameter("authorityType", Permission.class)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByMenuResourceId(int page, int pagesize,
			Long menuResourceId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier, _authority.description) FROM Authority _authority WHERE _authority.id NOT IN(SELECT _authority.id FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE _securityResource.id = :securityResourceId AND TYPE(_authority) = :authorityType)  AND TYPE(_authority) = :authorityType");
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("authorityType", Permission.class)//
				.addParameter("securityResourceId", menuResourceId)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByUserId(int page, int pagesize,
			PermissionDTO queryPermissionCondition, Long userId) {
		Map<String, Object> conditionVals = new HashMap<String, Object>();

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_permission.id,_permission.name, _permission.identifier,_permission.description)  FROM Permission _permission");

		assemblePermissionJpqlAndConditionValues(queryPermissionCondition, jpql, "_permission", conditionVals);

		jpqlHasWhereCondition(jpql);

		jpql.append("_permission.id NOT IN(SELECT _authority.id FROM Authorization _authorization JOIN _authorization.actor _actor JOIN _authorization.authority _authority WHERE TYPE(_authority) = :authorityType AND _actor.id= :userId)");

		conditionVals.put("authorityType", Permission.class);
		conditionVals.put("userId", userId);

		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PageElementResourceDTO> pagingQueryPageElementResources(int page, int pagesize,
			PageElementResourceDTO queryPageElementCondition) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PageElementResourceDTO(_securityResource.id,_securityResource.version, _securityResource.name,_securityResource.identifier, _securityResource.description) FROM SecurityResource _securityResource WHERE TYPE(_securityResource) = :securityResourceType");
		Map<String, Object> conditionVals = new HashMap<String, Object>();
		conditionVals.put("securityResourceType", PageElementResource.class);

		assemblePageElementResourceJpqlAndConditionValues(queryPageElementCondition, jpql, "_securityResource",
				conditionVals);

		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(conditionVals)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PageElementResourceDTO> pagingQueryGrantPageElementResourcesByRoleId(int page, int pagesize, Long roleId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PageElementResourceDTO(_securityResource.id,_securityResource.version, _securityResource.name,_securityResource.identifier, _securityResource.description) FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority WHERE TYPE(_securityResource) = :securityResourceType AND TYPE(_authority) = :authorityType AND _authority.id = :authorityId");
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("securityResourceType", PageElementResource.class)//
				.addParameter("authorityType", Role.class)//
				.addParameter("authorityId", roleId)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PageElementResourceDTO> pagingQueryNotGrantPageElementResourcesByRoleId(int page, int pagesize,
			Long roleId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PageElementResourceDTO(_securityResource.id,_securityResource.version, _securityResource.name, _securityResource.identifier, _securityResource.description) FROM SecurityResource _securityResource WHERE TYPE(_securityResource) =:_securityResourceType AND _securityResource.id NOT IN(SELECT _securityResource.id FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority  WHERE TYPE(_securityResource) =:_securityResourceType AND _authority.id = :authorityId ) ");
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("_securityResourceType", PageElementResource.class)//
				.addParameter("authorityId", roleId)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PermissionDTO> pagingQueryGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier, _authority.description) FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE _securityResource.id = :securityResourceId AND TYPE(_authority) = :authorityType");
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("securityResourceId", pageElementResourceId)//
				.addParameter("authorityType", Permission.class)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	@Override
	public Page<PermissionDTO> pagingQueryNotGrantPermissionsByPageElementResourceId(int page, int pagesize,
			Long pageElementResourceId) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.PermissionDTO(_authority.id, _authority.name,_authority.identifier, _authority.description) FROM Authority _authority WHERE _authority.id NOT IN(SELECT _authority.id FROM Authority _authority JOIN _authority.securityResources _securityResource WHERE _securityResource.id = :securityResourceId AND TYPE(_authority) = :authorityType) AND TYPE(_authority) = :authorityType");
		return getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.addParameter("securityResourceId", pageElementResourceId)//
				.addParameter("authorityType", Permission.class)//
				.setPage(page, pagesize)//
				.pagedList();
	}

	/*------------- Private helper methods  -----------------*/

	private void assembleUserJpqlAndConditionValues(UserDTO queryUserCondition, StringBuilder jpql,
			String conditionPrefix, Map<String, Object> conditionVals) {

		String whereCondition = " WHERE " + conditionPrefix;
		String andCondition = " AND " + conditionPrefix;

		if (null != queryUserCondition.getDisabled() && !"".equals(queryUserCondition.getDisabled())) {
			jpql.append(whereCondition);
			jpql.append(".disabled = :disabled");
			conditionVals.put("disabled", queryUserCondition.getDisabled());
		}

		if (!StringUtils.isBlank(queryUserCondition.getName())) {
			jpql.append(andCondition);
			jpql.append(".name LIKE :name");
			conditionVals.put("name", queryUserCondition.getName());
		}
		if (!StringUtils.isBlank(queryUserCondition.getUserAccount())) {
			jpql.append(andCondition);
			jpql.append(".userAccount LIKE :userAccount");
			conditionVals.put("userAccount", queryUserCondition.getUserAccount());
		}
		if (!StringUtils.isBlank(queryUserCondition.getEmail())) {
			jpql.append(andCondition);
			jpql.append(".email LIKE :email");
			conditionVals.put("email", queryUserCondition.getEmail());
		}
		if (!StringUtils.isBlank(queryUserCondition.getTelePhone())) {
			jpql.append(andCondition);
			jpql.append(".telePhone LIKE :telePhone");
			conditionVals.put("telePhone", queryUserCondition.getTelePhone());
		}
	}

	private void assembleRoleJpqlAndConditionValues(RoleDTO queryRoleCondition, StringBuilder jpql,
			String conditionPrefix, Map<String, Object> conditionVals) {

		String andCondition = " AND " + conditionPrefix;
		String whereCondition = " WHERE " + conditionPrefix;

		if (!StringUtils.isBlank(queryRoleCondition.getName())) {
			jpql.append(whereCondition);
			jpql.append(".name =:name");
			conditionVals.put("name", queryRoleCondition.getName());
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

		if (!StringUtils.isBlank(queryPermissionCondition.getName())) {
			jpql.append(whereCondition);
			jpql.append(".name =:name");
			conditionVals.put("name", queryPermissionCondition.getName());
		}

		if (!StringUtils.isBlank(queryPermissionCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description =:description");
			conditionVals.put("description", queryPermissionCondition.getDescription());
		}
	}

	private void assemblePageElementResourceJpqlAndConditionValues(
			PageElementResourceDTO queryPageElementResourceCondition, StringBuilder jpql, String conditionPrefix,
			Map<String, Object> conditionVals) {
		String andCondition = " AND " + conditionPrefix;

		if (!StringUtils.isBlank(queryPageElementResourceCondition.getName())) {
			jpql.append(andCondition);
			jpql.append(".name =:name");
			conditionVals.put("name", queryPageElementResourceCondition.getName());
		}
		if (!StringUtils.isBlank(queryPageElementResourceCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description =:description");
			conditionVals.put("description", queryPageElementResourceCondition.getDescription());
		}
		if (!StringUtils.isBlank(queryPageElementResourceCondition.getIdentifier())) {
			jpql.append(andCondition);
			jpql.append(".identifier =:identifier");
			conditionVals.put("identifier", queryPageElementResourceCondition.getIdentifier());
		}
	}

	private void assembleUrlAccessResourceJpqlAndConditionValues(UrlAccessResourceDTO queryUrlAccessResourceCondition,
			StringBuilder jpql, String conditionPrefix, Map<String, Object> conditionVals) {
		String andCondition = " AND " + conditionPrefix;
		String whereCondition = " WHERE " + conditionPrefix;

		if (!StringUtils.isBlank(queryUrlAccessResourceCondition.getName())) {
			jpql.append(whereCondition);
			jpql.append(".name =:name");
			conditionVals.put("name", queryUrlAccessResourceCondition.getName());
		}
		if (!StringUtils.isBlank(queryUrlAccessResourceCondition.getDescription())) {
			jpql.append(andCondition);
			jpql.append(".description =:description");
			conditionVals.put("description", queryUrlAccessResourceCondition.getDescription());
		}
		if (!StringUtils.isBlank(queryUrlAccessResourceCondition.getUrl())) {
			jpql.append(andCondition);
			jpql.append(".url =:url");
			conditionVals.put("url", queryUrlAccessResourceCondition.getUrl());
		}
	}

	/**
	 * 顶级菜单
	 */
	private List<MenuResourceDTO> findTopMenuResourceDTOByUserAccountAsRole(Set<Authority> authorities) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id,_securityResource.name, _securityResource.url, _securityResource.menuIcon, _securityResource.description,"
						+ "_securityResource.parent.id,_securityResource.level) FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority");
		jpql.append(" WHERE TYPE(_securityResource) = MenuResource");
		jpql.append(" AND _authority IN (:_authority)");// 用户拥有的Authority
		jpql.append(" AND _securityResource.parent IS NULL");// 顶级
		jpql.append(" AND _securityResource.level = :level");// 顶级
		jpql.append(" GROUP BY _securityResource.id");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_authority", authorities);
		map.put("level", 0);

		List<MenuResourceDTO> result = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(map)//
				.list();
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
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id,_securityResource.name, _securityResource.url, _securityResource.menuIcon, _securityResource.description,"
						+ "_securityResource.parent.id,_securityResource.level) FROM SecurityResource _securityResource JOIN _securityResource.authorities _authority");
		jpql.append(" WHERE TYPE(_securityResource) = MenuResource");
		jpql.append(" AND _authority IN (:_authority)");// 用户拥有的Authority
		jpql.append(" AND _securityResource.level > :level");//
		jpql.append(" GROUP BY _securityResource.id");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("_authority", authorities);
		map.put("level", 0);

		List<MenuResourceDTO> result = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(map)//
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
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id,_securityResource.name, _securityResource.url, _securityResource.menuIcon, _securityResource.description,"
						+ "_securityResource.parent.id,_securityResource.level) FROM SecurityResource _securityResource");
		jpql.append(" WHERE TYPE(_securityResource) = MenuResource");
		jpql.append(" AND _securityResource.level > :level");//
		jpql.append(" GROUP BY _securityResource.id");//

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("level", 0);

		List<MenuResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(map)//
				.list();

		return results;
	}

	private List<MenuResourceDTO> findTopMenuResource() {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW org.openkoala.security.facade.dto.MenuResourceDTO(_securityResource.id, _securityResource.name, _securityResource.url, _securityResource.menuIcon, _securityResource.description,"
						+ "_securityResource.parent.id,_securityResource.level) FROM SecurityResource _securityResource");
		jpql.append(" WHERE TYPE(_securityResource) = MenuResource");
		jpql.append(" AND _securityResource.parent IS NULL");// 顶级
		jpql.append(" AND _securityResource.level = :level");//
		jpql.append(" GROUP BY _securityResource.id");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("level", 0);

		List<MenuResourceDTO> results = getQueryChannelService()//
				.createJpqlQuery(jpql.toString())//
				.setParameters(map)//
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


}
