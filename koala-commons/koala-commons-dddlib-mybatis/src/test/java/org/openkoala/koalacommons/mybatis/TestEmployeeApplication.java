package org.openkoala.koalacommons.mybatis;

import java.util.Date;

import javax.inject.Inject;

import me.lingen.Employee;
import me.lingen.EmployeeApplication;

import org.dayatang.querychannel.Page;
import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.util.Assert;


public class TestEmployeeApplication extends KoalaBaseSpringTestCase { 
  
    @Inject
    private EmployeeApplication employeeApplication; 
  
    @Test
    public void test(){ 
    	for(int i=0;i<11;i++){
    		Employee employee = new Employee();
    		employee.setName("lingen");
    		employee.setBirthDate(new Date());
    		employee.setGender("man");
    		employee.setAge(i);
    		employee.save();
    	}
    	Page<Employee> pages = employeeApplication.findEmployeeByName("lingen", 1, 10); 
    	Assert.isTrue(pages.getPageCount()==2);
    	Assert.isTrue(pages.getStart()==10);
    	Assert.isTrue(pages.getData().size()==1);
    } 
 }