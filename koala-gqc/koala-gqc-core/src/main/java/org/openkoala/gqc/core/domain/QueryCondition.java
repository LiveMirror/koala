package org.openkoala.gqc.core.domain;

import java.sql.Types;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.dayatang.domain.ValueObject;
import org.openkoala.gqc.core.domain.utils.SqlStatmentMode;

/**
 * 查询条件基类	
 * 
 * 作    者：xmfang(xinmin.fang@gmail.com)
 */
@MappedSuperclass
public abstract class QueryCondition implements ValueObject, Comparable<QueryCondition> {

	private static final long serialVersionUID = 3994911481276462180L;
	
	/**
	 * 字段名称
	 */
	@Column(name = "FIELD_NAME")
	private String fieldName;

	/**
	 * 查询操作
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "QUERY_OPERATION")
	private QueryOperation queryOperation;
	
	/**
     * 字段数据类型（匹配java.sql.Types中的常量）
     */
	@Column(name = "FIELD_TYPE")
    private Integer fieldType;
	
	@Transient
	private DataSource dataSource;
    
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public QueryOperation getQueryOperation() {
		return queryOperation;
	}

	public void setQueryOperation(QueryOperation queryOperation) {
		this.queryOperation = queryOperation;
	}
	
	public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	String generateConditionValueStatment() {
		if (getFieldType() == Types.TIMESTAMP && isOracleDatabase()) {
			return "to_date(?,'YYYY-MM-DD HH:mi:SS')";
		}
		return "?";
	}
	
	@Transient
	private boolean isOracleDatabase() {
		if (getDataSource().getConnectUrl().startsWith("jdbc:oracle")) {
			return true;
		}
		return false;
	}

	/**
	 * 生成条件语句
	 * @return
	 */
    public abstract SqlStatmentMode generateConditionStatment();

	public int compareTo(QueryCondition other) {
		return getFieldName().compareTo(other.getFieldName());
	}
}
