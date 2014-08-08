package org.openkoala.security.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openkoala.security.core.IdentifierIsExistedException;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PAGE_ELEMENT_RESOURCE")
public class PageElementResource extends SecurityResource {

	private static final long serialVersionUID = 8933589588651981397L;

	@Column(name = "IDENTIFIER")
	private String identifier;

	protected PageElementResource() {}

	public PageElementResource(String name, String identifier) {
		super(name);
		checkArgumentIsNull("identifier", identifier);
		isIdentifierExisted(identifier);
		this.identifier = identifier;
	}

	@Override
	public void save() {
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
				.singleResult();
	}

	private void isIdentifierExisted(String identifier) {
		if (null != getby(identifier)) {
			throw new IdentifierIsExistedException("pageElemntResource identifier is existed.");
		}
	}

	public static boolean hasIdentifier(String identifier) {
		return getby(identifier) == null;
	}

	private static PageElementResource getby(String identifier) {
		PageElementResource ressult = getRepository()//
				.createCriteriaQuery(PageElementResource.class)//
				.eq("identifier", identifier)//
				.singleResult();
		return ressult;
	}

	public void changeIdentifier(String identifier) {
		checkArgumentIsNull("identifier", identifier);
		if (!identifier.equals(this.getIdentifier())) {
			isIdentifierExisted(identifier);
		}
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)//
				.append(getName())//
				.append(getIdentifier())//
				.append(getDescription())//
				.build();
	}

	public String getIdentifier() {
		return identifier;
	}

}