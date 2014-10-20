package org.openkoala.organisation.application;

import java.util.Date;
import java.util.Map;

import org.openkoala.organisation.core.domain.Employee;
import org.openkoala.organisation.core.domain.Organization;
import org.openkoala.organisation.core.domain.Post;

/**
 * 员工应用层接口
 * 
 */
public interface EmployeeApplication {

	/**
	 * 获取某个员工的所属机构
	 * 
	 * @param employee
	 * @param date
	 * @return
	 */
	Organization getOrganizationOfEmployee(Employee employee, Date date);

	/**
	 * 创建员工的任职责任信息
	 * 
	 * @param employee
	 * @param post
	 */
	void createEmployeeWithPost(Employee employee, Post post);

	/**
	 * 调整某个员工的任职信息
	 * 
	 * @param employee
	 * @param responsiblePosts
	 */
	void transformPost(Employee employee, Map<Post, Boolean> responsiblePosts);

	/**
	 * 获得某个员工的任职职务信息
	 * 
	 * @param employee
	 * @return
	 */
	Map<Post, Boolean> getPostsByEmployee(Employee employee);

}
