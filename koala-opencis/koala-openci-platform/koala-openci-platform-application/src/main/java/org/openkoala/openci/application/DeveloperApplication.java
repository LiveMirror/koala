package org.openkoala.openci.application;

import java.util.Collection;

import org.dayatang.querychannel.Page;
import org.openkoala.openci.core.Developer;

public interface DeveloperApplication {

	/**
	 * 分页查询开发者信息
	 * @param example
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	Page<Developer> pagingQeuryDevelopers(Developer example, int currentPage, int pagesize);
	
	/**
	 * 创建一个开发者
	 * @param developer
	 */
	void createDeveloper(Developer developer);
	
	/**
	 * 修改一个开发者信息
	 * @param developer
	 */
	void updateDeveloperInfo(Developer developer);
	
	/**
	 * 撤销一个开发者
	 * @param developer
	 */
	void abolishDeveloper(Developer developer);
	
	/**
	 * 撤销一批开发者
	 * @param developers
	 */
	void abolishDevelopers(Collection<Developer> developers);
	
}
