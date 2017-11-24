package com.ataccama.golf.processor.processing.errors;

public class RunningTimeoutException extends RunningException {
	private static final long serialVersionUID = 1L;

	public RunningTimeoutException(int millis) {
		super("The processing timed out after " + millis + " milliseconds.");
	}
}
