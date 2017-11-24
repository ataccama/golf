package com.ataccama.golf.gateway.web;

import java.time.Instant;
import java.util.UUID;

import com.ataccama.golf.commons.SolutionResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolutionDTO {
	private UUID id;
	private String email;
	private String task;
	private String code;
	private String language;
	private Instant created;
	private Instant updated;
	private Instant processingStarted;
	private Instant processingEnded;
	private SolutionResult processingResult;
	private String processingMessage;
	private Instant gradingStarted;
	private Instant gradingEnded;
	private SolutionResult gradingResult;
	private Integer gradingScore;
	private String gradingMessage;

	@JsonProperty("finished")
	public boolean isFinished() {
		switch (processingResult) {
		case FAILED:
		case INTERNAL_ERROR:
		case TIMEOUT:
			return true;
		case UNKNOWN:
			return false;
		case OK:
			// depending on grader
			break;
		default:
			throw new IllegalStateException("Unknown processing state " + processingResult);
		}

		switch (gradingResult) {
		case FAILED:
		case INTERNAL_ERROR:
		case TIMEOUT:
		case OK:
			return true;
		case UNKNOWN:
			return false;
		default:
			throw new IllegalStateException("Unknown grading state " + processingResult);
		}
	}
}
