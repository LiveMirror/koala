package org.openkoala.security.web.ini;

import javax.inject.Inject;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.DependsOn;

@DependsOn("queryChannel")
public class CustomChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	private String filterChainDefinitions;

	private int count;

	/**
	 * 通过filterChainDefinitions对默认的链接过滤定义
	 * 
	 * @param filterChainDefinitions
	 */
	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	public Section getObject() throws Exception {
		Ini ini = new Ini();
		ini.load(filterChainDefinitions);

		Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);

		if (CollectionUtils.isEmpty(section)) {
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}

		// TODO 扩展点!

		// 查出所有的permissions
		// Set<PermissionDTO> permissions = securityAccessFacade.findPermissions();
		// for (PermissionDTO permissionDTO : permissions) {
		// if ((!StringUtils.isBlank(permissionDTO.getIdentifier()) && !StringUtils.isBlank(permissionDTO.getUrl()))) {
		// section.put("perms[" + permissionDTO.getIdentifier() + "]", permissionDTO.getUrl());
		// }
		// }
		//
		// Set<RoleDTO> roles = securityAccessFacade.findRoles();
		//
		//
		//
		// for (RoleDTO roleDTO : roles) {
		// Set<PermissionDTO> permissionDTOs = roleDTO.getPermissionDTOs();
		// for (PermissionDTO permissionDTO : permissions) {
		// if(permissionDTOs.contains(permissionDTO)){
		// String url = permissionDTO.getUrl();
		// if(!StringUtils.isBlank(url))
		// section.put("roles[" + roleDTO.getRoleName() + "]", permissionDTO.getUrl());
		// }
		// }
		//
		// }

		// 获取到所有的资源url与permission的关联关系。
		// section.put("", "");// key是url value是permission字符串。
		// key 是url value 是role 字符串

		System.out.println("CustomChainDefinitionSectionMetaSource" + count++);
		return section;
	}

	public Class<?> getObjectType() {
		return Section.class;
	}

	public boolean isSingleton() {
		return Boolean.TRUE;
	}
}
