package org.openkoala.security.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 方法调用权限
 */
@Entity
@DiscriminatorValue("METHOD_INVOCATION_RESOURCE")
public class MethodInvocationResource extends SecurityResource {

	private static final long serialVersionUID = -6741395663493601253L;

	@Override
	public SecurityResource findByName(String name) {
		return getRepository()//
				.createNamedQuery("SecurityResource.findByName")//
				.addParameter("securityResourceType", MethodInvocationResource.class)//
				.addParameter("name", name)//
				.addParameter("disabled", false)//
				.singleResult();
	}

}