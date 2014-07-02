package org.openkoala.security.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("URL_ACCESS_RESOURCE")
public class UrlAccessResource extends SecurityResource {

	private static final long serialVersionUID = -9116913523532845475L;

	UrlAccessResource() {
	}

	public UrlAccessResource(String name) {
		super(name);
	}

	@Override
	public void update() {

	}

	public static List<String> getRoleNames(Set<Authority> authorities) {
		List<String> results = new ArrayList<String>();
		for (Authority authority : authorities) {
			if (authority instanceof Role) {
				results.add(((Role) authority).getName());
			}
		}
		return results;
	}

	public static List<String> getPermissionIdentifiers(Set<Authority> authorities) {
		List<String> results = new ArrayList<String>();
		for (Authority authority : authorities) {
			if (authority instanceof Permission) {
				results.add(((Permission) authority).getIdentifier());
			}
		}
		return results;
	}

	public static List<UrlAccessResource> findAllUrlAccessResources() {
		return getRepository().createNamedQuery("SecurityResource.findAllByType")//
				.addParameter("_securityResourceType", UrlAccessResource.class)//
				.addParameter("disabled", false)//
				.list();
	}
}