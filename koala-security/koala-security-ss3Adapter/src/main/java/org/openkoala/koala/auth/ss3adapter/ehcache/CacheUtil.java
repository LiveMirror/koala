package org.openkoala.koala.auth.ss3adapter.ehcache;

import java.util.List;
import java.util.Map;

import org.dayatang.cache.Cache;
import org.dayatang.domain.InstanceFactory;
import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.openkoala.koala.auth.ss3adapter.WildcardSecurityMetadataSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 缓存工具类
 * 
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 16, 2013 9:55:29 AM
 */
public final class CacheUtil {

	private static AuthDataService provider;

	private static Cache userCache = null;
	private static Cache resourceCache = null;

	private CacheUtil() {

	}

	public static AuthDataService getAuthDataService() {
		if (provider == null) {
			provider = InstanceFactory.getInstance(AuthDataService.class);
		}
		return provider;
	}

	/**
	 * 获取资源缓存信息
	 * 
	 * @return
	 */
	public static Cache getResourceCache() {
		if (resourceCache == null) {
			resourceCache = InstanceFactory.getInstance(Cache.class, "resource_cache");
		}
		return resourceCache;
	}

	/**
	 * 获取用户缓存信息
	 * 
	 * @return
	 */
	public static Cache getUserCache() {
		if (userCache == null) {
			userCache = InstanceFactory.getInstance(Cache.class, "user_cache");
		}
		return userCache;
	}

	/**
	 * 刷新资源缓存
	 * 
	 * @param url
	 */
	public static void refreshUrlAttributes(String url) {
		getResourceCache().remove(url);
		getResourceCache().put(url, getAuthDataService().getAttributes(url));

        //TODO 请在适当的时候重构 lingen
        if(getResourceCache().containsKey(WildcardSecurityMetadataSource.ALL_RESOURCE_PRIVI)){
            Map<String, List<String>> resMap = (Map<String, List<String>>) getResourceCache().get(WildcardSecurityMetadataSource.ALL_RESOURCE_PRIVI);
            resMap.remove(url);
            resMap.put(url,getAuthDataService().getAttributes(url));
            getResourceCache().remove(WildcardSecurityMetadataSource.ALL_RESOURCE_PRIVI);
            getResourceCache().put(WildcardSecurityMetadataSource.ALL_RESOURCE_PRIVI,resMap);

        }
	}

	/**
	 * 刷新资源标识对应的缓存
	 * 
	 * @param resourceIds
	 */
	public static void refreshUrlAttributes(List<Long> resourceIds) {
		if (resourceIds == null || resourceIds.isEmpty()) {
			return;
		}
		Map<String, List<String>> result = getAuthDataService().getAttributes(resourceIds);
		if (result != null && result.size() > 0) {
			for (String identifier : result.keySet()) {
				getResourceCache().remove(identifier);
				getResourceCache().put(identifier, result.get(identifier));
			}
		}

        //TODO 请在适当的时候重构  lingen
        if(getResourceCache().containsKey(WildcardSecurityMetadataSource.ALL_RESOURCE_PRIVI)){
            if (result != null && result.size() > 0) {
                Map<String, List<String>> resMap = (Map<String, List<String>>) getResourceCache().get(WildcardSecurityMetadataSource.ALL_RESOURCE_PRIVI);
                for (String identifier : result.keySet()) {
                    resMap.remove(identifier);
                    resMap.put(identifier,result.get(identifier));
                }
                getResourceCache().remove(WildcardSecurityMetadataSource.ALL_RESOURCE_PRIVI);
                getResourceCache().put(WildcardSecurityMetadataSource.ALL_RESOURCE_PRIVI,resMap);
            }
        }
	}

	/**
	 * 刷新用户授权信息
	 * 
	 * @param user
	 */
	public static void refreshUserAttributes(String user) {
		if (!getUserCache().containsKey(user)) {
			return;
		}
		CustomUserDetails cd = (CustomUserDetails) getUserCache().get(user);
		cd.getAuthorities().clear();
		for (String role : getAuthDataService().loadUserByUseraccount(user).getAuthorities()) {
			SimpleGrantedAuthority gai = new SimpleGrantedAuthority(role);
			cd.getAuthorities().add(gai);
		}
	}

	/**
	 * 将用户信息从缓存中删除
	 * 
	 * @param user
	 */
	public static void removeUserFromCache(String user) {
		getUserCache().remove(user);
	}

}
