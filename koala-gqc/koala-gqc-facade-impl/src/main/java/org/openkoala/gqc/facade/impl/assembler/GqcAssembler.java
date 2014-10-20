package org.openkoala.gqc.facade.impl.assembler;

import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.facade.dto.GeneralQueryDTO;

public class GqcAssembler {

	public static GeneralQueryDTO toDTO(GeneralQuery gq) {
		GeneralQueryDTO result = new GeneralQueryDTO();
		result.setCreateDate(gq.getCreateDate());
		result.setDescription(gq.getDataSource().getDataSourceDescription());
		result.setDynamicQueryConditions(DynamicQueryConditionAssembler.toDTOList(gq.getDynamicQueryConditions()));
		result.setPreQueryConditions(PreQueryConditionAssembler.toDTOList(gq.getPreQueryConditions()));
		result.setFieldDetails(FieldDetailAssembler.toDTOList(gq.getFieldDetails()));
		result.setId(gq.getId());
		result.setQueryName(gq.getQueryName());
		result.setTableName(gq.getTableName());
		result.setDataSourceDTO(DataSourceAssembler.toDTO(gq.getDataSource()));
		result.setVersion(gq.getVersion());
	/*	result.setDataSourceId(gq.getDataSource().getDataSourceId());
		result.setDsId(gq.getDataSource().getId());*/
		return result;
	}

	public static GeneralQuery toEntity(GeneralQueryDTO dto) {
		GeneralQuery result = new GeneralQuery();
		result.setCreateDate(dto.getCreateDate());
		result.setDescription(dto.getDescription());
		result.setDynamicQueryConditions(DynamicQueryConditionAssembler.toEntityList(dto.getDynamicQueryConditions()));
		result.setPreQueryConditions(PreQueryConditionAssembler.toEntityList(dto.getPreQueryConditions()));
		result.setFieldDetails(FieldDetailAssembler.toEntityList(dto.getFieldDetails()));
		result.setId(dto.getId());
		result.setQueryName(dto.getQueryName());
		result.setTableName(dto.getTableName());
		result.setDataSource(DataSourceAssembler.toEntity(dto.getDataSourceDTO()));
		result.setVersion(dto.getVersion());
		return result;
	}

}
