package org.openkoala.organisation.facade.impl.assembler;

import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.facade.dto.TransformOrganizationDTO;

public class TransformOrganizationAssembler {
	
	public static TransformOrganizationDTO toDTOByOrganization(Organization organization, boolean principal) {
		TransformOrganizationDTO result = new TransformOrganizationDTO();
		result.setPrincipal(principal);
		result.setOrganizationId(organization.getId());
		result.setOrganizationName(organization.getName());
		result.setOrganizationSn(organization.getSn());
		result.setOrganizationDescription(organization.getDescription());
		return result;
	}
	
}
