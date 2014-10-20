package org.openkoala.gqc.facade.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 封装GeneralQueryVo实例
 * 
 * @author lambo
 * 
 */
public class GeneralQueryDTO implements Serializable {

	private static final long serialVersionUID = -4126052932339716482L;

	private Long id;

	private int version;

	/**
	 * 数据源。
	 */
	private DataSourceDTO dataSourceDTO;

	/**
	 * 数据源主键id。
	 */
/*	private Long dsId;

	private String dataSourceId;
*/
	/**
	 * 查询名称。
	 */

	private String queryName;

	/**
	 * 表名。
	 */
	private String tableName;

	/**
	 * 描述。
	 */
	private String description;

	/**
	 * 创建日期。
	 */
	private Date createDate;

	/**
	 * 静态查询条件。
	 */
	private List<PreQueryConditionDTO> preQueryConditions = new ArrayList<PreQueryConditionDTO>();

	/**
	 * 动态查询条件。
	 */
	private List<DynamicQueryConditionDTO> dynamicQueryConditions = new ArrayList<DynamicQueryConditionDTO>();

	/**
	 * 查询结果要显示的字段。
	 */
	private List<FieldDetailDTO> fieldDetails = new ArrayList<FieldDetailDTO>();
	/**
	 * 获得可显示的静态条件
	 * @return
	 */
	
	public List<PreQueryConditionDTO> getVisiblePreQueryConditions() {
		List<PreQueryConditionDTO> results = new ArrayList<PreQueryConditionDTO>(preQueryConditions.size());
		for (PreQueryConditionDTO dto : preQueryConditions) {
			if (dto.getVisible()) {
				results.add(dto);
			}
		}
		return results;
	}
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

	public DataSourceDTO getDataSourceDTO() {
		return dataSourceDTO;
	}

	public void setDataSourceDTO(DataSourceDTO dataSourceDTO) {
		this.dataSourceDTO = dataSourceDTO;
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

	/*public Long getDsId() {
		return dsId;
	}

	public void setDsId(Long dsId) {
		this.dsId = dsId;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}*/

	public Date getCreateDate() {
		return new Date(createDate.getTime());
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<PreQueryConditionDTO> getPreQueryConditions() {
		return preQueryConditions;
	}

	public void setPreQueryConditions(List<PreQueryConditionDTO> preQueryConditions) {
		this.preQueryConditions = preQueryConditions;
	}

	public List<DynamicQueryConditionDTO> getDynamicQueryConditions() {
		return dynamicQueryConditions;
	}

	public void setDynamicQueryConditions(List<DynamicQueryConditionDTO> dynamicQueryConditions) {
		this.dynamicQueryConditions = dynamicQueryConditions;
	}

	public List<FieldDetailDTO> getFieldDetails() {
		return fieldDetails;
	}

	public void setFieldDetails(List<FieldDetailDTO> fieldDetails) {
		this.fieldDetails = fieldDetails;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dynamicQueryConditions == null) ? 0 : dynamicQueryConditions.hashCode());
		result = prime * result + ((fieldDetails == null) ? 0 : fieldDetails.hashCode());
		result = prime * result + ((preQueryConditions == null) ? 0 : preQueryConditions.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((queryName == null) ? 0 : queryName.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "general query : " + tableName;
	}

}
