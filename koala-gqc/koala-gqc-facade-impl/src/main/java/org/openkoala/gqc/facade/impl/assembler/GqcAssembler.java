package org.openkoala.gqc.facade.impl.assembler;
 
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.facade.dto.GeneralQueryDTO;

public class GqcAssembler {
	
	public static GeneralQueryDTO getDTO(GeneralQuery gq){
		GeneralQueryDTO result = new GeneralQueryDTO();
		result.setCreateDate(gq.getCreateDate());
		result.setDescription(gq.getDataSource().getDataSourceDescription());
		result.setDynamicQueryConditions(gq.getDynamicQueryConditions());
		result.setFieldDetails(gq.getFieldDetails());
		result.setId(gq.getId());
		result.setPreQueryConditions(gq.getPreQueryConditions());
		result.setQueryName(gq.getQueryName());
		result.setTableName(gq.getTableName());
		result.setDataSource(gq.getDataSource());
		result.setVersion(gq.getVersion());
		result.setDataSourceId(gq.getDataSource().getDataSourceId());
		result.setDsId(gq.getDataSource().getId());
		return result;
	}
	
	public static GeneralQuery getEntity(GeneralQueryDTO  dto){
		GeneralQuery result = new GeneralQuery();
		result.setCreateDate(dto.getCreateDate());
		result.setDescription(dto.getDescription());
		result.setDynamicQueryConditions(dto.getDynamicQueryConditions());
		result.setFieldDetails(dto.getFieldDetails());
		result.setId(dto.getId());
		result.setPreQueryConditions(dto.getPreQueryConditions());
		result.setQueryName(dto.getQueryName());
		result.setTableName(dto.getTableName());
		result.setFieldDetails(dto.getFieldDetails());
		result.setDataSource(dto.getDataSource());
		result.setVersion(dto.getVersion());
		return result;
	}

}
