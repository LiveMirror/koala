package org.openkoala.security.org.facade.impl.assembler;

import org.openkoala.security.org.facade.command.CreateEmpolyeeUserCommand;
import org.openkoala.security.org.core.domain.EmployeeUser;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;

public class EmployeeUserAssembler {

    public static EmployeeUser toEmployeeUser(CreateEmpolyeeUserCommand command) {
        EmployeeUser employeeUser = new EmployeeUser(command.getName(), command.getUserAccount());
        employeeUser.setCreateOwner(command.getCreateOwner());
        employeeUser.setDescription(command.getDescription());
        return employeeUser;
    }

    public static EmployeeUserDTO toEmployeeUserDTO(EmployeeUser employeeUser) {
        EmployeeUserDTO employeeUserDTO = new EmployeeUserDTO(employeeUser.getId(), employeeUser.getVersion(), employeeUser.getName(),//
                employeeUser.getUserAccount(), employeeUser.getCreateDate(), employeeUser.getDescription(),//
                employeeUser.getCreateOwner(), employeeUser.getLastModifyTime(), employeeUser.isDisabled());
        return employeeUserDTO;
    }
}
