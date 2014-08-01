package org.openkoala.security.shiro.extend;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

@Named
public class ShiroFilerChainManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShiroFilerChainManager.class);

	@Inject
	private DefaultFilterChainManager filterChainManager;

	@Inject
	private SecurityAccessFacade securityAccessFacade;

	private Map<String, NamedFilterList> defaultFilterChains;

	// @PostConstruct
	public void init() {
		defaultFilterChains = new LinkedHashMap<String, NamedFilterList>(filterChainManager.getFilterChains());
		LOGGER.info("defaultFilterChains:{}", defaultFilterChains);
	}

	// @PostConstruct
	public void initFilterChain() {
		// List<UrlAccessResourceDTO> urlAccessResources = securityAccessFacade.findAllUrlAccessResources();
		initFilterChains(Collections.<UrlAccessResourceDTO> emptyList());
	}

	public void initFilterChains(List<UrlAccessResourceDTO> urlAccessResources) {
		// 1、首先删除以前老的filter chain并注册默认的
		filterChainManager.getFilterChains().clear();
		if (defaultFilterChains != null) {
			filterChainManager.getFilterChains().putAll(defaultFilterChains);
		}

		if (!urlAccessResources.isEmpty()) {
			// 2、循环URL Filter 注册filter chain
			for (UrlAccessResourceDTO urlAccessResource : urlAccessResources) {
				String url = urlAccessResource.getUrl();
				if (!org.apache.commons.lang3.StringUtils.isBlank(url)) {
					// 注册roles filter
					if (!StringUtils.isEmpty(urlAccessResource.getRoles())) {
						filterChainManager.addToChain(url, "anyRole", urlAccessResource.getRoles());
					}
					// 注册perms filter
					if (!StringUtils.isEmpty(urlAccessResource.getPermissions())) {
						filterChainManager.addToChain(url, "perms", urlAccessResource.getPermissions());
					}
				}
			}
		}
		LOGGER.info("filterChain:{}", filterChainManager.getFilterChains());
	}

}
