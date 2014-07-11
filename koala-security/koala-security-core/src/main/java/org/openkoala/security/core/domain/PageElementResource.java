package org.openkoala.security.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PAGE_ELEMENT_RESOURCE")
public class PageElementResource extends SecurityResource {

	private static final long serialVersionUID = 8933589588651981397L;

	/**
	 * 页面元素类型。
	 */
	private String pageElementType;

	PageElementResource() {
	}

	public PageElementResource(String name) {
		super(name);
	}

	@Override
	public void update() {
		PageElementResource pageElementResource = get(PageElementResource.class, this.getId());
		pageElementResource.setName(this.getName());
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

	public String getPageElementType() {
		return pageElementType;
	}

	public void setPageElementType(String type) {
		this.pageElementType = type;
	}

}