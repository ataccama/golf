package com.ataccama.golf.processor.processing;

import com.ataccama.golf.commons.SolutionSubmitMessage;

public interface ProcessingService {
	void process(SolutionSubmitMessage solution);
}
