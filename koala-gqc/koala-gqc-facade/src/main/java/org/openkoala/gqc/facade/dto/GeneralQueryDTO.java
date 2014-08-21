package org.openkoala.gqc.facade.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



import org.dayatang.domain.CriteriaQuery;
import org.dayatang.querychannel.Page;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DynamicQueryCondition;
import org.openkoala.gqc.core.domain.FieldDetail;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.GeneralQueryEntity;
import org.openkoala.gqc.core.domain.PreQueryCondition;
import org.openkoala.gqc.core.domain.QueryCondition;
import org.openkoala.gqc.core.domain.utils.PagingQuerier;
import org.openkoala.gqc.core.domain.utils.QueryAllQuerier;
import org.openkoala.gqc.core.domain.utils.SqlStatmentMode;

/**
 * 封装GeneralQueryVo实例
 * @author lambo
 *
 */
public class GeneralQueryDTO {

	/**
	 * 
	 */
	
	private Long id;
	
	private int version;
	/**
	 * 数据源
	 */
	private DataSource dataSource;
	
	/**
	 * 数据源主键id
	 */
	private Long dsId;
	
	private String dataSourceId;
	
	/**
	 * 查询名称
	 */
	
	private String queryName;
	
	/**
	 * 表名
	 */
	
	private String tableName;
	
	/**
	 * 描述
	 */
	
	private String description;
	
	/**
	 * 创建日期
	 */
	
	private Date createDate;
	
	/**
	 * 静态查询条件
	 */
	
	private List<PreQueryCondition> preQueryConditions = new ArrayList<PreQueryCondition>();
	
	/**
	 * 动态查询条件
	 */
	
	private List<DynamicQueryCondition> dynamicQueryConditions = new ArrayList<DynamicQueryCondition>();
	
	/**
	 * 查询结果要显示的字段
	 */
	
	private List<FieldDetail> fieldDetails = new ArrayList<FieldDetail>();
	
	/**
	 * 查询所有查询器
	 */
	private transient QueryAllQuerier queryAllQuerier;
	
	/**
	 * 分页查询查询器
	 */
	private transient PagingQuerier pagingQuerier;
	
	public GeneralQueryDTO() {
		
	}
	
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public int getVersion() {
		return version;
	}



