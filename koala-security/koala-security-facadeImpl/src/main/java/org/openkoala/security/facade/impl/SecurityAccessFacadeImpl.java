package org.openkoala.security.facade.impl;

import static org.openkoala.security.facade.util.GenerateDTOUtils.*;
import static org.openkoala.security.facade.util.TransFromDomainUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
		LOGGER.info("SecurityAccessFacadeImpl findRoleDtosBy roles:{}", new Object[] { roles });
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

	@Override
	public Page<MenuResourceDTO> pagingQueryMenuResources(int currentPage, int pageSize, MenuResourceDTO menuResourceDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuResourceDTO> findMenuResourceDTOByUserAccountInRoleDTO(String userAccount, RoleDTO roleDTO) {
		Role role = transFromRoleBy(roleDTO);
		securityAccessApplication.checkAuthorization(userAccount, role);

		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(role);
		authorities.addAll(role.getPermissions());

		StringBuilder jpql = new StringBuilder(
				"SELECT _authority.securityResources FROM  Authority _authority JOIN _authority.securityResources. _securityResources");
		jpql.append("WHEY TYPE(_securityResources) = MenuResouce");

		List<MenuResource> menuResources = getQueryChannelService().createJpqlQuery(jpql.toString()).list();

		LOGGER.info("findMenuResourceDTOByUserAccountInRoleDTO:{}", new Object[] { menuResources });
		return null;
	}

}
