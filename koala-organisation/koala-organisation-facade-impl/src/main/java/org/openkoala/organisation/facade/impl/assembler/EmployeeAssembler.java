package org.openkoala.organisation.facade.impl.assembler;

import java.util.Date;
import java.util.List;

import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Gender;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.core.domain.Person;
import org.openkoala.organisation.core.domain.Post;
import org.openkoala.organisation.facade.dto.EmployeeDTO;

public class EmployeeAssembler {

	public static Employee toEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null ){
            return null;
        }

		Person person = new Person(employeeDTO.getName());
		person.setId(employeeDTO.getPersonId());
		person.setVersion(employeeDTO.getPersonVersion());
		person.setEmail(employeeDTO.getEmail());
		person.setFamilyPhone(employeeDTO.getFamilyPhone());
		if (employeeDTO.getGender() != null) {
			person.setGender(Gender.valueOf(employeeDTO.getGender()));
		}
		person.setIdNumber(employeeDTO.getIdNumber());
		person.setMobilePhone(employeeDTO.getMobilePhone());
		
		Employee result = new Employee(person, employeeDTO.getEntryDate());
		result.setId(employeeDTO.getId());
		result.setSn(employeeDTO.getSn());
		result.setVersion(employeeDTO.getVersion());
		
		if (employeeDTO.getCreateDate() != null) {
			result.setCreateDate(employeeDTO.getCreateDate());
		}
		if (employeeDTO.getTerminateDate() != null) {
			result.setTerminateDate(employeeDTO.getTerminateDate());
		}
		return result;
	}
	
	public static EmployeeDTO toDTO(Employee employee) {
		if (employee == null) {
			return null;
		}
		Date now = new Date();
		
		EmployeeDTO result = new EmployeeDTO();
		result.setId(employee.getId());
		result.setPersonId(employee.getPerson().getId());
		result.setPersonVersion(employee.getPerson().getVersion());
		result.setName(employee.getName());
		
		if (employee.getPerson().getGender() != null) {
			result.setGender(employee.getPerson().getGender().name());
		}
		
		Post post = employee.getPrincipalPost(now);
		if (post != null) {
			result.setPostName(post.getName());
		}
		
		List<Post> additionalPosts = employee.getAdditionalPosts(now);
		if (!additionalPosts.isEmpty()) {
			StringBuilder additionalPostNames = new StringBuilder();
			String separator = ", ";
			for (Post additionalPost : additionalPosts) {
				additionalPostNames.append(additionalPost.getName());
				additionalPostNames.append(separator);
			}
			result.setAdditionalPostNames(additionalPostNames.substring(0, additionalPostNames.length() - separator.length()));
		}
		
		Organization organization = employee.getOrganization(new Date());
		if (organization != null) {
			result.setOrganizationName(organization.getFullName());
		}
		
		result.setEmail(employee.getPerson().getEmail());
		result.setEntryDate(employee.getEntryDate());
		result.setFamilyPhone(employee.getPerson().getFamilyPhone());
		result.setIdNumber(employee.getPerson().getIdNumber());
		result.setMobilePhone(employee.getPerson().getMobilePhone());
		
		result.setSn(employee.getSn());
		result.setVersion(employee.getVersion());
		result.setCreateDate(employee.getCreateDate());
		result.setTerminateDate(employee.getTerminateDate());
		
		return result;
	}
	
}
