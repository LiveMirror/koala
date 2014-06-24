package org.openkoala.security.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("URL_ACCESS_RESOURCE")
public class UrlAccessResource extends SecurityResource {

	private static final long serialVersionUID = -9116913523532845475L;

	UrlAccessResource() {
	}
	
	public UrlAccessResource(String name){
		super(name);
	}
	
	@Override
	public void update() {
		
		
	}
}