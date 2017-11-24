package com.ataccama.golf.processor.processing;

import org.springframework.context.ApplicationEvent;

public class ProcessingReadyEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public ProcessingReadyEvent(Object source) {
		super(source);
	}
}
