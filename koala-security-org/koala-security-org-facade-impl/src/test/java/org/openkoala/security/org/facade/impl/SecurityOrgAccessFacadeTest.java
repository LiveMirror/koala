package org.openkoala.security.org.facade.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.inject.Inject;

import org.dayatang.utils.Page;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Person;
import org.openkoala.security.org.core.domain.EmployeeUser;
import org.openkoala.security.org.facade.SecurityOrgAccessFacade;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;

public class SecurityOrgAccessFacadeTest extends AbstractOrgFacadeIntegrationTestCase{

    @Inject
    private SecurityOrgAccessFacade securityOrgAccessFacade;

    private EmployeeUser employeeUser;

    @Before
    public void setUp(){

        employeeUser = new EmployeeUser("zhangsan","张三");
        Person person = new Person("张三");
        person.save();
        Employee employee = new Employee(person,"zhangsanNO",new Date());
        employee.save();
        employeeUser.setEmployee(employee);
        employeeUser.save();
    }

    @Test
    public void testPagingQueryEmployeeUsers(){
        Page<EmployeeUserDTO> result = securityOrgAccessFacade.pagingQueryEmployeeUsers(0, 10, new EmployeeUserDTO());
        assertFalse(result.getData().isEmpty());
        assertTrue(result.getData().size() == 1);
    }
}
