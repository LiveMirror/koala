package org.openkoala.koalacommons.mybatis;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.springframework.util.Assert;

import me.lingen.Employee;

/**
 * CREATE TABLE `Employee` ( `id` int(11) DEFAULT NULL, `name` varchar(255)
 * DEFAULT NULL, `age` varchar(255) DEFAULT NULL, `birthDate` date DEFAULT NULL,
 * `gender` varchar(255) DEFAULT NULL )
 * 
 * @author lingen
 * 
 */
public class EmployeeTest extends KoalaBaseSpringTestCase {

	@Test
	public void add() {
		Employee employee = new Employee();
		employee.setName("lingen");
		employee.setBirthDate(new Date());
		employee.setGender("man");
		employee.setAge(21);
		employee.save();
	}

	@Test
	public void update() {
		Employee employee = new Employee();
		employee.setId(1l);
		employee.setName("lingen");
		employee.setBirthDate(new Date());
		employee.setGender("man");
		employee.setAge(21);
		employee.save();
	}

	@Test
	public void remove() {
		Employee employee = new Employee();
		employee.setId(1l);
		employee.remove();
	}

	@Test
	public void get() {
		Employee employee = Employee.getRepository().get(Employee.class, 10l);
		Assert.isNull(employee);
	}

	@Test
	public void getAll() {
		Employee.getRepository().findAll(Employee.class);
	}

	@Test
	public void testQueryByName() {
		Employee employee = new Employee();
		employee.setName("lingen");
		employee.setBirthDate(new Date());
		employee.setGender("man");
		employee.setAge(21);
		employee.save();

		List<Employee> employees = Employee.findByName("lingen");
		System.out.println(employees);
		Assert.notNull(employees);
	}
	
	@Test
	public void testQueryByName2() {
		Employee employee = new Employee();
		employee.setName("lingen");
		employee.setBirthDate(new Date());
		employee.setGender("man");
		employee.setAge(21);
		employee.save();

		List<Employee> employees = Employee.findByName2("lingen");
		System.out.println(employees);
		Assert.notNull(employees);
	}
	
	@Test
	public void testQueryByNameSingle() {
		Employee employee = new Employee();
		employee.setName("lingen");
		employee.setBirthDate(new Date());
		employee.setGender("man");
		employee.setAge(21);
		employee.save();

		Employee employees = Employee.findByNameSingle("lingen");
		System.out.println(employees);
		Assert.notNull(employees);
	}

}
