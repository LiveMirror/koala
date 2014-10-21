package org.openkoala.koala.widget;

import java.io.Serializable;

import org.openkoala.koala.annotation.GeneratedSourceCode;
import org.openkoala.koala.annotation.ObjectFunctionCreate;

/**
 * 组织子系统
 * @author lingen
 *
 */
@ObjectFunctionCreate
public class Organization implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3933594587736581421L;

	@GeneratedSourceCode
	private Boolean OrganizationGenerateSourceCode = true;

	public Boolean getOrganizationGenerateSourceCode() {
		return OrganizationGenerateSourceCode;
	}

	public void setOrganizationGenerateSourceCode(Boolean organizationGenerateSourceCode) {
		OrganizationGenerateSourceCode = organizationGenerateSourceCode;
	}
	
}
