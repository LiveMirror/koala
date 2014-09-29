package me.lingen;

import org.dayatang.utils.Page;

public interface EmployeeApplication {

	public Page<Employee> findEmployeeByName(String name, int currentPage,
			int pageSize);
}
