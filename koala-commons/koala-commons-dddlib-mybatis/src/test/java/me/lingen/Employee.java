package me.lingen;

import org.openkoala.koala.commons.domain.KoalaMyBatisBaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author lingen
 * 
 */
public class Employee extends KoalaMyBatisBaseEntity {

	private static final long serialVersionUID = 4730728647139981256L;

	/**
	 * ID主键，自增长
	 */
	private long id;

	/**
	 * 员工名称
	 */
	private String name;

	/**
	 * 员工年龄
	 */
	private int age;

	/**
	 * 员工出生日期
	 */
	private Date birthDate;

	/**
	 * 员工性别
	 */
	private String gender;

	public Long getId() {
		return id;
	}

	public boolean existed() {
		return getRepository().exists(Employee.class, id);
	}

	public boolean notExisted() {
		return !getRepository().exists(Employee.class, id);
	}

	public boolean existed(String propertyName, Object propertyValue) {
		return !getRepository()
				.createNamedQuery(Employee.class,"propertyValueExists")
				.setParameters(propertyName, propertyValue).list().isEmpty();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", age=" + age
				+ ", birthDate=" + birthDate + ", gender=" + gender + "]";
	}

	@Override
	public String[] businessKeys() {
		return new String[]{"name"};
	}


	public static List<Employee> findByName(String name) {
		List<Employee> results = Employee.getRepository().createNamedQuery(Employee.class,"findByName").setParameters(name).list();
		return results;
	}


	public static List<Employee> findByName2(String name) {
		List<Employee> results = Employee.getRepository().createNamedQuery(Employee.class,"findByName2").addParameter("name", name).list();
		return results;
	}
	
	public static Employee findByNameSingle(String name) {
		return (Employee) Employee.getRepository().createNamedQuery(Employee.class,"findByName").setParameters(name).singleResult();
	}
	
	public void save(){
		getRepository().save(this);
	}

}
