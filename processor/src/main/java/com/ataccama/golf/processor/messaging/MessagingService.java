package com.ataccama.golf.processor.messaging;

import com.ataccama.golf.commons.SolutionProcessedMessage;

public interface MessagingService {
	void processed(SolutionProcessedMessage message);

	void register();
}
