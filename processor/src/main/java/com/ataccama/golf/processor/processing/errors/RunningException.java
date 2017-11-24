package com.ataccama.golf.processor.processing.errors;

public class RunningException extends Exception {
	private static final long serialVersionUID = 1L;

	public RunningException(String message) {
		super(message);
	}

	public RunningException(String message, Throwable cause) {
		super(message, cause);
	}
}
