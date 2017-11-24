package com.ataccama.golf.commons;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class SolutionGradedMessage {
	private UUID id;
	private String task;

	private Instant gradingStarted;
	private Instant gradingEnded;
	private SolutionResult gradingResult;
	private Integer gradingScore;
	private String gradingMessage;
}
