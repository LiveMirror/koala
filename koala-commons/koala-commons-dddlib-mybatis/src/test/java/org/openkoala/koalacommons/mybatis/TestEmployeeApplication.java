package org.openkoala.koalacommons.mybatis;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;

import me.lingen.Employee;
import me.lingen.EmployeeApplication;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.utils.Page;
import org.junit.Before;
import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.util.Assert;


public class TestEmployeeApplication extends KoalaBaseSpringTestCase { 
  
    @Inject
    private EmployeeApplication employeeApplication;

    @Before
    public void before() throws SQLException {
        Statement stmt  = null;
        try{
            String create = " CREATE TABLE `Employee` ( `id` int(11) DEFAULT NULL, `name` varchar(255) DEFAULT NULL, `age` varchar(255) DEFAULT NULL, `birthDate` date DEFAULT NULL, `gender` varchar(255) DEFAULT NULL )";
            DataSource dataSource = InstanceFactory.getInstance(DataSource.class);
            stmt = dataSource.getConnection().createStatement();
            stmt.execute(create);
        }catch(Exception e){
        }finally{
            stmt.close();
        }
    }

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