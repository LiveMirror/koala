package org.openkoala.security.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("URL_ACCESS_RESOURCE")
public class UrlAccessResource extends SecurityResource {

	private static final long serialVersionUID = -9116913523532845475L;

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}