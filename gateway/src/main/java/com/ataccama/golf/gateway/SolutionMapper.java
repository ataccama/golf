package com.ataccama.golf.gateway;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.ataccama.golf.commons.SolutionGradedMessage;
import com.ataccama.golf.commons.SolutionProcessedMessage;
import com.ataccama.golf.commons.SolutionSubmitMessage;
import com.ataccama.golf.gateway.db.Solution;
import com.ataccama.golf.gateway.web.NewSolution;
import com.ataccama.golf.gateway.web.SolutionDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SolutionMapper {
	// JPA related
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "created", ignore = true)
	@Mapping(target = "updated", ignore = true)
	// processing related
	@Mapping(target = "processingStarted", ignore = true)
	@Mapping(target = "processingEnded", ignore = true)
	@Mapping(target = "processingResult", ignore = true)
	@Mapping(target = "processingMessage", ignore = true)
	// grading related
	@Mapping(target = "gradingStarted", ignore = true)
	@Mapping(target = "gradingEnded", ignore = true)
	@Mapping(target = "gradingResult", ignore = true)
	@Mapping(target = "gradingScore", ignore = true)
	@Mapping(target = "gradingMessage", ignore = true)
	Solution toDb(NewSolution from);

	SolutionSubmitMessage toMessage(Solution solution);

	SolutionDTO toDto(Solution solution);

	// Fixed
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "email", ignore = true)
	@Mapping(target = "language", ignore = true)
	@Mapping(target = "task", ignore = true)
	@Mapping(target = "code", ignore = true)
	// JPA related
	@Mapping(target = "created", ignore = true)
	@Mapping(target = "updated", ignore = true)
	// grading related
	@Mapping(target = "gradingStarted", ignore = true)
	@Mapping(target = "gradingEnded", ignore = true)
	@Mapping(target = "gradingResult", ignore = true)
	@Mapping(target = "gradingScore", ignore = true)
	@Mapping(target = "gradingMessage", ignore = true)
	void intoDb(SolutionProcessedMessage message, @MappingTarget Solution solution);

	// Fixed
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "email", ignore = true)
	@Mapping(target = "language", ignore = true)
	@Mapping(target = "task", ignore = true)
	@Mapping(target = "code", ignore = true)
	// JPA related
	@Mapping(target = "created", ignore = true)
	@Mapping(target = "updated", ignore = true)
	// grading related
	@Mapping(target = "processingStarted", ignore = true)
	@Mapping(target = "processingEnded", ignore = true)
	@Mapping(target = "processingResult", ignore = true)
	@Mapping(target = "processingMessage", ignore = true)
	void intoDb(SolutionGradedMessage message, @MappingTarget Solution solution);

	List<SolutionDTO> toDtos(List<Solution> solutions);
}
