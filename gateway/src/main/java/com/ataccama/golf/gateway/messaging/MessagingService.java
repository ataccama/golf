package com.ataccama.golf.gateway.messaging;

import com.ataccama.golf.commons.SolutionSubmitMessage;

public interface MessagingService {
	void submit(SolutionSubmitMessage message);

	void discover();
}
