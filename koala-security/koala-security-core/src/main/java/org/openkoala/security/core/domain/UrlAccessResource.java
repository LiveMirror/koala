package org.openkoala.security.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;

@Entity
@DiscriminatorValue("URL_ACCESS_RESOURCE")
public class UrlAccessResource extends SecurityResource {

	private static final long serialVersionUID = -9116913523532845475L;

	protected UrlAccessResource() {}

	public UrlAccessResource(String name, String url) {
		super(name, url);
	}

	@Override
	public void save() {
		isNameExisted();
		isUrlExisted();
		super.save();
	}

	@Override
	public void update() {
		UrlAccessResource urlAccessResource = getBy(this.getId());
		if(!StringUtils.isBlank(this.getName()) && !urlAccessResource.getName().equals(this.getName())){
			isNameExisted();
			urlAccessResource.name = this.getName();
		}
		if(!StringUtils.isBlank(this.getUrl()) && !urlAccessResource.getUrl().equals(this.getUrl())){
			isUrlExisted();
			urlAccessResource.setUrl(this.getUrl());
		}
		urlAccessResource.setIdentifier(this.getIdentifier());
		urlAccessResource.setDescription(this.getDescription());
		urlAccessResource.setVersion(this.getVersion());
	}

	public static UrlAccessResource getBy(Long id) {
		return UrlAccessResource.get(UrlAccessResource.class, id);
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
		return UrlAccessResource.findAll(UrlAccessResource.class);
//		List<UrlAccessResource> results =  getRepository()//
//				.createNamedQuery("SecurityResource.findAllByType")//
//				.addParameter("securityResourceType", UrlAccessResource.class)//
//				.addParameter("disabled", false)//
//				.list();
//		return results;
	}

	@Override
	public SecurityResource findByName(String name) {
		return getRepository()//
				.createNamedQuery("SecurityResource.findByName")//
				.addParameter("securityResourceType", UrlAccessResource.class)//
				.addParameter("name", name)//
				.addParameter("disabled", false)//
				.singleResult();
	}

	@Override
	protected SecurityResource findByUrl(String url) {
		return getRepository()//
				.createCriteriaQuery(UrlAccessResource.class)//
				.eq("url", url)//
				.singleResult();
	}
	
	@Override
	public String[] businessKeys() {
		return new String[] { "name", "url" };
	}

}