package org.openkoala.security.core.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.openkoala.security.core.UrlIsExistedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("URL_ACCESS_RESOURCE")
public class UrlAccessResource extends SecurityResource {

	private static final long serialVersionUID = -9116913523532845475L;

	@Column(name = "URL")
	private String url;

	protected UrlAccessResource() {}

	public UrlAccessResource(String name, String url) {
		super(name);
		checkArgumentIsNull("url", url);
		isExistUrl(url);
		this.url = url;
	}

	@Override
	public void save() {
		super.save();
	}

	@Override
	public SecurityResource findByName(String name) {
		return getRepository()//
				.createNamedQuery("SecurityResource.findByName")//
				.addParameter("securityResourceType", UrlAccessResource.class)//
				.addParameter("name", name)//
				.singleResult();
	}

	public void changeUrl(String url) {

		checkArgumentIsNull("url", url);

		if (!url.equals(this.getUrl())) {
			isExistUrl(url);
			this.url = url;
			this.save();
		}
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
		List<UrlAccessResource> results = getRepository()//
				.createNamedQuery("SecurityResource.findAllByType")//
				.addParameter("securityResourceType", UrlAccessResource.class)//
				.list();
		return results;
	}

	/**
	 * 
	 * @param url url of the UrlAccessResource, can't be null.
	 * @return
	 */
	protected UrlAccessResource findByUrl(String url) {
		checkArgumentIsNull("url", url);
		return getRepository()//
				.createCriteriaQuery(UrlAccessResource.class)//
				.eq("url", url)//
				.singleResult();
	}

	private void isExistUrl(String url) {
		if (findByUrl(url) != null) {
			throw new UrlIsExistedException("url is existed.");
		}
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String[] businessKeys() {
		return new String[] { "name", "url" };
	}

}