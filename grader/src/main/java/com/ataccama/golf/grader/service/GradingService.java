package com.ataccama.golf.grader.service;

import com.ataccama.golf.commons.SolutionProcessedMessage;

public interface GradingService {
	void grade(SolutionProcessedMessage message);
}
