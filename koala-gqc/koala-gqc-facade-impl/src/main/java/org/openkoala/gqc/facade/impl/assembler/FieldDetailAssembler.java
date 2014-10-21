package org.openkoala.gqc.facade.impl.assembler;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.gqc.core.domain.FieldDetail;
import org.openkoala.gqc.facade.dto.FieldDetailDTO;

public class FieldDetailAssembler {

	public static FieldDetailDTO toDTO(FieldDetail entity) {
		FieldDetailDTO dto = new FieldDetailDTO();
		dto.setFieldName(entity.getFieldName());
		dto.setLabel(entity.getLabel());
		return dto;
	}

	public static FieldDetail toEntity(FieldDetailDTO dto) {
		FieldDetail entity = new FieldDetail();
		entity.setFieldName(dto.getFieldName());
		entity.setLabel(dto.getLabel());
		return entity;
	}

	public static List<FieldDetail> toEntityList(List<FieldDetailDTO> list) {
		ArrayList<FieldDetail> result = new ArrayList<FieldDetail>(list.size());
		for (FieldDetailDTO dto : list) {
			result.add(toEntity(dto));
		}
		return result;
	}

	public static List<FieldDetailDTO> toDTOList (List<FieldDetail> list) {
		ArrayList<FieldDetailDTO> result = new ArrayList<FieldDetailDTO>(list.size());
		for (FieldDetail entity : list) {
			result.add(toDTO(entity));
		}
		return result;
	}

}
