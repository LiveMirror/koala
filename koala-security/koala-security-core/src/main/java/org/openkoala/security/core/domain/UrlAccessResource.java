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
		UrlAccessResource urlAccessResource = get(UrlAccessResource.class, this.getId());
		urlAccessResource.setName(this.getName());
		urlAccessResource.setIdentifier(this.getIdentifier());
		urlAccessResource.setDescription(this.getDescription());
		urlAccessResource.setUrl(this.getUrl());
		urlAccessResource.setVersion(this.getVersion());
	}

	public static List<String> getRoleNames(Set<Authority> authorities) {
		List<String> results = new ArrayList<String>();
		for (Authority authority : authorities) {
			if (authority instanceof Role) {
				results.add(((Role) authority).getName().trim());
			}
		}
		return results;
	}

	public static List<String> getPermissionIdentifiers(Set<Authority> authorities) {
		List<String> results = new ArrayList<String>();
		for (Authority authority : authorities) {
			if (authority instanceof Permission) {
				results.add(((Permission) authority).getIdentifier().trim());
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