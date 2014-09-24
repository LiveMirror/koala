package org.openkoala.security.org.facade.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.organisation.domain.Employee;
import org.openkoala.organisation.domain.Person;
import org.openkoala.security.org.core.domain.EmployeeUser;
import org.openkoala.security.org.facade.SecurityOrgAccessFacade;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by luzhao on 14-8-29.
 */
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
        InvokeResult result = securityOrgAccessFacade.pagingQueryEmployeeUsers(0, 10, new EmployeeUserDTO());
        assertTrue(result.isSuccess());
    }
}
