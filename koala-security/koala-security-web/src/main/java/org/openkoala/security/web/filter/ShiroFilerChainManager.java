package org.openkoala.security.web.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.openkoala.security.facade.SecurityAccessFacade;
import org.openkoala.security.facade.dto.UrlAccessResourceDTO;
import org.springframework.util.StringUtils;

@Named
public class ShiroFilerChainManager {

    @Inject
    private DefaultFilterChainManager filterChainManager;
    
    @Inject
    private SecurityAccessFacade securityAccessFacade;
    
    private Map<String, NamedFilterList> defaultFilterChains;
    
//    @PostConstruct
    public void init() {
        defaultFilterChains = new HashMap<String, NamedFilterList>(filterChainManager.getFilterChains());
    }
    
//    @PostConstruct
    public void initFilterChain(){
    	List<UrlAccessResourceDTO> urlAccessResources = securityAccessFacade.findAllUrlAccessResources();
    	initFilterChains(urlAccessResources);
    }
    
    public void initFilterChains(List<UrlAccessResourceDTO> urlAccessResources) {
        //1、首先删除以前老的filter chain并注册默认的
        filterChainManager.getFilterChains().clear();
        if(defaultFilterChains != null) {
            filterChainManager.getFilterChains().putAll(defaultFilterChains);
        }

        //2、循环URL Filter 注册filter chain
        for (UrlAccessResourceDTO urlAccessResource : urlAccessResources) {
            String url = urlAccessResource.getUrl();
            if(!org.apache.commons.lang3.StringUtils.isBlank(url))
            {
	            //注册roles filter
	            if (!StringUtils.isEmpty(urlAccessResource.getRoles())) {
	                filterChainManager.addToChain(url, "anyRole", urlAccessResource.getRoles());
	            }
	            //注册perms filter
	            if (!StringUtils.isEmpty(urlAccessResource.getPermissions())) {
	                filterChainManager.addToChain(url, "perms", urlAccessResource.getPermissions());
	            }
            }
        }

    }

}
