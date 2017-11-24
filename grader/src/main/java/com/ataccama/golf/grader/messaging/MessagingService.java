package com.ataccama.golf.grader.messaging;

import com.ataccama.golf.commons.SolutionGradedMessage;

public interface MessagingService {
	void graded(SolutionGradedMessage message);

	void register();
}
