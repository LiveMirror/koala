package org.openkoala.organisation.facade.impl.assembler;

import java.util.Date;
import java.util.List;

import org.openkoala.organisation.core.domain.Company;
import org.openkoala.organisation.core.domain.Department;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.facade.dto.OrganizationDTO;

public class OrganizationAssembler {

	public static OrganizationDTO toDTO(Organization organization) {
        if (organization == null) {
            return null;
        }

		OrganizationDTO dto = new OrganizationDTO(organization.getId(), organization.getName());
		dto.setSn(organization.getSn());
		dto.setCreateDate(organization.getCreateDate());
		dto.setDescription(organization.getDescription());
		dto.setVersion(organization.getVersion());

		if (organization instanceof Company) {
			dto.setOrganizationType(OrganizationDTO.COMPANY);
		} else {
			dto.setOrganizationType(OrganizationDTO.DEPARTMENT);
		}

		List<Employee> principals = organization.getPrincipal(new Date());
		if (!principals.isEmpty()) {
			String separator = "/";
			StringBuilder principal = new StringBuilder();
			for (Employee employee : principals) {
				principal.append(employee.getName());
				principal.append(separator);
			}
			dto.setPrincipalName(principal.substring(0, principal.length() - separator.length()));
		}

		return dto;
	}

	public static Organization toEntity(OrganizationDTO organizationDTO) {
        if (organizationDTO == null) {
            return null;
        }

		Organization result = null;
		if (organizationDTO.getOrganizationType().equals(OrganizationDTO.COMPANY)) {
			result = new Company(organizationDTO.getName(), organizationDTO.getSn());
		} else {
			result = new Department(organizationDTO.getName(), organizationDTO.getSn());
		}

		result.setId(organizationDTO.getId());
		result.setSn(organizationDTO.getSn());
		result.setDescription(organizationDTO.getDescription());
		result.setVersion(organizationDTO.getVersion());
		
		if (organizationDTO.getCreateDate() != null) {
			result.setCreateDate(organizationDTO.getCreateDate());
		}
		if (organizationDTO.getTerminateDate() != null) {
			result.setTerminateDate(organizationDTO.getTerminateDate());
		}
		return result;
	}

}
