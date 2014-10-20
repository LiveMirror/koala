package org.openkoala.gqc.facade.impl.assembler;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.gqc.core.domain.DynamicQueryCondition;
import org.openkoala.gqc.core.domain.QueryOperation;
import org.openkoala.gqc.core.domain.WidgetType;
import org.openkoala.gqc.facade.dto.DynamicQueryConditionDTO;
import org.openkoala.gqc.facade.dto.WidgetTypeDTO;

public class DynamicQueryConditionAssembler {

	public static DynamicQueryCondition toEntity(DynamicQueryConditionDTO dto) {
		DynamicQueryCondition dqc = new DynamicQueryCondition();
		dqc.setEndValue(dto.getEndValue());
		dqc.setFieldType(dto.getFieldType());
		dqc.setLabel(dto.getLabel());
		dqc.setStartValue(dto.getStartValue());
		dqc.setFieldName(dto.getFieldName());
		dqc.setFieldType(dto.getFieldType());
		dqc.setQueryOperation(QueryOperation.valueOf(dto.getQueryOperation().toString()));
		dqc.setWidgetType(WidgetType.valueOf(dto.getWidgetTypeDTO().toString()));
		return dqc;
	}

	public static DynamicQueryConditionDTO toDTO(DynamicQueryCondition entity) {
		DynamicQueryConditionDTO dto = new DynamicQueryConditionDTO();
		dto.setStartValue(entity.getStartValue());
		dto.setEndValue(entity.getEndValue());
		dto.setLabel(entity.getLabel());
		dto.setWidgetTypeDTO(WidgetTypeDTO.valueOf(entity.getWidgetType().toString()));
		dto.setFieldType(entity.getFieldType());
		dto.setFieldName(entity.getFieldName());
		dto.setQueryOperation(org.openkoala.gqc.facade.dto.QueryOperation.valueOf(entity.getQueryOperation().toString()));
		return dto;
	}

	public static List<DynamicQueryConditionDTO> toDTOList(List<DynamicQueryCondition> list) {
		ArrayList<DynamicQueryConditionDTO> result = new ArrayList<DynamicQueryConditionDTO>(list.size());
		for (DynamicQueryCondition entity : list) {
			result.add(toDTO(entity));
		}
		return result;
	}

	public static List<DynamicQueryCondition> toEntityList(List<DynamicQueryConditionDTO> list) {
		ArrayList<DynamicQueryCondition> result = new ArrayList<DynamicQueryCondition>(list.size());
		for (DynamicQueryConditionDTO dto : list) {
			result.add(toEntity(dto));
		}
		return result;
	}

}
