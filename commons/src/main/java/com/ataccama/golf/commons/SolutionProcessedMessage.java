package com.ataccama.golf.commons;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class SolutionProcessedMessage {
	private UUID id;
	private String task;
	private String code;

	private Instant processingStarted;
	private Instant processingEnded;
	private SolutionResult processingResult;
	private String processingMessage;

	private String output;
}
