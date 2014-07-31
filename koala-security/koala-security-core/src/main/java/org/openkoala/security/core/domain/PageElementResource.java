package org.openkoala.security.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("PAGE_ELEMENT_RESOURCE")
public class PageElementResource extends SecurityResource {

	private static final long serialVersionUID = 8933589588651981397L;

	/**
	 * 页面元素类型。
	 */
	private String pageElementType;

	protected PageElementResource() {}

	public PageElementResource(String name) {
		super(name);
	}

	@Override
	public void save() {
		isNameExisted();
		super.save();
	}

	@Override
	public void update() {
		PageElementResource pageElementResource = getBy(this.getId());
		if (!StringUtils.isBlank(this.getName()) && !pageElementResource.getName().equals(this.getName())) {
			isNameExisted();
			pageElementResource.name = this.getName();
		}
		pageElementResource.setIdentifier(this.getIdentifier());
		pageElementResource.setPageElementType(this.getPageElementType());
		pageElementResource.setDescription(this.getDescription());
		pageElementResource.setVersion(this.getVersion());
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
				.append(pageElementType)//
				.build();
	}

	public String getPageElementType() {
		return pageElementType;
	}

	public void setPageElementType(String type) {
		this.pageElementType = type;
	}

}