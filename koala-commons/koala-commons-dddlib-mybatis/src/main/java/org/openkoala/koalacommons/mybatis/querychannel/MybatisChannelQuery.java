package org.openkoala.koalacommons.mybatis.querychannel;

import java.util.List;
import java.util.Map;

import org.dayatang.domain.QueryParameters;
import org.dayatang.utils.Assert;
import org.dayatang.utils.Page;
import org.openkoala.koala.commons.domain.MybatisEntityRepository;
import org.openkoala.koala.commons.domain.mybatis.MybatisBaseQuery;
import org.openkoala.koala.commons.domain.mybatis.MybatisNamedQuery;

public abstract class MybatisChannelQuery<E extends MybatisChannelQuery> {

	protected MybatisEntityRepository repository;
	
	private MybatisBaseQuery query;
	
	private int pageIndex;
	
	private static final String COUNT = "Count";
	

	// private int pageSize = Page.DEFAULT_PAGE_SIZE;

	public MybatisChannelQuery(MybatisEntityRepository repository) {
		this.repository = repository;
	}

	public void setQuery(MybatisBaseQuery query) {
		this.query = query;
	}

	/**
	 * 获取查询参数
	 * 
	 * @return 查询参数
	 */
	public QueryParameters getParameters() {
		return query.getParameters();
	}

	/**
	 * 设置定位命名参数（数组方式）
	 * 
	 * @param parameters 要设置的参数
	 * @return 该对象本身
	 */
	public E setParameters(Object... parameters) {
		query.setParameters(parameters);
		return (E) this;
	}

	/**
	 * 设置定位参数（列表方式）
	 * 
	 * @param parameters 要设置的参数
	 * @return 该对象本身
	 */
	public E setParameters(List<Object> parameters) {
		query.setParameters(parameters);
		return (E) this;
	}

	/**
	 * 设置命名参数（Map形式，Key是参数名称，Value是参数值）
	 * 
	 * @param parameters 要设置的参数
	 * @return 该对象本身
	 */
	public E setParameters(Map<String, Object> parameters) {
		query.setParameters(parameters);
		return (E) this;
	}

	/**
	 * 添加一个命名参数，Key是参数名称，Value是参数值。
	 * 
	 * @param key
	 *            命名参数名称
	 * @param value
	 *            参数值
	 * @return 该对象本身
	 */
	public E addParameter(String key, Object value) {
		query.addParameter(key, value);
		return (E) this;
	}

	/**
	 * 针对分页查询，获取firstResult。 firstResult代表从满足查询条件的记录的第firstResult + 1条开始获取数据子集。
	 * 
	 * @return firstResult的设置值，
	 */
	public int getFirstResult() {
		return query.getFirstResult();
	}

	/**
	 * 针对分页查询，设置firstResult。 firstResult代表从满足查询条件的记录的第firstResult + 1条开始获取数据子集。
	 * 
	 * @param firstResult
	 *            要设置的firstResult值。
	 * @return 该对象本身
	 */
	public E setFirstResult(int firstResult) {
		Assert.isTrue(firstResult >= 0, "First result must not be  than 0!");
		query.setFirstResult(firstResult);
		return (E) this;
	}

	/**
	 * 获取当前页码（0为第一页）
	 * 
	 * @return 当前页码
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * 获取每页记录数
	 * 
	 * @return 每页记录数
	 */
	public int getPageSize() {
		return query.getMaxResults();
	}

	/**
	 * 设置每页记录数
	 * 
	 * @param pageSize
	 *            每页记录数
	 * @return 该对象本身
	 */
	public E setPageSize(int pageSize) {
		Assert.isTrue(pageSize > 0, "Page size must be greater than 0!");
		query.setMaxResults(pageSize);
		return (E) this;
	}

	/**
	 * 设置分页信息
	 * 
	 * @param pageIndex
	 *            要设置的页码
	 * @param pageSize
	 *            要设置的页大小
	 * @return 该对象本身
	 */
	public E setPage(int pageIndex, int pageSize) {
		Assert.isTrue(pageIndex >= 0,
				"Page index must be greater than or equals to 0!");
		Assert.isTrue(pageSize > 0, "Page index must be greater than 0!");
		this.pageIndex = pageIndex;
		query.setMaxResults(pageSize);
		query.setFirstResult(Page.getStartOfPage(pageIndex, pageSize));
		return (E) this;
	}

	/**
	 * 返回查询结果数据页。
	 * 
	 * @param <T>
	 *            查询结果的列表元素类型
	 * @return 查询结果。
	 */
	public <T> List<T> list() {
		return query.list();
	}

	/**
	 * 返回查询结果数据页。
	 * 
	 * @param <T>
	 *            查询结果的列表元素类型
	 * @return 查询结果。
	 */
	public <T> Page<T> pagedList() {
		return new Page<T>(query.getFirstResult(), queryResultCount(),
				query.getMaxResults(), query.list());
	}

	/**
	 * 返回单条查询结果。
	 * 
	 * @param <T>
	 *            查询结果的类型
	 * @return 查询结果。
	 */
	public <T> T singleResult() {
		return (T) query.singleResult();
	}
	
	

	/**
	 * 获取符合查询条件的记录总数
	 * 
	 * @return 符合查询条件的记录总数
	 */
	public long queryResultCount(){
		String name = query.getQueryName();
		MybatisNamedQuery mybatisQuery = (MybatisNamedQuery)query;
		mybatisQuery.setQueryName(query.getQueryName()+COUNT);
		Long count = mybatisQuery.singleResult();
		mybatisQuery.setQueryName(name);
		return count;
	}

}