	public void setVersion(int version) {
		this.version = version;
	}



	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public GeneralQueryDTO(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDataSourceId() {
		return dataSourceId;
	}

	public Long getDsId() {
		return dsId;
	}

	public void setDsId(Long dsId) {
		this.dsId = dsId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public Date getCreateDate() {
		return new Date(createDate.getTime());
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	public List<PreQueryCondition> getPreQueryConditions() {
		return preQueryConditions;
	}

	public void setPreQueryConditions(List<PreQueryCondition> preQueryConditions) {
		this.preQueryConditions = preQueryConditions;
	}

	public List<DynamicQueryCondition> getDynamicQueryConditions() {
		return dynamicQueryConditions;
	}

	public void setDynamicQueryConditions(
			List<DynamicQueryCondition> dynamicQueryConditions) {
		this.dynamicQueryConditions = dynamicQueryConditions;
	}

public List<FieldDetail> getFieldDetails() {
		return fieldDetails;
	}

	public void setFieldDetails(List<FieldDetail> fieldDetails) {
		this.fieldDetails = fieldDetails;
	}

	

	private QueryAllQuerier obtainQueryAllQuerier() {
		if (queryAllQuerier == null) {
			queryAllQuerier = new QueryAllQuerier(obtainQuerySql(), dataSource);
		} else {
			queryAllQuerier.setQuerySql(obtainQuerySql());
		}
		return queryAllQuerier;
	}

	
	private PagingQuerier obtainPagingQuerier() {
		if (pagingQuerier == null) {
			pagingQuerier = new PagingQuerier(obtainQuerySql(), dataSource);
		} else {
			pagingQuerier.setQuerySql(obtainQuerySql());
		}
		return pagingQuerier;
	}

	/**
	 * 执行查询，返回查询结果，不分页。
	 * @return
	 */
	public List<Map<String, Object>> query() {
		return obtainQueryAllQuerier().query();
	}

	/**
	 * 执行分页查询，返回查询结果。
	 * @param startNumber
	 * @param pagesize
	 * @return 
	 */
	public List<Map<String, Object>> pagingQuery(int currentPage, int pagesize) {
		obtainPagingQuerier().changePagingParameter(Page.getStartOfPage(currentPage, pagesize), pagesize);
		return obtainPagingQuerier().query();
	}
	
	/**
	 * 执行分页查询，返回page对象。
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	public Page<Map<String, Object>> pagingQueryPage(int currentPage, int pagesize) {
		long totalCount = obtainPagingQuerier().caculateTotalCount();
		return new Page<Map<String, Object>>(Page.getStartOfPage(currentPage, pagesize), totalCount, pagesize, pagingQuery(currentPage, pagesize));
	}
	
	
	
	/**
	 * 生成查询SQL语句
	 * @return
	 */
	
	private SqlStatmentMode obtainQuerySql() {
		return generateCommonQuerySql();
	}
	
	/**
	 * 获得可现实的静态条件
	 * @return
	 */
	
	public List<PreQueryCondition> getVisiblePreQueryConditions() {
		List<PreQueryCondition> results = new ArrayList<PreQueryCondition>();
		for (PreQueryCondition preQueryCondition : preQueryConditions) {
			if (preQueryCondition.getVisible()) {
				results.add(preQueryCondition);
			}
		}
		return results;
	}
	
	private SqlStatmentMode generateCommonQuerySql() {
		SqlStatmentMode result = new SqlStatmentMode();
		result.setStatment(generateSelectPrefixStatement());
		
		addPreQueryConditionStatement(result);
		addDynamicQueryConditionStatement(result);
		
		return result;
	}
	
	private String generateSelectPrefixStatement() {
		StringBuilder result = new StringBuilder();
		result.append("select ");
		
		for (FieldDetail fieldDetail : fieldDetails) {
			result.append(fieldDetail.getFieldName() + ",");
		}
		result.deleteCharAt(result.length() - 1);
		result.append(" from " + tableName);
		
		if (!preQueryConditions.isEmpty() || !dynamicQueryConditions.isEmpty()) {
			result.append(" where 1=1");
		}

		return result.toString();
	}
	
	private void addPreQueryConditionStatement(SqlStatmentMode sqlStatmentMode) {
		generateConditionStatement(preQueryConditions, sqlStatmentMode);
	}
	
	private void addDynamicQueryConditionStatement(SqlStatmentMode sqlStatmentMode) {
		generateConditionStatement(dynamicQueryConditions, sqlStatmentMode);
	}
	
	private <T extends QueryCondition> void generateConditionStatement(List<T> queryConditions, SqlStatmentMode sqlStatmentMode) {
		if (queryConditions.isEmpty()) {
			return;
		}
		
		for (T queryCondition : queryConditions) {
			queryCondition.setDataSource(dataSource);
			SqlStatmentMode conditionSqlStatment = queryCondition.generateConditionStatment();
			sqlStatmentMode.setStatment(sqlStatmentMode.getStatment() + conditionSqlStatment.getStatment());
			sqlStatmentMode.addValues(conditionSqlStatment.getValues());
		}
	}
	
	/**
	 * 通过字段名称查询动态查询条件
	 * @param fieldName
	 * @return
	 */
	public DynamicQueryCondition getDynamicQueryConditionByFieldName(String fieldName) {
		for (DynamicQueryCondition dynamicQueryCondition : dynamicQueryConditions) {
			if (dynamicQueryCondition.getFieldName().equals(fieldName)) {
				return dynamicQueryCondition;
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dynamicQueryConditions == null) ? 0 : dynamicQueryConditions
						.hashCode());
		result = prime * result
				+ ((fieldDetails == null) ? 0 : fieldDetails.hashCode());
		result = prime
				* result
				+ ((preQueryConditions == null) ? 0 : preQueryConditions
						.hashCode());
		result = prime * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result
				+ ((queryName == null) ? 0 : queryName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "general query : " + tableName;
	}

	@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	}
	

