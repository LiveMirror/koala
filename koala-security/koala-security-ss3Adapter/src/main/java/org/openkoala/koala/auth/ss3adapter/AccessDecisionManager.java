package org.openkoala.koala.auth.ss3adapter;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;

import org.dayatang.domain.InstanceFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * ClassName:AccessDecisionManager Function: 决策类 当请求访问时，判断用户是否具有访问所需的所有权限
 * 
 * @author caoyong
 * @version 1.0, 2012-12-04
 * @since ss31.0
 */
// @Component("accessDecisionManager")
public class AccessDecisionManager implements org.springframework.security.access.AccessDecisionManager {

	/**
	 * authentication用户认证后 存有用户的所有权限 configAttributes访问所需要的权限 若无权则抛出异常
	 */
	public void decide(Authentication authentication, Object url, Collection<ConfigAttribute> configAttributes) {
		if (configAttributes.equals(Collections.EMPTY_LIST)) {
			return;
		}

		org.dayatang.cache.Cache userCache = InstanceFactory.getInstance(org.dayatang.cache.Cache.class, "user_cache");
		CustomUserDetails currentUser = (CustomUserDetails) userCache.get(authentication.getName());
		
		if (currentUser.isSuper()) {
			return;
		}
		
		if (userCache.containsKey(authentication.getName())) {
			for (ConfigAttribute configAttribute : configAttributes) {
				for (GrantedAuthority gAuthority : currentUser.getAuthorities()) {
					if (configAttribute.getAttribute().trim().equals(gAuthority.getAuthority().trim())) {
						return;
					}
				}
			}
		}

		for (ConfigAttribute configAttribute : configAttributes) {
			for (GrantedAuthority gAuthority : authentication.getAuthorities()) {
				if (configAttribute.getAttribute().trim().equals(gAuthority.getAuthority().trim())) {
					return;
				}
			}
		}
		// 无权限抛出拒绝异常
		throw new AccessDeniedException(MessageFormat.format("Denied to access [{0}]", url));
	}

	public boolean supports(ConfigAttribute arg0) {
		return true;
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

}
