package org.openkoala.organisation.facade.impl.assembler;

import org.openkoala.organisation.core.domain.Job;
import org.openkoala.organisation.facade.dto.JobDTO;

public class JobAssembler {

	public static JobDTO toDTO(Job job) {
        if (job == null ) {
            return null;
        }

		JobDTO dto = new JobDTO(job.getId(), job.getName(), job.getSn());
		dto.setCreateDate(job.getCreateDate());
		dto.setDescription(job.getDescription());
		dto.setVersion(job.getVersion());
		return dto;
	}

	public static Job toEntity(JobDTO jobDTO) {
        if (jobDTO == null) {
            return null;
        }

		Job result = new Job(jobDTO.getName(), jobDTO.getSn());
		result.setId(jobDTO.getId());
		result.setDescription(jobDTO.getDescription());
		result.setVersion(jobDTO.getVersion());
		
		if (jobDTO.getCreateDate() != null) {
			result.setCreateDate(jobDTO.getCreateDate());
		}
		if (jobDTO.getTerminateDate() != null) {
			result.setTerminateDate(jobDTO.getTerminateDate());
		}
		
		return result;
	}

}
