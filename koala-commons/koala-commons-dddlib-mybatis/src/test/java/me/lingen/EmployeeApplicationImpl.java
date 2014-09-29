package me.lingen;

import javax.inject.Inject;
import javax.inject.Named;

import org.dayatang.utils.Page;
import org.openkoala.koalacommons.mybatis.MybatisQueryChannelService;
import org.springframework.transaction.annotation.Transactional;


@Named("employeeApplication") 
@Transactional
public class EmployeeApplicationImpl implements EmployeeApplication { 
 
   @Inject
   public MybatisQueryChannelService queryChannel; 
 
   public Page<Employee> findEmployeeByName(String name,int currentPage,int pageSize) { 
       Page<Employee> employees = 
           queryChannel.createNamedQuery(Employee.class, "findEmployeeByName").addParameter("name", name).setPage(currentPage, pageSize).pagedList();
       return employees; 
   } 
}
