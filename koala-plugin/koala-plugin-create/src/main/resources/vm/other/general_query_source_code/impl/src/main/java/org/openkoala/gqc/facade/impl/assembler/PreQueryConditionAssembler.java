package org.openkoala.gqc.facade.impl.assembler;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.gqc.core.domain.PreQueryCondition;
import org.openkoala.gqc.core.domain.QueryOperation;
import org.openkoala.gqc.facade.dto.PreQueryConditionDTO;

public class PreQueryConditionAssembler {
	public static PreQueryCondition toEntity(PreQueryConditionDTO dto){
		PreQueryCondition pqc = new PreQueryCondition();
		pqc.setValue(dto.getValue());
		pqc.setStartValue(dto.getStartValue());
		pqc.setEndValue(dto.getEndValue());
		pqc.setVisible(dto.getVisible());
		pqc.setFieldName(dto.getFieldName());
		pqc.setFieldType(dto.getFieldType());
		pqc.setQueryOperation(QueryOperation.valueOf(dto.getQueryOperation().toString()));
		return pqc;
	}
	
	public static PreQueryConditionDTO toDTO(PreQueryCondition entity){
		PreQueryConditionDTO dto = new PreQueryConditionDTO();
		dto.setStartValue(entity.getStartValue());
		dto.setEndValue(entity.getEndValue());
		dto.setValue(entity.getValue());
		dto.setVisible(entity.getVisible());
		dto.setFieldName(entity.getFieldName());
		dto.setFieldType(entity.getFieldType());
		dto.setQueryOperation(org.openkoala.gqc.facade.dto.QueryOperation.valueOf(entity.getQueryOperation().toString()));
		return dto;
	}
	
	public static List<PreQueryCondition> toEntityList(List<PreQueryConditionDTO> list){
		ArrayList<PreQueryCondition> result = new ArrayList<PreQueryCondition>(list.size());
		for(PreQueryConditionDTO dto : list){
			result.add(toEntity(dto));
		}
		return result;
	}
	public static List<PreQueryConditionDTO> toDTOList(List<PreQueryCondition> list){
		ArrayList<PreQueryConditionDTO> result = new ArrayList<PreQueryConditionDTO>(list.size());
		for(PreQueryCondition entity : list){
			result.add(toDTO(entity));
		}
		return result;	
	}
	

}
