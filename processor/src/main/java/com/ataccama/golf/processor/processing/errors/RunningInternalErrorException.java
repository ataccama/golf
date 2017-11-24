package com.ataccama.golf.processor.processing.errors;

public class RunningInternalErrorException extends RunningException {
	private static final long serialVersionUID = 1L;

	public RunningInternalErrorException(String message) {
		super(message);
	}
	
	public RunningInternalErrorException(String message, Throwable cause) {
		super(message, cause);
	}
}
