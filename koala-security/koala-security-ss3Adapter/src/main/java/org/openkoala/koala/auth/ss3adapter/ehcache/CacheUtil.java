package org.openkoala.koala.auth.ss3adapter.ehcache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.UserDetails;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.openkoala.koala.auth.ss3adapter.WildcardSecurityMetadataSource;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.dayatang.domain.InstanceFactory;

public class CacheUtil {

	private static AuthDataService provider;

	private static com.dayatang.cache.Cache userCache = null;
	private static com.dayatang.cache.Cache resourceCache = null;

	public static AuthDataService getAuthDataService() {
		if (provider == null) {
			provider = InstanceFactory.getInstance(AuthDataService.class);
		}
		return provider;
	}

	public static com.dayatang.cache.Cache getResourceCache() {
		if (resourceCache == null) {
			resourceCache = InstanceFactory.getInstance(com.dayatang.cache.Cache.class, "resource_cache");
		}
		return resourceCache;
	}

	public static com.dayatang.cache.Cache getUserCache() {
		if (userCache == null) {
			userCache = InstanceFactory.getInstance(com.dayatang.cache.Cache.class, "user_cache");
		}
		return userCache;
	}

	public static void refreshUrlAttributes(String url) {
		getResourceCache().remove(url);
		getResourceCache().put(url, getAuthDataService().getAttributes(url));

        if(getResourceCache().isKeyInCache(WildcardSecurityMetadataSource.ALL_RESOURCE_MAP)){
            Map<String, List<String>> resMap = (Map<String, List<String>>) getResourceCache().get(WildcardSecurityMetadataSource.ALL_RESOURCE_MAP);
            resMap.remove(url);
            resMap.put(url,getAuthDataService().getAttributes(url));
            getResourceCache().remove(WildcardSecurityMetadataSource.ALL_RESOURCE_MAP);
            getResourceCache().put(WildcardSecurityMetadataSource.ALL_RESOURCE_MAP,resMap);

        }
	}

	public static void refreshUserAttributes(String user) {
		if (!getUserCache().isKeyInCache(user)) {
			UserDetails userdetail = getUserDetails(user);
			CustomUserDetails userCd = new CustomUserDetails(userdetail.getPassword(), userdetail.getUseraccount(), userdetail
					.isAccountNonExpired(), userdetail.isAccountNonLocked(), userdetail.isCredentialsNonExpired(),
					userdetail.isEnabled(), new ArrayList());
			getUserCache().put(user, userCd);
			
		}
		CustomUserDetails cd = (CustomUserDetails) getUserCache().get(user);
		cd.getAuthorities().clear();
		for (String role : getUserDetails(user).getAuthorities()) {
			GrantedAuthorityImpl gai = new GrantedAuthorityImpl(role);
			cd.getAuthorities().add(gai);
		}
	}

	private static UserDetails getUserDetails(String user) {
		return getAuthDataService().loadUserByUseraccount(user);
	}

	public static void removeUserFromCache(String user) {
		getUserCache().remove(user);
	}

}
