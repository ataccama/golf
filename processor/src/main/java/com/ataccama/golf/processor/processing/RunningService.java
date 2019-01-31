package com.ataccama.golf.processor.processing;

import java.io.File;

import com.ataccama.golf.processor.processing.errors.RunningException;

public interface RunningService {
	void run(File solutionDir, boolean allowTimeout) throws RunningException, InterruptedException;
}
