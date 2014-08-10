package org.openkoala.security.facade.impl.assembler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.security.core.domain.MenuResource;
import org.openkoala.security.core.domain.PageElementResource;
import org.openkoala.security.core.domain.Permission;
import org.openkoala.security.core.domain.Role;
import org.openkoala.security.core.domain.UrlAccessResource;
import org.openkoala.security.facade.dto.MenuResourceDTO;
import org.openkoala.security.facade.dto.PageElementResourceDTO;
import org.openkoala.security.facade.dto.PermissionDTO;
import org.openkoala.security.facade.dto.RoleDTO;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;

/**
 * 转换领域工具类
 * 
 * @author luzhao
 * 
 */
public final class TransFromDomainUtils {


	
	public static Role transFromRoleBy(RoleDTO roleDTO) {
		Role result = null;
		if (!StringUtils.isBlank(roleDTO.getRoleId() + "")) {
			result.setId(roleDTO.getRoleId());
		}else{
			result = new Role(roleDTO.getRoleName());
		}
		result.setVersion(roleDTO.getVersion());
		result.setDescription(roleDTO.getDescription());
		return result;
	}

	public static Permission transFromPermissionBy(PermissionDTO permissionDTO) {
		Permission result = null;
		if (!StringUtils.isBlank(permissionDTO.getPermissionId() + "")) {
			result = Permission.getBy(permissionDTO.getPermissionId());
			result.setId(permissionDTO.getPermissionId());
		}else{
			result  = new Permission(permissionDTO.getPermissionName(), permissionDTO.getIdentifier());
		}
		result.setDescription(permissionDTO.getDescription());
		result.setVersion(permissionDTO.getVersion());
		return result;
	}

	public static MenuResource transFromMenuResourceBy(MenuResourceDTO menuResourceDTO) {
		MenuResource result = new MenuResource(menuResourceDTO.getName());
		if (!StringUtils.isBlank(menuResourceDTO.getId() + "")) {
			result.setId(menuResourceDTO.getId());
		}
		result.setDescription(menuResourceDTO.getDescription());
		result.setMenuIcon(menuResourceDTO.getIcon());
		result.setUrl(menuResourceDTO.getUrl());
		result.setVersion(menuResourceDTO.getVersion());
		return result;
	}

	public static UrlAccessResource transFromUrlAccessResourceBy(UrlAccessResourceDTO urlAccessResourceDTO) {
		UrlAccessResource result = new UrlAccessResource(urlAccessResourceDTO.getName(),urlAccessResourceDTO.getUrl());
		result.setDescription(urlAccessResourceDTO.getDescription());
		if (!StringUtils.isBlank(urlAccessResourceDTO.getId() + "")) {
			result.setId(urlAccessResourceDTO.getId());
		}
		result.setVersion(urlAccessResourceDTO.getVersion());
		return result;
	}
	public static PageElementResource transFromPageElementResourceBy(PageElementResourceDTO pageElementResourceDTO) {
		PageElementResource result = new PageElementResource(pageElementResourceDTO.getName(),pageElementResourceDTO.getIdentifier());
		result.setDescription(pageElementResourceDTO.getDescription());
		if (!StringUtils.isBlank(pageElementResourceDTO.getId() + "")) {
			result.setId(pageElementResourceDTO.getId());
		}
		result.setVersion(pageElementResourceDTO.getVersion());
		return result;
	}

	public static List<MenuResource> transFromMenuResourcesBy(List<MenuResourceDTO> menuResourceDTOs) {
		List<MenuResource> results = new ArrayList<MenuResource>();
		for (MenuResourceDTO menuResourceDTO : menuResourceDTOs) {
			MenuResource result = transFromMenuResourceBy(menuResourceDTO);
			results.add(result);
		}
		return results;
	}

}
