package org.openkoala.security.web.ini;

import javax.inject.Inject;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.springframework.beans.factory.FactoryBean;

public class CustomChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section>{
	@Inject
	private SecurityAccessFacade securityAccessFacade;

	private String filterChainDefinitions;

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
		
		if(CollectionUtils.isEmpty(section))
		{
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}
		
		//获取到所有的资源url与permission的关联关系。
		section.put("", "");//key是url value是permission字符串。
		
		return section;
	}

	public Class<?> getObjectType() {
		return Section.class;
	}

	public boolean isSingleton() {
		return Boolean.TRUE;
	}
}
