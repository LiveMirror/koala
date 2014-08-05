package org.openkoala.security.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PAGE_ELEMENT_RESOURCE")
public class PageElementResource extends SecurityResource {

	private static final long serialVersionUID = 8933589588651981397L;

	protected PageElementResource() {}

	public PageElementResource(String name, String identifier) {
		super(name);
		this.identifier = identifier;
	}

	@Override
	public void save() {
		isNameExisted();
		super.save();
	}

	public static PageElementResource getBy(String securityResourceName) {
		return getRepository().createCriteriaQuery(PageElementResource.class)//
				.eq("name", securityResourceName)//
				.singleResult();
	}

	public static PageElementResource getBy(Long id) {
		return PageElementResource.get(PageElementResource.class, id);
	}

	@Override
	public SecurityResource findByName(String name) {
		return getRepository()//
				.createNamedQuery("SecurityResource.findByName")//
				.addParameter("securityResourceType", PageElementResource.class)//
				.addParameter("name", name)//
				.addParameter("disabled", false)//
				.singleResult();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(name)//
				.append(getIdentifier())//
				.append(getDescription())//
				.build();
	}

	public static boolean hasIdentifier(String identifier) {
		PageElementResource ressult = getRepository()//
				.createCriteriaQuery(PageElementResource.class)//
				.eq("identifier", identifier)//
				.eq("disabled", false)//
				.singleResult();
		return ressult == null ? false : true;
	}

}