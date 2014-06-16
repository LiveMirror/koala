package org.openkoala.security.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("METHOD_INVOCATION_RESOURCE")
public class MethodInvocationResource extends SecurityResource {

	private static final long serialVersionUID = -6741395663493601253L;

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}