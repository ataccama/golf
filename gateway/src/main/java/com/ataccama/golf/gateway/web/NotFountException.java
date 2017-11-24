package com.ataccama.golf.gateway.web;

public class NotFountException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotFountException() {
		super();
	}

	public NotFountException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFountException(String message) {
		super(message);
	}

	public NotFountException(Throwable cause) {
		super(cause);
	}
}
