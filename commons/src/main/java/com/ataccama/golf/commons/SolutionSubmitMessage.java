package com.ataccama.golf.commons;

import java.util.UUID;

import lombok.Data;

@Data
public class SolutionSubmitMessage {
	private UUID id;
	private String task;
	private String code;
	private String language;
}
